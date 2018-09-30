package com.taotao.search.utils;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class SendMail {

	public static void sendEmail(String subject, String text) throws MessagingException {
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", "true");// 设置访问smtp服务器需要认证
		properties.setProperty("mail.transport.protocol", "smtp"); // 设置访问服务器的协议

		Session session = Session.getDefaultInstance(properties);
		session.setDebug(true); // 打开debug功能

		Message msg = new MimeMessage(session);
		// 这里填你登录163邮箱所用的用户名
		msg.setFrom(new InternetAddress("xxxxxxxx@163.com")); // 设置发件人，163邮箱要求发件人与登录用户必须一致（必填），其它邮箱不了解
		msg.setSubject(subject); // 设置邮件主题
		msg.setText(text); // 设置邮件内容

		Transport trans = session.getTransport();
		// 下面四个参数，前两个可以认为是固定的，不用变，后两个参数分别是登录163邮箱的用户名以及客户端授权密码（注意，不是登录密码）
		trans.connect("smtp.163.com", 25, "xxxxxxxx@163.com", "xxxxxxx"); // 连接邮箱smtp服务器，25为默认端口
		// 要发送到哪个邮箱，这里以qq邮箱为例
		trans.sendMessage(msg, new Address[] { new InternetAddress("xxxxxx@qq.com") }); // 发送邮件

		trans.close(); // 关闭连接
	}

	// 群发一封邮件
	public static void groupSendEmail(String subject, String content) throws MessagingException {
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", "smtp.163.com");

		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("xxxxxx@163.com", "xxxxxx");// 163邮箱用户名和客户端授权密码（注意，不是登录密码）
			}
		});
		session.setDebug(true);

		Message msg = new MimeMessage(session);
		// 邮件发送者
		msg.setFrom(new InternetAddress("xxxxxxx@163.com"));
		msg.setSubject(subject);
		// 注意第二个参数要写成"text/html;charset=utf-8"，表明这是一封html邮件
		msg.setContent(content, "text/html;charset=utf-8");
		// 要群发给哪些邮箱
		msg.setRecipients(RecipientType.TO, InternetAddress.parse("yyyyyyy@163.com,zzzzzz@qq.com"));

		Transport.send(msg);
	}

	public static void main(String[] args) {
		try {
			groupSendEmail("出现了异常，请及时处理！", "内容管理系统出现重大错误，请及时进行处理");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
