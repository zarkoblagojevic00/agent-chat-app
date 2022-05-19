package agents;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import messagemanager.AgentMessage;
import messagemanager.MessageManagerRemote;
import model.User;
import model.UserWithHostDTO;
import sessionmanager.SessionManagerRemote;
import sessionmanager.dtos.SessionInfoDTO;
import util.JsonMarshaller;
import ws.WebSocket;
import ws.WebSocketResponse;

@Stateful
@Remote(Agent.class)
public class MasterAgent extends DiscreetAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String WEB_SOCKET_MASTER_SESSION_ID = "master";
	
	public static final String MASTER_AGENT_ID = "master";

	@EJB
	private SessionManagerRemote sessionManager;
	
	@EJB
	private MessageManagerRemote messageManager;
	
	@EJB
	private WebSocket ws;

	@PostConstruct
	public void postConstruct() {
		System.out.println("Created Master Agent!");
	}

	@Override
	protected void handleMessageDiscreetly(AgentMessage message) {
		switch (message.getType()) {
		case REGISTER:
			register(message);
			break;
		case LOG_IN:
			login(message);
			break;
		case GET_LOGGED_IN:
			getLoggedIn(message);
			break;
		case GET_REGISTERED:
			getRegistered(message);
			break;
		case LOG_OUT:
			logout(message);
			break;
		default:
			ws.onMessage(WEB_SOCKET_MASTER_SESSION_ID, "Invalid option.");
			break;
		}
	}

	private void register(AgentMessage message) {
		boolean success = sessionManager.register((User) message.getArgument("user"));
		String response = JsonMarshaller.toJson(new WebSocketResponse(message.getType(), success, null));
		ws.onMessage(WEB_SOCKET_MASTER_SESSION_ID, response);
	}
	
	private void login(AgentMessage message) {
		SessionInfoDTO dto = sessionManager.login((User) message.getArgument("user"));
		String response = JsonMarshaller.toJson(new WebSocketResponse(message.getType(), dto != null , dto));
		ws.onMessage(WEB_SOCKET_MASTER_SESSION_ID, response); 
	}
	
	private void getLoggedIn(AgentMessage message) {
		List<UserWithHostDTO> loggedIn = sessionManager.getLoggedInUsers();
		String response = JsonMarshaller.toJson(new WebSocketResponse(message.getType(), true, loggedIn));
		ws.onMessage(WEB_SOCKET_MASTER_SESSION_ID, response); 
	}
	
	private void getRegistered(AgentMessage message) {
		List<UserWithHostDTO> registered = sessionManager.getRegisteredUsers();
		String response = JsonMarshaller.toJson(new WebSocketResponse(message.getType(), true, registered));
		ws.onMessage(WEB_SOCKET_MASTER_SESSION_ID, response); 
	}
	
	private void logout(AgentMessage message) {
		String username = (String) message.getArgument("username");
		boolean success = sessionManager.logout(username);
		String response = JsonMarshaller.toJson(new WebSocketResponse(message.getType(), success, null));
		ws.onMessage(WEB_SOCKET_MASTER_SESSION_ID, response); 
	}


	
}
