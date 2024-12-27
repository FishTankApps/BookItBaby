package com.fishtankapps.communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;


public abstract class Communicator {
	
	protected ObjectOutputStream sendStream; 
	protected ObjectInputStream  receiveStream;
	
	private Queue<Message> messages;
	private ArrayList<ConnectionStateListener> connectionStateListeners;
	private ArrayList<MessageListener> messageListeners;
	private Thread messageCheckerThread;
	
	private Socket socket;
	
	private int messageCount;
	
	protected Communicator() {
		sendStream = null;
		receiveStream = null;
		
		messageCount = 0;
		
		messages = new LinkedList<>();
		messageListeners = new ArrayList<>();
		connectionStateListeners = new ArrayList<>();
		
		messageCheckerThread = new Thread(() -> {
				try {
					while(true) {
						Thread.sleep(10);
						Message message = (Message) receiveStream.readObject();
						
						if(message != null) {
							if(message instanceof CloseConnectionMessage)
								throw new RuntimeException("Connection Closed");
							
							if(messageListeners.size() == 0)
								messages.offer(message);
							else
								for(MessageListener l : messageListeners)
									l.messageReceived(message);				
						}

						if (messageListeners.size() > 0) {
							while(messages.size() > 0) {
								message = messages.poll();
								for(MessageListener l : messageListeners)
									l.messageReceived(message);			
							}
						}
					}
						
				} catch (Exception e) {}
				
				closeConnection(false);
			});
	}
	
	protected abstract Socket openSocket();
	protected abstract void close();
	
	public boolean openConnection() {
		try {
			socket = openSocket();
			
			if(socket == null)
				return false;
			
			sendStream = new ObjectOutputStream(socket.getOutputStream()); 
			receiveStream = new ObjectInputStream(socket.getInputStream());
			
			messageCheckerThread.start();
			
			for(ConnectionStateListener l : connectionStateListeners)
				l.onConnectionOpenned();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	public boolean closeConnection() {
		return closeConnection(false);
	}
	
	private boolean closeConnection(boolean sendCloseMessage) {
		try {
			if(sendCloseMessage)
				sendMessage(new CloseConnectionMessage());
			
			messageCheckerThread.interrupt();
			
			if(sendStream != null) {
				sendStream.flush();
				sendStream.close(); 
			}
			if(receiveStream != null)
				receiveStream.close();
			
			if(socket != null)
				socket.close();			
			
			for(ConnectionStateListener l : connectionStateListeners)
				l.onConnectionClosed();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	public boolean isActive() {
		return socket.isConnected();
	}
	
	public final boolean sendMessage(Message message) {
		try {
			sendStream.writeObject(message);
			
			messageCount++;
			if(messageCount > 50) {
				messageCount = 0;
				sendStream.reset();
			}			
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	public void addConnectionStateListener(ConnectionStateListener l) {
		connectionStateListeners.add(l);
	}
	
	public void removeConnectionStateListener(ConnectionStateListener l) {
		connectionStateListeners.remove(l);
	}
	
	
	
 	public final void addMessageListener(MessageListener l) {
		messageListeners.add(l);
	}

	private class CloseConnectionMessage implements Message {
		private static final long serialVersionUID = 9003411882426054034L;
	}
}