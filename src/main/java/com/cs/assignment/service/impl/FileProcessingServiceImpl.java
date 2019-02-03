package com.cs.assignment.service.impl;

import static com.cs.assignment.utils.FileProcessingConstants.FILE_PROCESSING_NAGATIVE_RESPONSE;
import static com.cs.assignment.utils.FileProcessingConstants.FILE_PROCESSING_NUTRAL_RESPONSE;
import static com.cs.assignment.utils.FileProcessingConstants.FILE_PROCESSING_POSITIVE_RESPONSE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;

import com.cs.assignment.dao.StoreEventDetailDao;
import com.cs.assignment.dao.impl.StoreEventDetailDaoImpl;
import com.cs.assignment.model.EventDetails;
import com.cs.assignment.service.FileProcessingService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * @author dharmanna.p.kori
 *
 */
public class FileProcessingServiceImpl implements FileProcessingService {

	private static Logger logger = Logger
			.getLogger(FileProcessingServiceImpl.class);

	private StoreEventDetailDao storeEventDetailDao = new StoreEventDetailDaoImpl();

	@Override
	public synchronized String processFile(String fileName, boolean dbGrantIssue) {
		String response = null;
		List<EventDetails> eventDetailList = new ArrayList<EventDetails>();
		if (!fileName.isEmpty() && fileName != null) {
			try {
				// parse file and get json object
				eventDetailList = parseFile(fileName, eventDetailList);
			} catch (IOException exception) {
				logger.error("File has some issues while parsing:" + fileName);
				response = FILE_PROCESSING_NAGATIVE_RESPONSE;
				exception.printStackTrace();
			} catch (JsonSyntaxException exception) {
				logger.error("File has some issues while converting to json object:"
						+ fileName);
				response = FILE_PROCESSING_NAGATIVE_RESPONSE;
				exception.printStackTrace();
			}
		} else {
			logger.info("Not a valid file name");
			response = FILE_PROCESSING_NAGATIVE_RESPONSE;
		}
		// check processing time of each event
		if (evaluateProcessingTime(eventDetailList, dbGrantIssue) != null) {
			response = FILE_PROCESSING_POSITIVE_RESPONSE;
		}
		return response;
	}

	/**
	 * This method parse the file and convert json string to object
	 *
	 * @param fileName
	 * @param eventDetailList
	 * @return
	 * @throws Exception
	 *             - can throw IO or JsonSyntaxException
	 */
	private List<EventDetails> parseFile(String fileName,
			List<EventDetails> eventDetailList) throws IOException,
			JsonSyntaxException {
		logger.info("Parsing the file:" + fileName);
		File file = new File(fileName);
		LineIterator it = FileUtils.lineIterator(file);
		try {
			while (it.hasNext()) {
				String line = it.nextLine();
				Gson gson = new GsonBuilder().setDateFormat(
						"yyyy-MM-dd'T'HH:mm:ssz").create();
				eventDetailList.add(gson.fromJson(line, EventDetails.class));
			}
		} finally {
			it.close();
		}
		return eventDetailList;
	}

	/**
	 * Checks each record , finds the events with more than 4ms store in
	 * database
	 *
	 * @param eventDetailList
	 * @return
	 */
	private String evaluateProcessingTime(List<EventDetails> eventDetailList,
			boolean dbGrantIssue) {
		String response = null;
		Map<String, EventDetails> eventDetailMap = new HashMap<String, EventDetails>();
		List<EventDetails> storeEventDetailList = new ArrayList<EventDetails>();

		for (EventDetails currentEventDetail : eventDetailList) {
			logger.debug("Processing event id: " + currentEventDetail.getId());
			if (eventDetailMap.get(currentEventDetail.getId()) != null) {
				EventDetails previousEventRecord = eventDetailMap
						.get(currentEventDetail.getId());
				Long prevTime = previousEventRecord.getTimestamp();
				Long currTime = currentEventDetail.getTimestamp();
				if (prevTime > currTime && (prevTime - currTime > 4)) {
					currentEventDetail.setEventDuration(prevTime - currTime);
					storeEventDetailList.add(currentEventDetail);
				} else if (currTime > prevTime && (currTime - prevTime > 4)) {
					currentEventDetail.setEventDuration(currTime - prevTime);
					storeEventDetailList.add(currentEventDetail);
				}
			} else {
				eventDetailMap.put(currentEventDetail.getId(),
						currentEventDetail);
			}
		}
		// store
		if (!storeEventDetailList.isEmpty()) {
			response = storeEventDetailDao.storeEventDetailsToFile(
					storeEventDetailList, dbGrantIssue);
		} else {
			response = FILE_PROCESSING_NUTRAL_RESPONSE;
		}
		return response;
	}
}
