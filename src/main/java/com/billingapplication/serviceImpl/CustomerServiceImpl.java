package com.billingapplication.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.billingapplication.entity.Admin;
import com.billingapplication.entity.Customer;
import com.billingapplication.entity.Subscribe;
import com.billingapplication.entity.User;
import com.billingapplication.exception.RecordNotFoundException;
import com.billingapplication.repository.CustomerRepository;
import com.billingapplication.repository.SubscribeRepository;
import com.billingapplication.service.CustomerService;

import jakarta.mail.internet.MimeMessage;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Value("${spring.mail.username}")
	private String fromEmail;
	@Autowired
	CustomerRepository customerRepo;
	@Autowired
	JavaMailSender javamailSend;
	@Autowired
	SubscribeRepository subRepo;

	@Override
	public Customer createCustomerAcc(Customer customer) {
		customer.setRole("ROLE_CUSTOMER");
		Customer newCustomer = customerRepo.save(customer);
		try {
			MimeMessage mess = javamailSend.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mess, true);

			mimeMessageHelper.setFrom(fromEmail);
			mimeMessageHelper.setTo(customer.getEmail());
			mimeMessageHelper.setSubject("Welcome to Our Billing Application!");
			mimeMessageHelper.setText("<div style='font-family: Arial, sans-serif; color: #333;'>"
					+ "<h1 style='color: #4CAF50;'>Welcome, " + customer.getName() + "!</h1>"
					+ "<p style='font-size: 16px;'>Your account has been successfully created. Here are your login details:</p>"
					+ "<table border='1' cellpadding='10' cellspacing='0' style='border-collapse: collapse; width: 100%; max-width: 600px;'>"
					+ "  <tr>"
					+ "    <th style='background-color: #4CAF50; color: white; text-align: left;'>Username</th>"
					+ "    <td style='text-align: left;'>" + customer.getEmail() + "</td>" + "  </tr>" + "  <tr>"
					+ "    <th style='background-color: #4CAF50; color: white; text-align: left;'>Password</th>"
					+ "    <td style='text-align: left;'>" + customer.getPassword() + "</td>" + "  </tr>" + "</table>"
					+ "<br>" + "<p style='font-size: 16px;'>Thank you for joining us!</p>"
					+ "<p style='font-size: 16px;'>Best Regards,<br><span style='font-weight: bold;'>Admin Team</span></p>"
					+ "<div style='margin-top: 20px; font-size: 12px; color: #999;'>"
					+ "<p>This is an automated email, please do not reply.</p>" + "</div>" + "</div>", true);
			javamailSend.send(mess);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return newCustomer;
	}

	public boolean existsEmail(String email) {
		return customerRepo.existsByEmail(email);
	}

	@Override
	public List<Customer> getAllCustomer(Customer customer) {
		return customerRepo.findAll();
	}

	@Override
	public Customer updateCustomer(Customer customer) {
		Optional<Customer> cu = customerRepo.findById(customer.getId());
		if (cu.isPresent()) {
			Customer c = cu.get();
			c.setName(customer.getName());
			c.setEmail(customer.getEmail());
			c.setCity(customer.getCity());
			c.setPassword(customer.getPassword());
			c.setMobileNumber(customer.getMobileNumber());
			c.setRole("ROLE_CUSTOMER");
			return customerRepo.save(c);
		} else {
			throw new RecordNotFoundException("Customer with id " + customer.getId() + " not found");
		}
	}

	@Override
	public void deleteCustomer(int id) {
		Optional<Customer> cu = customerRepo.findById(id);
		if (cu.isPresent()) {
			Customer c = cu.get();
			customerRepo.delete(c);
		} else {
			throw new RecordNotFoundException("Customer with id " + id + " not found");
		}

	}

	@Override
	public String getEmailById(int id) {
		Optional<Customer> c = customerRepo.findById(id);
		if (c.isPresent()) {
			Customer us = c.get();
			return us.getEmail();
		} else {
			throw new RecordNotFoundException("Customer with id " + id + " not found");
		}
	}

	@Override
	public List<Customer> searchCustomer(String search) {
		return customerRepo.searchCustomer(search);
	}

	@Override
	public Customer getCustomerById(int id) {
		Optional<Customer> cusDb = customerRepo.findById(id);
		if (cusDb.isPresent()) {
			Customer c = cusDb.get();
			return c;
		} else {
			throw new RecordNotFoundException("Customer with id " + id + " not found");
		}
	}

	@Override
	public Customer validateCustomer(String email, String password) {
		Customer customer = customerRepo.findByEmail(email);
		if (customer != null && customer.getPassword().equals(password)) {
			return customer;
		}
		return null;
	}

	@Override
	public Subscribe subscribeMail(Subscribe subscribe) {
		Subscribe newSub = subRepo.save(subscribe);
		try {
			MimeMessage mess = javamailSend.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mess, true);
			mimeMessageHelper.setFrom(fromEmail);
			mimeMessageHelper.setTo(subscribe.getEmail());
			mimeMessageHelper.setSubject("Welcome to Invoicify!");
			mimeMessageHelper.setText("<!DOCTYPE html>" + "<html lang='en'>" + "<head>" + "<meta charset='UTF-8'>"
					+ "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
					+ "<title>Welcome to Invoicify</title>" + "<style>"
					+ "body { font-family: 'Helvetica Neue', Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }"
					+ ".container { width: 90%; max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); overflow: hidden; }"
					+ ".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }"
					+ "h1 { margin: 0; font-size: 24px; }"
					+ "p { font-size: 16px; color: #333; line-height: 1.6; padding: 0 20px; }"
					+ "ul { list-style: none; padding: 0; margin: 20px 0; }"
					+ "ul li { margin: 15px 0; font-size: 14px; color: #555; padding-left: 20px; }"
					+ ".footer { margin-top: 20px; border-top: 1px solid #e0e0e0; padding: 10px 20px; font-size: 14px; color: #777; text-align: center; }"
					+ "img { width: 100%; border-top-left-radius: 8px; border-top-right-radius: 8px; }" + "</style>"
					+ "</head>" + "<body>" + "<div class='container'>"
					+ "<img src='https://img.freepik.com/free-vector/utility-bills-concept-illustration_114360-15641.jpg?t=st=1729437015~exp=1729440615~hmac=1f5e8ce0eb3a94b2e19d5bcb4d41a340fe1f2bc355a1a27cdd1c6f9222211230&w=740' alt='Invoicify Logo'>"
					+ "<div class='header'>" + "<h1>Welcome " + subscribe.getEmail().split("@")[0] + "!</h1>"

					+ "</div>" + "<p>Thank you for subscribing to Invoicify! We’re thrilled to have you on board.</p>"
					+ "<p><strong>What to Expect:</strong></p>" + "<ul>"
					+ "<li>Product Updates: Stay informed about the latest features and improvements.</li>"
					+ "<li>Helpful Resources: Access tips and tutorials to optimize your billing experience.</li>"
					+ "<li>Exclusive Insights: Get valuable information on best practices and industry trends.</li>"
					+ "</ul>"
					+ "<p>If you have any questions or need assistance, please don’t hesitate to reach out. We’re here to help you make the most of your Invoicify experience!</p>"
					+ "<div class='footer'>" + "<p>Best regards,</p>" + "<p>The Invoicify Team</p>"
					+ "<p>+91-1234567890</p>" + "</div>" + "</div>" + "</body>" + "</html>", true);
			javamailSend.send(mess);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return newSub;

	}

	@Override
	public String addToWallet(int customerid, Double amount) {
		if(amount<=0) {
			return "Amount should be greater than 0";
		}
		Customer customer = customerRepo.findById(customerid)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setWalletBalance(customer.getWalletBalance() + amount);
        customerRepo.save(customer);

        return "Wallet updated successfully. New balance: " + customer.getWalletBalance();
	}
}
