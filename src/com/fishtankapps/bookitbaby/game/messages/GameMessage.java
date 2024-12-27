package com.fishtankapps.bookitbaby.game.messages;

import com.fishtankapps.bookitbaby.game.BookItBabyGame;
import com.fishtankapps.communication.Message;

public class GameMessage implements Message {

	private static final long serialVersionUID = 5844754199530295529L;

	private BookItBabyGame game;
	
	public GameMessage(BookItBabyGame game) {
		this.game = game;
	}
	
	public BookItBabyGame getGame() {
		return game;
	}
}
