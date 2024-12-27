package com.fishtankapps.bookitbaby.game.messages;

import com.fishtankapps.bookitbaby.game.GameEvent;
import com.fishtankapps.communication.Message;

public class GameEventMessage implements Message {
	
	private static final long serialVersionUID = 5695012974556458437L;
	private GameEvent event;
	
	public GameEventMessage(GameEvent event) {
		this.event = event;
	}
	
	public GameEvent getEvent() {
		return event;
	}

}
