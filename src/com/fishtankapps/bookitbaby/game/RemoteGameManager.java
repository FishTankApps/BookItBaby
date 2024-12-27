package com.fishtankapps.bookitbaby.game;

import com.fishtankapps.bookitbaby.game.messages.BlueScoreUpdateMessage;
import com.fishtankapps.bookitbaby.game.messages.BuzzerUpdateMessage;
import com.fishtankapps.bookitbaby.game.messages.CurrentQuestionUpdateMessage;
import com.fishtankapps.bookitbaby.game.messages.GameEventMessage;
import com.fishtankapps.bookitbaby.game.messages.GameMessage;
import com.fishtankapps.bookitbaby.game.messages.QuestionUsedMassChangeMessage;
import com.fishtankapps.bookitbaby.game.messages.RedScoreUpdateMessage;
import com.fishtankapps.bookitbaby.game.messages.RepaintMessage;
import com.fishtankapps.bookitbaby.game.messages.TeamNameUpdateMessage;
import com.fishtankapps.communication.Communicator;
import com.fishtankapps.communication.Message;

public class RemoteGameManager {
	
	private boolean ignoreMessage;
	
	public RemoteGameManager(Communicator communicator, GameManager manager, boolean sendGame) {
		ignoreMessage = false;
		
		communicator.addMessageListener(message -> handleMessage(message, manager));
		
		manager.addRepaintListener(() -> {if(!ignoreMessage) communicator.sendMessage(new RepaintMessage());});
		manager.addBuzzerListener((team) -> {if(!ignoreMessage) communicator.sendMessage(new BuzzerUpdateMessage(team));});
		manager.addGameEventListener((event) -> {if(!ignoreMessage) communicator.sendMessage(new GameEventMessage(event));});
		
		manager.addRedTeamScoreListener((score) -> {if(!ignoreMessage) communicator.sendMessage(new RedScoreUpdateMessage(score));});
		manager.addBlueTeamScoreListener((score) -> {if(!ignoreMessage) communicator.sendMessage(new BlueScoreUpdateMessage(score));});
		
		manager.addTeamRenameListener((red, blue) -> {if(!ignoreMessage) communicator.sendMessage(new TeamNameUpdateMessage(red, blue));});
		manager.addCurrentQuestionListener((question) -> {if(!ignoreMessage) communicator.sendMessage(new CurrentQuestionUpdateMessage(question));});
		
		manager.addQuestionUsedMassChangeListener((questionUsed) ->  {if(!ignoreMessage) communicator.sendMessage(new QuestionUsedMassChangeMessage(questionUsed));});
		
		if(sendGame)
			communicator.sendMessage(new GameMessage(manager.getGame()));
	}
	
	private void handleMessage(Message message, GameManager manager) {
		ignoreMessage = true;
		
		if(message instanceof BuzzerUpdateMessage)
			handleBuzzerUpdateMessage((BuzzerUpdateMessage) message, manager);
		else if(message instanceof GameEventMessage)
			handleGameEventMessage((GameEventMessage) message, manager);
		else if(message instanceof RedScoreUpdateMessage)
			handleRedScoreUpdateMessage((RedScoreUpdateMessage) message, manager);
		else if(message instanceof BlueScoreUpdateMessage)
			handleBlueScoreUpdateMessage((BlueScoreUpdateMessage) message, manager);
		else if(message instanceof TeamNameUpdateMessage)
			handleTeamNameUpdateMessage((TeamNameUpdateMessage) message, manager);
		else if(message instanceof CurrentQuestionUpdateMessage)
			handleCurrentQuestionUpdateMessage((CurrentQuestionUpdateMessage) message, manager);
		else if(message instanceof QuestionUsedMassChangeMessage)
			handleCurrentQuestionUpdateMessage((QuestionUsedMassChangeMessage) message, manager);
		else if(message instanceof GameMessage)
			handleGameMessage((GameMessage) message, manager);
		else if(message instanceof RepaintMessage)
			handleRepaintMessage((RepaintMessage) message, manager);
		
		ignoreMessage = false;
	}

	private void handleBuzzerUpdateMessage(BuzzerUpdateMessage message, GameManager manager) {
		Team team = ((BuzzerUpdateMessage) message).getTeam();
		
		if(team == Team.RED)
			manager.buzzInRed();
		else if(team == Team.BLUE)
			manager.buzzInBlue();
		else if(team == Team.NONE)
			manager.clearBuzzers();
	}
	private void handleGameEventMessage(GameEventMessage message, GameManager manager) {
		manager.sendGameEvent(message.getEvent());
	}
	private void handleRedScoreUpdateMessage(RedScoreUpdateMessage message, GameManager manager) {
		manager.setRedScore(message.getScore());
	}
	private void handleBlueScoreUpdateMessage(BlueScoreUpdateMessage message, GameManager manager) {
		manager.setBlueScore(message.getScore());
	}
	private void handleTeamNameUpdateMessage(TeamNameUpdateMessage message, GameManager manager) {
		manager.updateTeamNames(message.getRedTeamName(), message.getBlueTeamName());
	}
	private void handleCurrentQuestionUpdateMessage(CurrentQuestionUpdateMessage message, GameManager manager) {
		manager.setCurrentQuestion(message.getQuestion());
	}
	private void handleCurrentQuestionUpdateMessage(QuestionUsedMassChangeMessage message, GameManager manager) {
		manager.setQuestionsUsed(message.getQuestionUsed());
	}
	
	private void handleGameMessage(GameMessage message, GameManager manager) {
		manager.setGame(message.getGame());
	}
	private void handleRepaintMessage(RepaintMessage message, GameManager manager) {
		manager.callForRepaint();
	}
}