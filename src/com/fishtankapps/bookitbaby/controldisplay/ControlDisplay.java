package com.fishtankapps.bookitbaby.controldisplay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

import com.fishtankapps.bookitbaby.game.GameEvent;
import com.fishtankapps.bookitbaby.game.GameManager;
import com.fishtankapps.bookitbaby.game.GameSnapshot;
import com.fishtankapps.bookitbaby.game.Team;
import com.fishtankapps.bookitbaby.gui.ChainGBC;
import com.fishtankapps.bookitbaby.images.ImageManager;
import com.fishtankapps.bookitbaby.questions.ChristInContextQuestion;
import com.fishtankapps.bookitbaby.questions.DrewOrFalse;
import com.fishtankapps.bookitbaby.questions.MultiplingChoice;
import com.fishtankapps.bookitbaby.questions.Patching;
import com.fishtankapps.bookitbaby.questions.PhilInTheBlank;
import com.fishtankapps.bookitbaby.questions.SongAnswer;
import com.fishtankapps.bookitbaby.sounds.SoundPlayer;
import com.fishtankapps.bookitbaby.util.Constants;
import com.fishtankapps.bookitbaby.util.Utilities;
import com.fishtankapps.communication.Client;

public class ControlDisplay {

	private JFrame frame;
	private GameManager manager;
	private JPanel questionButtonPanel;	
	
	private Client client;
	private JLabel connectionStatusLabel;
	private JPanel buzzedInTeamDisplay;
	private JCheckBoxMenuItem autoSaveProgress;
	
	public ControlDisplay(GameManager manager) {
		this.manager = manager;
		client = null;
		
		initJFrame();
		
		frame.setVisible(true);
		manager.addGameEventListener(this::handleGameEvent);
		
		manager.addRedTeamScoreListener(this::onGameChange);
		manager.addBlueTeamScoreListener(this::onGameChange);
		manager.addTeamRenameListener(this::onGameChange);
		manager.addCurrentQuestionListener(this::onGameChange);
	}
	
	
	private void initJFrame() {
		frame = new JFrame();
		
		frame.setSize(800, 600);
		frame.setTitle("Book It, Baby! Control Panel");
		frame.setIconImage(ImageManager.BOOKIT_BABY_LOGO);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLocationRelativeTo(null);
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		frame.addWindowListener(new WindowListener() {
			
			public void windowClosing(WindowEvent e) {
				onWindowClosing();
			}

			public void windowOpened(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			
		});
	
		frame.setJMenuBar(initMenuBar());
		frame.add(initPlayPanel());		
	}
	
	private JMenuBar initMenuBar() {
		JMenuBar menuBar = new JMenuBar();		
		
		menuBar.add(initFileMenu());
		menuBar.add(initSoundsMenu());
		menuBar.add(initVisualsMenu());
		menuBar.add(initTeamsMenu());
		
		return menuBar;
	}
	private JMenu initFileMenu() {
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem saveProgressItem = new JMenuItem("Save Game Progress");
		autoSaveProgress = new JCheckBoxMenuItem("Auto Save Progress");
		JMenuItem loadProgressSnapshot = new JMenuItem("Load Progress Snapshot");
		JMenuItem exitItem = new JMenuItem("Exit");
		
		saveProgressItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
		loadProgressSnapshot.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.CTRL_DOWN_MASK));
		exitItem.setAccelerator(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK));
		
		exitItem.addActionListener(e -> onWindowClosing());
		saveProgressItem.addActionListener(e -> saveGameProgress());
		loadProgressSnapshot.addActionListener(e -> viewPreviousSnapshots());
		
		fileMenu.add(saveProgressItem);
		fileMenu.add(loadProgressSnapshot);
		fileMenu.addSeparator();
		fileMenu.add(autoSaveProgress);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		
		return fileMenu;
	}
	private JMenu initSoundsMenu() {
		JMenu soundsMenu = new JMenu("Sounds");
		
		JMenuItem cheeringItem = new JMenuItem("Play Cheering");
		JMenuItem laughingItem = new JMenuItem("Play Laughing");
		JMenuItem disapointedItem = new JMenuItem("Play Disapointed");
		JMenuItem themeSongItem = new JMenuItem("Play Theme Song");
		JMenuItem fadeThemeSongItem = new JMenuItem("Fade Out Theme Song");
		JMenuItem stopAllSoundsItem = new JMenuItem("Stop All Sounds");
		
		cheeringItem.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.ALT_DOWN_MASK));
		laughingItem.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.ALT_DOWN_MASK));
		disapointedItem.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.ALT_DOWN_MASK));
		themeSongItem.setAccelerator(KeyStroke.getKeyStroke('T', InputEvent.ALT_DOWN_MASK));
		fadeThemeSongItem.setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.ALT_DOWN_MASK));
		stopAllSoundsItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.ALT_DOWN_MASK));
		
		cheeringItem.addActionListener(l -> SoundPlayer.playClip(SoundPlayer.CHEERING_SOUND_CLIP));
		disapointedItem.addActionListener(l -> SoundPlayer.playClip(SoundPlayer.DISAPPOINTED_SOUND_CLIP));
		laughingItem.addActionListener(l -> SoundPlayer.playClip(SoundPlayer.LAUGHTER_SOUND_CLIP));
		themeSongItem.addActionListener(l -> SoundPlayer.playThemeSong());
		fadeThemeSongItem.addActionListener(l -> SoundPlayer.fadeOutThemeSong());
		stopAllSoundsItem.addActionListener(l -> SoundPlayer.stopAllSounds());
		
		soundsMenu.add(cheeringItem);
		soundsMenu.add(laughingItem);
		soundsMenu.add(disapointedItem);
		soundsMenu.add(themeSongItem);
		soundsMenu.addSeparator();
		soundsMenu.add(fadeThemeSongItem);
		soundsMenu.add(stopAllSoundsItem);
		
		return soundsMenu;
	}
	private JMenu initVisualsMenu() {
		JMenu visualsMenu = new JMenu("Visuals");
		
		JMenuItem openCurtainItem = new JMenuItem("Open Curtain");
		JMenuItem resetCurtainItem = new JMenuItem("Reset Curtain");
		JMenuItem fadeOutDisplayItem = new JMenuItem("Fade Out Audience Display");
		JMenuItem clearDisplayItem = new JMenuItem("Clear Audience Display Effects");
		JMenuItem repaintDisplayItem = new JMenuItem("Repaint Audience Display");
		
		openCurtainItem.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
		resetCurtainItem.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK));
		fadeOutDisplayItem.setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK));
		repaintDisplayItem.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		clearDisplayItem.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));

		openCurtainItem.addActionListener(l -> manager.sendGameEvent(GameEvent.OPEN_CURTAIN));
		resetCurtainItem.addActionListener(l -> manager.sendGameEvent(GameEvent.RESET_CURTAIN));
		fadeOutDisplayItem.addActionListener(l -> manager.sendGameEvent(GameEvent.FADE_OUT_DISPLAY));
		clearDisplayItem.addActionListener(l -> manager.sendGameEvent(GameEvent.CLEAR_DISPLAY_EFFECTS));
		repaintDisplayItem.addActionListener(l -> manager.callForRepaint());
		
		visualsMenu.add(openCurtainItem);
		visualsMenu.add(resetCurtainItem);
		visualsMenu.add(fadeOutDisplayItem);
		visualsMenu.add(clearDisplayItem);
		visualsMenu.addSeparator();
		visualsMenu.add(repaintDisplayItem);
		
		return visualsMenu;
	}
	private JMenu initTeamsMenu() {
		JMenu teamsMenu = new JMenu("Teams");
		
		JMenuItem buzzInBlueItem = new JMenuItem("Buzz in Blue");
		JMenuItem buzzInRedItem = new JMenuItem("Buzz in Red");
		JMenuItem clearBuzzInItem = new JMenuItem("Clear Buzzed in Team");
		
		clearBuzzInItem.setAccelerator(KeyStroke.getKeyStroke('C', 0));
		buzzInBlueItem.setAccelerator(KeyStroke.getKeyStroke('B', 0));
		buzzInRedItem.setAccelerator(KeyStroke.getKeyStroke('R', 0));
		
		clearBuzzInItem.addActionListener(l -> { if(!isTyping()) manager.clearBuzzers(); });
		buzzInBlueItem.addActionListener(l ->  { if(!isTyping()) buzzInTeam(Team.BLUE); });
		buzzInRedItem.addActionListener(l ->   { if(!isTyping()) buzzInTeam(Team.RED); });
		
		teamsMenu.add(buzzInRedItem);
		teamsMenu.add(buzzInBlueItem);
		teamsMenu.addSeparator();
		teamsMenu.add(clearBuzzInItem);
		
		return teamsMenu;
	}
	
	private boolean isTyping() {
		return frame.getFocusOwner() instanceof JTextComponent;
	}
		
	private JPanel initPlayPanel() {
		JPanel mainPanel = new JPanel(new GridBagLayout());
		
		mainPanel.setFocusable(true);		
		mainPanel.add(initTeamControlPanel(),   ChainGBC.getInstance(0, 0).setFill(false, true));
		mainPanel.add(initSoundPanel(),         ChainGBC.getInstance(0, 1).setFill(false, false));
		mainPanel.add(initRemoteDisplayPanel(), ChainGBC.getInstance(0, 2).setFill(false, false));
		mainPanel.add(initQuestionPanel(),      ChainGBC.getInstance(1, 0).setFill(true,  true).setWidthAndHeight(1, 3));
		mainPanel.add(initVisualsPanel(),       ChainGBC.getInstance(2, 0).setFill(false, true).setWidthAndHeight(1, 3));
		
		return mainPanel;
	}
	private JPanel initTeamControlPanel() {
		JPanel teamPanel = new JPanel(new GridBagLayout());
		teamPanel.setBorder(BorderFactory.createTitledBorder("Team Controls"));
		
		JLabel redTeamNameLabel  = new JLabel("Red Team Name:",  JLabel.RIGHT);
		JLabel blueTeamNameLabel = new JLabel("Blue Team Name:", JLabel.RIGHT);
		
		JTextField redTeamNameField  = new JTextField(manager.getRedTeamName());
		JTextField blueTeamNameField = new JTextField(manager.getBlueTeamName());

		JButton updateNamesButton  = new JButton("Update Names");
		updateNamesButton.addActionListener((e) -> 
				manager.updateTeamNames(redTeamNameField.getText(), blueTeamNameField.getText()));

		JButton increaseRedScoreButton  = new JButton("+");
		JButton increaseBlueScoreButton = new JButton("+");
		JButton decreaseRedScoreButton  = new JButton("-");
		JButton decreaseBlueScoreButton = new JButton("-");
		
		increaseRedScoreButton.addActionListener(e -> manager.incrementRedScore(1));
		increaseBlueScoreButton.addActionListener(e -> manager.incrementBlueScore(1));
		decreaseRedScoreButton.addActionListener(e -> manager.decrementRedScore(1));
		decreaseBlueScoreButton.addActionListener(e -> manager.decrementBlueScore(1));
		
		JLabel redScoreLabel =  new JLabel("Red Score: 0",  JLabel.CENTER);
		JLabel blueScoreLabel = new JLabel("Blue Score: 0", JLabel.CENTER);
		
		manager.addRedTeamScoreListener(score -> redScoreLabel.setText("Red Score: " + score));
		manager.addBlueTeamScoreListener(score -> blueScoreLabel.setText("Blue Score: " + score));
		
		JButton buzzInRedButton = new JButton("Buzz in Red");
		JButton buzzInBlueButton = new JButton("Buzz in Blue");
		JButton clearBuzzInButton = new JButton("Clear Buzz In");
		
		buzzInRedButton.addActionListener(e -> buzzInTeam(Team.RED));
		buzzInBlueButton.addActionListener(e -> buzzInTeam(Team.BLUE));
		clearBuzzInButton.addActionListener(e -> manager.clearBuzzers());
		
		JPanel buzzedInTeamPanel = new JPanel(new GridBagLayout());
		buzzedInTeamPanel.setBorder(BorderFactory.createTitledBorder(null, "Buzzed in Team", TitledBorder.CENTER, TitledBorder.TOP));
		
		buzzedInTeamDisplay = new JPanel();
		buzzedInTeamDisplay.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
		buzzedInTeamPanel.add(buzzedInTeamDisplay, ChainGBC.getInstance(0, 0).setFill(true).setPadding(2));
		
		
		teamPanel.add(redTeamNameLabel, ChainGBC.getInstance(0, 0).setFill(false, false));
		teamPanel.add(redTeamNameField, ChainGBC.getInstance(1, 0).setFill(true, false).setWidthAndHeight(2, 1));
		
		teamPanel.add(blueTeamNameLabel, ChainGBC.getInstance(0, 1).setFill(false, false));
		teamPanel.add(blueTeamNameField, ChainGBC.getInstance(1, 1).setFill(true, false).setWidthAndHeight(2, 1));
		
		
		teamPanel.add(updateNamesButton, ChainGBC.getInstance(0, 2).setFill(false, false).setWidthAndHeight(4, 1));
		
		
		teamPanel.add(increaseRedScoreButton,  ChainGBC.getInstance(0, 3).setFill(true, false).setWidthAndHeight(2, 1));
		teamPanel.add(increaseBlueScoreButton, ChainGBC.getInstance(2, 3).setFill(true, false).setWidthAndHeight(2, 1));
			
		teamPanel.add(redScoreLabel,  ChainGBC.getInstance(0, 4).setFill(true, false).setWidthAndHeight(2, 1));
		teamPanel.add(blueScoreLabel, ChainGBC.getInstance(2, 4).setFill(true, false).setWidthAndHeight(2, 1));
		
		teamPanel.add(decreaseRedScoreButton,  ChainGBC.getInstance(0, 5).setFill(true, false).setWidthAndHeight(2, 1));
		teamPanel.add(decreaseBlueScoreButton, ChainGBC.getInstance(2, 5).setFill(true, false).setWidthAndHeight(2, 1));
		
		teamPanel.add(buzzInRedButton,   ChainGBC.getInstance(0, 6).setFill(true, false).setWidthAndHeight(2, 1));
		teamPanel.add(buzzInBlueButton,  ChainGBC.getInstance(2, 6).setFill(true, false).setWidthAndHeight(2, 1));
		teamPanel.add(clearBuzzInButton, ChainGBC.getInstance(0, 7).setFill(true, false).setWidthAndHeight(4, 1));
		
		teamPanel.add(Box.createHorizontalStrut(250), ChainGBC.getInstance(0, 8).setFill(false, false).setWidthAndHeight(4, 1));
		teamPanel.add(buzzedInTeamPanel,              ChainGBC.getInstance(0, 9).setFill(false, true).setWidthAndHeight(4, 1));
		
		
		return teamPanel;
	}
	private JPanel initRemoteDisplayPanel() {
		JPanel remoteDisplayPanel = new JPanel(new GridBagLayout());
		remoteDisplayPanel.setBorder(BorderFactory.createTitledBorder("Remote Display"));
		
		JLabel ipLabel = new JLabel("IP Address:", JLabel.RIGHT);
		JTextField ipAddress = new JTextField("192.168.8.120");
		
		connectionStatusLabel = new JLabel("Statis: Disconnected", JLabel.CENTER);
		
		JButton connectButton = new JButton("Connect");
		JButton disconnectButton = new JButton("Disconnect");
		
		connectButton.addActionListener(l -> connectToRemote(ipAddress.getText()));
		disconnectButton.addActionListener(l -> disconnectFromRemote());

		remoteDisplayPanel.add(ipLabel,   ChainGBC.getInstance(0, 0).setFill(false));
		remoteDisplayPanel.add(ipAddress, ChainGBC.getInstance(1, 0).setFill(true, false).setWidthAndHeight(4, 1));
		
		remoteDisplayPanel.add(connectButton,    ChainGBC.getInstance(0, 1).setFill(false).setWidthAndHeight(2, 1));
		remoteDisplayPanel.add(Box.createHorizontalGlue(), ChainGBC.getInstance(2, 1).setFill(true, false));
		remoteDisplayPanel.add(disconnectButton, ChainGBC.getInstance(3, 1).setFill(false).setWidthAndHeight(2, 1));
		
		remoteDisplayPanel.add(connectionStatusLabel, ChainGBC.getInstance(0, 2).setFill(true, false).setWidthAndHeight(5, 1));
		
		return remoteDisplayPanel;
	}
	private JPanel initQuestionPanel() {
		JPanel questionPanel = new JPanel(new GridBagLayout());
		questionPanel.setBorder(BorderFactory.createTitledBorder("Questions"));
		
		questionButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		questionButtonPanel.setBorder(BorderFactory.createTitledBorder("Question Selection"));
		
		updateQuestionButtons();
		manager.addCurrentQuestionListener(currentQuestion -> updateQuestionButtons());
		manager.addQuestionUsedMassChangeListener(ignore -> updateQuestionButtons());
		
		questionPanel.add(initCurrentQuestionPanel(), ChainGBC.getInstance(0, 0).setFill(true, false));
		questionPanel.add(questionButtonPanel, ChainGBC.getInstance(0, 1).setFill(true, true));
		questionPanel.add(Box.createVerticalGlue(), ChainGBC.getInstance(0, 2).setFill(true, false));
		
		return questionPanel;
	}
	private JPanel initCurrentQuestionPanel() {
		JPanel currentQuestionPanel = new JPanel(new GridBagLayout());
		currentQuestionPanel.setBorder(BorderFactory.createTitledBorder("Active Question"));
		
		JLabel questionPromptLabel = new JLabel("Question Prompt: (null)", JLabel.LEFT);
		JLabel questionAnswerLabel = new JLabel("Question Answer: (null)", JLabel.LEFT);
		JLabel questionTypeLabel = new JLabel("Question Type: (null)", JLabel.LEFT);
		JLabel questionOtherInfoLabel = new JLabel("Other Info: (null)", JLabel.LEFT);
		
		JButton revealQuestionButton = new JButton("Reveal Question");
		JButton revealAnswerButton = new JButton("Reveal Answer");
		JButton clearQuestionButton = new JButton("Clear Question");
		JButton playSongButton = new JButton("Play Song");
		JButton pauseSongButton = new JButton("Pause Song");
		JButton revealNextOptionButton = new JButton("Reveal Next Option");
		
		JButton correctButton = new JButton("Correct");
		JButton incorrectButton = new JButton("Incorrect");
		
		correctButton.setForeground(Color.GREEN.darker());
		incorrectButton.setForeground(Color.RED.darker());
		
		JButton startTimerButton = new JButton("Start Timer");
		JButton pauseTimerButton = new JButton("Pause Timer");
		
		revealQuestionButton.setVisible(false);
		revealAnswerButton.setVisible(false);
		clearQuestionButton.setVisible(false);
		playSongButton.setVisible(false);
		pauseSongButton.setVisible(false);
		revealNextOptionButton.setVisible(false);
		startTimerButton.setVisible(false);
		pauseTimerButton.setVisible(false);
		correctButton.setVisible(false);
		incorrectButton.setVisible(false);
		
		clearQuestionButton.addActionListener(l -> manager.clearCurrentQuestion());
		
		revealQuestionButton.addActionListener(  l -> manager.sendGameEvent(GameEvent.REVEAL_QUESTION));
		revealAnswerButton.addActionListener(    l -> manager.sendGameEvent(GameEvent.REVEAL_ANSWER));
		playSongButton.addActionListener(        l -> manager.sendGameEvent(GameEvent.PLAY_SONG));
		pauseSongButton.addActionListener(       l -> manager.sendGameEvent(GameEvent.PAUSE_SONG));
		startTimerButton.addActionListener(      l -> manager.sendGameEvent(GameEvent.START_TIMER));
		pauseTimerButton.addActionListener(      l -> manager.sendGameEvent(GameEvent.PAUSE_TIMER));
		revealNextOptionButton.addActionListener(l -> manager.sendGameEvent(GameEvent.REVEAL_OPTION));
		
		correctButton.addActionListener(l   -> handleAnswerValidation(true));
		incorrectButton.addActionListener(l -> handleAnswerValidation(false));
		
		manager.addCurrentQuestionListener(question -> {
			revealAnswerButton.setVisible(false);
			clearQuestionButton.setVisible(false);
			playSongButton.setVisible(false);
			pauseSongButton.setVisible(false);
			revealNextOptionButton.setVisible(false);
			startTimerButton.setVisible(false);
			pauseTimerButton.setVisible(false);
			revealQuestionButton.setVisible(false);
			correctButton.setVisible(false);
			incorrectButton.setVisible(false);
			
			questionPromptLabel.setText("Question Prompt: (null)");
			questionAnswerLabel.setText("Question Answer: (null)");
			questionTypeLabel.setText("Question Type: (null)");
			questionOtherInfoLabel.setText("Other Info: (null)");
			
			if(question == null) return;
			
			
			if(question instanceof ChristInContextQuestion)
				question = ((ChristInContextQuestion) question).getQuestion();
			
			if (question instanceof DrewOrFalse) {
				DrewOrFalse dof = (DrewOrFalse) question;
				revealQuestionButton.setVisible(true);
				clearQuestionButton.setVisible(true);
				startTimerButton.setVisible(true);
				pauseTimerButton.setVisible(true);
				
				questionPromptLabel.setText("Question Prompt: " + dof.getPrompt());
				questionTypeLabel.setText("Question Type: Drew or False");
				questionOtherInfoLabel.setText("Other Info: Timer Length=" + Utilities.millisToString(dof.getTimerLength()));
			} else if (question instanceof MultiplingChoice) {
				MultiplingChoice mc = (MultiplingChoice) question;
				revealQuestionButton.setVisible(true);
				revealAnswerButton.setVisible(true);
				clearQuestionButton.setVisible(true);
				revealNextOptionButton.setVisible(true);
				correctButton.setVisible(true);
				incorrectButton.setVisible(true);
				
				questionPromptLabel.setText("Question Prompt: " + mc.getPrompt());
				questionAnswerLabel.setText("Question Answer: " + mc.getCorrectAnswer());
				questionTypeLabel.setText("Question Type: Multipling Choice");
				questionOtherInfoLabel.setText("Other Info: Answers=" + Arrays.toString(mc.getAnswers()));
			} else if (question instanceof Patching) {
				Patching p = (Patching) question;
				revealQuestionButton.setVisible(true);
				revealAnswerButton.setVisible(true);
				clearQuestionButton.setVisible(true);
				correctButton.setVisible(true);
				incorrectButton.setVisible(true);
				
				questionPromptLabel.setText("Question Prompt: " + p.getPrompt());
				questionAnswerLabel.setText("Question Answer: " + p.getAnswer());
				questionTypeLabel.setText("Question Type: Patching");
				questionOtherInfoLabel.setText("Other Info: Hidden Answer=\"" + p.getHiddenAnswer() + "\"");
			} else if (question instanceof PhilInTheBlank) {
				PhilInTheBlank pitb = (PhilInTheBlank) question;
				revealQuestionButton.setVisible(true);
				revealAnswerButton.setVisible(true);
				clearQuestionButton.setVisible(true);
				correctButton.setVisible(true);
				incorrectButton.setVisible(true);
				
				questionPromptLabel.setText("Question Prompt: " + pitb.getPrompt());
				questionAnswerLabel.setText("Question Answer: " + pitb.getAnswer());
				questionTypeLabel.setText("Question Type: Phil in the Blank");
				questionOtherInfoLabel.setText("Other Info: Letter Index=" + pitb.getLetterIndex() + " ('" + pitb.getAnswer().charAt(pitb.getLetterIndex()) + "')");
			} else if (question instanceof SongAnswer) {
				SongAnswer sa = (SongAnswer) question;
				revealQuestionButton.setVisible(true);
				revealAnswerButton.setVisible(true);
				clearQuestionButton.setVisible(true);
				playSongButton.setVisible(true);
				pauseSongButton.setVisible(true);
				correctButton.setVisible(true);
				incorrectButton.setVisible(true);
				
				questionPromptLabel.setText("Question Prompt: " + sa.getPrompt());
				questionAnswerLabel.setText("Question Answer: " + sa.getSongName());
				questionTypeLabel.setText("Question Type: Song Answer");
				questionOtherInfoLabel.setText("Other Info: Song File=\"" + sa.getSongName() + "\"");
			}
		});
		
		
		
		currentQuestionPanel.add(questionPromptLabel,    ChainGBC.getInstance(0, 0).setFill(false, false).setWidthAndHeight(100, 1));
		currentQuestionPanel.add(questionAnswerLabel,    ChainGBC.getInstance(0, 1).setFill(false, false).setWidthAndHeight(100, 1));
		currentQuestionPanel.add(questionTypeLabel,      ChainGBC.getInstance(0, 2).setFill(false, false).setWidthAndHeight(100, 1));
		currentQuestionPanel.add(questionOtherInfoLabel, ChainGBC.getInstance(0, 3).setFill(false, false).setWidthAndHeight(100, 1));
		
		currentQuestionPanel.add(revealQuestionButton,  ChainGBC.getInstance(0, 4).setFill(false, false));
		currentQuestionPanel.add(clearQuestionButton, ChainGBC.getInstance(1, 4).setFill(false, false));
		currentQuestionPanel.add(revealAnswerButton,  ChainGBC.getInstance(2, 4).setFill(false, false));
		
		currentQuestionPanel.add(revealNextOptionButton, ChainGBC.getInstance(3, 4).setFill(false, false));
		
		currentQuestionPanel.add(playSongButton, ChainGBC.getInstance(3, 4).setFill(false, false));
		currentQuestionPanel.add(pauseSongButton, ChainGBC.getInstance(4, 4).setFill(false, false));
		
		currentQuestionPanel.add(startTimerButton, ChainGBC.getInstance(3, 4).setFill(false, false));
		currentQuestionPanel.add(pauseTimerButton, ChainGBC.getInstance(4, 4).setFill(false, false));		
		
		currentQuestionPanel.add(Box.createHorizontalGlue(), ChainGBC.getInstance(5, 4).setFill(true, false));
		
		currentQuestionPanel.add(correctButton, ChainGBC.getInstance(6, 4).setFill(false, false));
		currentQuestionPanel.add(incorrectButton, ChainGBC.getInstance(7, 4).setFill(false, false));
		
		
		return currentQuestionPanel;
	}
	private JPanel initSoundPanel() {
		JPanel soundPanel = new JPanel(new GridBagLayout());
		soundPanel.setBorder(BorderFactory.createTitledBorder("Sounds"));
		
		JPanel soundEffectPanel = new JPanel(new GridLayout(2, 2));
		soundEffectPanel.setBorder(BorderFactory.createTitledBorder("Sound Effects"));
		
		JButton cheeringButton = new JButton("Cheering");
		JButton disappointedButton = new JButton("Disappointed");
		JButton laughterButton = new JButton("Laughter");
		
		cheeringButton.addActionListener(l -> SoundPlayer.playClip(SoundPlayer.CHEERING_SOUND_CLIP));
		disappointedButton.addActionListener(l -> SoundPlayer.playClip(SoundPlayer.DISAPPOINTED_SOUND_CLIP));
		laughterButton.addActionListener(l -> SoundPlayer.playClip(SoundPlayer.LAUGHTER_SOUND_CLIP));
				
		soundEffectPanel.add(cheeringButton);
		soundEffectPanel.add(disappointedButton);
		soundEffectPanel.add(laughterButton);
		
		
		
		JPanel themeSongPanel = new JPanel(new GridLayout(1, 2));
		themeSongPanel.setBorder(BorderFactory.createTitledBorder("Theme Song"));
		
		JButton startThemeSongButton = new JButton("Start");
		JButton stopThemeSongButton = new JButton("Fade Out");
		
		startThemeSongButton.addActionListener(l -> SoundPlayer.playThemeSong());
		stopThemeSongButton.addActionListener(l -> SoundPlayer.fadeOutThemeSong());
		
		themeSongPanel.add(startThemeSongButton);
		themeSongPanel.add(stopThemeSongButton);
		
		
		JButton stopAllSounds = new JButton("Stop All Sounds");
		stopAllSounds.addActionListener(l -> SoundPlayer.stopAllSounds());
		
		soundPanel.add(soundEffectPanel, ChainGBC.getInstance(0, 0).setFill(true, false));
		soundPanel.add(themeSongPanel,   ChainGBC.getInstance(0, 1).setFill(true, false));
		soundPanel.add(stopAllSounds,    ChainGBC.getInstance(0, 2).setFill(true, false));
		
		
		return soundPanel;
	}
	private JPanel initVisualsPanel() {
		JPanel visualsPanel = new JPanel(new GridBagLayout());
		visualsPanel.setBorder(BorderFactory.createTitledBorder("Audience Visuals"));
		
		JButton openCurtainButton = new JButton("Open Curtain");
		openCurtainButton.addActionListener(l -> manager.sendGameEvent(GameEvent.OPEN_CURTAIN));
		
		JButton resetCurtainButton = new JButton("Reset Curtain");
		resetCurtainButton.addActionListener(l -> manager.sendGameEvent(GameEvent.RESET_CURTAIN));
		
		JButton fadeOutButton = new JButton("Fade Out Display");
		fadeOutButton.addActionListener(l -> manager.sendGameEvent(GameEvent.FADE_OUT_DISPLAY));
		
		JButton clearDisplayStateButton = new JButton("Clear Display Effects");
		clearDisplayStateButton.addActionListener(l -> manager.sendGameEvent(GameEvent.CLEAR_DISPLAY_EFFECTS));

		JButton repaintButton = new JButton("Repaint");
		repaintButton.addActionListener(l -> manager.callForRepaint());
		
		JButton showTeamPickerButton = new JButton("Pick 1st Team");
		showTeamPickerButton.addActionListener(l -> manager.sendGameEvent(GameEvent.SHOW_TEAM_PICKER));
		
		JButton showQuestionPickerButton = new JButton("Pick Question");
		showQuestionPickerButton.addActionListener(l -> manager.sendGameEvent(GameEvent.SHOW_QUESTION_PICKER));
		
		visualsPanel.add(openCurtainButton,        ChainGBC.getInstance(0, 0).setFill(false).setPadding(3, 3, 3, 3));
		visualsPanel.add(resetCurtainButton,       ChainGBC.getInstance(0, 1).setFill(false).setPadding(3, 3, 3, 3));
		visualsPanel.add(fadeOutButton,            ChainGBC.getInstance(0, 2).setFill(false).setPadding(3, 3, 3, 3));
		visualsPanel.add(clearDisplayStateButton,  ChainGBC.getInstance(0, 3).setFill(false).setPadding(3, 3, 3, 3));
		visualsPanel.add(repaintButton,            ChainGBC.getInstance(0, 4).setFill(false).setPadding(3, 3, 20, 20));
		visualsPanel.add(showTeamPickerButton,     ChainGBC.getInstance(0, 5).setFill(false).setPadding(3, 3, 3, 3));
		visualsPanel.add(showQuestionPickerButton, ChainGBC.getInstance(0, 6).setFill(false).setPadding(3, 3, 3, 3));		
		visualsPanel.add(Box.createVerticalGlue(), ChainGBC.getInstance(0, 7).setFill(true));
		
		return visualsPanel;
	}
	
	private void updateQuestionButtons() {
		questionButtonPanel.removeAll();
		
		boolean[] questionUsed = manager.getQuestionsUsed();
		Font buttonFont = new Font("Impact", Font.PLAIN, 20);
		
		for(int index = 0; index < questionUsed.length; index++) {
			int questionIndex = index;
			JButton questionButton = new JButton("#" + (index + 1));
			questionButton.setFont(buttonFont);
			questionButton.setPreferredSize(new Dimension(70, 70));
			
			questionButton.setForeground(questionUsed[index] ? Color.GRAY : Color.BLACK);
		
			questionButton.addActionListener(l -> manager.setCurrentQuestion(questionIndex));
			questionButtonPanel.add(questionButton);
		}
		
		questionButtonPanel.revalidate();
		questionButtonPanel.repaint();
	}
		
	
	private void buzzInTeam(Team team) {
		if(!isBuzzValid())
			return;

		if(team == Team.RED)
			manager.buzzInRed();
		else 
			manager.buzzInBlue();
		
		SoundPlayer.playRandomBuzzerSound();
	
	}
	private boolean isBuzzValid() {
		return 	manager.getBuzzedInTeam() == Team.NONE &&
				manager.getCurrentQuestion() != null && 
				!(manager.getCurrentQuestion() instanceof DrewOrFalse);
	}
	
	
	private void connectToRemote(String ip) {
		
		new Thread(() -> {
			if(client != null) {
				client.closeConnection();
			}
			
			client = new Client(ip, Constants.CONNECTION_PORT);
			boolean result = client.openConnection();
			
			connectionStatusLabel.setText(result ? "Connected" : "Disconnected: Failed to Connect");
			
			if(!result)
				client = null;
			else {
				manager.connectToRemoteManger(client, true);
				//manager.clearCurrentQuestion();
			}	
		}).start();		
		
	}
	private void disconnectFromRemote() {
		if(client != null) {
			boolean result = client.closeConnection();
			connectionStatusLabel.setText(result ? "Disconnected" : "Connected: Failed to Disconnect");
			
			if(result)
				client = null;
		}		
	}
	
	
	private void handleGameEvent(GameEvent event) {
		
		if(event == GameEvent.SHUTDOWN)
			frame.dispose();
		else if (event == GameEvent.BUZZ_IN_CLEAR)
			buzzedInTeamDisplay.setBackground(null);
		else if (event == GameEvent.RED_BUZZ_IN)
			buzzedInTeamDisplay.setBackground(Color.RED);
		else if (event == GameEvent.BLUE_BUZZ_IN)
			buzzedInTeamDisplay.setBackground(Color.BLUE);
	}
	private void handleAnswerValidation(boolean correct) {
		if(manager.getBuzzedInTeam() == Team.NONE)
			return;
		
		if(correct) {
			SoundPlayer.playClip(SoundPlayer.CHEERING_SOUND_CLIP);
			
			if(manager.isRedBuzzedIn())
				manager.incrementRedScore((manager.getCurrentQuestion() instanceof ChristInContextQuestion) ? 2 : 1);
			else if(manager.isBlueBuzzedIn())
				manager.incrementBlueScore((manager.getCurrentQuestion() instanceof ChristInContextQuestion) ? 2 : 1);
			
			manager.clearBuzzers();
			manager.sendGameEvent(GameEvent.REVEAL_ANSWER);
			manager.sendGameEvent(GameEvent.PAUSE_SONG);
		} else {
			SoundPlayer.playClip(SoundPlayer.DISAPPOINTED_SOUND_CLIP);
			manager.clearBuzzers();
		}
	}
	
	
	private void onWindowClosing() {
		int option = JOptionPane.showConfirmDialog(frame, "Are you sure that you would like to exit?", "Are You Sure?", JOptionPane.YES_NO_CANCEL_OPTION);
		
		if(option == JOptionPane.YES_OPTION) {
			frame.dispose();
			manager.sendGameEvent(GameEvent.SHUTDOWN);
			System.exit(0);
		}
	}

	
	private void viewPreviousSnapshots() {
		File[] snapshots = GameSnapshot.checkForPreviousSnapshots(manager);
		GameSnapshotChooser chooser = new GameSnapshotChooser(snapshots, manager);
		
		chooser.addSnapshotLoadRequestListener(snapshot -> {
			manager.loadGameSnapshot(snapshot, true);
		});
		
		chooser.showDialog();
	}
	private void onGameChange(Object... ignore) {
		if(autoSaveProgress.isSelected())
			saveGameProgress();
	}
	private void saveGameProgress() {
		GameSnapshot snapshot = manager.createGameSnapshot();
		GameSnapshot.saveSnapshot(snapshot);
	}
}	