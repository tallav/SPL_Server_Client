package bgu.spl181.net.impl.BBreactor;
/*
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;
import bgu.spl181.net.json.*;
*/
import bgu.spl181.net.protocols.LineMessageEncoderDecoder;
import bgu.spl181.net.protocols.MovieRentalServiceProtocol;
import bgu.spl181.net.srv.Server;

public class ReactorMain {
	
	public static void main(String[] args){
		
        Server.reactor(
                Runtime.getRuntime().availableProcessors(),
                Integer.parseInt(args[0]), //port
                () ->  new MovieRentalServiceProtocol(), //protocol factory
                LineMessageEncoderDecoder::new //message encoder decoder factory
        ).serve();
	}
	
}

