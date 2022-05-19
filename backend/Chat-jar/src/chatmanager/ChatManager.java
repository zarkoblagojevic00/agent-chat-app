package chatmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.Message;

public class ChatManager {
	private static final String ALL_CHAT = "all";
	
	private Map<String, List<Message>> chats;

	public ChatManager() {
		chats = new HashMap<>();
		chats.put(ALL_CHAT, new ArrayList<>());
	}
	
	public ResponseMessageDTO sendMessage(Message message) {
		String recipientUsername = message.getRecipient().getUsername();
		addMessage(recipientUsername, message);
		return mapMessageToResponseMessage(message);
	}
	
	public ResponseMessageDTO receiveMessage(Message message) {
		String senderUsername = message.getSender().getUsername();
		addMessage(senderUsername, message);
		return mapMessageToResponseMessage(message);
	}
	
	public List<ResponseMessageDTO> getAllChat() {
		return mapMessagesToResponseMessages(chats.get(ALL_CHAT));
	}
	
	public List<ResponseMessageDTO> getChatWithUser(String username) {
		return mapMessagesToResponseMessages(chats.get(username));
	}
	
	private void addMessage(String username, Message message) {
		if (username != ALL_CHAT) {
			addPrivateMessage(message, username);
		} else {
			addAllChatMessage(message);
		}
	}
	
	private void addAllChatMessage(Message message) {
		chats.get(ALL_CHAT).add(message);
	}
	
	private void addPrivateMessage(Message message, String username) {
		if (!chats.containsKey(username)) {
			chats.put(username, new ArrayList<>());
		}
		chats.get(username).add(message);	
	}
	
	private List<ResponseMessageDTO> mapMessagesToResponseMessages(List<Message> messages) {
		return messages.stream().map(this::mapMessageToResponseMessage).collect(Collectors.toList());
	}
	
	private ResponseMessageDTO mapMessageToResponseMessage(Message message) {
		return new ResponseMessageDTO(
				message.getSubject(), 
				message.getContent(), 
				message.getSender().getUsername(), 
				message.getRecipient().getUsername(), 
				message.getCreated()
		);
	}
	
	
}
