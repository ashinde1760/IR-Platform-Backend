package com.Anemoi.InvestorRelation.config;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("my.config")
public class Config {

	private boolean removeComma;
	private boolean removeBracket;
	private boolean removeSpace;
	private boolean dateMatcher;

	public boolean isRemoveComma() {
		return removeComma;
	}

	public void setRemoveComma(boolean removeComma) {
		this.removeComma = removeComma;
	}

	public boolean isRemoveBracket() {
		return removeBracket;

	}

	public void setRemoveBracket(boolean removebracket) {
		this.removeBracket = removebracket;
	}

	public boolean isRemoveSpace() {
		return removeSpace;

	}

	public void setRemoveSpace(boolean removespace) {
		this.removeSpace = removespace;
	}

	public boolean isDateMatcher() {
		return dateMatcher;

	}

	public void setDateMatcher(boolean dateMatcher) {
		this.dateMatcher = dateMatcher;
	}

}
