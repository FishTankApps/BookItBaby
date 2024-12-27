package com.fishtankapps.bookitbaby.game;

import java.io.Serializable;

import javax.swing.JComponent;

import com.fishtankapps.animation.AnimationGroup;

public abstract class Question implements Serializable {

	private static final long serialVersionUID = -1451519280646542388L;
	
	public abstract JComponent getQuestionDisplay(GameManager manager);
	public abstract AnimationGroup getTypeLogoAnimation();
	public abstract void handleGameEvent(GameEvent gameEvent);
	
	@Override
	public abstract boolean equals(Object o);
	
	@Override
	public abstract int hashCode();
	
	public static enum QuestionType {
		DREW_OR_FALSE("Drew or False"), MULTIPLYING_CHOICE("Multiplying Choice"), PATCHING("Patching"), 
			PHIL_IN_THE_BLANK("Phil in the Blank"), SONG_ANSWER("Song Answer");
		
		private String name;
		private QuestionType(String name) {
			this.name = name;
		}
		public String toString() {
			return name;
		}
	}
}
