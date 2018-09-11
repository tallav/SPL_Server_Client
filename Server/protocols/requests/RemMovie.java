package bgu.spl181.net.protocols.requests;

import bgu.spl181.net.api.bidi.ConnectionsImpl;
import bgu.spl181.net.json.*;

/**
 * Server removes a movie by the given name from the system. 
 */
public class RemMovie extends Request {

	public RemMovie(String message, ConnectionsImpl<String> connections, int connectionId, MoviesIO movies) {
		super(message, connections, connectionId, movies);
	}

	@Override
	public void execute() {
		String[] splitAddMovie = message.split("\"");
		// [0]=REQUEST remmovie , [1]=movie name
		String movieName = splitAddMovie[1];
			if(!movies.hasMovie(movieName)) { // the movie doesn't exists in the system
				connections.send(connectionId, "ERROR request remmovie failed");
				return;
			}
			Movie movie = movies.getMovie(movieName);
			boolean isRented = connections.getUsersIO().rented(movieName); // checks if the movie is rented by one of the users
		    if(isRented){
		    	connections.send(connectionId, "ERROR request remmovie failed");
		    	return;
		    }else{
		    	movies.removeMovie(movie);
		    	connections.send(connectionId, "ACK remmovie " + "\"" + movieName + "\" " + "success.");
		    	connections.broadcast("BROADCAST movie " + "\"" + movieName + "\" " + "removed");
		    }
		return;
	}
	
}
