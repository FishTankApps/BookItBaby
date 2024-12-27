package com.fishtankapps.bookitbaby.core;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.fishtankapps.bookitbaby.audiencedisplay.AudienceDisplay;
import com.fishtankapps.bookitbaby.competitordisplay.CompetitorDisplay;
import com.fishtankapps.bookitbaby.controldisplay.ControlDisplay;
import com.fishtankapps.bookitbaby.game.BookItBabyGame;
import com.fishtankapps.bookitbaby.game.GameManager;
import com.fishtankapps.bookitbaby.game.Question;
import com.fishtankapps.bookitbaby.game.editor.GameEditorDisplay;
import com.fishtankapps.bookitbaby.images.ImageManager;
import com.fishtankapps.bookitbaby.questions.ChristInContextQuestion;
import com.fishtankapps.bookitbaby.questions.DrewOrFalse;
import com.fishtankapps.bookitbaby.questions.MultiplingChoice;
import com.fishtankapps.bookitbaby.questions.Patching;
import com.fishtankapps.bookitbaby.questions.PhilInTheBlank;
import com.fishtankapps.bookitbaby.questions.SongAnswer;
import com.fishtankapps.bookitbaby.sounds.SoundPlayer;
import com.fishtankapps.bookitbaby.util.Constants;
import com.fishtankapps.bookitbaby.util.FontLoader;

public class Launcher {

	public static void main(String[] args) {
		//generateAiGUnit7ReviewGame();		
		//launch(args);
		launch(new String[] {"C:\\Users\\Whitaker\\Documents\\Christian\\BookItBaby!\\AiG Unit 6 Review.bbgz"});
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
		
		File gameFile = null;
		
		if(args.length > 0) {
			gameFile = new File(args[0]);
		} else {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.setIconImage(ImageManager.BOOKIT_BABY_LOGO);
			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Book It, Baby! Game Files", "bbgz"));
			fileChooser.setAcceptAllFileFilterUsed(false);
			
			fileChooser.setCurrentDirectory(new File("C:\\Users\\Whitaker\\Documents\\Christian\\BookItBaby!"));
			
			fileChooser.setDialogTitle("Select Game File");
			int result = fileChooser.showOpenDialog(frame);
			frame.dispose();	
					
			if(result != JFileChooser.CANCEL_OPTION)			
				gameFile = fileChooser.getSelectedFile();			
		}
		
		if(gameFile == null || !gameFile.exists())
			return;
		
		BookItBabyGame game = BookItBabyGame.openFile(gameFile);
		GameManager manager = new GameManager(game);
		
//		new AudienceDisplay(manager);		
//		new ControlDisplay(manager);
		
		new GameEditorDisplay().openFile(gameFile);
	}
	private static void launchRaspberryPi() {
		try {
			FontLoader.loadFonts();
			SoundPlayer.load();
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {e.printStackTrace();}
		
		GameManager manager = new GameManager();				
		new CompetitorDisplay(manager);
	}

	
	@SuppressWarnings("unused")
	private static void generateBookItBabyGame() {
		BookItBabyGame game = new BookItBabyGame();
		
		
		DrewOrFalse drewOrFalse = new DrewOrFalse("Draw something cool!", 60_000);
		MultiplingChoice mc = new MultiplingChoice("What is 5 + 5?", "10", "five", "taco", "tim", "53.2");
		Patching patching = new Patching("What does Gelato always say?", "Never leave home without your tools!", false, true, true, false, true, false);
		PhilInTheBlank phil = new PhilInTheBlank("What is a soft taco?", "Taco", 2);
		SongAnswer song = new SongAnswer("Name this song!", "Belly Button", "BellyButton.mp3");
		
		game.getQuestions().add(drewOrFalse);
		game.getQuestions().add(patching);
		game.getQuestions().add(mc);
		game.getQuestions().add(phil);
		game.getQuestions().add(song);
		
		File[] files = {new File("C:\\Users\\Whitaker\\Music\\Songs\\Favorite Songs\\Live from Paducah\\Belly Button.mp3")};
		File output = new File(".\\game.bbgz");
		
		BookItBabyGame.createGameFile(files, game, output);
	}
	
	
	@SuppressWarnings("unused")
	private static void generateAiGUnit7ReviewGame() {
		BookItBabyGame game = new BookItBabyGame();
		
		//PhilInTheBlank   question1  = new PhilInTheBlank("The second C of History is...", "Corruption", 1);
		//MultiplingChoice question2  = new MultiplingChoice("What was Abel's job?", "Care for sheep", "Grow crops", "Fight dragons", "Work with metal", "Build houses", "Cook food", "Gather food", "Explore the land");
		//Patching         question3  = new Patching("Finish this section of Romans 5:12:", "Death spread to all men because all sinned.", true, true, true, false, true, true, true, false);
		//SongAnswer       question10 = new SongAnswer("Name this song!", "Build a Boat", "build-a-boat.wav");
		//DrewOrFalse      question20 = new DrewOrFalse("Draw a picture of Noah's Ark during the flood.", 90_000);
		//Question question20 = new ChristInContextQuestion(new Patching("What event does the Passover point to?", "Christ's death on the cross", false, true, true, true, false));
		
		Question question1  = new MultiplingChoice("Of the 12 spies Moses sent, how many had faith they could conquer the land?", "Two", "One", "Three", "Four", "Six", "Seven", "Eight", "Ten", "All Twelve", "Four Hundred", "Pizza");
		Question question2  = new PhilInTheBlank("Who led a rebellion against Moses?", "Korah", 0);
		Question question3  = new Patching("What did Balak, king of Moab, hire Balaam to do?", "Bring a curse upon the Israelites.", true, true, false, true, true, false);
		Question question4  = new MultiplingChoice("Who became the leader of the Israelites after Moses?", "Joshua", "Judah", "Jesus", "King David", "Jordan", "Spiderman", "Joseph", "Solomon", "King Herod", "Jacob", "Abraham");
		Question question5  = new PhilInTheBlank("What river did God part allowing Israel to cross?", "Jordon", 3);
		Question question6  = new Patching("What strategy did the Israelites use to conquer Jericho?", "They followed God 's instructions.", true, false, false, true, true);
		Question question7  = new PhilInTheBlank("What caused Israel to lose the battle at Ai? Their...", "Sin", 2);
		Question question8  = new MultiplingChoice("What promise to Abraham did God fulfill during Joshua's leadership?", "Ownership of Canaan", "Many Descendants", "Blessing to All Nations", "A Son", "Deliverance from Egypt", "Lots of Pizza", "Lots of Wealth");
		Question question9  = new Patching("What happened each time Israel disobeyed God and worshiped idols?", "God would allow them to be defeated.", false, true, true, true, true, true, false);
		Question question10 = new MultiplingChoice("Which of these did the spies not see in Canaan?", "Chariots", "Good Fruit", "Fortified Cities", "Strong Armies", "Giants");
		Question question11 = new Patching("When they complained, what animal did God punish the Israelite with?", "Fiery Serpents", true, false);
		Question question12 = new PhilInTheBlank("Instead of cursing Israel, Balaam ______ Israel.", "Blessed", 0);
		Question question13 = new MultiplingChoice("Who hid the spies Joshua sent to Jericho?", "Rahab", "Sarah", "Abigail", "Ruth", "Naomi", "Mary", "Esther", "Miram");
		Question question14 = new Patching("What did the Israelites set up after crossing the Jordan?", "Memorial Stones", true, false);
		Question question15 = new PhilInTheBlank("Who (and her family) was spared from destruction at Jericho?", "Rahab", 4);
		Question question16 = new Patching("What happened when Joshua prayed for more time during battle?", "The sun stood still.", true, false, true, false);
		Question question17 = new PhilInTheBlank("Because of his _____, Caleb was able to conquer a city of giants.", "Faith", 1);
		Question question18 = new MultiplingChoice("Who did God use to deliver Israel from their enemies?", "The Judges", "Moses", "Joshua and Caleb", "The Avengers", "The Kings of Israel", "The Prophets", "The Priests", "The Pharisees", "The Disciples", "The Apostles");
		Question question19 = new ChristInContextQuestion(new Patching("What event does the bronze serpent point us to?", "Christ's death on the cross.", false, true, true, true, false));
		Question question20 = new ChristInContextQuestion(new PhilInTheBlank("What does the scarlet chord remind us of? Jesus'...", "Blood", 0));
		Question question21 = new SongAnswer("Guess this song about conquering with God's power!", "Jericho - Andrew Ripp", "jericho.wav");
		Question question22 = new SongAnswer("Guess this song about traveling to somewhere better!", "Promised Land - TobyMac", "promised-land.wav");
		Question question23 = new DrewOrFalse("Draw Israel crossing the Jordan river.", 90_000);
		Question question24 = new DrewOrFalse("Draw the walls of Jericho falling down.", 90_000);
		
		
		game.getQuestions().add(question1);
		game.getQuestions().add(question2);
		game.getQuestions().add(question3);
		game.getQuestions().add(question4);
		game.getQuestions().add(question5);
		game.getQuestions().add(question6);
		game.getQuestions().add(question7);
		game.getQuestions().add(question8);
		game.getQuestions().add(question9);
		game.getQuestions().add(question10);
		game.getQuestions().add(question11);
		game.getQuestions().add(question12);
		game.getQuestions().add(question13);
		game.getQuestions().add(question14);
		game.getQuestions().add(question15);
		game.getQuestions().add(question16);
		game.getQuestions().add(question17);
		game.getQuestions().add(question18);
		game.getQuestions().add(question19);
		game.getQuestions().add(question20);
		game.getQuestions().add(question21);
		game.getQuestions().add(question22);
		game.getQuestions().add(question23);
		game.getQuestions().add(question24);
		
		game.shuffleQuestions();
		
		
		File[] files = {new File("C:\\Users\\Whitaker\\Music\\Laboritory\\jericho.wav"),
						new File("C:\\Users\\Whitaker\\Music\\Laboritory\\promised-land.wav")};
		File output = new File("C:\\Users\\Whitaker\\Documents\\Christian\\BookItBaby!\\AiG Unit 7 Review.bbgz");
		
		BookItBabyGame.createGameFile(files, game, output);
	}
}