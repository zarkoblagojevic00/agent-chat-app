package rest;

import java.util.Arrays;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import messagemanager.AgentMessage;
import messagemanager.MessageManagerRemote;
import models.User;

@Stateless
@Path("/master")
public class ChatRestBean implements ChatRest {

	@EJB
	private MessageManagerRemote messageManager;
	
	@Override
	public void register(User user) {
		AgentMessage message = new AgentMessage("external_msg", AgentMessage.Type.REGISTER, Arrays.asList("master"));
		
		message.addArgument("username", user.getUsername());
		message.addArgument("password", user.getPassword());
		
		messageManager.post(message);
	}

	@Override
	public void login(User user) {
		AgentMessage message = new AgentMessage("external_msg", AgentMessage.Type.LOG_IN, Arrays.asList("master"));
		message.addArgument("username", user.getUsername());
		message.addArgument("password", user.getPassword());
		
		messageManager.post(message);
	}

	@Override
	public void getloggedInUsers() {
		AgentMessage message = new AgentMessage("external_msg", AgentMessage.Type.GET_LOGGED_IN, Arrays.asList("master"));
		
		messageManager.post(message);
	}

}
