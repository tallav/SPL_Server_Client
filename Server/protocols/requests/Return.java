package bgu.spl181.net.protocols.requests;

import bgu.spl181.net.api.bidi.ConnectionsImpl;
import bgu.spl181.net.json.*;

/**
 * Server tries to add the movie to the user rented movie list, 
 * remove the cost from the user's balance, add the movie to the user rented movie list
 * and reduce the amount available for rent by 1. 
 */
public class Return extends Request {

	public Return(String message, ConnectionsImpl<String> connections, int connectionId, MoviesIO movies) {
		super(message, connections, connectionId, movies);
	}

	@Override
	public void execute() {
		String[] splitMsg = message.split("\"");
		// [0]=REQUEST rent , [1]=movie name
		String movieName = splitMsg[1];
		if(!movies.hasMovie(movieName)) { // the movie doesn't exists in the system
			connections.send(connectionId, "ERROR request return failed");
			return;
		}
		Movie movie = movies.getMovie(movieName);
		String userName = connections.getConnUser(connectionId).getUsername();
		User user = connections.getUsersIO().getUser(userName);
		if(!isRented(user, movie)) { // checks if the user has the movie
			connections.send(connectionId, "ERROR request return failed");
			return;
		}
		// removes the movie from the user's list
		connections.getUsersIO().updateUserMovies(user, movie, false);
		// sets the available amount of the movie
		int newAmount = movies.incAmount(movie);
		if(newAmount < 0){
			connections.send(connectionId, "ERROR request return failed");
			return;
		}
		// request completed - sends a message and broadcast
		connections.send(connectionId, "ACK return " + "\"" + movieName + "\" " + "success");
		connections.broadcast("BROADCAST movie " + "\"" + movieName + "\" " + newAmount + " " + movie.getPrice());
		return;
	}
	
}
