package com.fishtankapps.bookitbaby.core;

import javax.swing.UIManager;

import com.fishtankapps.bookitbaby.competitordisplay.CompetitorDisplay;
import com.fishtankapps.bookitbaby.game.GameManager;
import com.fishtankapps.bookitbaby.game.editor.GameEditorDisplay;
import com.fishtankapps.bookitbaby.sounds.SoundPlayer;
import com.fishtankapps.bookitbaby.util.Constants;
import com.fishtankapps.bookitbaby.util.FontLoader;


public class Launcher {

	public static void main(String[] args) {
		launch(args);
	}
	
	private static void launch(String[] args) {
		if(Constants.OPERATING_SYSTEM == Constants.OperatingSystem.WINDOWS)
			launchLaptop(args);
		else
			launchRaspberryPi();
	}

	private static void launchLaptop(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SoundPlayer.load();
		} catch (Exception e) {e.printStackTrace();}

		GameEditorDisplay.launch(args);
	}
	private static void launchRaspberryPi() {
		try {
			FontLoader.loadFonts();
			SoundPlayer.load();
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {e.printStackTrace();}
		
		GameManager manager = new GameManager();				
		CompetitorDisplay.launch(manager);
	}
}