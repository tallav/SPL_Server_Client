package bgu.spl181.net.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.gson.Gson;

public class MoviesIO {
	final static String PATH_DB = "/Database/Movies.json";
	private Gson gson;
	private final String fullPath;
	private ReadWriteLock rwLock = new ReentrantReadWriteLock();
	private Lock readLock;
	private Lock writeLock;
	private Movies movies;
	
	public MoviesIO() {
		gson = new Gson();
		fullPath = System.getProperty("user.dir") + PATH_DB;
		movies = getMoviesObj();
		readLock = rwLock.readLock();
		writeLock = rwLock.writeLock();
	}
	
	public boolean hasMovie(String name) {
		readLock.lock();
    	for(Movie m : movies.getMovies()) {
    		if(m.getName().equals(name)) {
    			readLock.unlock();
    			return true;
    		}
    	}
    	readLock.unlock();
    	return false;
    }
	
	public Movie getMovie(String name) {
		readLock.lock();
    	for(Movie m : movies.getMovies()) {
    		if(m.getName().equals(name)) {
    			readLock.unlock();
    			return m.clone();
    		}
    	}
    	readLock.unlock();
    	return null;
    }
	
	public List<String> getMoviesList(){
		readLock.lock();
		LinkedList<String> moviesList = new LinkedList<String>();
		for(Movie m : movies.getMovies()) {
			moviesList.add(m.getName());
    	}
		readLock.unlock();
		return moviesList;
	}
    
    public void addMovie(Movie movie){
    	int maxId = findMaxId();
    	Integer id = maxId + 1;
    	movie.setId(id.toString());
    	writeLock.lock();
    	movies.getMovies().add(movie);
    	updateMovies();
    	writeLock.unlock();
    }
    
    public boolean removeMovie(Movie movie){
    	writeLock.lock();
    	Iterator<Movie> it = movies.getMovies().iterator();
    	int ind=0;
    	boolean find=false;
    	while(it.hasNext() && !find){
    		Movie curr = it.next();
    		if(curr.getName().equals(movie.getName())){
    			find = true;
    			movies.getMovies().remove(ind);
    		}
    		ind = ind + 1;
    	}
    	updateMovies();
    	writeLock.unlock();
    	return find;
    }
    
    public int decAmount(Movie movie){
    	int ans = -1;
    	writeLock.lock();
    	for(Movie m : movies.getMovies()){
    		if(m.getName().equals(movie.getName())){
    			int amount = Integer.parseInt(m.getAvailableAmount());
    			if(amount>0){
    				Integer newAmount = amount - 1;
    				m.setAvailableAmount(newAmount.toString());
    				ans = newAmount.intValue();
    			}
    		}
    	}
    	updateMovies();
    	writeLock.unlock();
    	return ans;
    }
    
    public int incAmount(Movie movie){
    	int ans = -1;
    	writeLock.lock();
    	for(Movie m : movies.getMovies()){
    		if(m.getName().equals(movie.getName())){
    			int amount = Integer.parseInt(m.getAvailableAmount());
                        Integer newAmount = amount + 1;
                        if(newAmount <= Integer.parseInt(m.getTotalAmount())){
                            m.setAvailableAmount(newAmount.toString());
                            ans = newAmount;
                        }
    		}
    	}
    	updateMovies();
    	writeLock.unlock();
    	return ans;
    }
    
    public int updatePrice(Movie movie, String newPrice) {
    	int ans = -1;
    	writeLock.lock();
    	for(Movie m : movies.getMovies()){
    		if(m.getName().equals(movie.getName())){
    			if(Integer.parseInt(newPrice) > 0) {
    				m.setPrice(newPrice);
    				ans = Integer.parseInt(newPrice);
    			}				
    		}
    	}
    	updateMovies();
    	writeLock.unlock();
    	return ans;
    }
    
    public int findMaxId() {
    	readLock.lock();
    	int max = 0;
    	for(Movie m : movies.getMovies()){
    		int temp = Integer.parseInt(m.getId());
    		if(temp > max) {
    			max = temp;
    		}
    	}
    	readLock.unlock();
    	return max;
    }
    
	private void updateMovies(){
		String text = gson.toJson(movies);
		try {
			writeToFile(fullPath,text);
		}catch(IOException e) {
			throw new RuntimeException("Cannot write to file");
		}
	}
	
	private Movies getMoviesObj() {
		try {
			String jsonFile = readFile(fullPath);
			Movies movies = gson.fromJson(jsonFile, Movies.class);
			return movies;
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
