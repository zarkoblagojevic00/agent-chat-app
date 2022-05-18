package agents;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import agentmanager.AgentManagerRemote;
import chatmanager.ChatManagerRemote;
import messagemanager.AgentMessage;
import ws.WSChat;

@Stateful
@Remote(Agent.class)
public class UserAgent extends DiscreetAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ChatManagerRemote chatManager;
	@EJB
	private AgentManagerRemote agentManager;
	@EJB
	private WSChat ws;

	@PostConstruct
	public void postConstruct() {
		System.out.println("Created User Agent!");
	}

	@Override
	protected void handleMessageDiscreetly(AgentMessage message) {
		if (message.getType() != AgentMessage.Type.RECEIVE_MESSAGE) return; 
		
		String sender = (String) message.getSender();
		String msg = (String) message.getArgument("message");
		
		System.out.println("Recipient :" + getAgentId());
		System.out.println("Received from :" + sender);
		System.out.println("Message : " + msg);
	
	}
	
	
}
