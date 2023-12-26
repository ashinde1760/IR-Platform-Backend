package com.Anemoi.MailSession;

public class MailServiceConstant {

	public static final String USER_NAME = "email.account.userName";
	public static final String ROLENAME = "email.account.roleName";
	public static final String MAIL_PROPERTIES_FILE = "mail.properties";
	public static final String PWD = "email.account.password";
	
	public static final String SUBJECT_FOR_MASTER ="Addition of new Master Admin";
	public static final String UPDATE_SUBJECT_FORMATSER ="Modification of Master Admin";
	
	public static final String SUBJECT_FOR_ANALYST ="Addition of new Analyst Admin";
	public static final String UPDATE_SUBJECTFOR_ANALYST ="Modification of Analyst Admin";

	public static final String SUBJECT_FOR_CLIENTADMIN ="Addition of new Client Admin";
	
	public static final String SUBJECT_FOR_UPDATECLIENTADMIN ="Modification of client admin";

	public static final String SUBJECT_FOR_MASTERDATABASE="Addition/Modification of Master database";

	public static final String SUBJECT_FOR_NEWMEETING_SCHEDULE ="New Meeting Scheduled";
	
	public static final String SUBJECT_FOR_UPDATEMEETING ="Addition /Modifications in Meeting data";

	public static final String SUBJECT_FOR_FILE_UPLOADED="New File uploaded";

	public static final String SUBJECT_FOR_ANALYSTSETUP=" Addition/Modfication made in Analyst Setup";

	public static final String SUBJECT_FOR_CLIENTSETUP="Addition/Modfication made in Client Setup";

	public static final String MASTERDATABASENOTIFICATION_BODY= "<!DOCTYPE html> <html> <head> <meta name=\"viewport\" "
			+ "content=\"width=device-width, initial-scale=1\"> <style> body { margin: 0; overflow: hidden; } "
			+ ".mail-container { position: relative; float: left; width: 55%; height: 100vh; margin: 0 27.5%; margin-top: 3vh; } "
			+ ".mail-msg-box { position: relative; float: left; width: 70%; height: auto; background-color: white; border: 1px solid gray; } "
			+ ".heading { position: relative; float: left; width: 100%; height: auto; color: white; font-size: 1.5rem; font-family: Georgia; background-color: #92a8d1; padding: 5px 0;} "
			+ ".body { position: relative; float: left; width: 100%; padding: 2rem; box-sizing: border-box; }"
			+ " .salutation-row > span {color:black;}.salutation-row { position: relative; float: left; width: 100%; font-size: 1rem; } "
			+ ".user-name { font-weight: bold; color: darkgray; font-size: 1rem; text-align: center; } "
			+ ".default-password, .email-otp { position: relative; float: left; width: 100%; color: #60686f; margin-top: 3vh; }"
			+ " .default-password span, .email-otp span { color: black; font-weight: bold; margin-left: 3px; }"
			+ " .footer { position: relative; float: left; width: 100%; height: auto; margin: 3vh 0 5vh; padding: 5px 0; text-align: center; } </style> </head>"
			+ " <body> <div class=\"mail-container\"> <div class=\"mail-msg-box\"> <div class=\"heading\"> "
			+ "<div style=\"font-size: 1.15rem; text-align: center; margin-top: 5px;\">"
			+ "<span style=\"font-size: 0.7rem; color: red; margin-left: 5px;margin-top:5px;\">"
			+ "</span></div> <div style=\"text-align: center;\">"
			+ "Investor Relation </div> </div> <div class=\"body\"> "
			+ "<div class=\"salutation-row\"> <div style=\"font-size: 1.25rem; color: green; text-align: center;\">"
			+ "</div>  <div class=\"user-name\" style=\"font-weight: bold; color:red\"></div> <div style=\"font-size: 1.15rem; text-align: center; margin-top: 5px;\">"
			+ " You have added  <span>$$lineItem_size$$</span> lineItems in <span>$$tableName$$</span> successfully</div> </div> <div class=\"default-password\"> <div style=\"text-align: center;\">"
			+" Line Item Names:[  <span>$$lineItem_name$$</span>]</div> </div> <div class=\"default-password\"> <div style=\"text-align: center;\">"
			
			+ " </div> </div> </body> </html>";
	
	 public static final String EMAIL_BODY_ADDMASTER_ADMIN = "<html><body>"
	            + "<p>Dear All,</p>"
	            + "<p>New master admin has been added.</p>"
	            + "<p>Name:%s</p>"
	            + "<p>Link: <a href='%s'>%s</a></p>"
	            + "<p>Regards,<br>"
	            + "Capital Market Advisory Team<br>"
	            + "PwC</p>"
	            + "</body></html>";
	
	  
	

	 

	public static final String LINEITEM_SIZE= "$$lineItem_size$$";
	public static final String LINEITEM_NAME= "$$lineItem_name$$";
	public static final String TABLENAME= "$$tableName$$";

	public static final String PORTAL_LINK= "$$portalLink$$";

	public static final String CLIENT_NAME= "$$client_name$$";

	public static final String ROLLNAME = "$$roleName$$";
	public static final String CONTENT_TYPE_HTML = "text/html";



}
