
package bgu.spl181.net.json;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("movies")
    @Expose
    private List<Movie> movies;
    @SerializedName("balance")
    @Expose
    private String balance;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
    
    public boolean addMovie(Movie movie) {
    	return movies.add(movie);
    }
    
    public boolean removeMovie(Movie movie) {
		for(Movie m : movies) {
			if((m.getName()).equals(movie.getName())) {
				movies.remove(m);
				return true;
			}
		}
		return false;    	
    }
    
    public User clone(){
    	User ans = new User();
    	ans.username = new String(this.username);
    	ans.type = new String(this.type);
    	ans.password = new String(this.password);
    	ArrayList<Movie> newMovies = new ArrayList<Movie>();
    	for(Movie m : movies){
    		newMovies.add(m.cloneUserMovie());
    	}
    	ans.movies = newMovies;
    	if(this.country!=null){
            ans.country = new String(this.country);
        }else{
            ans.country = new String();
        }
    	ans.balance = new String(this.balance);
    	return ans;
    }

}
