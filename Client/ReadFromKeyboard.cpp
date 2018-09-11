/*
 * ReadFromKeyboard.cpp
 *
 *  Created on: 15 בינו׳ 2018
 *      Author: USER
 */

#include <boost/thread.hpp>
#include <stdlib.h>
#include "../include/ReadFromKeyboard.h"
#include "../include/ConnectionHandler.h"


ReadFromKeyboard::ReadFromKeyboard(ConnectionHandler& handler):handler(handler) {
	// TODO Auto-generated constructor stub

}

void ReadFromKeyboard::run(){
	while(handler.isLoggedIn()){
            const short bufsize = 1024;
            char buf[bufsize];
            if(handler.getStop()==true){
                std::string s;
                //std::cin>>s;
                handler.logout();
            }else{
                std::cin.getline(buf, bufsize);
                std::string line(buf);
                //int len=line.length();
                if (!handler.sendLine(line)) {
                    break;
                }
                // connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
            }
	}

}

ReadFromKeyboard::~ReadFromKeyboard() {
	// TODO Auto-generated destructor stub
}

