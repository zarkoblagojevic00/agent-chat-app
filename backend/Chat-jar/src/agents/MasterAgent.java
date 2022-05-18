package agents;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import agentmanager.AgentManagerRemote;
import chatmanager.ChatManagerRemote;
import messagemanager.AgentMessage;
import messagemanager.MessageManagerRemote;
import models.User;
import util.JNDILookup;
import ws.WSChat;

@Stateful
@Remote(Agent.class)
public class MasterAgent extends DiscreetAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String WEB_SOCKET_NAME = "master";

	@EJB
	private ChatManagerRemote chm;
	
	@EJB
	private AgentManagerRemote agm;
	
	@EJB
	private WSChat ws;

	@PostConstruct
	public void postConstruct() {
		System.out.println("Created Master Agent!");
	}

	protected MessageManagerRemote msm() {
		return (MessageManagerRemote) JNDILookup.lookUp(JNDILookup.MessageManagerLookup, MessageManagerRemote.class);
	}

	@Override
	protected void handleMessageDiscreetly(AgentMessage message) {
		String option = "";
		String response = "";
		
		switch (message.getType()) {
		case REGISTER:
			String username = (String) message.getArgument("username");
			String password = (String) message.getArgument("password");

			boolean result = chm.register(new User(username, password));
			

			response = "REGISTER!Registered: " + (result ? "Yes!" : "No!");
			break;
		case LOG_IN:
			username = (String) message.getArgument("username");
			password = (String) message.getArgument("password");
			result = chm.login(username, password);
			
			if (result) {
				agm.startAgent(username, JNDILookup.UserAgentLookup);
			}

			response = "LOG_IN!Logged in: " + (result ? "Yes!" : "No!");
			break;
		case GET_LOGGED_IN:
			response = "LOGGEDIN!";
			List<User> users = chm.loggedInUsers();
			for (User u : users) {
				response += u.toString() + "|";
			}
			break;
			
		default:
			response = "ERROR!Option: " + option + " does not exist.";
			break;
		}
		
		List<String> recipients = chm.loggedInUsers().stream().map(User::getUsername).collect(Collectors.toList());
		AgentMessage pingUserAgent = new AgentMessage(getAgentId(), AgentMessage.Type.RECEIVE_MESSAGE, recipients);
		pingUserAgent.addArgument("message", response);
		msm().post(pingUserAgent);
		
		ws.onMessage(WEB_SOCKET_NAME, response);
		
		System.out.println(response);
	}

	
}
