package bgu.spl181.net.protocols.requests;

import java.util.List;

import bgu.spl181.net.api.bidi.ConnectionsImpl;
import bgu.spl181.net.json.*;

public abstract class Request {
	
	protected String message;
	protected ConnectionsImpl<String> connections;
	protected int connectionId;
	protected MoviesIO movies;
	
	public abstract void execute();
	
	public Request(String message, ConnectionsImpl<String> connections, int connectionId, MoviesIO movies) {
		this.message = message;
		this.connections = connections;
		this.connectionId = connectionId;
		this.movies = movies;
	}
	
	/**
	 * checks if the user has enough money to rent the movie
	 * @param user
	 * @param movie
	 * @return true if the user has enough money in his balance
	 */
	protected boolean checkBalance(User user, Movie movie) {
		int balance = Integer.parseInt(user.getBalance());
		int price = Integer.parseInt(movie.getPrice());
		if(balance >= price) {
			return true;
		}
		return false;
	}
	/**
	 * checks if the user already rented the movie
	 * @param user
	 * @param movie
	 * @return true if the user has the movie
	 */
	protected boolean isRented(User user, Movie movie) {
		List<Movie> userMovies = user.getMovies();
		for(Movie m : userMovies) {
			if((m.getName()).equals(movie.getName())) {
				return true;
			}
		}
		return false;
	}
	/**
	 * checks if the movie is banded in the user's country
	 * @param user
	 * @param movie
	 * @return true if the movie is banded
	 */
	protected boolean isBanned(User user, Movie movie) {
		String userCountry = user.getCountry();
		List<String> bannedCountries = movie.getBannedCountries();
		for(String country : bannedCountries) {
			if(country.equals(userCountry)) {
				return true;
			}
		}
		return false;
	}

}
