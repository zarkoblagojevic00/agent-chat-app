package sessionmanager.dtos;

import java.io.Serializable;

public class SessionInfoDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7200969962633282573L;
	private String sessionId;
	private String user;
	
	public SessionInfoDTO(String sessionId, String user) {
		super();
		this.sessionId = sessionId;
		this.user = user;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	
	public String getUser() {
		return user;
	}
}
