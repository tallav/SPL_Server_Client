package bgu.spl181.net.api.bidi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import bgu.spl181.net.json.*;
import bgu.spl181.net.srv.*;

public class ConnectionsImpl<T> implements Connections<T>{
	
	private HashMap<Integer, ConnectionHandler<T>> connections;
	private HashMap<Integer, User> connectedUsers;
	private int currInd = 0;
	private MoviesIO movies;
	private UsersIO users;
	
	public ConnectionsImpl(){
		connections  = new HashMap<Integer, ConnectionHandler<T>>();
		connectedUsers = new HashMap<Integer, User>();
		movies = new MoviesIO();
		users = new UsersIO();
		//initialize();
	}
	
	@Override
	public boolean send(int connectionId, T msg) {
		if(connections.containsKey(connectionId)) {
			ConnectionHandler<T> ch = connections.get(new Integer(connectionId));
			ch.send(msg);
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public void broadcast(T msg) {
		Iterator<Entry<Integer, ConnectionHandler<T>>> it = connections.entrySet().iterator();
		while(it.hasNext()) {
			ConnectionHandler<T> ch = it.next().getValue();
			ch.send(msg);
		}
	}

	@Override
	public void disconnect(int connectionId) {
		connections.remove(connectionId);
	}
	
	public int add(ConnectionHandler<T> connHandler){
		int ans = currInd;
		connections.put(new Integer(ans), connHandler);
		currInd = currInd + 1;
		return ans;
	}

	public boolean login(Integer connId, User user) {
		if(connectedUsers.containsValue(user)) {
			return false;
		}
		connectedUsers.put(connId, user);
		return true;
	}
	
	public void signout(Integer connId) {
		if(connectedUsers.containsKey(connId)) {
			connectedUsers.remove(connId);
		}
	}	
	
	public User getConnUser(int connId) {
		return connectedUsers.get(connId);
	}
	
	public int getCurrInd(){
		return currInd;
	}
	
	public boolean isConnected(int connId){
		return connectedUsers.containsKey(connId);
	}
	
	public boolean isConnected(String name){
		Set<Entry<Integer,User>> users = connectedUsers.entrySet();
		Iterator<Entry<Integer,User>> it = users.iterator();
		while(it.hasNext()){
			Entry<Integer,User> curr = it.next();
			if(curr.getValue().getUsername().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public void initialize(){
		/*final String dir = System.getProperty("user.dir");
		String path = dir + "/Database/Users_Example.json";
		Gson gson = new Gson();
		try{
		String jsonFile1 = readFile(path);
		this.users = gson.fromJson(jsonFile1, Users.class);
		}catch(IOException e) {
			throw new RuntimeException("Cannot read to file");
		}
		User u = users.getUser("john");
		connectedUsers.put(0, u);*/
	}
	
	public MoviesIO getMoviesIO(){
		return movies;
	}
	
	public UsersIO getUsersIO(){
		return users;
	}

}
