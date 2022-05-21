package rest;

import javax.ejb.Remote;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import model.User;

@Remote
public interface UserController {
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public void register(User user);
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public void login(User user);
	
	@GET
	@Path("/loggedIn/{username}")
	public void getloggedInUsers(@PathParam("username") String username);
	
	@GET
	@Path("/registered/{username}")
	public void getRegisteredUsers(@PathParam("username") String username);
	
	@DELETE
	@Path("loggedIn/{user}") 
	public void logout(@PathParam("user") String username);
}
