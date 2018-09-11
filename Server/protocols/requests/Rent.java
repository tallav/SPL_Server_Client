package bgu.spl181.net.protocols.requests;

import bgu.spl181.net.api.bidi.ConnectionsImpl;
import bgu.spl181.net.json.*;

/**
 * Server tries to add the movie to the user rented movie list, 
 * remove the cost from the user's balance, add the movie to the user rented movie list
 * and reduce the amount available for rent by 1. 
 */
public class Rent extends Request {

	public Rent(String message, ConnectionsImpl<String> connections, int connectionId, MoviesIO movies) {
		super(message, connections, connectionId, movies);
	}

	@Override
	public void execute() {
		String[] splitMsg = message.split("\"");
		// [0]=REQUEST rent , [1]=movie name			
		String movieName = splitMsg[1];
		if(!movies.hasMovie(movieName)) { // the movie doesn't exists in the system
			connections.send(connectionId, "ERROR request rent failed");
			return;
		}
		Movie movie = movies.getMovie(movieName);
		String userName = connections.getConnUser(connectionId).getUsername();
		User user = connections.getUsersIO().getUser(userName);
		if(checkBalance(user, movie) && !isRented(user, movie) && !isBanned(user, movie)) { // checks if the user can rent the movie
			int amount = movies.decAmount(movie);
			if(amount < 0) { // decreases the available amount by 1
				connections.send(connectionId, "ERROR request rent failed");
				return;
			}
			// sets the user's balance
			int price =  Integer.parseInt(movie.getPrice());
			connections.getUsersIO().updateBalance(user, price, false);
			// adds the movie to the user's list
			connections.getUsersIO().updateUserMovies(user, movie, true);
			// request completed - sends a message and broadcast
			connections.send(connectionId, "ACK rent " + "\"" + movieName + "\" " + "success");
			connections.broadcast("BROADCAST movie " + "\"" + movieName + "\" " + amount + " " + price);
		}else { // the user can't rent
			connections.send(connectionId, "ERROR request rent failed"); 
		}
		return;
	}
	
}
