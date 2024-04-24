package com.Anemoi.MailSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Anemoi.InvestorRelation.CashFlow.CashFlowEntity;
import com.Anemoi.InvestorRelation.ClientDetails.ClientDetailsEntity;
import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import com.Anemoi.InvestorRelation.DataIngestion.DataIngestionModel;
import com.Anemoi.InvestorRelation.MeetingScheduler.MSTeamschedule;
import com.Anemoi.InvestorRelation.ShareHolderDataFrom.ShareHolderDataFromQuaryConstant;
import com.Anemoi.InvestorRelation.ShareHolderDataFrom.ShareHolderDataFromServiceImpl;
import com.Anemoi.InvestorRelation.UserModel.UserEntity;

import jakarta.inject.Singleton;

@Singleton
public class MailServiceImpl implements MailService {
	
	private static String DATABASENAME = "databaseName";
	private static String dataBaseName() {

		List<String> tenentList = ReadPropertiesFile.getAllTenant();
		for (String tenent : tenentList) {
			DATABASENAME = ReadPropertiesFile.dataBaseName(tenent);
		}
		return DATABASENAME;

	}
	private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	
//	public void emailSendToAllMasterAdmin(UserEntity user, ArrayList<String> emailList) {
//
//		final String smtpHost = "apprelaycentral.pwcinternal.com";
//
//		final int smtpPort = 25;
//
//		Properties props = new Properties();
////		props.load(inputStream);
//
//		props.put("mail.smtp.host", smtpHost);
//
//		props.put("mail.smtp.port", smtpPort);
//
//		props.put("mail.smtp.starttls.enable", true);
//
//		Session session = Session.getInstance(props);
//
//		try
//
//		{
//
//			Message message = new MimeMessage(session);
//			message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));
//
//			for (String recipientEmail : emailList) {
//				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
//			}
//
//			message.setSubject(MailServiceConstant.SUBJECT_FOR_MASTER);
//			String name = user.getFirstName() + " " + user.getLastName();
//			String content = "<html><body>" + "<p>Dear All,</p>" + "<p>New master admin has been added.</p>"
//					+ "<p>Name: " + name + " ,</p>"
//					+ "<p>Link: <a href='https://ipzpvtgxaswv001.pwcglb.com/'>Portal Link</a></p>" + "<p>Regards,<br>"
//					+ "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";
//			message.setContent(content, "text/html; charset=utf-8");
//			System.out.println(message.toString());
//
//			Transport.send(message);
//
//			System.out.println("email sent successfully");
//
//		} catch (MessagingException e)
//
//		{
//
//			e.printStackTrace();
//
//		}
//	}

	public void emailSendToAllMasterAdmin1(UserEntity user, ArrayList<String> emailList) {
	    final String smtpHost = "apprelaycentral.pwcinternal.com";
	    final int smtpPort = 25;
	    Properties props = new Properties();
	    props.put("mail.smtp.host", smtpHost);
	    props.put("mail.smtp.port", smtpPort);
	    props.put("mail.smtp.starttls.enable", true);
	    Session session = Session.getInstance(props);
	 
	    try {
	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));
	 
	        for (String recipientEmail : emailList) {
	            if (isValidEmail(recipientEmail)) {
	                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
	            } else {
	                System.out.println("Skipping invalid email: " + recipientEmail);
	            }
	        }
	 
	        if (message.getAllRecipients().length == 0) {
	            System.out.println("No valid recipients found. Skipping sending email.");
	            return;
	        }
	 
	        message.setSubject(MailServiceConstant.SUBJECT_FOR_MASTER);
	        String name = user.getFirstName() + " " + user.getLastName();
	        String content = "<html><body>" + "<p>Dear All,</p>" + "<p>New master admin has been added.</p>"
	                + "<p>Name: " + name + " ,</p>"
	                + "<p>Link: <a href='https://ipzpvtgxaswv001.pwcglb.com/'>Portal Link</a></p>" + "<p>Regards,<br>"
	                + "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";
	        message.setContent(content, "text/html; charset=utf-8");
	        System.out.println(message.toString());
	 
	        Transport.send(message);
	 
	        System.out.println("Email sent successfully");
	    } catch (SendFailedException e) {
            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
            e.printStackTrace();
	 
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }
	}
	
	@Override
	public void emailSendToAllMasterAdmin(UserEntity user, ArrayList<String> emailList) {
        final String smtpHost = "apprelaycentral.pwcinternal.com";
        final int smtpPort = 25;
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.starttls.enable", "true");
 
        Session session = Session.getInstance(props);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));
 
            // Create a list to store valid recipients
            ArrayList<InternetAddress> validRecipients = new ArrayList<>();
 
            // Add valid recipients to the list
            for (String recipientEmail : emailList) {
                if (isValidEmail(recipientEmail)) {
                    validRecipients.add(new InternetAddress(recipientEmail));
                } else {
                    System.out.println("Skipping invalid email: " + recipientEmail);
                }
            }
 
            // Add valid recipients to the message
            if (!validRecipients.isEmpty()) {
                message.setRecipients(Message.RecipientType.TO, validRecipients.toArray(new InternetAddress[0]));
 
                // Set email subject and content
                message.setSubject(MailServiceConstant.SUBJECT_FOR_MASTER);
                String name = user.getFirstName() + " " + user.getLastName();
                String content = "<html><body>" + "<p>Dear All,</p>" + "<p>New master admin has been added.</p>"
                        + "<p>Name: " + name + " ,</p>"
                        + "<p>Link: <a href='https://ipzpvtgxaswv001.pwcglb.com/'>Portal Link</a></p>" + "<p>Regards,<br>"
                        + "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";
                message.setContent(content, "text/html; charset=utf-8");
 
                // Send the email
                Transport.send(message);
                System.out.println("Email sent successfully");
            } else {
                System.out.println("No valid recipients found. Skipping sending email.");
            }
        } catch (SendFailedException e) {
            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
            e.printStackTrace();
        } catch (MessagingException e) {
            System.out.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
	 
	private boolean isValidEmail(String email) {
	    // Check if email is not null and not empty
	    if (email == null || email.isEmpty()) {
	        return false;
	    }
	    // Use JavaMail API to validate email addresses
	    try {
	        InternetAddress internetAddress = new InternetAddress(email);
	        internetAddress.validate();
	        return true;
	    } catch (AddressException ex) {
	        return false;
	    }
	}
	
	@Override
	public void sendLineItemAddNotification(Transport transport, String createdBy, String tableName,
			List<String> lineItems) {
		// TODO Auto-generated method stub
		Session session = MailSessionInstance.getMailSession();

		logger.debug("got the session object");
		MimeMessage message = new MimeMessage(session);
		String userName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.USER_NAME);
		String password1 = ReadPropertiesFile.readRequestProperty(MailServiceConstant.PWD);
		String userrollName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.ROLLNAME);
		logger.debug("username :: " + userName);
		logger.debug("username :: " + userrollName);
		try {
			message.setFrom(new InternetAddress(userName));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(createdBy));

//			message.setSubject(MailServiceConstant.MSG_FOR_MASTER);
			message.setSubject("Sending Notification");

			String lineItemslist = String.join(", ", lineItems);

			String content = MailServiceConstant.MASTERDATABASENOTIFICATION_BODY
					.replace(MailServiceConstant.LINEITEM_SIZE, String.valueOf(lineItems.size()))
					.replace(MailServiceConstant.TABLENAME, tableName)
					.replace(MailServiceConstant.LINEITEM_NAME, lineItemslist);
			System.out.println("content" + lineItemslist);
			message.setContent(content, MailServiceConstant.CONTENT_TYPE_HTML);
			logger.debug("sending the mail............");
			long start = System.currentTimeMillis();
			transport.sendMessage(message, message.getAllRecipients());
			long end = System.currentTimeMillis();
			logger.debug("mail sent in :" + (end - start) + "ms");
			logger.debug("first time registration mail successfully send to user : ");
		  } catch (SendFailedException e) {
	            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
	            e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void updateMasterAdminsendMailToconcernMasterAdmin(UserEntity user, ArrayList<String> emailList) {

		final String smtpHost = "apprelaycentral.pwcinternal.com";

		final int smtpPort = 25;

		Properties props = new Properties();
//		props.load(inputStream);

		props.put("mail.smtp.host", smtpHost);

		props.put("mail.smtp.port", smtpPort);

		props.put("mail.smtp.starttls.enable", true);

		Session session = Session.getInstance(props);

		try

		{

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));

			for (String ccRecipientEmail : emailList) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
			}

			message.setSubject(MailServiceConstant.UPDATE_SUBJECT_FORMATSER);
			String name = user.getFirstName() + " " + user.getLastName();
//			        logger.debug("Name: " + name);

			String content = "<html><body>" + "<p> Dear " + name + ",</p>"
					+ "<p>Your account details have been modified.</p>"
					+ "<p>Link: <a href='https://ipzpvtgxaswv001.pwcglb.com/'>Portal Link</a></p>" + "<p>Regards,<br>"
					+ "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";
			message.setContent(content, "text/html; charset=utf-8");
			System.out.println(message.toString());

			Transport.send(message);

			System.out.println("email sent successfully");
		  } catch (SendFailedException e) {
	            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
	            e.printStackTrace();
		} catch (MessagingException e)

		{

			e.printStackTrace();

		}
//		 Session session = MailSessionInstance.getMailSession();
//		    logger.debug("Got the session object");  
//		    MimeMessage message = new MimeMessage(session);
//		    
//		    String userName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.USER_NAME);
//		    String password1 = ReadPropertiesFile.readRequestProperty(MailServiceConstant.PWD);
//		    
//		    try {
//		        message.setFrom(new InternetAddress(userName));
//		    
//		            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
//		       
//		            for (String ccRecipientEmail : emailList) {
//		                message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
//		            }
//	
//		        message.setSubject(MailServiceConstant.UPDATE_SUBJECT_FORMATSER);
//		        String name = user.getFirstName() + " " + user.getLastName();
//		        logger.debug("Name: " + name);
//		        String portalLink=ReadPropertiesFile.readResponseProperty("portalLink");
//
//		        String content= "<html><body>"
//			            + "<p> Dear " +  name  +",</p>"
//			            + "<p>Your account details have been modified.</p>"
//			            + "<p>Link: <a href='" + ReadPropertiesFile.readResponseProperty("portalLink") + "'>Portal Link</a></p>"
//			            + "<p>Regards,<br>"
//			            + "Capital Market Advisory Team<br>"
//			            + "PwC</p>"
//			            + "</body></html>";
//		        
//		        message.setContent(content, "text/html; charset=utf-8");
//		        
//		        long start = System.currentTimeMillis();
//		        transport.sendMessage(message, message.getAllRecipients());
//		        long end = System.currentTimeMillis();
//		        
//		        logger.debug("Mail sent in: " + (end - start) + "ms");
//		        System.out.println("Email sent successfully.");
//		        
//		    } catch (AddressException e) {
//		        e.printStackTrace();
//		    } catch (MessagingException e) {
//		        e.printStackTrace();
//		    }

	}

	@Override
//	public void emailSendToAnalstAdminAndCCAllMasterAdmin(UserEntity user, ArrayList<String> emailList) {
//
//		final String smtpHost = "apprelaycentral.pwcinternal.com";
//
//		final int smtpPort = 25;
//
//		Properties props = new Properties();
////		props.load(inputStream);
//
//		props.put("mail.smtp.host", smtpHost);
//
//		props.put("mail.smtp.port", smtpPort);
//
//		props.put("mail.smtp.starttls.enable", true);
//
//		Session session = Session.getInstance(props);
//
//		try
//
//		{
//
//			Message message = new MimeMessage(session);
//			message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));
//
//			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
//
//			for (String ccRecipientEmail : emailList) {
//				message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
//			}
//
//			message.setSubject(MailServiceConstant.SUBJECT_FOR_ANALYST);
//			String name = user.getFirstName() + " " + user.getLastName();
//
//			String content = "<html><body>" + "<p>Dear All,</p>" + "<p>New analyst admin has been added.</p>"
//					+ "<p>Dear " + name + " ,</p>"
//					+ "<p>Link: <a href='https://ipzpvtgxaswv001.pwcglb.com/'>Portal Link</a></p>" + "<p>Regards,<br>"
//					+ "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";
//
//			message.setContent(content, "text/html; charset=utf-8");
//
//			Transport.send(message);
//
//			System.out.println("email sent successfully");
//
//		} catch (MessagingException e)
//
//		{
//
//			e.printStackTrace();
//
//		}
//	}
	
	
	public void emailSendToAnalstAdminAndCCAllMasterAdmin(UserEntity user, ArrayList<String> emailList) {
	    final String smtpHost = "apprelaycentral.pwcinternal.com";
	    final int smtpPort = 25;
	    Properties props = new Properties();
	    props.put("mail.smtp.host", smtpHost);
	    props.put("mail.smtp.port", smtpPort);
	    props.put("mail.smtp.starttls.enable", true);
	    Session session = Session.getInstance(props);
	 
	    try {
	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));
	 
	        // Add the main recipient (user's email)
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
	 
	        // Add CC recipients
	        for (String ccRecipientEmail : emailList) {
	            if (isValidEmail(ccRecipientEmail)) {
	                message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
	            } else {
	                System.out.println("Skipping invalid CC email: " + ccRecipientEmail);
	            }
	        }
	 
	        if (message.getAllRecipients().length == 0) {
	            System.out.println("No valid CC recipients found. Skipping sending email.");
	            return;
	        }
	 
	        message.setSubject(MailServiceConstant.SUBJECT_FOR_ANALYST);
	        String name = user.getFirstName() + " " + user.getLastName();
	 
	        String content = "<html><body>" + "<p>Dear All,</p>" + "<p>New analyst admin has been added.</p>"
	                + "<p>Dear " + name + " ,</p>"
	                + "<p>Link: <a href='https://ipzpvtgxaswv001.pwcglb.com/'>Portal Link</a></p>" + "<p>Regards,<br>"
	                + "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";
	 
	        message.setContent(content, "text/html; charset=utf-8");
	 
	        Transport.send(message);
	 
	        System.out.println("Email sent successfully");
	    } catch (SendFailedException e) {
            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
            e.printStackTrace();
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }
	}
	 
	
	
	
//		 Session session = MailSessionInstance.getMailSession();
//		    logger.debug("Got the session object");  
//		    MimeMessage message = new MimeMessage(session);
//		    
//		    String userName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.USER_NAME);
//		    String password1 = ReadPropertiesFile.readRequestProperty(MailServiceConstant.PWD);
//		    
//		    try {
//		        message.setFrom(new InternetAddress(userName));
//		    
//		            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
//		       
//		            for (String ccRecipientEmail : emailList) {
//		                message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
//		            }
//	
//		        message.setSubject(MailServiceConstant.SUBJECT_FOR_ANALYST);
//		        String name = user.getFirstName() + " " + user.getLastName();
//		        logger.debug("Name: " + name);
//		        String portalLink=ReadPropertiesFile.readResponseProperty("portalLink");
//            
//		        String content= "<html><body>"
//			            + "<p>Dear All,</p>"
//			            + "<p>New analyst admin has been added.</p>"
//			            + "<p>Dear " +name+ " ,</p>"
//			            + "<p>Link: <a href='" + ReadPropertiesFile.readResponseProperty("portalLink") + "'>Portal Link</a></p>"
//			            + "<p>Regards,<br>"
//			            + "Capital Market Advisory Team<br>"
//			            + "PwC</p>"
//			            + "</body></html>";
//
//		        message.setContent(content, "text/html; charset=utf-8");
//		        
//		        long start = System.currentTimeMillis();
//		        transport.sendMessage(message, message.getAllRecipients());
//		        long end = System.currentTimeMillis();
//		        
//		        logger.debug("Mail sent in: " + (end - start) + "ms");
//		        System.out.println("Email sent successfully.");
//		        
//		    } catch (AddressException e) {
//		        e.printStackTrace();
//		    } catch (MessagingException e) {
//		        e.printStackTrace();
//		    }

//	}

	@Override
//	public void updateAnalystAdminmailtoAnalsytAdminAndAllMasterAdmins(UserEntity user, ArrayList<String> emailList) {
//
//		final String smtpHost = "apprelaycentral.pwcinternal.com";
//
//		final int smtpPort = 25;
//
//		Properties props = new Properties();
////		props.load(inputStream);
//
//		props.put("mail.smtp.host", smtpHost);
//
//		props.put("mail.smtp.port", smtpPort);
//
//		props.put("mail.smtp.starttls.enable", true);
//
//		Session session = Session.getInstance(props);
//
//		try
//
//		{
//
//			Message message = new MimeMessage(session);
//			message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));
//
//			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
//
//			for (String ccRecipientEmail : emailList) {
//				message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
//			}
//
//			message.setSubject(MailServiceConstant.UPDATE_SUBJECTFOR_ANALYST);
//			String name = user.getFirstName() + " " + user.getLastName();
////		        logger.debug("Name: " + name);
//
//			String portalLink = ReadPropertiesFile.readResponseProperty("portalLink");
//
//			String content = "<html><body>" + "<p> Dear " + name + ",</p>"
//					+ "<p>Your account details have been modified.</p>"
//					+ "<p>Link: <a href='https://ipzpvtgxaswv001.pwcglb.com/'>Portal Link</a></p>" + "<p>Regards,<br>"
//					+ "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";
//
//			message.setContent(content, "text/html; charset=utf-8");
//
//			Transport.send(message);
//
//			System.out.println("email sent successfully");
//
//		} catch (MessagingException e)
//
//		{
//
//			e.printStackTrace();
//
//		}
	
	
	public void updateAnalystAdminmailtoAnalsytAdminAndAllMasterAdmins(UserEntity user, ArrayList<String> emailList) {
	    final String smtpHost = "apprelaycentral.pwcinternal.com";
	    final int smtpPort = 25;
	    Properties props = new Properties();
	    props.put("mail.smtp.host", smtpHost);
	    props.put("mail.smtp.port", smtpPort);
	    props.put("mail.smtp.starttls.enable", true);
	    Session session = Session.getInstance(props);
	 
	    try {
	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));
	 
	        // Add the main recipient (user's email)
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
	 
	        // Add CC recipients
	        for (String ccRecipientEmail : emailList) {
	            if (isValidEmail(ccRecipientEmail)) {
	                message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
	            } else {
	                System.out.println("Skipping invalid CC email: " + ccRecipientEmail);
	            }
	        }
	 
	        if (message.getAllRecipients().length == 0) {
	            System.out.println("No valid CC recipients found. Skipping sending email.");
	            return;
	        }
	 
	        message.setSubject(MailServiceConstant.UPDATE_SUBJECTFOR_ANALYST);
	        String name = user.getFirstName() + " " + user.getLastName();
	 
	        String portalLink = ReadPropertiesFile.readResponseProperty("portalLink");
	 
	        String content = "<html><body>" + "<p> Dear " + name + ",</p>"
	                + "<p>Your account details have been modified.</p>"
	                + "<p>Link: <a href='https://ipzpvtgxaswv001.pwcglb.com/'>Portal Link</a></p>" + "<p>Regards,<br>"
	                + "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";
	 
	        message.setContent(content, "text/html; charset=utf-8");
	 
	        Transport.send(message);
	 
	        System.out.println("Email sent successfully");
	    } catch (SendFailedException e) {
            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
            e.printStackTrace();
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }
	}
	 
	

//		 Session session = MailSessionInstance.getMailSession();
//		    logger.debug("Got the session object");  
//		    MimeMessage message = new MimeMessage(session);
//		    
//		    String userName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.USER_NAME);
//		    String password1 = ReadPropertiesFile.readRequestProperty(MailServiceConstant.PWD);
//		    
//		    try {
//		        message.setFrom(new InternetAddress(userName));
//		    
//		            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
//		       
//		            for (String ccRecipientEmail : emailList) {
//		                message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
//		            }
//	
//		        message.setSubject(MailServiceConstant.UPDATE_SUBJECTFOR_ANALYST);
//		        String name = user.getFirstName() + " " + user.getLastName();
//		        logger.debug("Name: " + name);
//		        
//		        String portalLink=ReadPropertiesFile.readResponseProperty("portalLink");
//
//		        String content= "<html><body>"
//			            + "<p> Dear " + name +",</p>"
//			            + "<p>Your account details have been modified.</p>"
//			            + "<p>Link: <a href='" + ReadPropertiesFile.readResponseProperty("portalLink") + "'>Portal Link</a></p>"
//			            + "<p>Regards,<br>"
//			            + "Capital Market Advisory Team<br>"
//			            + "PwC</p>"
//			            + "</body></html>";
//
//		        message.setContent(content, "text/html; charset=utf-8");
//		        
//		        long start = System.currentTimeMillis();
//		        transport.sendMessage(message, message.getAllRecipients());
//		        long end = System.currentTimeMillis();
//		        
//		        logger.debug("Mail sent in: " + (end - start) + "ms");
//		        System.out.println("Email sent successfully.");
//		        
//		    } catch (AddressException e) {
//		        e.printStackTrace();
//		    } catch (MessagingException e) {
//		        e.printStackTrace();
//		    }
//		

	

	@Override
	public void newMeetingScheduleMailSendToClientAdmin(Transport transport, MSTeamschedule msts,
			List<String> clientAdmins, List<String> analystAdmins) {
		// TODO Auto-generated method stub
		Session session = MailSessionInstance.getMailSession();
		logger.debug("Got the session object");
		MimeMessage message = new MimeMessage(session);

		String userName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.USER_NAME);
		String password1 = ReadPropertiesFile.readRequestProperty(MailServiceConstant.PWD);

		try {
			message.setFrom(new InternetAddress(userName));

			for (String recipientEmail : clientAdmins) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
			}
			for (String ccRecipientEmail : analystAdmins) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
			}

			message.setSubject(MailServiceConstant.SUBJECT_FOR_NEWMEETING_SCHEDULE);
			String participatName = String.join(", ", msts.getParticipant());

			String content = "<html><body>" + "<p>Dear All,</p>" + "<p>A new meeting has been scheduled.</p>"
					+ "<p> Title : " + msts.getTitle() + "</p>" + "<p> Date & Time : {" + msts.getMeetingDate()
					+ "} {StartTime: " + msts.getStartTime() + " EndTime:" + msts.getEndTime() + "}</p>"
					+ "<p> Participants:{ " + participatName + "} </p>" + "<p>Link: <a href='"
					+ ReadPropertiesFile.readResponseProperty("portalLink") + "'>Portal Link</a></p>"
					+ "<p>Regards,<br>" + "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";
			message.setContent(content, "text/html; charset=utf-8");

			long start = System.currentTimeMillis();
			transport.sendMessage(message, message.getAllRecipients());
			long end = System.currentTimeMillis();

			logger.debug("Mail sent in: " + (end - start) + "ms");
			System.out.println("Email sent successfully.");
		  } catch (SendFailedException e) {
	            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
	            e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateMeetingMailSendToClientAdmin(Transport transport, MSTeamschedule teamschedule,
			List<String> clientAdmins, List<String> analystAdmins) {
		Session session = MailSessionInstance.getMailSession();
		logger.debug("Got the session object");
		MimeMessage message = new MimeMessage(session);

		String userName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.USER_NAME);
		String password1 = ReadPropertiesFile.readRequestProperty(MailServiceConstant.PWD);

		try {
			message.setFrom(new InternetAddress(userName));

			for (String recipientEmail : clientAdmins) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
			}
			for (String ccRecipientEmail : analystAdmins) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
			}

			message.setSubject(MailServiceConstant.SUBJECT_FOR_UPDATEMEETING);
			String participatName = String.join(", ", teamschedule.getParticipant());

			String content = "<html><body>" + "<p>Dear All,</p>"
					+ "<p>Following modifications are made in the meeting data.</p>" + "<p> Title : "
					+ teamschedule.getTitle() + "</p>" + "<p> Date & Time : {" + teamschedule.getMeetingDate()
					+ "} {StartTime: " + teamschedule.getStartTime() + " EndTime:" + teamschedule.getEndTime() + "}</p>"
					+ "<p> Participants:{ " + participatName + "} </p>" + "<p>Link: <a href='"
					+ ReadPropertiesFile.readResponseProperty("portalLink") + "'>Portal Link</a></p>"
					+ "<p>Regards,<br>" + "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";
			message.setContent(content, "text/html; charset=utf-8");

			long start = System.currentTimeMillis();
			transport.sendMessage(message, message.getAllRecipients());
			long end = System.currentTimeMillis();

			logger.debug("Mail sent in: " + (end - start) + "ms");
			System.out.println("Email sent successfully.");
		  } catch (SendFailedException e) {
	            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
	            e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void uploadFileMailSendToClientAdminCCAnalystAdmin(DataIngestionModel ingestionModel,
			List<String> clientAdmins, List<String> analystAdmins) {
//		Session session = MailSessionInstance.getMailSession();
//	    logger.debug("Got the session object");  
//	    MimeMessage message = new MimeMessage(session);
//	    
//	    String userName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.USER_NAME);
//	    String password1 = ReadPropertiesFile.readRequestProperty(MailServiceConstant.PWD);
//	    
//	    try {
//	        message.setFrom(new InternetAddress(userName));

		final String smtpHost = "apprelaycentral.pwcinternal.com";

		final int smtpPort = 25;

		Properties props = new Properties();
//		props.load(inputStream);

		props.put("mail.smtp.host", smtpHost);

		props.put("mail.smtp.port", smtpPort);

		props.put("mail.smtp.starttls.enable", true);

		Session session = Session.getInstance(props);

		try

		{

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));

			for (String recipientEmail : clientAdmins) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
			}
			for (String ccRecipientEmail : analystAdmins) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
			}

			message.setSubject(MailServiceConstant.SUBJECT_FOR_FILE_UPLOADED);

			String content = "<html><body>" + "<p>Dear All,</p>" + "<p> " + ingestionModel.getDocumentType() + " for "
					+ ingestionModel.getReportDate() + " has been uploaded</p>"
					+ "<p>Link: <a href='https://ipzpvtgxaswv001.pwcglb.com/'>Portal Link</a></p>" + "<p>Regards,<br>"
					+ "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";
			message.setContent(content, "text/html; charset=utf-8");

			Transport.send(message);
			System.out.println("Email sent successfully.");
		  } catch (SendFailedException e) {
	            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
	            e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void clientAdminCCmasterAdminandAnalystAdmin(ClientDetailsEntity detailsEntity,
			ArrayList<String> usernameList) {
		// TODO Auto-generated method stub
//		 Session session = MailSessionInstance.getMailSession();
//		    logger.debug("Got the session object");  
//		    MimeMessage message = new MimeMessage(session);
//		    
//		    String userName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.USER_NAME);
//		    String password1 = ReadPropertiesFile.readRequestProperty(MailServiceConstant.PWD);
//		    
//		    try {
//		        message.setFrom(new InternetAddress(userName));

		final String smtpHost = "apprelaycentral.pwcinternal.com";

		final int smtpPort = 25;

		Properties props = new Properties();
//		props.load(inputStream);

		props.put("mail.smtp.host", smtpHost);

		props.put("mail.smtp.port", smtpPort);

		props.put("mail.smtp.starttls.enable", true);

		Session session = Session.getInstance(props);

		try

		{

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));

			List<String> emailIDs = detailsEntity.getEmailId();
			for (String email : emailIDs) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			}
			List<String> anaystAdmin = detailsEntity.getAssignAA();
			message.addRecipient(Message.RecipientType.CC, new InternetAddress(detailsEntity.getCreatedBy()));

			for (String ccRecipientEmail : anaystAdmin) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
			}

			message.setSubject(MailServiceConstant.SUBJECT_FOR_CLIENTADMIN);
			String clientName = String.join(", ", usernameList);
			logger.debug("Name: " + clientName);
			String portalLink = ReadPropertiesFile.readResponseProperty("portalLink");

			String content = "<html><body>" + "<p> Dear All,</p>" + "<p>New client admin has been added.</p>"
					+ "<p>Name : {" + clientName + " } </p>"
					+ "<p>Link: <a href='https://ipzpvtgxaswv001.pwcglb.com/'>Portal Link</a></p>" + "<p>Regards,<br>"
					+ "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";

			message.setContent(content, "text/html; charset=utf-8");

			Transport.send(message);

			System.out.println("Email sent successfully.");
		  } catch (SendFailedException e) {
	            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
	            e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void clientAdminCCAnalystAdmin(ClientDetailsEntity detailsEntity, ArrayList<String> usernameList) {
//		 Session session = MailSessionInstance.getMailSession();
//		    logger.debug("Got the session object");  
//		    MimeMessage message = new MimeMessage(session);
//		    
//		    String userName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.USER_NAME);
//		    String password1 = ReadPropertiesFile.readRequestProperty(MailServiceConstant.PWD);
//		    
//		    try {
//		        message.setFrom(new InternetAddress(userName));

		final String smtpHost = "apprelaycentral.pwcinternal.com";

		final int smtpPort = 25;

		Properties props = new Properties();
//		props.load(inputStream);

		props.put("mail.smtp.host", smtpHost);

		props.put("mail.smtp.port", smtpPort);

		props.put("mail.smtp.starttls.enable", true);

		Session session = Session.getInstance(props);

		try

		{

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));

			List<String> emailIDs = detailsEntity.getEmailId();
			for (String email : emailIDs) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			}
			List<String> anaystAdmin = detailsEntity.getAssignAA();

			for (String ccRecipientEmail : anaystAdmin) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
			}

			message.setSubject(MailServiceConstant.SUBJECT_FOR_CLIENTADMIN);
			String clientName = String.join(", ", usernameList);
			logger.debug("Name: " + clientName);
			String portalLink = ReadPropertiesFile.readResponseProperty("portalLink");

			String content = "<html><body>" + "<p> Dear All,</p>" + "<p>New client admin has been added.</p>"
					+ "<p>Name : {" + clientName + " } </p>"
					+ "<p>Link: <a href='https://ipzpvtgxaswv001.pwcglb.com/'>Portal Link</a></p>" + "<p>Regards,<br>"
					+ "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";

			message.setContent(content, "text/html; charset=utf-8");
			Transport.send(message);
			System.out.println("Email sent successfully.");
		  } catch (SendFailedException e) {
	            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
	            e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void updateClientAdminCCMasterAdminAndAnalystAdmin(ClientDetailsEntity clientDetailsEntity,
			ArrayList<String> usernameList) {
		// TODO Auto-generated method stub
//		 Session session = MailSessionInstance.getMailSession();
//		    logger.debug("Got the session object");  
//		    MimeMessage message = new MimeMessage(session);
//		    
//		    String userName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.USER_NAME);
//		    String password1 = ReadPropertiesFile.readRequestProperty(MailServiceConstant.PWD);
//		    
//		    try {
//		        message.setFrom(new InternetAddress(userName));

		final String smtpHost = "apprelaycentral.pwcinternal.com";

		final int smtpPort = 25;

		Properties props = new Properties();
//		props.load(inputStream);

		props.put("mail.smtp.host", smtpHost);

		props.put("mail.smtp.port", smtpPort);

		props.put("mail.smtp.starttls.enable", true);

		Session session = Session.getInstance(props);

		try

		{

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));

			List<String> emailIDs = clientDetailsEntity.getEmailId();
			for (String email : emailIDs) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			}
			List<String> anaystAdmin = clientDetailsEntity.getAssignAA();
			message.addRecipient(Message.RecipientType.CC, new InternetAddress(clientDetailsEntity.getModifiedBy()));

			for (String ccRecipientEmail : anaystAdmin) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
			}

			message.setSubject(MailServiceConstant.SUBJECT_FOR_UPDATECLIENTADMIN);
			String clientName = String.join(", ", usernameList);
			logger.debug("Name: " + clientName);

			String content = "<html><body>" + "<p> Dear {" + clientName + " },</p>"
					+ "<p>Your account details have been modified.</p>"
					+ "<p>Link: <a href='https://ipzpvtgxaswv001.pwcglb.com/'>Portal Link</a></p>" + "<p>Regards,<br>"
					+ "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";

			message.setContent(content, "text/html; charset=utf-8");

			Transport.send(message);
			System.out.println("Email sent successfully.");
		  } catch (SendFailedException e) {
	            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
	            e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateclientAdminCCAnalystAdmin(ClientDetailsEntity clientDetailsEntity,
			ArrayList<String> usernameList) {
		// TODO Auto-generated method stub
//		 Session session = MailSessionInstance.getMailSession();
//		    logger.debug("Got the session object");  
//		    MimeMessage message = new MimeMessage(session);
//		    
//		    String userName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.USER_NAME);
//		    String password1 = ReadPropertiesFile.readRequestProperty(MailServiceConstant.PWD);
//		    
//		    try {
//		        message.setFrom(new InternetAddress(userName));

		final String smtpHost = "apprelaycentral.pwcinternal.com";

		final int smtpPort = 25;

		Properties props = new Properties();
//		props.load(inputStream);

		props.put("mail.smtp.host", smtpHost);

		props.put("mail.smtp.port", smtpPort);

		props.put("mail.smtp.starttls.enable", true);

		Session session = Session.getInstance(props);

		try

		{

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));

			List<String> emailIDs = clientDetailsEntity.getEmailId();
			for (String email : emailIDs) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			}
			List<String> anaystAdmin = clientDetailsEntity.getAssignAA();

			for (String ccRecipientEmail : anaystAdmin) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
			}

			message.setSubject(MailServiceConstant.SUBJECT_FOR_UPDATECLIENTADMIN);
			String clientName = String.join(", ", usernameList);
			logger.debug("Name: " + clientName);
			String portalLink = ReadPropertiesFile.readResponseProperty("portalLink");

			String content = "<html><body>" + "<p> Dear {" + clientName + " },</p>"
					+ "<p>Your account details have been modified.</p>"
					+ "<p>Link: <a href='https://ipzpvtgxaswv001.pwcglb.com/'>Portal Link</a></p>" + "<p>Regards,<br>"
					+ "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";

			message.setContent(content, "text/html; charset=utf-8");

			Transport.send(message);
			System.out.println("Email sent successfully.");
		  } catch (SendFailedException e) {
	            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
	            e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void MasterDatabaseMail(ArrayList<String> analystAdminEmails, ArrayList<String> masterAdminEmails,
			ArrayList<String> tablesName) {
//		Session session = MailSessionInstance.getMailSession();
//	    logger.debug("Got the session object");  
//	    MimeMessage message = new MimeMessage(session);
//	    
//	    String userName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.USER_NAME);
//	    String password1 = ReadPropertiesFile.readRequestProperty(MailServiceConstant.PWD);
//	    
//	    try {
//	        message.setFrom(new InternetAddress(userName));

		final String smtpHost = "apprelaycentral.pwcinternal.com";

		final int smtpPort = 25;

		Properties props = new Properties();
//		props.load(inputStream);

		props.put("mail.smtp.host", smtpHost);

		props.put("mail.smtp.port", smtpPort);

		props.put("mail.smtp.starttls.enable", true);

		Session session = Session.getInstance(props);

		try

		{

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));

			for (String toEmail : analystAdminEmails) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			}
			for (String ccRecipientEmail : masterAdminEmails) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
			}
			message.setSubject(MailServiceConstant.SUBJECT_FOR_MASTERDATABASE);

			String tableName = String.join(", ", tablesName);
			String portalLink = ReadPropertiesFile.readResponseProperty("portalLink");

			String content = "<html><body>" + "<p>Dear All,</p>"
					+ "<p>Addition / Modification to master database has been made.</p>" + "<p>DB:{ " + tableName
					+ "}</p>" + "<p>Link: <a href='https://ipzpvtgxaswv001.pwcglb.com/'>Portal Link</a></p>"
					+ "<p>Regards,<br>" + "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";

			message.setContent(content, "text/html; charset=utf-8");
			Transport.send(message);
			System.out.println("Email sent successfully.");
		  } catch (SendFailedException e) {
	            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
	            e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void lineItemAddAnalystLineItem(ArrayList<String> analystAdminEmails, ArrayList<String> masterAdminEmails,
			Set<String> masterTableName) {
		// TODO Auto-generated method stub
//		 Session session = MailSessionInstance.getMailSession();
//		    logger.debug("Got the session object");  
//		    MimeMessage message = new MimeMessage(session);
//		    
//		    String userName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.USER_NAME);
//		    String password1 = ReadPropertiesFile.readRequestProperty(MailServiceConstant.PWD);
//		    
//		    try {
//		        message.setFrom(new InternetAddress(userName));
//		        

		final String smtpHost = "apprelaycentral.pwcinternal.com";

		final int smtpPort = 25;

		Properties props = new Properties();
//		props.load(inputStream);

		props.put("mail.smtp.host", smtpHost);

		props.put("mail.smtp.port", smtpPort);

		props.put("mail.smtp.starttls.enable", true);

		Session session = Session.getInstance(props);

		try

		{

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));

			for (String toEmail : analystAdminEmails) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			}
			for (String ccRecipientEmail : masterAdminEmails) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipientEmail));
			}

			message.setSubject(MailServiceConstant.SUBJECT_FOR_ANALYSTSETUP);
			String tablename = String.join(", ", masterTableName);

			String content = "<html><body>" + "<p>Dear All,</p>"
					+ "<p>Addition / Modification in brokerage firms has been made</p>" + "<p> DB :{ " + tablename
					+ " }</p>" + "<p>Link: <a href='https://ipzpvtgxaswv001.pwcglb.com/'>Portal Link</a></p>"
					+ "<p>Regards,<br>" + "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";
			message.setContent(content, "text/html; charset=utf-8");
			Transport.send(message);
			System.out.println("Email sent successfully.");
		  } catch (SendFailedException e) {
	            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
	            e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addclientlineItemnotification(String clientName, String email,
			ArrayList<String> analystAdminForthisClient, Set<String> masterName) {
//		Session session = MailSessionInstance.getMailSession();
//	    logger.debug("Got the session object");  
//	    MimeMessage message = new MimeMessage(session);
//	    
//	    String userName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.USER_NAME);
//	    String password1 = ReadPropertiesFile.readRequestProperty(MailServiceConstant.PWD);
//	    
//	    try {
//	        message.setFrom(new InternetAddress(userName));

		final String smtpHost = "apprelaycentral.pwcinternal.com";

		final int smtpPort = 25;

		Properties props = new Properties();
//		props.load(inputStream);

		props.put("mail.smtp.host", smtpHost);

		props.put("mail.smtp.port", smtpPort);

		props.put("mail.smtp.starttls.enable", true);

		Session session = Session.getInstance(props);

		try

		{

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));

			for (String toemial : analystAdminForthisClient) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(toemial));
			}

			message.addRecipient(Message.RecipientType.CC, new InternetAddress(email));

			message.setSubject(MailServiceConstant.SUBJECT_FOR_CLIENTSETUP);
			String tablename = String.join(", ", masterName);

			String content = "<html><body>" + "<p>Dear All,</p>" + "<p>Addition / Modification in client " + clientName
					+ "   has beed made,</p>" + "<p>DB :{ " + tablename + "}</p>"
					+ "<p>Link: <a href='https://ipzpvtgxaswv001.pwcglb.com/'>Portal Link</a></p>" + "<p>Regards,<br>"
					+ "Capital Market Advisory Team<br>" + "PwC</p>" + "</body></html>";

			message.setContent(content, "text/html; charset=utf-8");
			Transport.send(message);
			System.out.println("Email sent successfully.");
		  } catch (SendFailedException e) {
	            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
	            e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void uploadShareHolderDataExcelSheetToSendMail(String msg,String createdBy,String status, String region) throws ClassNotFoundException {
		try

		{
		final String smtpHost = "apprelaycentral.pwcinternal.com";
		PreparedStatement preStatement=null;
		Connection connection=null;
		
		final int smtpPort = 25;

		Properties props = new Properties();
//		props.load(inputStream);

		props.put("mail.smtp.host", smtpHost);

		props.put("mail.smtp.port", smtpPort);

		props.put("mail.smtp.starttls.enable", true);

		Session session = Session.getInstance(props);

		

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(createdBy));
 
			message.setSubject("Investor Relation");

			message.setText(" Dear User,\r\n"
					+ "  \r\n"
					+ " The 'Shareholder data' file which you have uploaded has processed successfully, kindly refresh the application and check.");

		


				System.out.println(message.toString());


				Transport.send(message);
				Date date=new Date();
				String dataBaseName = MailServiceImpl.dataBaseName();
				connection=InvestorDatabaseUtill.getConnection();
				preStatement=connection.prepareStatement(ShareHolderDataFromQuaryConstant.INSERT_INTO_MAILDATA
						.replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				preStatement.setString(1, msg);
				preStatement.setString(2,createdBy);
				preStatement.setString(3,status);
				preStatement.setString(4,region);
				preStatement.setLong(5,date.getTime());
				preStatement.executeQuery();			
			System.out.println("email sent successfully");
		  } catch (SendFailedException e) {
	            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
	            e.printStackTrace();
		}catch(Exception e)

	{

		e.printStackTrace();

	}

}

	@Override
	public void uploadShareHolderDataExcelSheetNOTUploadToSendMail(String msg, String createdBy, String status,
			String Reason) {
		try

		{
		final String smtpHost = "apprelaycentral.pwcinternal.com";
		PreparedStatement preStatement=null;
		Connection connection=null;
		
		final int smtpPort = 25;

		Properties props = new Properties();
//		props.load(inputStream);

		props.put("mail.smtp.host", smtpHost);

		props.put("mail.smtp.port", smtpPort);

		props.put("mail.smtp.starttls.enable", true);

		Session session = Session.getInstance(props);

		

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("in_investorrelation@pwc.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(createdBy));
 
			message.setSubject("Investor Relation");

			message.setText(" Dear User,\r\n"
					+ "  \r\n"
					+ " The 'Shareholder data' file which you have uploaded has failed.\r\n"
					+ "Reason : "+Reason);

				System.out.println(message.toString());


				System.out.println(message.toString());



				Transport.send(message);
				Date date=new Date();
				String dataBaseName = MailServiceImpl.dataBaseName();
				connection=InvestorDatabaseUtill.getConnection();
				preStatement=connection.prepareStatement(ShareHolderDataFromQuaryConstant.INSERT_INTO_MAILDATA
						.replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				preStatement.setString(1, msg);
				preStatement.setString(2,createdBy);
				preStatement.setString(3,status);
				preStatement.setString(4,Reason);
				preStatement.setLong(5,date.getTime());
				preStatement.executeQuery();			
			System.out.println("email sent successfully");
		  } catch (SendFailedException e) {
	            System.out.println("Failed to send email to one or more recipients. Some addresses may be invalid.");
	            e.printStackTrace();
		}catch(Exception e)

	{

		e.printStackTrace();

	}
	}

}
