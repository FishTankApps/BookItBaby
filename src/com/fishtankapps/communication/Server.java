package com.fishtankapps.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Communicator {

	private ServerSocket server;
	
	int port;
	
	public Server(int port) {
		this.port = port;
	}
	
	@Override
	protected Socket openSocket() {
		try {
			server = new ServerSocket(port);
			return server.accept();
		} catch (Exception e) {
			return null;
		}		
	}

	@Override
	protected void close() {
		try {
			server.close();
		} catch (IOException e) {}
	}
}
