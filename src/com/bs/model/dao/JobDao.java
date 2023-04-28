package com.bs.model.dao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.bs.model.dto.Employee;
import com.bs.model.dto.Job;
import com.bs.service.JdbcTemplate;

public class JobDao {
	private static final String SQL_PATH = "/sql/board/board_sql.properties";
	private static final String SELECT_ALL_FROM_JOB = "selectAllFromJob";
	private static final String WHERE = "where";
	private static final String COL = "#COL";
	private static final String SYNTAX = "#SYNTAX";
	private static final String COL_JOB_CODE = "JOB_CODE";
	private static final String COL_JOB_NAME = "JOB_NAME";
	private static final String EQUAL = "=";
	private static final String LIKE = "LIKE";
	
	private final Properties sql;
	
	public JobDao() {
		sql = new Properties();
		loadProperties(sql);
	}
	
	private void loadProperties(Properties prop) {
		final String path = JobDao.class.getResource(SQL_PATH).getPath();
		try (FileReader fileReader = new FileReader(path)) {
			prop.load(fileReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void matchWithEmployee(Connection conn, Employee employee) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = sql.getProperty(SELECT_ALL_FROM_JOB) + " " + sql.getProperty(WHERE);
		query = query.replace(COL, COL_JOB_CODE);
		query = query.replace(SYNTAX, EQUAL);
		
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
	
	public List<String> findJobCodeByJobName(Connection conn, String jobName) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> jobCodes = new ArrayList<>();
		
		String query = sql.getProperty(SELECT_ALL_FROM_JOB) + " " + sql.getProperty(WHERE);
		query = query.replace("#COL", COL_JOB_NAME);
		query = query.replace("#SYNTAX", LIKE);
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "%" + jobName + "%");
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				jobCodes.add(rs.getString("JOB_CODE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcTemplate.close(rs);
			JdbcTemplate.close(pstmt);
		}
		return jobCodes;
	}

	private Job generateJob(ResultSet rs) throws SQLException {
		return new Job(rs.getString(COL_JOB_CODE), rs.getString(COL_JOB_NAME));
	}
}
