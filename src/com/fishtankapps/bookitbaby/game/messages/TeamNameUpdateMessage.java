package com.fishtankapps.bookitbaby.game.messages;

import com.fishtankapps.communication.Message;

public class TeamNameUpdateMessage implements Message {

	private static final long serialVersionUID = -5599697786094245864L;

	private String redTeamName, blueTeamName;

	public TeamNameUpdateMessage(String redTeamName, String blueTeamName) {
		super();
		this.redTeamName = redTeamName;
		this.blueTeamName = blueTeamName;
	}

	public String getRedTeamName() {
		return redTeamName;
	}

	public String getBlueTeamName() {
		return blueTeamName;
	}
}
