package com.fishtankapps.bookitbaby.game.messages;

import com.fishtankapps.bookitbaby.game.Team;
import com.fishtankapps.communication.Message;

public class BuzzerUpdateMessage implements Message {

	private static final long serialVersionUID = 2937079120070738271L;
	private Team team;

	public BuzzerUpdateMessage(Team team) {
		this.team = team;
	}
	
	public Team getTeam() {
		return team;
	}
}
