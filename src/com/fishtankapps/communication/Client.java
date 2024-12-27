package com.fishtankapps.communication;

import java.net.Socket;

public class Client extends Communicator {

	private String ipAddress;
	private int port;
	
	public Client(String ipAddress, int port) {
		this.ipAddress = ipAddress;
		this.port = port;
	}

	protected Socket openSocket() {
		try {
			return new Socket(ipAddress, port);
		} catch (Exception e) {
			return null;
		}
	}

	protected void close() {}
}
