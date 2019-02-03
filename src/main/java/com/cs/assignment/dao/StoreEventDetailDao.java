/**
 *
 */
package com.cs.assignment.dao;

import java.util.List;

import com.cs.assignment.model.EventDetails;

/**
 * @author dharmanna.p.kori
 *
 */
public interface StoreEventDetailDao {

	String storeEventDetailsToFile(List<EventDetails> eventDetailList, boolean dbGrantIssue);
}
