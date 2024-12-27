package com.fishtankapps.bookitbaby.game.editor;

import java.util.ArrayList;

import com.fishtankapps.bookitbaby.game.Question;
import com.fishtankapps.bookitbaby.game.Question.QuestionType;
import com.fishtankapps.bookitbaby.questions.ChristInContextQuestion;
import com.fishtankapps.bookitbaby.questions.DrewOrFalse;
import com.fishtankapps.bookitbaby.questions.MultiplingChoice;
import com.fishtankapps.bookitbaby.questions.Patching;
import com.fishtankapps.bookitbaby.questions.PhilInTheBlank;
import com.fishtankapps.bookitbaby.questions.SongAnswer;

public class QuestionProperties {

	private ArrayList<OnChangeListener> changeListeners;
	
	private QuestionType type;
	
	private boolean isChristInContext;
	
	private String prompt;
	private String answer;
	private String songFile;
	
	private long timerLength;
	private int letterIndex;
	
	private ArrayList<String> incorrectAnswers;
	private ArrayList<Boolean> wordVisibility;
	
	public QuestionProperties() {
		type = null;
		
		isChristInContext = false;
		
		prompt = "";
		answer = "";
		songFile = "";
		
		letterIndex = -1;
		timerLength = -1;
		
		incorrectAnswers = new ArrayList<>();
		wordVisibility = new ArrayList<>();
		changeListeners = new ArrayList<>();
		
		//editor = new QuestionPropertiesEditor(this, null);
	}
	public QuestionProperties(Question question) {
		this();
		
		if(question instanceof ChristInContextQuestion) {
			isChristInContext = true;
			question = ((ChristInContextQuestion) question).getQuestion();
		}
		
		if (question instanceof DrewOrFalse) {
			type = QuestionType.DREW_OR_FALSE;
			DrewOrFalse q = (DrewOrFalse) question;
			prompt = q.getPrompt();
			timerLength = q.getTimerLength();
			
		} else if (question instanceof MultiplingChoice) {
			type = QuestionType.MULTIPLYING_CHOICE;
			MultiplingChoice q = (MultiplingChoice) question;			
			prompt = q.getPrompt();
			answer = q.getCorrectAnswer();
			for(String s : q.getAnswers())
				incorrectAnswers.add(s);
			
			incorrectAnswers.remove(answer);			
			
		} else if (question instanceof Patching) {
			type = QuestionType.PATCHING;
			Patching q = (Patching) question;
			prompt = q.getPrompt();
			answer = q.getAnswer();
			
			for(boolean b : q.getWordVisibility())
				wordVisibility.add(b);
			
		} else if (question instanceof PhilInTheBlank) {
			type = QuestionType.PHIL_IN_THE_BLANK;
			PhilInTheBlank q = (PhilInTheBlank) question;
			prompt = q.getPrompt();
			answer = q.getAnswer();
			letterIndex = q.getLetterIndex();
			
		} else if (question instanceof SongAnswer) {
			type = QuestionType.SONG_ANSWER;
			SongAnswer q = (SongAnswer) question;
			prompt = q.getPrompt();
			answer = q.getSongName();
			songFile = q.getSongFileName();
		}
	}
	
	public void addOnChangeListener(OnChangeListener l) {
		changeListeners.add(l);
	}
	
	public QuestionType getQuestionType() {
		return type;
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
	public String getIncorrectAnswersString() {
		if(incorrectAnswers.size() == 0)
			return "";
		
		String s = incorrectAnswers.get(0);
		
		for(int i = 0; i < incorrectAnswers.size(); i++)
			s += "; " + incorrectAnswers.get(i);
		
		return s;
	}
	public ArrayList<Boolean> getWordVisibility() {
		return wordVisibility;
	}
	public boolean isChristInContextQuestion() {
		return isChristInContext;
	}
	
	public void setQuestionType(QuestionType type) {
		this.type = type;
		notifyChangeListeners(Property.QUESTION_TYPE);
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
		notifyChangeListeners(Property.PROMPT);
	}
	public void setAnswer(String answer) {
		this.answer = answer;
		notifyChangeListeners(Property.ANSWER);
	}
	public void setSongFile(String songFile) {
		this.songFile = songFile;
		notifyChangeListeners(Property.SONG_FILE);
	}
	public void setTimeLimit(long timeLimit) {
		this.timerLength = timeLimit;
		notifyChangeListeners(Property.TIMER_LENGTH);
	}
	public void setLetterIndex(int letterIndex) {
		this.letterIndex = letterIndex;
		notifyChangeListeners(Property.LETTER_INDEX);
	}
	public void setIncorrectAnswers(ArrayList<String> incorrectAnswers) {
		this.incorrectAnswers = incorrectAnswers;
		notifyChangeListeners(Property.INCORRECT_ANSWERS);
	}
	public void setIncorrectAnswers(String incorrectAnswers) {
		this.incorrectAnswers.clear();
		
		String[] split = incorrectAnswers.split(";");
		
		for(String s : split)
			this.incorrectAnswers.add(s.trim());
		
		notifyChangeListeners(Property.INCORRECT_ANSWERS);
	}
	public void setWordVisibility(ArrayList<Boolean> wordVisibility) {
		this.wordVisibility = wordVisibility;
		notifyChangeListeners(Property.WORD_VISIBILITY);
	}
	public void setIsChristInContextQuestion(boolean isChristInContext) {
		this.isChristInContext = isChristInContext;
		notifyChangeListeners(Property.IS_CHRIST_IN_CONTEXT);
	}
	
	private void notifyChangeListeners(Property p) {
		for(OnChangeListener l : changeListeners)
			l.onChange(this, p);
	}
	
	
	public boolean canGenerateQuestionType(QuestionType type) {
		
		if (type == QuestionType.DREW_OR_FALSE)
			return canGenerateDrewOrFalse();
		else if (type == QuestionType.MULTIPLYING_CHOICE)
			return canGenerateMultiplingChoice();
		else if (type == QuestionType.PATCHING)
			return canGeneratePatching();
		else if (type == QuestionType.PHIL_IN_THE_BLANK)
			return canGeneratePhilInTheBlank();
		else if (type == QuestionType.SONG_ANSWER)
			return canGenerateSongAnswer();
		else
			return false;		
	}
	public boolean canGenerateDrewOrFalse() {
		return !prompt.equals("") && timerLength != -1;
	}
	public boolean canGenerateMultiplingChoice() {
		return !prompt.equals("") && !answer.equals("") && incorrectAnswers.size() > 0;
	}
	public boolean canGeneratePatching() {
		return !prompt.equals("") && !answer.equals("") && wordVisibility.size() > 0;
	}
	public boolean canGeneratePhilInTheBlank() {
		return !prompt.equals("") && !answer.equals("") && letterIndex != -1;
	}
	public boolean canGenerateSongAnswer() {
		return !prompt.equals("") && !answer.equals("") && !songFile.equals("");
	}
	
	
	public Question generateQuestion() {
		
		if(!canGenerateQuestionType(type))
			return null;
		
		Question q = null;
		
		if (type == QuestionType.DREW_OR_FALSE)
			q = new DrewOrFalse(prompt, timerLength);
		else if (type == QuestionType.MULTIPLYING_CHOICE)
			q = new MultiplingChoice(prompt, answer, incorrectAnswers);
		else if (type == QuestionType.PATCHING)
			q = new Patching(prompt, answer, wordVisibility);
		else if (type == QuestionType.PHIL_IN_THE_BLANK)
			q = new PhilInTheBlank(prompt, answer, letterIndex);
		else if (type == QuestionType.SONG_ANSWER)
			q = new SongAnswer(prompt, answer, songFile);
		
		if(isChristInContext)
			q = new ChristInContextQuestion(q);
		
		return q;
	}

	public static interface OnChangeListener {
		public void onChange(QuestionProperties qp, Property p);
	}
	
	public static enum Property {
		QUESTION_TYPE, IS_CHRIST_IN_CONTEXT, PROMPT, ANSWER, SONG_FILE, TIMER_LENGTH, LETTER_INDEX,
			INCORRECT_ANSWERS, WORD_VISIBILITY;
	}
	
}