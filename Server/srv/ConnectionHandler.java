package bgu.spl181.net.srv;

import java.io.Closeable;

public interface ConnectionHandler<T> extends Closeable{

    void send(T msg) ;

}