package com.billingapplication.serviceImpl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.billingapplication.entity.Customer;
import com.billingapplication.entity.Invoice;
import com.billingapplication.entity.Payment;
import com.billingapplication.repository.CustomerRepository;
import com.billingapplication.repository.InvoiceRepository;
import com.billingapplication.repository.PaymentRepository;
import com.billingapplication.service.PaymentService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class PaymentServiceImpl implements PaymentService {
	@Value("${spring.mail.username}")
    private String fromEmail;
    @Autowired
    CustomerRepository cusRepo;

    @Autowired
    InvoiceRepository invRepo;

    @Autowired
    PaymentRepository payRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public String payByWallet(int customerId, int invoiceId) {
        Customer customer = cusRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Invoice invoice = invRepo.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if ("Paid".equals(invoice.getPaymentStatus())) {
            return "{\"message\": \"Invoice is already fully paid.\"}";
        }

        double totalAmount = invoice.getTotalAmount();
        if (customer.getWalletBalance() < totalAmount) {
            return String.format("{\"message\": \"Insufficient wallet balance to pay the total invoice amount of %.2f. Go to settings and add money to your wallet.\"}",
                    (totalAmount - customer.getWalletBalance()));
        }

        if (totalAmount <= 0) {
            return "{\"message\": \"The total amount for this invoice is zero. Payment cannot be processed.\"}";
        }

        double newBalance = customer.getWalletBalance() - totalAmount;
        customer.setWalletBalance(newBalance);
        invoice.setAmountPaid(totalAmount);
        invoice.setPaymentStatus("Paid");
        invoice.setPaymentDate(LocalDate.now());

        Payment payment = new Payment();
        payment.setAmount(totalAmount);
        payment.setPaymentMethod("Wallet");
        payment.setInvoice(invoice);
        payment.setPaymentDate(LocalDate.now());

        cusRepo.save(customer);
        invRepo.save(invoice);
        payRepo.save(payment);

        // Send payment confirmation email
        try {
            sendPaymentConfirmationEmail(customer, invoice);
        } catch (MessagingException e) {
            e.printStackTrace(); // Handle exceptions
            return "{\"message\": \"Payment Successful, but failed to send confirmation email.\"}"; // Optional: Return message if email fails
        }

        return "{\"message\": \"Payment Successful\"}"; // Return a valid JSON response
    }

    private void sendPaymentConfirmationEmail(Customer customer, Invoice invoice) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setFrom(fromEmail); // Set your "from" email
        helper.setTo(customer.getEmail());
        helper.setSubject("Payment Confirmation for Invoice #" + invoice.getId());
        helper.setText(buildEmailBody(customer, invoice), true); // Set to true for HTML content
        
        mailSender.send(message);
    }

    private String buildEmailBody(Customer customer, Invoice invoice) {
        StringBuilder body = new StringBuilder();
        body.append("<html>")
            .append("<head>")
            .append("<style>")
            .append("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }")
            .append(".container { max-width: 600px; margin: 20px auto; padding: 20px; background-color: #fff; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); }")
            .append("h2 { color: #333; }")
            .append("p { color: #555; line-height: 1.5; }")
            .append(".footer { margin-top: 20px; padding: 10px; text-align: center; font-size: 12px; color: #777; }")
            .append(".highlight { color: #28a745; font-weight: bold; }") // Highlighting amounts
            .append("</style>")
            .append("</head>")
            .append("<body>")
            .append("<div class='container'>")
            .append("<h2>Dear ").append(customer.getName()).append(",</h2>")
            .append("<p>We are pleased to inform you that your payment of <span class='highlight'>₹").append(invoice.getAmountPaid()).append("</span> for Invoice ID: <span class='highlight'>").append(invoice.getId()).append("</span> has been received successfully.</p>")
            .append("<p><strong>Invoice Date:</strong> ").append(invoice.getInvoiceDate()).append("</p>")
            .append("<p><strong>Payment Method:</strong> Wallet</p>")
            .append("<p>Thank you for your business! If you have any questions, feel free to reach out to us.</p>")
            .append("</div>")
            .append("<div class='footer'>")
            .append("<p>Best regards,<br>Invoicify</p>")
            .append("</div>")
            .append("</body>")
            .append("</html>");
        return body.toString();
    }

}
