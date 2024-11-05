package com.billingapplication.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.billingapplication.entity.Customer;
import com.billingapplication.entity.Invoice;
import com.billingapplication.entity.InvoiceProduct;
import com.billingapplication.entity.Product;
import com.billingapplication.exception.RecordNotFoundException;
import com.billingapplication.repository.CustomerRepository;
import com.billingapplication.repository.InvoiceRepository;
import com.billingapplication.repository.ProductRepository;
import com.billingapplication.service.InvoiceService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private InvoiceRepository invoiceRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public Invoice createInvoice(List<Integer> productIds, List<Integer> quantities, int customerId) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setPaymentStatus("Unpaid");
        invoice.setInvoiceProducts(new ArrayList<>()); // Initialize the list

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found"));

        invoice.setCustomer(customer);
        invoice.setCustomer_name(customer.getName());
        invoice.setCustomer_email(customer.getEmail());
        invoice.setCustomer_city(customer.getCity());
        invoice.setCustomer_mobileno(customer.getMobileNumber());

        double totalAmount = 0;
        for (int i = 0; i < productIds.size(); i++) {
            Product product = productRepo.findById(productIds.get(i))
                    .orElseThrow(() -> new RecordNotFoundException("Product not found"));

            InvoiceProduct invoiceProduct = new InvoiceProduct();
            invoiceProduct.setProduct(product);
            invoiceProduct.setInvoice(invoice);
            invoiceProduct.setQuantity(quantities.get(i));
            invoiceProduct.setProduct_name(product.getProdName());
            invoiceProduct.setProductPrice(quantities.get(i) * product.getPrice());
            totalAmount += product.getPrice() * quantities.get(i);
            invoice.getInvoiceProducts().add(invoiceProduct);
        }

        invoice.setTotalAmount(totalAmount);
        Invoice savedInvoice = invoiceRepo.save(invoice);

        // Send email after invoice creation
        try {
			sendInvoiceEmail(savedInvoice);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return savedInvoice;
    }

    private void sendInvoiceEmail(Invoice invoice) throws MessagingException {
        MimeMessage mess = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mess, true);
        try {
            helper.setFrom(fromEmail);
            helper.setTo(invoice.getCustomer_email());
            helper.setSubject("Invoice Generated: #" + invoice.getId());
            helper.setText(buildEmailBody(invoice), true); // Set to true for HTML content
            mailSender.send(mess);
        } catch (MessagingException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    private String buildEmailBody(Invoice invoice) {
        StringBuilder body = new StringBuilder();
        body.append("<html>")
            .append("<body>")
            .append("<h2>Dear ").append(invoice.getCustomer_name()).append(",</h2>")
            .append("<p>Your invoice has been successfully generated.</p>")
            .append("<p><strong>Invoice ID:</strong> ").append(invoice.getId()).append("</p>")
            .append("<p><strong>Invoice Date:</strong> ").append(invoice.getInvoiceDate()).append("</p>")
            .append("<p><strong>Total Amount:</strong> ₹").append(invoice.getTotalAmount()).append("</p>")
            .append("<h3>Items:</h3>")
            .append("<table style='border-collapse: collapse; width: 100%;'>")
            .append("<tr style='background-color: #f2f2f2;'>")
            .append("<th style='border: 1px solid #dddddd; padding: 8px;'>Product Name</th>")
            .append("<th style='border: 1px solid #dddddd; padding: 8px;'>Quantity</th>")
            .append("<th style='border: 1px solid #dddddd; padding: 8px;'>Price</th>")
            .append("</tr>");

        for (InvoiceProduct item : invoice.getInvoiceProducts()) {
            body.append("<tr>")
                .append("<td style='border: 1px solid #dddddd; padding: 8px;'>").append(item.getProduct_name()).append("</td>")
                .append("<td style='border: 1px solid #dddddd; padding: 8px;'>").append(item.getQuantity()).append("</td>")
                .append("<td style='border: 1px solid #dddddd; padding: 8px;'>₹").append(item.getProductPrice()).append("</td>")
                .append("</tr>");
        }

        body.append("</table>")
            .append("<p>Thank you for your business!</p>")
            .append("<p>To pay your invoice, please log in to your account.</p>") // Added line for login and payment
            .append("</body>")
            .append("</html>");
        return body.toString();
    }


    @Override
    public Invoice getInvoiceById(int invoiceId) {
        return invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new RecordNotFoundException("Invoice with id " + invoiceId + " not found"));
    }

    @Override
    public void deleteInvoice(int invoiceId) {
        Optional<Invoice> invoiceDb = invoiceRepo.findById(invoiceId);
        if (invoiceDb.isPresent()) {
            invoiceRepo.delete(invoiceDb.get());
        } else {
            throw new RecordNotFoundException("Invoice with id " + invoiceId + " not found");
        }
    }

    @Override
    public List<Invoice> getAllInvoices(Invoice inv) {
        return invoiceRepo.findAll();
    }

    @Override
    public List<Invoice> getInvoiceByCustomerId(int customerid) {
        return invoiceRepo.findByCustomerId(customerid);
    }

    @Override
    public Map<String, Integer> getPaymentStatusPieChart() {
        List<Invoice> inv = invoiceRepo.findAll();
        Map<String, Integer> categoryCount = new HashMap<>();
        for (Invoice invoice : inv) {
            String paymentStatus = invoice.getPaymentStatus();
            categoryCount.put(paymentStatus, categoryCount.getOrDefault(paymentStatus, 0) + 1);
        }
        return categoryCount;
    }

    @Override
    public List<Invoice> orderHistory(int customerid) {
        return invoiceRepo.orderHistory(customerid);
    }
}
