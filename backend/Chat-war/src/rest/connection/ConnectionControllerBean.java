package rest.connection;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.Path;

import connectionmanager.ConnectionManagerRemote;
import model.Host;
import rest.connection.restclient.clientproxies.ConnectionResteasyClientProxy;
import rest.connection.restclient.clientproxies.UserResteasyClientProxy;
import sessionmanager.SessionManagerRemote;

@Singleton
@Startup
@Remote(ConnectionController.class)
@Path("/connections")
public class ConnectionControllerBean implements ConnectionController {
	@EJB
	private ConnectionManagerRemote connectionManager;
	
	@EJB 
	private SessionManagerRemote sessionManager;
	
	@PostConstruct
	private void init() {
		
		if (connectionManager.getCurrentNode().getMasterAlias() != null)
			initiateHandshake();
	}
	
	@Override
	public void registerNode(Host node) {
		System.out.println(String.format("Registering new node: %s", node.getAlias()));
		connectionManager.addNode(node);
		informExistingNodesThatNewNodeIsRegistered(node);
		informNewNodeOfExistingNodes(node);
		informNewNodeOfLoggedInUsers(node);
		informNewNodeOfRegisteredUsers(node);
	}

	@Override
	public void addNode(Host node) {
		connectionManager.addNode(node);
	}

	@Override
	public void receiveNodesFromMaster(ArrayList<Host> nodes) {
		System.out.println("\n-- Receiving nodes -- ");
		Host master = new Host("", connectionManager.getCurrentNode().getMasterAlias(),"");
		connectionManager.addNode(master);
		
		for (Host node: nodes) {
			System.out.println(String.format("NODE: %s %s", node.getAlias(), node.getAlias().equals(connectionManager.getCurrentNode().getAlias()) ? "(self)" : ""));
			connectionManager.addNode(node);
		}
	}
	
	@Override
	public void removeNode(String nodeAlias) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean ping() {
		// TODO Auto-generated method stub
		return false;
	}
	
	// done by new node
	private void initiateHandshake() {
		new ConnectionResteasyClientProxy(connectionManager.getCurrentNode().getMasterAlias())
			.performAction((rest) -> rest.registerNode(connectionManager.getCurrentNode()));
	}
	
	// done by master node
	private void informExistingNodesThatNewNodeIsRegistered(Host newNode) {
		for (String existingNodeAlias: connectionManager.getAllNodeAliases()) {
			if (existingNodeAlias.equals(newNode.getAlias())) continue;
			
			new ConnectionResteasyClientProxy(existingNodeAlias)
				.performAction(rest -> rest.addNode(newNode));
		}
	}
	
	// done by master node
	private void informNewNodeOfExistingNodes(Host node) {
		ArrayList<Host> nodes = new ArrayList<Host>(connectionManager.getAllNodes());
		new ConnectionResteasyClientProxy(node.getAlias())
			.performAction(rest -> rest.receiveNodesFromMaster(nodes));
	}

	// done by master node
	private void informNewNodeOfLoggedInUsers(Host node) {
		new UserResteasyClientProxy(node.getAlias())
			.performAction(rest -> rest.receiveLoggedInUsersFromMasterNode(sessionManager.getFullLoggedInUsers()));
	}

	// done by master node
	private void informNewNodeOfRegisteredUsers(Host node) {
		new UserResteasyClientProxy(node.getAlias())
			.performAction(rest -> rest.receiveRegisteredUsersFromMasterNode(sessionManager.getFullRegisteredUsers()));
	}

	
}