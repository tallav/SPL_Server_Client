/*
 * ReadFromSocket.cpp
 *
 *  Created on: 15 בינו׳ 2018
 *      Author: USER
 */

#include "../include/ReadFromSocket.h"

#include <stdlib.h>
#include "../include/ConnectionHandler.h"

ReadFromSocket::ReadFromSocket(ConnectionHandler& handler):handler(handler){
	// TODO Auto-generated constructor stub

}

ReadFromSocket::~ReadFromSocket() {
	// TODO Auto-generated destructor stub
}

void ReadFromSocket::run(){
	while(handler.isLoggedIn() && !handler.getStop()){
            std::string answer;
            // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
            // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
            if (!handler.getLine(answer)) {
                std::cout << "Disconnected. Exiting...\n";
                break;
            }

                    //len=answer.length();
                    // A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
                    // we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
            //answer.resize(len-1);
            std::cout << answer;
            if (answer == "ACK signout succeeded\n") {
                std::cout << "Ready to exit. Press enter\n";//now we need to terminate!!!!!!!!!!!!!!!!!
                handler.setStop();
            }
	}
}

