package bgu.spl181.net.protocols.requests;

import bgu.spl181.net.api.bidi.ConnectionsImpl;
import bgu.spl181.net.json.*;

/**
 * Server adds the amount given to the user's balance.
 */
public class BalanceAdd extends Request {

	public BalanceAdd(String message, ConnectionsImpl<String> connections, int connectionId, MoviesIO movies) {
		super(message, connections, connectionId, movies);
	}

	@Override
	public void execute() {
		String[] splitMsg = message.split(" ");
		// [0]= REQUEST , [1]= balance , [2]=add , [3]=amount
		String userName = connections.getConnUser(connectionId).getUsername();
		User user = connections.getUsersIO().getUser(userName);
		int amount = Integer.parseInt(splitMsg[3]);
		int update = connections.getUsersIO().updateBalance(user, amount, true);
		if(update > 0) {
			connections.send(connectionId, "ACK balance " + update + " added " + amount);	
		}
	}
	
}
