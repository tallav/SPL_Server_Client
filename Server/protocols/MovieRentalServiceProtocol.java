package bgu.spl181.net.protocols;

import bgu.spl181.net.json.*;
import bgu.spl181.net.protocols.requests.*;

public class MovieRentalServiceProtocol extends UserServiceTextBasedProtocol{
	
	private boolean admin = false;
	private MoviesIO movies;
	
	private void checkAdmin(){
		User user = connections.getConnUser(connectionId);
		if(user.getType().equals("admin")){
			admin = true;
		}
	}
	
	@Override
	public void continueProcess(String message) {
		//message = message.substring(0, message.length());
		checkAdmin();
		movies = connections.getMoviesIO();
		String[] splitMsg = message.split(" ");
		Request req;
		switch (splitMsg[1]){
			case "balance": 
				if(splitMsg[2].equals("info")) {
					req = new BalanceInfo(message, connections, connectionId, movies);
					req.execute();
				}
				if(splitMsg[2].equals("add")) {
					req = new BalanceAdd(message, connections, connectionId, movies);
					req.execute();
				}
				break;
			case "info":
				req = new Info(message, connections, connectionId, movies);
				req.execute();
				break;
			case "rent":
				req = new Rent(message, connections, connectionId, movies);
				req.execute();
				break;
			case "return":
				req = new Return(message, connections, connectionId, movies);
				req.execute();
				break;
			case "addmovie":
				if(admin) {
					req = new AddMovie(message, connections, connectionId, movies);
					req.execute();
				}else {
					connections.send(connectionId, "ERROR request " + splitMsg[1] + " failed");
					return;
				}
				break;
			case "remmovie":
				if(admin) {
					req = new RemMovie(message, connections, connectionId, movies);
					req.execute();
				}else {
					connections.send(connectionId, "ERROR request " + splitMsg[1] + " failed");
					return;
				}
				break;
			case "changeprice":
				if(admin) {
					req = new ChangePrice(message, connections, connectionId, movies);
					req.execute();
				}else {
					connections.send(connectionId, "ERROR request " + splitMsg[1] + " failed");
					return;
				}
				break;
		}
	}

}
