package sessionmanager;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Singleton;

import agentmanager.AgentManagerRemote;
import model.Message;
import model.User;
import model.UserWithHostDTO;
import rest.dtos.NewMessageDTO;
import sessionmanager.dtos.SessionInfoDTO;
import util.JNDILookup;


@Singleton
@LocalBean
@Remote(SessionManagerRemote.class)
public class SessionManagerBean implements SessionManagerRemote {

	private Map<String, User> registered = new HashMap<>();
	private Map<String, User> loggedIn = new HashMap<>();
	
	@EJB
	private AgentManagerRemote agentManager;
	
	
	/**
	 * Default constructor.
	 */
	public SessionManagerBean() {
	}

	@Override
	public boolean register(User user) {
		if (!isUsernameUnique(user.getUsername())) {
			return false;
		}
		registered.put(user.getUsername(), user);
		return true;
	}

	@Override
	public SessionInfoDTO login(User user) {
		if(!isUserRegistered(user)) {
			return null;
		}
		
		loggedIn.put(user.getUsername(), user);
		String username = user.getUsername();
		agentManager.startAgent(username, JNDILookup.UserAgentLookup);
		return new SessionInfoDTO(username, username, getHostAlias());
	}

	@Override
	public List<UserWithHostDTO> getLoggedInUsers() {
		return Collections.unmodifiableList(mapUsersToUserWithHost(loggedIn));
	}
	
	@Override
	public List<UserWithHostDTO> getRegisteredUsers() {
		return Collections.unmodifiableList(mapUsersToUserWithHost(registered));
	}
	
	@Override
	public boolean logout(String username) {
		User existing = loggedIn.get(username);
		if (existing == null) return false;
		
		loggedIn.remove(username);
		agentManager.stopAgent(username);
		return true;
	}
	
	@Override
	public List<String> getLocalRecipients() {
		return getLoggedInUsers().stream().map(UserWithHostDTO::getUsername).collect(Collectors.toList());
	}
	
	@Override
	public User getLoggedInUser(String username) {
		return loggedIn.get(username);
	}
	
	@Override
	public Message unpackMessage(NewMessageDTO dto) {
		User sender = parseUser(dto.getSender());
		User recipient = parseUser(dto.getRecipient());
		return new Message(sender, recipient, LocalDateTime.now(), dto.getSubject(), dto.getContent());
	}

	private User parseUser(String username) {
		return username.equals("all") ? new User("all", "") : getLoggedInUser(username);
	}

	@Override
	public List<String> getOtherLocalRecipients(String username) {
		return getLocalRecipients().stream().filter(un -> !username.equals(un)).collect(Collectors.toList());
	}

	
	private boolean isUsernameUnique(String username) {
		return !registered.containsKey(username);
	}
	
	private boolean isUserRegistered(User user) {
		User existing = registered.get(user.getUsername());
		if (existing == null) return false;
		
		return existing.getPassword().equals(user.getPassword());
	}
	
	private List<UserWithHostDTO> mapUsersToUserWithHost(Map<String, User> users) {
		return users.values().stream().map(this::mapUserToUserWithHost).collect(Collectors.toList());
	}

	private UserWithHostDTO mapUserToUserWithHost(User user) {
		return new UserWithHostDTO(user.getUsername(), getHostAlias());
	}
	
	private String getHostAlias() {
		return "test@test";
	}

	@Override
	public UserWithHostDTO getUserWithHost(String username) {
		return mapUserToUserWithHost(registered.get(username));
	}

	
	
}
