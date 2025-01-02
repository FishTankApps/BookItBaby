package com.fishtankapps.bookitbaby.questions;

import java.util.ArrayList;

import javax.swing.JComponent;

import com.fishtankapps.animation.AnimationGroup;
import com.fishtankapps.bookitbaby.game.GameEvent;
import com.fishtankapps.bookitbaby.game.GameManager;
import com.fishtankapps.bookitbaby.game.Question;
import com.fishtankapps.bookitbaby.game.editor.QuestionProperties;

public class QuestionDraft extends Question {

	
	private static final long serialVersionUID = 380074674624044653L;
	
	private QuestionType type;
	
	private boolean isChristInContext;
	
	private String prompt;
	private String answer;
	private String songFile;
	
	private long timerLength;
	private int letterIndex;
	
	private ArrayList<String> incorrectAnswers;
	private ArrayList<Boolean> wordVisibility;
	
	public QuestionDraft() {
		type = QuestionType.DREW_OR_FALSE;
		isChristInContext = false;
		prompt = "";
		answer = " ";
		songFile = "";
		timerLength = 0;
		letterIndex = 0;
		
		incorrectAnswers = new ArrayList<>();
		wordVisibility = new ArrayList<>();
	}
	
	public QuestionDraft(QuestionProperties qp) {
		type = qp.getQuestionType();
		isChristInContext = qp.isChristInContextQuestion();
		prompt = qp.getPrompt();
		answer = qp.getAnswer();
		songFile = qp.getSongFile();
		timerLength = qp.getTimeLimit();
		letterIndex = qp.getLetterIndex();
		
		incorrectAnswers = qp.getIncorrectAnswers();
		wordVisibility = qp.getWordVisibility();
	}


	public QuestionType getQuestionType() {
		return type;
	}
	public boolean isChristInContextQuestion() {
		return isChristInContext;
	}
	public String getPrompt() {
		return prompt;
	}
	public String getAnswer() {
		return answer;
	}
	public String getSongFile() {
		return songFile;
	}
	public long getTimeLimit() {
		return timerLength;
	}
	public int getLetterIndex() {
		return letterIndex;
	}
	public ArrayList<String> getIncorrectAnswers() {
		return incorrectAnswers;
	}
	public ArrayList<Boolean> getWordVisibility() {
		return wordVisibility;
	}
	
	
	public JComponent getQuestionDisplay(GameManager manager) {
		return null;
	}

	public AnimationGroup getTypeLogoAnimation() {
		return null;
	}

	public void handleGameEvent(GameEvent gameEvent) {}

	public boolean equals(Object o) {
		return false;
	}

	public int hashCode() {
		return 0;
	}

}
