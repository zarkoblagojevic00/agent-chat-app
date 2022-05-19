package agents;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import chatmanager.ChatManager;
import chatmanager.ResponseMessageDTO;
import messagemanager.AgentMessage;
import messagemanager.MessageManagerRemote;
import model.Message;
import rest.dtos.NewMessageDTO;
import sessionmanager.SessionManagerRemote;
import util.JsonMarshaller;
import ws.WebSocket;
import ws.WebSocketResponse;

@Stateful
@Remote(Agent.class)
public class UserAgent extends DiscreetAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ChatManager chatManager;
	
	
	@EJB
	private SessionManagerRemote sessionManager;
	
	@EJB
	private MessageManagerRemote messageManager;
	
	@EJB
	private WebSocket ws;

	@PostConstruct
	public void postConstruct() {
		System.out.println("Created User Agent!");
		chatManager = new ChatManager();
	}

	@Override
	protected void handleMessageDiscreetly(AgentMessage message) {
		switch (message.getType()) {
		case RECEIVE_MESSAGE:
			receiveMessage(message);
			break;
		case SEND_MESSAGE_ALL:
			sendMessageToAll(message);
			break;
		default:
			ws.onMessage(getAgentId(), "Invalid option.");
			break;
		}
	
	}

	private void receiveMessage(AgentMessage message) {
		Message receivedMessage = (Message) message.getArgument("payload");
		ResponseMessageDTO dto = chatManager.receiveMessage(receivedMessage);
		ws.onMessage(getAgentId(), JsonMarshaller.toJson(new WebSocketResponse(message.getType(), true, dto)));
	}
	
	private void sendMessageToAll(AgentMessage message) {
		Message parsedMessage = sessionManager.unpackMessage((NewMessageDTO) message.getArgument("payload"));
		List<String> forwardTo = sessionManager.getOtherLocalRecipients(getAgentId());
		AgentMessage forwardMessage = new AgentMessage(getAgentId(), AgentMessage.Type.RECEIVE_MESSAGE, forwardTo);
		forwardMessage.addArgument("payload", parsedMessage);
		messageManager.post(forwardMessage);
		
		ResponseMessageDTO wsMessage = chatManager.sendMessage(parsedMessage);
		ws.onMessage(getAgentId(), JsonMarshaller.toJson(new WebSocketResponse(AgentMessage.Type.RECEIVE_MESSAGE, true, wsMessage)));
	}
}
