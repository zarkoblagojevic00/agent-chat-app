package sessionmanager;

import java.util.List;

import javax.ejb.Remote;

import model.Message;
import model.User;
import model.UserWithHostDTO;
import rest.dtos.NewMessageDTO;
import sessionmanager.dtos.SessionInfoDTO;

@Remote
public interface SessionManagerRemote {

	public SessionInfoDTO login(User user);

	public boolean register(User user);

	public List<UserWithHostDTO> getLoggedInUsers();
	
	public List<UserWithHostDTO> getRegisteredUsers();
	
	public boolean logout(String username);

	public List<String> getLocalRecipients();

	public User getLoggedInUser(String username);
	
	public Message unpackMessage(NewMessageDTO dto);

	public List<String> getOtherLocalRecipients(String username);
}
