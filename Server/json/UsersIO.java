package bgu.spl181.net.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.gson.Gson;

public class UsersIO {
	final static String PATH_DB = "/Database/Users.json";
	private Gson gson;
	private final String fullPath;
	private ReadWriteLock rwLock = new ReentrantReadWriteLock();
	private Lock readLock;
	private Lock writeLock;
	private Users users;
	
	public UsersIO(){
		gson = new Gson();
		fullPath = System.getProperty("user.dir") + PATH_DB;
		users = getUsersObj();
		readLock = rwLock.readLock();
		writeLock = rwLock.writeLock();
	}
	
	public boolean hasUser(String name){
		boolean ans=false;
		readLock.lock();
		ans = userExists(name);
		readLock.unlock();
		return ans;
	}
	
	public User getUser(String name){
		readLock.lock();
    	for(User u : users.getUsers()) {
    		if(u.getUsername().equals(name)) {
    			readLock.unlock();
    			return u.clone();
    		}
    	}
		readLock.unlock();
		return null;
	}
	
	public boolean addUser(User user){
		writeLock.lock();
		String name = user.getUsername();
		if(userExists(name)){
			writeLock.unlock();
			return false;
		}
		users.getUsers().add(user);
		updateUsers();
		writeLock.unlock();
		return true;
	}
	
	private boolean userExists(String name){
		Iterator<User> it = users.getUsers().iterator();
		while(it.hasNext()){
			if(it.next().getUsername().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public String currBalance(User user) {
    	readLock.lock();
    	for(User u : users.getUsers()){
    		if(u.getUsername().equals(user.getUsername())){
    			readLock.unlock();
    			return u.getBalance();
			}				
		}
    	readLock.unlock();
    	return null;
	}
	
    public int updateBalance(User user, int amount, boolean add) {
    	int ans = -1;
    	writeLock.lock();
    	for(User u : users.getUsers()){
    		if(u.getUsername().equals(user.getUsername())){
    			int balance = Integer.parseInt(u.getBalance());
    			if(amount > 0) {
    				Integer newBalance = new Integer(balance);
    				if(add) { newBalance = balance + amount; }
    				if(!add) { newBalance = balance - amount; }
	    			u.setBalance(newBalance.toString());
	    			ans = newBalance;
    			}				
    		}
    	}
    	updateUsers();
    	writeLock.unlock();
    	return ans;
    }
    
    public boolean updateUserMovies(User user, Movie movie, boolean add) {
    	boolean ans = false;
    	writeLock.lock();
    	for(User u : users.getUsers()){
    		if(u.getUsername().equals(user.getUsername())){
    			if(add) { u.addMovie(movie.cloneUserMovie()); }
    			if(!add) { u.removeMovie(movie);}
    			ans = true;
    		}
    	}
    	updateUsers();
    	writeLock.unlock();
    	return ans;
    }
    
	// checks if the movie is rented by one of the users
	public boolean rented(String movieName) {
		boolean ans = false;
    	readLock.lock();
    	for(User u : users.getUsers()){
			if(isRented(u, movieName)) {
				ans = true;
			}
    	}
    	updateUsers();
    	readLock.unlock();
    	return ans;
	}
	
	private boolean isRented(User user, String movieName) {
		List<Movie> userMovies = user.getMovies();
		for(Movie m : userMovies) {
			if((m.getName()).equals(movieName)) {
				return true;
			}
		}
		return false;
	}

	private void updateUsers(){
		String text = gson.toJson(users);
		try {
			writeToFile(fullPath,text);
		}catch(IOException e) {
			throw new RuntimeException("Cannot write to file");
		}
	}

	private Users getUsersObj() {
		try {
			String jsonFile = readFile(fullPath);
			Users users = gson.fromJson(jsonFile, Users.class);
			return users;
		}catch(IOException e) {
			throw new RuntimeException("Cannot read to file");
		}
	}
	
	private String readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
	
	private void writeToFile(String fileName, String text) throws IOException{
			FileWriter fw = null;
			BufferedWriter bw = null;
		try{
			fw = new FileWriter(fileName);
			bw = new BufferedWriter(fw);
			bw.write(text);
		}catch(IOException e){
			e.printStackTrace();
		}
		finally{
			try{
				if(bw != null){
					bw.close();
				}
				if(fw !=null){
					fw.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

}
