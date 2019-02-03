/**
 *
 */
package com.cs.assignment.model;

/**
 * @author dharmanna.p.kori
 *
 */

public class EventDetails {

	// Log file event id
	private String id;
	// Log file event state
	private String state;
	// Event type
	private String type;
	// Host server
	private String host;
	// event start or end time
	private Long timestamp;
	// duration of event
	private Long eventDuration;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the eventDuration
	 */
	public Long getEventDuration() {
		return eventDuration;
	}

	/**
	 * @param eventDuration
	 *            the eventDuration to set
	 */
	public void setEventDuration(Long eventDuration) {
		this.eventDuration = eventDuration;
	}

}
