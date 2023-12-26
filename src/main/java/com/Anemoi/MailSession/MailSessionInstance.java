package com.Anemoi.MailSession;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

public class MailSessionInstance {

	private static final Logger logger = LoggerFactory.getLogger(MailSessionInstance.class);

	private static Session session = null;

	public MailSessionInstance() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Session getMailSession() {

		if (session == null) {
			initializeMailSessin();
		}
		return session;

	}

	private static synchronized void initializeMailSessin() {
		if (session == null) {
			Properties emailProperties = PropertiesUtil
					.getPropertiesFromResoures(MailServiceConstant.MAIL_PROPERTIES_FILE);
			String userName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.USER_NAME);
			String password = ReadPropertiesFile.readRequestProperty(MailServiceConstant.PWD);
			String rollName = ReadPropertiesFile.readRequestProperty(MailServiceConstant.ROLLNAME);

			session = Session.getDefaultInstance(emailProperties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {

					return new PasswordAuthentication(userName, password);

				}
			});

		}
	}

	public static void closeTransport(Transport transport) {
		if (transport != null) {
			try {
				transport.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
