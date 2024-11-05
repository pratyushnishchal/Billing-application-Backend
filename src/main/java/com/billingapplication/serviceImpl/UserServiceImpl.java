package com.billingapplication.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.billingapplication.entity.User;
import com.billingapplication.exception.RecordNotFoundException;
import com.billingapplication.repository.UserRepository;
import com.billingapplication.service.UserService;

import jakarta.mail.internet.MimeMessage;

@Service
public class UserServiceImpl implements UserService{
	@Value("${spring.mail.username}")
	private String fromEmail;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	JavaMailSender javamailSend;
	@Override
	public User createAccountant(User user) {
		user.setRole("ROLE_ACCOUNTANT");
		User newAcc=userRepo.save(user);
		try {
			MimeMessage mess=javamailSend.createMimeMessage();
			MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mess,true);
			
			mimeMessageHelper.setFrom(fromEmail);
			mimeMessageHelper.setTo(user.getEmail());
			mimeMessageHelper.setSubject("Welcome to Our Billing Application!");
			mimeMessageHelper.setText("<div style='font-family: Arial, sans-serif; color: #333;'>"
			        + "<h1 style='color: #4CAF50;'>Welcome, " + user.getName() + "!</h1>"
			        + "<p style='font-size: 16px;'>Your account has been successfully created. Here are your login details:</p>"
			        + "<table border='1' cellpadding='10' cellspacing='0' style='border-collapse: collapse; width: 100%; max-width: 600px;'>"
			        + "  <tr>"
			        + "    <th style='background-color: #4CAF50; color: white; text-align: left;'>Username</th>"
			        + "    <td style='text-align: left;'>" + user.getEmail() + "</td>"
			        + "  </tr>"
			        + "  <tr>"
			        + "    <th style='background-color: #4CAF50; color: white; text-align: left;'>Password</th>"
			        + "    <td style='text-align: left;'>" + user.getPassword() + "</td>"
			        + "  </tr>"
			        + "</table>"
			        + "<br>"
			        + "<p style='font-size: 16px;'>Thank you for joining us!</p>"
			        + "<p style='font-size: 16px;'>Best Regards,<br><span style='font-weight: bold;'>Admin Team</span></p>"
			        + "<div style='margin-top: 20px; font-size: 12px; color: #999;'>"
			        + "<p>This is an automated email, please do not reply.</p>"
			        + "</div>"
			        + "</div>",true);
			javamailSend.send(mess);
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
		return newAcc;
	}

	@Override
	public boolean existsEmail(String email) {
		return userRepo.existsByEmail(email);
	}

	@Override
	public User updateAccountant(User user) {
		Optional<User> u=userRepo.findById(user.getId());
		if(u.isPresent()) {
			User us=u.get();
			us.setName(user.getName());
			us.setCity(user.getCity());
			us.setEmail(user.getEmail());
			us.setMobileNumber(user.getMobileNumber());
			us.setPassword(user.getPassword());
			us.setRole("ROLE_ACCOUNTANT");
			return userRepo.save(us);
		}
		else {
            throw new RecordNotFoundException("Accountant with id " + user.getId() + " not found");
        }
	}

	@Override
	public List<User> getAllAccountant(User user) {
		return userRepo.findAll();
	}

	@Override
	public void deleteAccountant(int id) {
		Optional<User> u=userRepo.findById(id);
		if(u.isPresent()) {
			User us=u.get();
			userRepo.delete(us);
		}
		else {
			throw new RecordNotFoundException("Accountant with id " + id + " not found");
		}
	}

	@Override
	public User validateAccountant(String email,String password) {
		User u=userRepo.findByEmail(email);
		if(u!=null && u.getPassword().equals(password)) {
			return u;
		}
		return null;
	}

	@Override
	public String getEmailById(int id) {
		Optional<User> u=userRepo.findById(id);
		if(u.isPresent()) {
			User us=u.get();
			return us.getEmail();
		}
		return null;
	}

	@Override
	public User getAccountantById(int id) {
		Optional<User> accDB=userRepo.findById(id);
		if(accDB.isPresent()) {
			User user=accDB.get();
			return user;
		}
		else {
			throw new RecordNotFoundException("Record Not found");
		}
	}

	

}
