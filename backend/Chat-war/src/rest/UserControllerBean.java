package rest;

import java.util.Arrays;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import agents.MasterAgent;
import messagemanager.AgentMessage;
import messagemanager.MessageManagerRemote;
import model.User;


@Stateless
@Path("/users")
public class UserControllerBean implements UserController {

	@EJB
	private MessageManagerRemote messageManager;
	
	@Override
	public void register(User user) {
		AgentMessage message = new AgentMessage("rest_external", AgentMessage.Type.REGISTER, Arrays.asList(MasterAgent.MASTER_AGENT_ID));
		message.addArgument("user", user);
		messageManager.post(message);
	}

	@Override
	public void login(User user) {
		AgentMessage message = new AgentMessage("rest_external", AgentMessage.Type.LOG_IN, Arrays.asList(MasterAgent.MASTER_AGENT_ID));
		message.addArgument("user", user);
		messageManager.post(message);
	}

	@Override
	public void getloggedInUsers() {
		AgentMessage message = new AgentMessage("rest_external", AgentMessage.Type.GET_LOGGED_IN, Arrays.asList(MasterAgent.MASTER_AGENT_ID));
		messageManager.post(message);
	}

	@Override
	public void getRegisteredUsers() {
		AgentMessage message = new AgentMessage("rest_external", AgentMessage.Type.GET_REGISTERED, Arrays.asList(MasterAgent.MASTER_AGENT_ID));
		messageManager.post(message);
	}

	@Override
	public void logout(String username) {
		AgentMessage message = new AgentMessage("rest_external", AgentMessage.Type.LOG_OUT, Arrays.asList(MasterAgent.MASTER_AGENT_ID));
		message.addArgument("username", username);
		messageManager.post(message);
	}

}
