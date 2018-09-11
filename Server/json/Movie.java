
package bgu.spl181.net.json;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("bannedCountries")
    @Expose
    private List<String> bannedCountries;
    @SerializedName("availableAmount")
    @Expose
    private String availableAmount;
    @SerializedName("totalAmount")
    @Expose
    private String totalAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<String> getBannedCountries() {
        return bannedCountries;
    }

    public void setBannedCountries(List<String> bannedCountries) {
        this.bannedCountries = bannedCountries;
    }

    public String getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String toString(){
    	String movieInfo = "\"" + name + "\" " + availableAmount + " " + price + " ";
    	String countries = "";
    	if(bannedCountries != null) {
    		for(String country : bannedCountries){
    			String add = "\"" + country + "\" ";
    			countries = countries + add;
    		}
    	}
    	if(countries.length() > 1) {
    		countries = countries.substring(0, countries.length()-1);
    	}else {
    		movieInfo = movieInfo.substring(0, movieInfo.length()-1);
    	}
    	return movieInfo + countries;
    }
    
    public Movie clone(){
    	//return this.clone();
    	Movie ans = new Movie();
    	ans.id = new String(this.id);
    	ans.name = new String(this.name);
    	ans.price = new String(this.price);
    	ans.bannedCountries = new ArrayList<String>(this.bannedCountries);
    	ans.availableAmount = new String(this.availableAmount);
    	ans.totalAmount = new String(this.totalAmount);
    	return ans;
    }
    
    public Movie cloneUserMovie(){
    	Movie ans = new Movie();
    	ans.id = new String(this.id);
    	ans.name = new String(this.name);
    	ans.price = null;
    	ans.bannedCountries = null;
    	ans.availableAmount = null;
    	ans.totalAmount = null;
    	return ans;
    }
    
}
