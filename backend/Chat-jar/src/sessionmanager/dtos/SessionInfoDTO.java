package sessionmanager.dtos;

import java.io.Serializable;

public class SessionInfoDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7200969962633282573L;
	private String sessionId;
	private String username;
	
	public SessionInfoDTO(String sessionId, String user) {
		super();
		this.sessionId = sessionId;
		this.username = user;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	
	public String getUsername() {
		return username;
	}
}
