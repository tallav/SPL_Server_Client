package bgu.spl181.net.protocols.requests;

import bgu.spl181.net.api.bidi.ConnectionsImpl;
import bgu.spl181.net.json.*;

/**
 * Server removes a movie by the given name from the system. 
 */
public class ChangePrice extends Request {

	public ChangePrice(String message, ConnectionsImpl<String> connections, int connectionId, MoviesIO movies) {
		super(message, connections, connectionId, movies);
	}

	@Override
	public void execute() {
		String[] splitbyq = message.split("\"");
		// [0]=REQUEST changeprice , [1]=movie name, [2]= price 
		String movieName = splitbyq[1];
		String price = splitbyq[2].substring(1);
		if(!movies.hasMovie(movieName)) { // the movie doesn't exists in the system
			connections.send(connectionId, "ERROR request changeprice failed");
			return;
		}
		Movie movie = movies.getMovie(movieName);
		if(movies.updatePrice(movie, price) > 0) {
			connections.send(connectionId, "ACK changeprice " + "\"" + movieName + "\" " + "success.");
                        connections.broadcast("BROADCAST movie " + "\"" + movieName + "\" " + movie.getAvailableAmount() + " " + price);
		}else{
			connections.send(connectionId, "ERROR request changeprice failed");
			return;
		}
		return;
	}
	
}
