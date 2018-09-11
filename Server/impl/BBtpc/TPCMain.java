package bgu.spl181.net.impl.BBtpc;

import bgu.spl181.net.srv.Server;
import bgu.spl181.net.protocols.*;

public class TPCMain {
	public static void main(String[] args){
		
        Server.threadPerClient(
        		Integer.parseInt(args[0]), 
                () ->  new MovieRentalServiceProtocol(), //protocol factory
                LineMessageEncoderDecoder::new //message encoder decoder factory
        ).serve();
	}
}
