package com.fishtankapps.bookitbaby.game.messages;

import com.fishtankapps.communication.Message;

public class BlueScoreUpdateMessage implements Message {

	private static final long serialVersionUID = 6089128673554409125L;

	private int score;

	public BlueScoreUpdateMessage(int score) {
		super();
		this.score = score;
	}
	
	public int getScore() {
		return score;
	}
}
