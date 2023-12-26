package com.Anemoi.InvestorRelation.Audithistory;

import java.util.ArrayList;

public interface AuditHistoryDao {

	void addAuditHistoryDetails(AuditHistoryEntity auditHistoryEntity, String dataBaseName);

	ArrayList<AuditHistoryEntity> getAuditHistory(String dataBaseName);
	
	

}
