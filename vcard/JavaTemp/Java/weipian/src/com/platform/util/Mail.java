package com.platform.util;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.qq.com");
		props.put("mail.smtp.auth", "true");
		try {
			PopupAuthenticator auth = new PopupAuthenticator();
			Session session = Session.getInstance(props, auth);
			session.setDebug(true);
			MimeMessage message = new MimeMessage(session);
			Address addressFrom = new InternetAddress(
					PopupAuthenticator.mailuser + "@qq.com", "烂帽子！");
			Address addressTo = new InternetAddress("1184322396@qq.com", "LUO");
			message.setText("哈哈！烂帽子！");
			message.setSubject("Test sent Mail by javaMail");
			message.setFrom(addressFrom);
			message.addRecipient(Message.RecipientType.TO, addressTo);
			message.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.qq.com", PopupAuthenticator.mailuser,
					PopupAuthenticator.password);
			transport.send(message);
			transport.close();
			System.out.println("sent suc");
		} catch (Exception e) {
			System.out.println(e.toString());
			System.out.println("sent fail");
		}
	}
}

class PopupAuthenticator extends Authenticator {
	public static final String mailuser = "793187559";
	public static final String password = "suyulong54600000";

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(mailuser, password);
	}
}