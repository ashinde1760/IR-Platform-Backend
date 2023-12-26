package com.Anemoi.InvestorRelation.whitelabeling;

import java.util.ArrayList;

public interface WhiteLableingDao {

	WhiteLableingEntity addcssFileAndLogoFile(WhiteLableingEntity entity, String dataBaseName) throws DaoException;

	ArrayList<WhiteLableingEntity> getlist(String dataBaseName) throws DaoException;

	WhiteLableingEntity getById(String clientName, String dataBaseName) throws DaoException;

}
