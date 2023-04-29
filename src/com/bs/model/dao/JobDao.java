package com.bs.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.bs.common.Constants;
import com.bs.common.JdbcTemplate;
import com.bs.common.PropertiesGenerator;
import com.bs.model.dto.Employee;
import com.bs.model.dto.Job;

public class JobDao {
	private static final String INSERT_INTO_JOB = "insertIntoJob";
	private static final String UPDATE_JOB = "updateJob";
	private static final String TABLE_JOB = "JOB";
	private static final String COL_JOB_CODE = "JOB_CODE";
	private static final String COL_JOB_NAME = "JOB_NAME";
	
	private final Properties sql;
	
	public JobDao() {
		sql = new Properties();
		PropertiesGenerator.load(sql);
	}
	
	public void matchWithEmployee(Connection conn, Employee employee) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = sql.getProperty(Constants.SELECT_ALL_FROM) + " " + sql.getProperty(Constants.WHERE);
		query = replace(query, COL_JOB_CODE, Constants.EQUAL);
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, employee.getJobCode());
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				employee.setJob(generateJob(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(rs);
			JdbcTemplate.close(pstmt);
		}
	}
	
	public List<String> findJobCodeBy(Connection conn, String jobName) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> jobCodes = new ArrayList<>();
		
		String query = sql.getProperty(Constants.SELECT_ALL_FROM) + " " + sql.getProperty(Constants.WHERE);
		query = replace(query, COL_JOB_NAME, Constants.LIKE);
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "%" + jobName + "%");
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				jobCodes.add(rs.getString(COL_JOB_CODE));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(rs);
			JdbcTemplate.close(pstmt);
		}
		return jobCodes;
	}
	
	public int insertIntoJob(Connection conn, Job job) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = sql.getProperty(INSERT_INTO_JOB);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, job.getJobCode());
			pstmt.setString(2, job.getJobName());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(pstmt);
		}
		return result;
	}
	
	public int updateJobBy(Connection conn, Job job) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = sql.getProperty(UPDATE_JOB);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, job.getJobName());
			pstmt.setString(2, job.getJobCode());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(pstmt);
		}
		return result;
	}
	
	public int deleteFromJobBy(Connection conn, String jobCode) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = sql.getProperty(Constants.DELETE_FROM) + " " + sql.getProperty(Constants.WHERE);
		query = replace(query, COL_JOB_CODE, Constants.EQUAL);
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, jobCode);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(pstmt);
		}
		return result;
	}
	
	private String replace(String query, String column, String operator) {
		query = query.replace(Constants.TABLE, TABLE_JOB);
		query = query.replace(Constants.COL, column);
		return query.replace(Constants.OPERATOR, operator);
	}

	private Job generateJob(ResultSet rs) throws SQLException {
		return new Job(rs.getString(COL_JOB_CODE), rs.getString(COL_JOB_NAME));
	}
}
