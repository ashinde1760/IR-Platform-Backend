package com.Anemoi.InvestorRelation.Audithistory;

import java.util.ArrayList;
import java.util.List;

import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class AuditHistoryServiceimpl implements AuditHistoryService {


	@Inject
	AuditHistoryDao dao;
	
	private static String DATABASENAME = "databasename";

	private static String dataBaseName() {
		List<String> tenentList = ReadPropertiesFile.getAllTenant();
		for (String tenent : tenentList) {
			DATABASENAME = ReadPropertiesFile.dataBaseName(tenent);
		}
		return DATABASENAME;
	}
	@Override
	public void addAuditHistory(AuditHistoryEntity auditHistoryEntity) {
		// TODO Auto-generated method stub
		try
		{
		String dataBaseName=AuditHistoryServiceimpl.dataBaseName();
		this.dao.addAuditHistoryDetails(auditHistoryEntity,dataBaseName);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
	}
	@Override
	public ArrayList<AuditHistoryEntity> getAllAuditHistory() {
		try
		{
			String dataBaseName=AuditHistoryServiceimpl.dataBaseName();
			ArrayList<AuditHistoryEntity> list=this.dao.getAuditHistory(dataBaseName);
			return list;
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
