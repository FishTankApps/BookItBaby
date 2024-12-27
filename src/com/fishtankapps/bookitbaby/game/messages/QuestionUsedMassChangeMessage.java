package com.fishtankapps.bookitbaby.game.messages;

import com.fishtankapps.communication.Message;

public class QuestionUsedMassChangeMessage implements Message {

	private static final long serialVersionUID = -5254061320470198060L;

	private boolean[] questionUsed;
	
	public QuestionUsedMassChangeMessage(boolean[] questionUsed) {
		this.questionUsed = questionUsed;
	}
	
	public boolean[] getQuestionUsed() {
		return questionUsed;
	}
}
