#include <stdlib.h>
#include <boost/thread.hpp>

#include "../include/ConnectionHandler.h"
#include "../include/ReadFromKeyboard.h"
#include "../include/ReadFromSocket.h"

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/



int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    ReadFromKeyboard keyboard(connectionHandler);
    ReadFromSocket sock(connectionHandler);

    boost::thread th1(&ReadFromKeyboard::run,&keyboard);
    boost::thread th2(&ReadFromSocket::run,&sock);
    th1.join();
    th2.join();
    connectionHandler.close();
    return 0;
}
