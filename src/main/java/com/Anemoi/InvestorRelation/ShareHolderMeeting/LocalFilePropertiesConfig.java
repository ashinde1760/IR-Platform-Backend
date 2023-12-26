package com.Anemoi.InvestorRelation.ShareHolderMeeting;

import javax.validation.constraints.NotNull;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Property;

@ConfigurationProperties("files.local")
public class LocalFilePropertiesConfig implements LocalRepoConfiguration {

	@NotNull
	private String baseDir;
	
	@NotNull
	private String fileDir;
	
	@NotNull
	private Long multipartUploadThreshold;
	
	@NotNull
	private Integer maxUplaodThreads;

	@Override
	public @NotNull String getBaseDir() {
		return baseDir;
	}

	@Override
	public @NotNull Long getMultiPartUploadedThred() {

		return multipartUploadThreshold;
	}

	public Long getMultipartUploadThreshold() {
		return multipartUploadThreshold;
	}

	public void setMultipartUploadThreshold(Long multipartUploadThreshold) {
		if (multipartUploadThreshold != null) {
			System.out.println(this.multipartUploadThreshold);
			this.multipartUploadThreshold = multipartUploadThreshold;
		}
	}

	@Override
	public @NotNull Integer getMaxUploadTheads() {

		return maxUplaodThreads;
	}

	public void setMaxUplaodThreads(Integer maxUplaodThreads) {
		if (maxUplaodThreads != null) {
			System.out.println(this.maxUplaodThreads);
			this.maxUplaodThreads = maxUplaodThreads;
		}
	}

	public Integer getMaxUplaodThreads() {
		return maxUplaodThreads;
	}

	public void setBaseDir(String baseDir) {
		if (baseDir != null) {
			this.baseDir = baseDir;
			System.out.println(this.baseDir);
		}
	}

	@Override
	public @NotNull String getfileDir() {
		// TODO Auto-generated method stub
		return fileDir;
	}



	public void setFileDir(String fileDir) {
		if (fileDir != null) {
			this.fileDir = fileDir;
			System.out.println(this.fileDir);
		}
	}
	
	

}
