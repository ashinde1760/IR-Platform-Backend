package com.Anemoi.InvestorRelation.ShareHolderMeeting;

import javax.validation.constraints.NotNull;

public interface LocalRepoConfiguration {
	
	@NotNull
	String getBaseDir();
	
	@NotNull
	 Long getMultiPartUploadedThred();
	
	@NotNull
	Integer getMaxUploadTheads();
	
	@NotNull
	String getfileDir();

}
