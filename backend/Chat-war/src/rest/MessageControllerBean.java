package rest;

import java.util.Arrays;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import messagemanager.AgentMessage;
import messagemanager.MessageManagerRemote;
import rest.dtos.NewMessageDTO;

@Stateless
@Path("/messages")
public class MessageControllerBean implements MessageController {

	@EJB
	private MessageManagerRemote messageManager;
	
	@Override
	public void messageAll(NewMessageDTO message) {
		AgentMessage agentMessage = new AgentMessage("rest_external", AgentMessage.Type.SEND_MESSAGE_ALL, Arrays.asList(message.getSender()));
		agentMessage.addArgument("payload", message);
		messageManager.post(agentMessage);
	}

	@Override
	public void messageUser(NewMessageDTO message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getUserMessages(String username) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getGlobalChat(String username) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getChatWithOtherUser(String username, String recipient) {
		// TODO Auto-generated method stub

	}

}
