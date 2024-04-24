package com.Anemoi.InvestorRelation.ReportTableHeader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;

import jakarta.inject.Singleton;

@Singleton
public class ReportTableHeaderDaoImpl implements ReportTableHeaderDao {

	@Override
	public ReportTableHeaderEntity addReportTableHeader(ReportTableHeaderEntity entity, String dataBaseName)
			throws ReportTableHeaderDaoException {
		Connection con = null;
		PreparedStatement preparedStatement=null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			preparedStatement = con.prepareStatement(ReportTableHeaderQueryConstant.INSERT_INTO_REPORTTABLE_HEADERTABLE
					.replace(ReportTableHeaderQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));

			String id = UUID.randomUUID().toString();
			entity.setTableHeaderId(id);
			Date date = new Date();
			preparedStatement.setString(1, entity.getTableHeaderId());
			preparedStatement.setString(2, entity.getTableHeaderName());
			preparedStatement.setString(3, entity.getDescription());
			preparedStatement.setLong(4, date.getTime());
			preparedStatement.setLong(5, date.getTime());
			preparedStatement.executeUpdate();
			return entity;
		} catch (Exception e) {
			throw new ReportTableHeaderDaoException("unable to add" + e.getMessage());
		}finally {

			InvestorDatabaseUtill.close(preparedStatement, con);
		}

	}

	@Override
	public ArrayList<ReportTableHeaderEntity> getTableHeaderDetails(String dataBaseName)
			throws ReportTableHeaderDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		ArrayList<ReportTableHeaderEntity> list = new ArrayList<>();
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(ReportTableHeaderQueryConstant.SELECT_REPORTTABLE_HEADERLIST
					.replace(ReportTableHeaderQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			rs = psta.executeQuery();
			while (rs.next()) {
				ReportTableHeaderEntity entity = buildData(rs);
				list.add(entity);

			}
			return list;
		} catch (Exception e) {
			throw new ReportTableHeaderDaoException("unable to get" + e.getMessage());
		}finally {

			InvestorDatabaseUtill.close(psta, con);
		}
	}

	private ReportTableHeaderEntity buildData(ResultSet rs) throws SQLException {
		ReportTableHeaderEntity headerEntity = new ReportTableHeaderEntity();
		headerEntity.setTableHeaderId(rs.getString("tableHeaderId"));
		headerEntity.setTableHeaderName(rs.getString("tableHeaderName"));
		headerEntity.setDescription(rs.getString("description"));
		headerEntity.setCreatedOn(rs.getLong("createdOn"));
		headerEntity.setModifiedOn(rs.getLong("modifiedOn"));
		return headerEntity;
	}

	@Override
	public ReportTableHeaderEntity getTableHeaderDetailsById(String tableHeaderId, String dataBaseName)
			throws ReportTableHeaderDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(ReportTableHeaderQueryConstant.SELECT_TABLEHEADER_DETAILSBY_ID
					.replace(ReportTableHeaderQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, tableHeaderId);
			rs = psta.executeQuery();
			while (rs.next()) {
				ReportTableHeaderEntity entity = buildData(rs);
				return entity;
			}
		} catch (Exception e) {

			throw new ReportTableHeaderDaoException("unable to get table header by id" + e.getMessage());
		}finally {

			InvestorDatabaseUtill.close(psta, con);
		}
		return null;
	}

	@Override
	public ArrayList<ReportTableHeaderEntity> addMultipleObject(ArrayList<ReportTableHeaderEntity> entityObject,
			String dataBaseName) throws ReportTableHeaderDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			Iterator ir = entityObject.iterator();
			while (ir.hasNext()) {
				ReportTableHeaderEntity entity = (ReportTableHeaderEntity) ir.next();
				psta = con.prepareStatement(ReportTableHeaderQueryConstant.INSERT_INTO_REPORTTABLE_HEADERTABLE
						.replace(ReportTableHeaderQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				String id = UUID.randomUUID().toString();
				Date d = new Date();
				entity.setTableHeaderId(id);
				psta.setString(1, entity.getTableHeaderId());
				psta.setString(2, entity.getTableHeaderName());
				psta.setString(3, entity.getDescription());
				psta.setLong(4, d.getTime());
				psta.setLong(5, d.getTime());
				psta.executeUpdate();
			}
		} catch (Exception e) {
			throw new ReportTableHeaderDaoException("unable to add" + e.getMessage());
		}finally {

			InvestorDatabaseUtill.close(psta, con);
		}
		return entityObject;

	}

	@Override
	public void deleteTableHeaderDetails(String tableHeaderId, String dataBaseName)
			throws ReportTableHeaderDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(ReportTableHeaderQueryConstant.DELETE_TABLEHEADER_DETAILSBY_ID
					.replace(ReportTableHeaderQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, tableHeaderId);
			psta.executeUpdate();
		} catch (Exception e) {

			throw new ReportTableHeaderDaoException("unable to delete" + e.getMessage());

		}finally {

			InvestorDatabaseUtill.close(psta, con);
		}

	}

	@Override
	public void updateTableHeaderDetails(ReportTableHeaderEntity entity, String tableHeaderId, String dataBaseName)
			throws ReportTableHeaderDaoException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement psta = null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			Date d = new Date();
			psta = con.prepareStatement(ReportTableHeaderQueryConstant.UPDATE_TABLEHEADER_DETAILSBY_ID
					.replace(ReportTableHeaderQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, entity.getTableHeaderName());
			psta.setString(2, entity.getDescription());
			psta.setLong(3, d.getTime());
			psta.setString(4, tableHeaderId);
			psta.executeUpdate();
		} catch (Exception e) {

			throw new ReportTableHeaderDaoException("unable to update" + e.getMessage());

		}finally {

			InvestorDatabaseUtill.close(psta, con);
		}
	}

}
