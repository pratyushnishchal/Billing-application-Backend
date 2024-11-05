package com.billingapplication.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.billingapplication.entity.ContactUs;
import com.billingapplication.repository.ContactUsRepository;
import com.billingapplication.service.ContactUsService;

import jakarta.mail.internet.MimeMessage;

@Service
public class ContactUsServiceImpl implements ContactUsService {
	@Autowired
	ContactUsRepository contactRepo;
	@Value("${spring.mail.username}")
	private String fromEmail;
	@Autowired
	JavaMailSender javamailSend;

	@Override
	public ContactUs askQuery(ContactUs contactUs) {
		ContactUs contactSub = contactRepo.save(contactUs);
		try {
			MimeMessage mess = javamailSend.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mess, true);
			helper.setTo(contactUs.getEmail());
			helper.setFrom(fromEmail);
			helper.setSubject("Thank You for Contacting Invoicify!");
			helper.setText("<!DOCTYPE html>" + "<html lang='en'>" + "<head>" + "<meta charset='UTF-8'>"
					+ "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
					+ "<title>Thank You for Contacting Us</title>" + "<style>"
					+ "body { font-family: 'Helvetica Neue', Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }"
					+ ".container { width: 90%; max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); overflow: hidden; }"
					+ ".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }"
					+ "h1 { margin: 0; font-size: 24px; }"
					+ "p { font-size: 16px; color: #333; line-height: 1.6; padding: 0 20px; }"
					+ ".footer { margin-top: 20px; border-top: 1px solid #e0e0e0; padding: 10px 20px; font-size: 14px; color: #777; text-align: center; }"
					+ "</style>" + "</head>" + "<body>" + "<div class='container'>" + "<div class='header'>"
					+ "<h1>Thank You for Contacting Us!</h1>" + "</div>" + "<p>Dear " + contactUs.getName() + ",</p>"
					+ "<p>Thank you for reaching out to <strong>Invoicify</strong>! We have received your inquiry and one of our representatives will get back to you within 24-48 hours.</p>"
					+ "<p>In the meantime, if you have any urgent questions, feel free to contact us at <strong>+91-1234567890</strong> or reply to this email.</p>"
					+ "<p>Best regards,</p>" + "<p><strong>Invoicify</strong></p>" + "<p>Bangalore</p>"
					+ "<div class='footer'>" + "<p>&copy; " + java.time.Year.now()
					+ " Invoicify. All rights reserved.</p>" + "</div>" + "</div>" + "</body>" + "</html>", true);
			javamailSend.send(mess);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return contactSub;
	}

}
