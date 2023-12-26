package com.Anemoi.InvestorRelation.Audithistory;

import java.util.ArrayList;

public interface AuditHistoryService {
	
	public void addAuditHistory(AuditHistoryEntity auditHistoryEntity);

	public ArrayList<AuditHistoryEntity> getAllAuditHistory();

}
