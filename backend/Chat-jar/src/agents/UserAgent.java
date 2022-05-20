package agents;

import java.util.Arrays;
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
		case SEND_MESSAGE_USER:
			sendMessageToUser(message);
			break;
		case SEND_MESSAGE_ALL:
			sendMessageToAll(message);
			break;
		case GET_ALL_MESSAGES:
			getAllMessages();
			break;
			
		case GET_ALL_CHAT_MESSAGES:
			getAllChatMessages();
			break;
			
		case GET_USER_CHAT_MESSAGES:
			getUserChatMessages(message);
			break;
			
		default:
			ws.onMessage(getAgentId(), "Invalid option.");
			break;
		}
	}

	private void receiveMessage(AgentMessage message) {
		Message receivedMessage = (Message) message.getArgument("payload");
		ResponseMessageDTO dto = chatManager.receiveMessage(receivedMessage);
		echoMessagesToWebsocket(Arrays.asList(dto));
	}
	
	private void sendMessageToUser(AgentMessage message) {
		Message parsedMessage = sessionManager.unpackMessage((NewMessageDTO) message.getArgument("payload"));
		sendMessage(parsedMessage, Arrays.asList(parsedMessage.getRecipient().getUsername()));
	}

	private void sendMessageToAll(AgentMessage message) {
		Message parsedMessage = sessionManager.unpackMessage((NewMessageDTO) message.getArgument("payload"));
		sendMessage(parsedMessage, sessionManager.getOtherLocalRecipients(getAgentId()));
	}
	
	private void sendMessage(Message parsedMessage, List<String> forwardTo) {
		AgentMessage forwardMessage = new AgentMessage(getAgentId(), AgentMessage.Type.RECEIVE_MESSAGE, forwardTo);
		forwardMessage.addArgument("payload", parsedMessage);
		messageManager.post(forwardMessage);
		
		ResponseMessageDTO wsMessage = chatManager.sendMessage(parsedMessage);
		echoMessagesToWebsocket(Arrays.asList(wsMessage));
	}
	
	private void getAllMessages() {
		echoMessagesToWebsocket(chatManager.getAllUserMessages());
	}
	
	private void getAllChatMessages() {
		echoMessagesToWebsocket(chatManager.getAllChat());
		
	}

	private void getUserChatMessages(AgentMessage message) {
		String username = (String) message.getArgument("chatWith");
		echoMessagesToWebsocket(chatManager.getChatWithUser(username));
	}
	
	private void echoMessagesToWebsocket(List<ResponseMessageDTO> messages) {
		WebSocketResponse webSocketResponse = new WebSocketResponse(AgentMessage.Type.RECEIVE_MESSAGE, true, messages);
		ws.onMessage(getAgentId(), JsonMarshaller.toJson(webSocketResponse));
	}

	
}
