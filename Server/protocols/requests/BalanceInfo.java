package bgu.spl181.net.protocols.requests;

import bgu.spl181.net.api.bidi.ConnectionsImpl;
import bgu.spl181.net.json.*;

/**
 * Server returns the user's current balance.
 */
public class BalanceInfo extends Request {

	public BalanceInfo(String message, ConnectionsImpl<String> connections, int connectionId, MoviesIO movies) {
		super(message, connections, connectionId, movies);
	}

	@Override
	public void execute() {
		String userName = connections.getConnUser(connectionId).getUsername();
		User user = connections.getUsersIO().getUser(userName);
		String balance = connections.getUsersIO().currBalance(user);
		connections.send(connectionId, "ACK balance " + balance);
		return;
	}
	
}
