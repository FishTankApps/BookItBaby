package com.fishtankapps.bookitbaby.game;

import java.io.Serializable;

public enum GameEvent implements Serializable {
	PLAY_SONG, PAUSE_SONG, START_TIMER, PAUSE_TIMER, REVEAL_QUESTION, REVEAL_ANSWER, REVEAL_OPTION, SHUTDOWN, OPEN_CURTAIN,
	FADE_OUT_DISPLAY, RESET_CURTAIN, CLEAR_DISPLAY_EFFECTS, RED_BUZZ_IN, BLUE_BUZZ_IN, BUZZ_IN_CLEAR, SHOW_TEAM_PICKER, SHOW_QUESTION_PICKER;
}
