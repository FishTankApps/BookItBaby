package com.fishtankapps.bookitbaby.game;

import java.io.File;
import java.util.ArrayList;

import com.fishtankapps.communication.Communicator;

public class GameManager {

	private ArrayList<TeamRenameListener> teamRenameListeners;
	private ArrayList<RedTeamScoreListener> redTeamScoreListeners;
	private ArrayList<BlueTeamScoreListener> blueTeamScoreListeners;
	private ArrayList<CurrentQuestionListener> currentQuestionListeners;
	private ArrayList<QuestionUsedMassChangeListener> questionUsedMassChangeListeners;
	private ArrayList<GameEventListener> gameEventListeners;
	private ArrayList<BuzzerListener> buzzerListeners;
	private ArrayList<RepaintListener> repaintListeners;
	
	private Team buzzedInTeam;
	
	private String redTeamName, blueTeamName;
	private int redTeamScore, blueTeamScore;
	
	private BookItBabyGame game;
	private Question currentQuestion;
	private int currentQuestionIndex;
	private boolean[] questionUsed;
	
	public GameManager() {
		buzzedInTeam = Team.NONE;
		
		currentQuestion = null;
		currentQuestionIndex = -1;
		questionUsed = new boolean[0];
		
		buzzerListeners = new ArrayList<>();
		repaintListeners = new ArrayList<>();
		gameEventListeners = new ArrayList<>();
		teamRenameListeners = new ArrayList<>();
		redTeamScoreListeners = new ArrayList<>();
		blueTeamScoreListeners = new ArrayList<>();
		currentQuestionListeners = new ArrayList<>();
		questionUsedMassChangeListeners = new ArrayList<>();
		
		redTeamName = "Team Half-Baked Turkey Sandwiches";
		blueTeamName = "Team Big Green Moldy Bagel";
		
		redTeamScore = blueTeamScore = 0;
	}
	
	public GameManager(BookItBabyGame game) {
		this.game = game;
		
		buzzedInTeam = Team.NONE;
		
		currentQuestion = null;
		currentQuestionIndex = -1;
		questionUsed = new boolean[game.getQuestions().size()];
		
		for(int i=0; i<questionUsed.length; i++)
			questionUsed[i] = false;
		
		
		buzzerListeners = new ArrayList<>();
		repaintListeners = new ArrayList<>();
		gameEventListeners = new ArrayList<>();
		teamRenameListeners = new ArrayList<>();
		redTeamScoreListeners = new ArrayList<>();
		blueTeamScoreListeners = new ArrayList<>();
		currentQuestionListeners = new ArrayList<>();
		questionUsedMassChangeListeners = new ArrayList<>();
		
		redTeamName = "Team Half-Baked Turkey Sandwiches";
		blueTeamName = "Team Big Green Moldy Bagel";
		
		redTeamScore = blueTeamScore = 0;
	}
	
	void setGame(BookItBabyGame game) {
		this.game = game;
		
		buzzedInTeam = Team.NONE;
		
		currentQuestion = null;
		currentQuestionIndex = -1;
		questionUsed = new boolean[game.getQuestions().size()];
		
		for(int i=0; i<questionUsed.length; i++)
			questionUsed[i] = false;
		
		redTeamScore = blueTeamScore = 0;
	}
	BookItBabyGame getGame() {
		return game;
	}
	public void connectToRemoteManger(Communicator communicator, boolean sendGame) {
		new RemoteGameManager(communicator, this, sendGame);
	}
	
	public int getGameHash() {
		return game.hashCode();
	}
	public GameSnapshot createGameSnapshot() {
		return new GameSnapshot(redTeamName, blueTeamName, redTeamScore, blueTeamScore, questionUsed, game.hashCode());
	}
	public boolean loadGameSnapshot(GameSnapshot snapshot, boolean force) {
		if(!force && game.hashCode() != snapshot.getGameHash())
			return false;
		
		clearCurrentQuestion();
		
		updateTeamNames(snapshot.getRedTeamName(), snapshot.getBlueTeamName());
		setRedScore(snapshot.getRedTeamScore());
		setBlueScore(snapshot.getBlueTeamScore());
		setQuestionsUsed(snapshot.getQuestionUsed());
		
		return true;
	}
		
	
	public void updateTeamNames(String redTeamName, String blueTeamName) {
		this.redTeamName = redTeamName;
		this.blueTeamName = blueTeamName;
		
		for(TeamRenameListener l : teamRenameListeners)
			l.onTeamsRenamed(redTeamName, blueTeamName);
	}
	public void updateTeamNamesSilent(String redTeamName, String blueTeamName) {
		this.redTeamName = redTeamName;
		this.blueTeamName = blueTeamName;
	}
	
	public void incrementRedScore(int amount) {
		redTeamScore += amount;
		
		for(RedTeamScoreListener l : redTeamScoreListeners)
			l.onScoreChanged(redTeamScore);
	}
	public void decrementRedScore(int amount) {
		redTeamScore -= amount;
		
		for(RedTeamScoreListener l : redTeamScoreListeners)
			l.onScoreChanged(redTeamScore);
	}
	public void setRedScore(int amount) {
		redTeamScore = amount;
		
		for(RedTeamScoreListener l : redTeamScoreListeners)
			l.onScoreChanged(redTeamScore);
	}

	public void incrementBlueScore(int amount) {
		blueTeamScore += amount;
		
		for(BlueTeamScoreListener l : blueTeamScoreListeners)
			l.onScoreChanged(blueTeamScore);
	}
	public void decrementBlueScore(int amount) {
		blueTeamScore -= amount;
		
		for(BlueTeamScoreListener l : blueTeamScoreListeners)
			l.onScoreChanged(blueTeamScore);
	}
	public void setBlueScore(int amount) {
		blueTeamScore = amount;
		
		for(BlueTeamScoreListener l : blueTeamScoreListeners)
			l.onScoreChanged(blueTeamScore);
	}	

	public void clearBuzzers() {
		buzzedInTeam = Team.NONE;
		
		for(BuzzerListener l : buzzerListeners)
			l.buzzerChanged(buzzedInTeam);
		
		sendGameEvent(GameEvent.BUZZ_IN_CLEAR);
	}
	public void buzzInRed() {
		if(buzzedInTeam == Team.RED) return;
		
		buzzedInTeam = Team.RED;
		
		for(BuzzerListener l : buzzerListeners)
			l.buzzerChanged(buzzedInTeam);
		
		sendGameEvent(GameEvent.RED_BUZZ_IN);
	}
	public void buzzInBlue() {
		if(buzzedInTeam == Team.BLUE) return;
		
		buzzedInTeam = Team.BLUE;
		
		for(BuzzerListener l : buzzerListeners)
			l.buzzerChanged(buzzedInTeam);
		
		sendGameEvent(GameEvent.BLUE_BUZZ_IN);
	}
	
	public Team getBuzzedInTeam() {
		return buzzedInTeam;
	}
	public boolean isRedBuzzedIn() {
		return buzzedInTeam == Team.RED;
	}
	public boolean isBlueBuzzedIn() {
		return buzzedInTeam == Team.BLUE;
	}
	
	public int getRedTeamScore() {
		return redTeamScore;
	}
	public int getBlueTeamScore() {
		return blueTeamScore;
	}
	
	public String getRedTeamName() {
		return redTeamName;
	}
	public String getBlueTeamName() {
		return blueTeamName;
	}
	
	public boolean[] getQuestionsUsed() {
		boolean[] clone = new boolean[questionUsed.length];
		
		for(int i=0; i<clone.length; i++)
			clone[i] = questionUsed[i];
		
		return clone;
	}
	public Question getCurrentQuestion() {
		return currentQuestion;
	}
	public int getCurrentQuestionIndex() {
		return currentQuestionIndex;
	}
	
	public void setQuestionsUsed(boolean[] questionUsed) {
		for(int i=0; i<questionUsed.length && i < this.questionUsed.length; i++)
			this.questionUsed[i] = questionUsed[i];
		
		for(QuestionUsedMassChangeListener l : questionUsedMassChangeListeners)
			l.questionUsedMassChanged(questionUsed);
	}
	public void setCurrentQuestion(int index) {
		questionUsed[index] = true;
		currentQuestionIndex = index;
		currentQuestion = game.getQuestions().get(index);
		
		for(CurrentQuestionListener l : currentQuestionListeners)
			l.currentQuestionChanged(currentQuestion);
	}
	public void setCurrentQuestion(Question question) {
		int index = game.getQuestions().indexOf(question);
		if(index < 0)
			clearCurrentQuestion();
		else
			setCurrentQuestion(index);
	}
	public void clearCurrentQuestion() {
		currentQuestion = null;
		currentQuestionIndex = -1;
		
		for(CurrentQuestionListener l : currentQuestionListeners)
			l.currentQuestionChanged(currentQuestion);
	}

	
	public File getInternalFile(String fileName) {
		return game.getInternalFile(fileName);
	}
	
	public void sendGameEvent(GameEvent gameEvent) {
		for(GameEventListener l : gameEventListeners)
			l.gameEventSent(gameEvent);
	}
	
	public void callForRepaint() {
		for(RepaintListener l : repaintListeners)
			l.handleRepaint();
	}
	
	
	public void addBuzzerListener(BuzzerListener l) {
		buzzerListeners.add(l);
	}
	public void addRepaintListener(RepaintListener l) {
		repaintListeners.add(l);
	}
	public void addGameEventListener(GameEventListener l) {
		gameEventListeners.add(l);
	}
	public void addTeamRenameListener(TeamRenameListener l) {
		teamRenameListeners.add(l);
	}
	public void addRedTeamScoreListener(RedTeamScoreListener l) {
		redTeamScoreListeners.add(l);
	}
	public void addBlueTeamScoreListener(BlueTeamScoreListener l) {
		blueTeamScoreListeners.add(l);
	}
	public void addCurrentQuestionListener(CurrentQuestionListener l) {
		currentQuestionListeners.add(l);
	}
	public void addQuestionUsedMassChangeListener(QuestionUsedMassChangeListener l) {
		questionUsedMassChangeListeners.add(l);
	}
	
	public interface RepaintListener {
		public void handleRepaint();
	}
	public interface BuzzerListener {
		public void buzzerChanged(Team team);
	}
	public interface GameEventListener {
		public void gameEventSent(GameEvent event);
	}
	public interface TeamRenameListener {
		public void onTeamsRenamed(String redTeamName, String blueTeamName);
	}
	public interface RedTeamScoreListener {
		public void onScoreChanged(int score);
	}
	public interface BlueTeamScoreListener {
		public void onScoreChanged(int score);
	}
	public interface CurrentQuestionListener {
		public void currentQuestionChanged(Question currentQuestion);
	}
	public interface QuestionUsedMassChangeListener {
		public void questionUsedMassChanged(boolean[] questionUsed);
	}
}