/**
 *
 */
package com.cs.assignment.dao.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hsqldb.jdbc.JDBCDataSource;

import com.cs.assignment.dao.StoreEventDetailDao;
import com.cs.assignment.model.EventDetails;
import com.cs.assignment.utils.FileProcessingConstants;

/**
 * @author dharmanna.p.kori
 *
 */
public class StoreEventDetailDaoImpl implements StoreEventDetailDao {

	@Override
	public String storeEventDetailsToFile(List<EventDetails> eventDetailList,
			boolean dbGrantIssue) {
		if (!dbGrantIssue) {
			storeDataIntoHsqlFileDB(eventDetailList);
		} else {
			storeDataInfoFile(eventDetailList);
		}
		return FileProcessingConstants.FILE_PROCESSING_POSITIVE_RESPONSE;
	}

	/**
	 * Store in hsql db
	 *
	 * @param eventDetailList
	 */
	private void storeDataIntoHsqlFileDB(List<EventDetails> eventDetailList) {
		// Exceptions may occur
		try {
			// Get a DataSource object and set the URL 'logFileEventsDb' in
			JDBCDataSource dataSource = new JDBCDataSource();
			dataSource.setDatabase("jdbc:hsqldb:file:logFileEventsDb");
			// Connect to the database
			Connection conn = dataSource.getConnection("SA", "");
			fillFileEvents(conn, eventDetailList);
			conn.close();
		} catch (Exception e) {
			// Print out the error message
			e.printStackTrace();
		}
	}

	/**
	 * Store in file
	 * @param eventDetailList
	 */
	private void storeDataInfoFile(List<EventDetails> eventDetailList) {
		File file = new File("logFileEventsDbFile.data");
		FileWriter archivo = null;
		try {
			Files.deleteIfExists(file.toPath());
			archivo = new FileWriter(file);
			archivo.write(String.format("%20s %20s %20s %20s %20s\r\n", "ID",
					"TYPE", "HOST", "EVENT_DURATION", "ALERT"));
			for (EventDetails eventDetail : eventDetailList) {
				archivo.write(String.format("%20s %20s %20s %20s %20s \r\n",
						eventDetail.getId(),
						(eventDetail.getType() != null ? eventDetail.getType()
								: ""),
						(eventDetail.getHost() != null ? eventDetail.getHost()
								: ""), eventDetail.getEventDuration(), "true"));
			}
			archivo.flush();
			archivo.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param conn
	 * @param eventDetailList
	 * @param root
	 *
	 * @throws SQLException
	 */
	private void fillFileEvents(Connection conn,
			List<EventDetails> eventDetailList) throws SQLException {
		// Create a statement object
		Statement stat = conn.createStatement();
		PreparedStatement prep = null;
		// Try to drop the table
		try {
			stat.executeUpdate("DROP TABLE FileEvents");
		} catch (SQLException e) {
			// Ignore Exception, because the table may not yet exist
		}
		String sql = "CREATE TABLE FILEEVENTS (id varchar(255), "
				+ " type varchar(255), host varchar(255), eventduration bigint,"
				+ " alert varchar(10), primary key(id));";
		stat.executeUpdate(sql);
		// Close the Statement object, it is no longer used
		stat.close();
		for (EventDetails eventDetail : eventDetailList) {
			String values = "VALUES ("
					+ eventDetail.getId()
					+ ","
					+ (eventDetail.getType() != null ? eventDetail.getType()
							: "")
					+ ","
					+ (eventDetail.getHost() != null ? eventDetail.getHost()
							: "") + "," + eventDetail.getEventDuration()
					+ ",true";
			String insertStmt = "INSERT INTO logFileEventsDb.FILEEVENTS (id,type,host,eventduration,alert) "
					+ values + ");";
			prep = conn.prepareCall(insertStmt);
			// Close the PreparedStatement
			prep.close();
		}
	}
}
