package bgu.spl181.net.protocols.requests;

import bgu.spl181.net.api.bidi.ConnectionsImpl;
import bgu.spl181.net.json.*;

/**
 * Server returns information about the movies in the system. 
 */
public class Info extends Request {

	public Info(String message, ConnectionsImpl<String> connections, int connectionId, MoviesIO movies) {
		super(message, connections, connectionId, movies);
	}

	@Override
	public void execute() {
		String[] splitMsg = message.split("\"");
		// [0]=REQUEST info , [1]=movie name
		if(splitMsg.length == 2){ // the client entered a movie name
			String movieName = splitMsg[1];
			if(!movies.hasMovie(movieName)) { // the movie doesn't exists in the system
				System.out.println("entered if error");
				connections.send(connectionId, "ERROR request info failed");
				return;
			}
			String movieInfo = movies.getMovie(movieName).toString();
			connections.send(connectionId, "ACK info " + movieInfo); // sends the info for the movie specified 
		}else{ // the client didn't enter a movie name
			String moviesNames = "";
			for(String m : movies.getMoviesList()){
				String add = "\"" + m + "\" ";
				moviesNames = moviesNames + add;
			}
			if(moviesNames.length() > 1) {
				moviesNames = moviesNames.substring(0, moviesNames.length()-1);
			}
			connections.send(connectionId, "ACK info " + moviesNames); // sends the names of all the movies in the system 
		}
		return;
	}
	
}
