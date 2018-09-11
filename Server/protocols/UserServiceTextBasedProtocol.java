package bgu.spl181.net.protocols;

import java.util.LinkedList;

import bgu.spl181.net.api.bidi.*;
import bgu.spl181.net.json.*;

public abstract class UserServiceTextBasedProtocol implements BidiMessagingProtocol<String>{

	protected boolean shouldTerminate = false;
	protected int connectionId;
	protected ConnectionsImpl<String> connections;
	protected UsersIO users;
	
	@Override
	public void start(int connectionId, Connections<String> connections) {
		if(!(connections instanceof ConnectionsImpl)) {
			throw new RuntimeException("Invalid Connections");
		}
		this.connections = (ConnectionsImpl<String>)connections;
		this.connectionId = connectionId;
		this.users = this.connections.getUsersIO();
	}
	
	public abstract void continueProcess(String message);
	
	/*
	public void process(String message) {
		message = message.substring(0, message.length()-1);
		continueProcess(message);
	}*/

	@Override
	public void process(String message) {
		//message = message.substring(0, message.length()-1);
		String[] splitMsg = message.split(" ");
		// [0]=REGISTER, [1]=username , [2]=password, [3]=country (optional)
		if(splitMsg[0].equals("REGISTER")) {
			if(splitMsg.length < 3){
				connections.send(connectionId, "ERROR registration failed");
				return;
			}else{
				String userName = splitMsg[1];
				String password = splitMsg[2];
				String country = null;
				if(users.hasUser(userName) || connections.isConnected(connectionId)) { //Check if username is taken
					connections.send(connectionId, "ERROR registration failed");
					return;
				}
				if(splitMsg.length > 3) {
					if(!splitMsg[3].startsWith("country=")){
						connections.send(connectionId, "ERROR registration failed");
						return;
					} else {
						country = splitMsg[3].substring(9, splitMsg[3].length()-1);
					}
				}
				User u = new User();
				u.setUsername(userName);
				u.setPassword(password);
				if(country != null) {
					u.setCountry(country);
				}
				// default values to a new user 
				u.setType("normal");
				u.setMovies(new LinkedList<Movie>());
				u.setBalance("0");
				if(users.addUser(u)){
					connections.send(connectionId, "ACK registration succeeded");
				}else{
					connections.send(connectionId, "ERROR registration failed");
				}
			}
		}
		// [0]=LOGIN, [1]=username , [2]=password
		if(splitMsg[0].equals("LOGIN")) {
			if(splitMsg.length != 3){
				connections.send(connectionId, "ERROR login failed");
				return;
			}
			String userName = splitMsg[1];
			String password = splitMsg[2];
			if(connections.isConnected(connectionId) || connections.isConnected(userName)) {
				connections.send(connectionId, "ERROR login failed");
				return;
			}
			User u = users.getUser(userName);
			if(u!=null && u.getPassword().equals(password)) {
				if(connections.login(connectionId,u)){
					connections.send(connectionId, "ACK login succeeded");
					return;
				}else{
					connections.send(connectionId, "ERROR login failed");
					return ;
				}
			}else {
				connections.send(connectionId, "ERROR login failed");
				return;
			}
			
		}
		// [0]=SIGNOUT
		if(splitMsg[0].equals("SIGNOUT")) {
			if(!connections.isConnected(connectionId)) {
				connections.send(connectionId, "ERROR signout failed");
				return;
			}else {
				connections.signout(connectionId);
				connections.send(connectionId, "ACK signout succeeded");
				shouldTerminate = true;
				//TODO: terminate the client!
			}
		}
		// [0]=REQUEST, [1]=reqName, [2]...=parameters
		if(splitMsg[0].equals("REQUEST")) {
			if(!connections.isConnected(connectionId)) {
				connections.send(connectionId, "ERROR request " + splitMsg[1] + " failed");
				return;	
			}else {
				continueProcess(message);
			}
		}
		return;
	}
	
	@Override
	public boolean shouldTerminate() {
		return shouldTerminate;
	}

}
