package bgu.spl181.net.protocols.requests;

import java.util.LinkedList;
import java.util.List;

import bgu.spl181.net.api.bidi.ConnectionsImpl;
import bgu.spl181.net.json.*;

/**
 * The server adds a new movie to the system with the given information. 
 * The new movie ID will be the highest ID in the system + 1. 
 */
public class AddMovie extends Request {

	public AddMovie(String message, ConnectionsImpl<String> connections, int connectionId, MoviesIO movies) {
		super(message, connections, connectionId, movies);
	}

	@Override
	public void execute() {
		Movie newMovie = creatMovie(message);
		if(newMovie == null) {
			connections.send(connectionId, "ERROR request addmovie failed");
			return;
		}else {
			movies.addMovie(newMovie);
			connections.send(connectionId, "ACK addmovie " + "\"" + newMovie.getName() + "\" " + "success.");
			connections.broadcast("BROADCAST movie " + "\"" + newMovie.getName() + "\" " + newMovie.getTotalAmount() + " " + newMovie.getPrice());
		}
		return;
	}
	
	private Movie creatMovie(String msg) {
		String[] splitAddMovie = msg.split("\"");
		// [0]=REQUEST addmovie , [1]=movie name, [2]= totalAmount price , [3]=country, [4]=space, [5]=...
		String movieName = splitAddMovie[1];
		if(movies.hasMovie(movieName)) { // movie name already exists in the system
			return null;
		}
		String temp = splitAddMovie[2];
		String[] split2 = temp.split(" ");
		// [0]=Blank , [1]= totalAmount , [2]=price
		if(split2.length > 2) {
			String totalAmount = split2[1];
			String price = split2[2];
			if(Integer.parseInt(totalAmount) > 0 && Integer.parseInt(price) > 0){
				Movie addMovie = new Movie();
				addMovie.setName(movieName);
				addMovie.setPrice(price);
				addMovie.setTotalAmount(totalAmount);
				addMovie.setAvailableAmount(totalAmount);
				//TODO: what is the id of a new movie???
				addMovie.setId("0");
				List<String> countries = new LinkedList<String>();
				if(splitAddMovie.length > 3) {
					for(int i = 3; i < splitAddMovie.length; i=i+2) {
						countries.add(splitAddMovie[i]);
					}
				}
				addMovie.setBannedCountries(countries);
				return addMovie;
			}
		}
		return null;
	}
	
}
