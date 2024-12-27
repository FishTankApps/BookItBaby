package com.fishtankapps.bookitbaby.game.messages;

import com.fishtankapps.bookitbaby.game.Question;
import com.fishtankapps.communication.Message;

public class CurrentQuestionUpdateMessage implements Message {

	private static final long serialVersionUID = -5254061320470198060L;

	private Question question;
	
	public CurrentQuestionUpdateMessage(Question question) {
		this.question = question;
	}
	
	public Question getQuestion() {
		return question;
	}
}
