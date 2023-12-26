package com.Anemoi.InvestorRelation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadPropertiesFile {

	private static final Logger logger = LoggerFactory.getLogger(ReadPropertiesFile.class);

	private static Properties responseProperties;
	private static Properties dataBaseName;
	private static Properties mailProperties;
	private static Properties requestProperties;
	private static Properties readOCRKey;
	private static Properties readkeyword;
	private static Properties meetingHost;

	static {
		dataBaseName = ReadProperties.getPropertiesFromResources("database.properties");
		responseProperties = ReadProperties.getPropertiesFromResources("response.properties");
		mailProperties = ReadProperties.getPropertiesFromResources("mail.properties");
		requestProperties = ReadProperties.getPropertiesFromResources("request.properties");
		readkeyword = ReadProperties.getPropertiesFromResources("RequestDataIngestionKeyword.properties");
		readOCRKey = ReadProperties.getPropertiesFromResources("ocrKey.properties");
		meetingHost = ReadProperties.getPropertiesFromResources("meetingHost.properties");
	}

	public static String readResponseProperty(String key) {
		logger.debug("inside readResponseProperties : key:" + key);
		String value = responseProperties.getProperty(key);
		return value;

	}

	public static String readOCrKey(String key) {
		logger.debug("inside readResponseProperties : key:" + key);
		String value = readOCRKey.getProperty(key);
		return value;

	}
	public static String readMeetingHost(String key) {
		logger.debug("inside readResponseProperties : key:" + key);
		String value = meetingHost.getProperty(key);
		return value;

	}


	public static List<String> getAllTenant() {

		List<String> tenantsList = new ArrayList<String>();

		dataBaseName.keySet().forEach(tenant -> tenantsList.add((String) tenant));
		return tenantsList;
	}

	public static String dataBaseName(String key) {

		String value = dataBaseName.getProperty(key);
		return value;
	}

	public static String readRequestProperty(String key) {
		logger.debug("inside readRequestProperty :key: " + key);
		String value = requestProperties.getProperty(key);
		return value;
	}

//	public static String getFile(String key) {
//		String value = requestAnalystDetailsFile.getProperty(key);
//		return value;
//	}

	public static String readMailProperty(String key) {
		logger.debug("inside readMailProperty :key: " + key);
		String value = mailProperties.getProperty(key);
		return value;
	}

	public static String readKeywordProperties(String key) {
		logger.debug("inside readkeyword Property :key: " + key);
		String value = readkeyword.getProperty(key);
		return value;
	}

}
