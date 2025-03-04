package com.fishtankapps.bookitbaby.game.editor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.fishtankapps.bookitbaby.audiencedisplay.AudienceDisplay;
import com.fishtankapps.bookitbaby.configuration.BookItBabyConfiguration;
import com.fishtankapps.bookitbaby.controldisplay.ControlDisplay;
import com.fishtankapps.bookitbaby.game.BookItBabyGame;
import com.fishtankapps.bookitbaby.game.GameEvent;
import com.fishtankapps.bookitbaby.game.GameManager;
import com.fishtankapps.bookitbaby.game.Question;
import com.fishtankapps.bookitbaby.gui.ChainGBC;
import com.fishtankapps.bookitbaby.gui.NotificationDialog;
import com.fishtankapps.bookitbaby.images.ImageManager;

public class GameEditorDisplay {
	
	private static final int PREVIEW_WIDTH = 1600;
	private static final int PREVIEW_HEIGHT = 500;
	
	private JLabel noQuestionSelectedLabel;
	private JLabel incompleteQuestionLabel;

	private Question previewedQuestion;
	
	private JPanel questionEditorPanel, previewPanel;
	private JFrame frame;
	private GameEditor gameEditor;
	
	private long lastPreviewChange = 0;
	private File openGameFile = null;
	private boolean gameEdited = false;
	
	private GameEditorDisplay() {
		gameEditor = new GameEditor(frame);
		gameEditor.addQuestionPropertiesChangeListener(this::onGameEdit);
		gameEditor.addQuestionPropertiesListChangeListener(this::onGameEdit);
		initJFrame();	
		BookItBabyConfiguration.getLastOpenedAudioPath();
	}
	
	private GameEditorDisplay(String[] args) {
		this();
		
		if(args.length > 0) {
			openFile(new File(args[0]));
		}
	}
		
	private void initJFrame() {
		frame = new JFrame();
		
		frame.setSize(800, 600);
		frame.setTitle("Book It, Baby!");
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
		
		frame.add(initEditPanel());		
		frame.setVisible(true);
	}
	
	private JMenuBar initMenuBar() {
		JMenuBar menuBar = new JMenuBar();		
		
		menuBar.add(initFileMenu());
		menuBar.add(initGameMenu());
		
		return menuBar;
	}
	private JMenu initFileMenu() {
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem openFileMenuItem = new JMenuItem("Open File...");		
		JMenuItem createNewGameItem = new JMenuItem("Create New Game");
		JMenuItem saveGameItem = new JMenuItem("Save Game");
		JMenuItem saveGameAsItem = new JMenuItem("Save Game As");
		JMenuItem exitItem = new JMenuItem("Exit");
		
		openFileMenuItem.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
		createNewGameItem.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
		saveGameItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
		saveGameAsItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		
		saveGameItem.addActionListener(e -> saveToFile(false));
		saveGameAsItem.addActionListener(e -> saveToFile(true));
		openFileMenuItem.addActionListener(this::openFile);
		createNewGameItem.addActionListener(this::createNewGame);
		
		exitItem.addActionListener(e -> onWindowClosing());
		
		fileMenu.add(createNewGameItem);
		fileMenu.add(openFileMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(saveGameItem);
		fileMenu.add(saveGameAsItem);
		fileMenu.add(exitItem);	
		
		return fileMenu;
	}
	private JMenu initGameMenu() {
		JMenu gameMenu = new JMenu("Game");
		
		JMenuItem playGame = new JMenuItem("Play Game");		
		
		playGame.addActionListener(e -> playGame());
		
		gameMenu.add(playGame);
		
		return gameMenu;
	}
	private JPanel initEditPanel() {
		JPanel editPanel = new JPanel(new GridBagLayout());
		
		QuestionList questionSelector = new QuestionList(gameEditor);
		
		questionEditorPanel = new JPanel(new GridLayout(1, 1, 0, 0));
		questionEditorPanel.setBorder(BorderFactory.createTitledBorder("Question Editor"));
		
		gameEditor.addSelectedQuestionPropertiesListener(this::updateQuestionEditor);
		
		PlayGameButton playGameButton = new PlayGameButton();
		playGameButton.addActionListeners(e -> playGame());
		
		
		incompleteQuestionLabel = new JLabel("Incomplete Question", JLabel.CENTER);
		incompleteQuestionLabel.setFont(incompleteQuestionLabel.getFont().deriveFont(Font.BOLD, 32f));
		
		noQuestionSelectedLabel = new JLabel("Select a Question to Display a Preview", JLabel.CENTER);
		noQuestionSelectedLabel.setFont(noQuestionSelectedLabel.getFont().deriveFont(Font.BOLD, 32f));
		
		JPanel bottomPanel = new JPanel(new GridBagLayout());
		bottomPanel.setBorder(BorderFactory.createTitledBorder("Display Preview"));
		
		JButton revealAnswer = new JButton("Reveal Answer");
		JButton revealPossibleAnswer = new JButton("Reveal Possible Answer");
		
		revealAnswer.addActionListener(e -> { 
			if(previewedQuestion != null)
				previewedQuestion.handleGameEvent(GameEvent.REVEAL_ANSWER);});
		
		revealPossibleAnswer.addActionListener(e -> { 
			if(previewedQuestion != null)
				previewedQuestion.handleGameEvent(GameEvent.REVEAL_OPTION);});
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		previewPanel = new JPanel(new GridLayout(1, 1, 0, 0)) {
			private static final long serialVersionUID = 1L;

			public void paint(Graphics graphics) {
				Graphics2D g = (Graphics2D) graphics;
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				AffineTransform backup = g.getTransform();
				AffineTransform t = g.getTransform();
				
				double ratio = Math.min(((double) panel.getWidth()) / PREVIEW_WIDTH, 
						((double) panel.getHeight()) / PREVIEW_HEIGHT);
				
				t.scale(ratio, ratio);
				g.setTransform(t);
				
				super.paint(g);
				
				g.setTransform(backup);
			}
		};
		
		previewPanel.add(noQuestionSelectedLabel);
		Dimension previewPanelSize = new Dimension(PREVIEW_WIDTH, PREVIEW_HEIGHT);

		previewPanel.setMaximumSize(previewPanelSize);
		previewPanel.setMinimumSize(previewPanelSize);
		previewPanel.setPreferredSize(previewPanelSize);
		
		gameEditor.addQuestionPropertiesChangeListener((e1, e2, e3) -> updatePreviewPanel(true));
		gameEditor.addSelectedQuestionPropertiesListener(e -> updatePreviewPanel(false));
		
		panel.add(previewPanel);
		
		bottomPanel.add(revealAnswer, ChainGBC.getInstance(0, 0).setFill(false, false));
		bottomPanel.add(revealPossibleAnswer, ChainGBC.getInstance(1, 0).setFill(false, false));
		bottomPanel.add(panel, ChainGBC.getInstance(0, 1).setFill(true, true).setWidthAndHeight(3, 1));
		
		
		editPanel.add(questionSelector,    ChainGBC.getInstance(0, 0).setFill(false, true).setWidthAndHeight(1, 2));
		editPanel.add(questionEditorPanel, ChainGBC.getInstance(1, 0).setFill(true, false));
		editPanel.add(playGameButton,      ChainGBC.getInstance(2, 0).setFill(false, false));
		editPanel.add(bottomPanel,         ChainGBC.getInstance(1, 1).setFill(true, false).setWidthAndHeight(2, 1));
		
		return editPanel;
	}
	
	private void updateQuestionEditor(QuestionProperties qp) {
		questionEditorPanel.removeAll();
		
		if(qp != null) {
			QuestionPropertiesEditor editor = qp.getQuestionPropertiesEditor();
			
			editor.setQuestionIndex(gameEditor.getSelectedQuestionPropertiesIndex());
			editor.passGameEditor(gameEditor);
			
			questionEditorPanel.add(editor);
		}		
		
		
		questionEditorPanel.revalidate();
		questionEditorPanel.repaint();
	}
	private void updatePreviewPanel(final boolean wait) {
		
		lastPreviewChange = System.currentTimeMillis();
		final long longCurrentTime = lastPreviewChange;
		
		new Thread(() -> {
			try {
				if(wait)
					Thread.sleep(1_000);
				
				if(longCurrentTime == lastPreviewChange || !wait) {
					previewPanel.removeAll();
					
					if(gameEditor.getSelectedQuestionProperties() == null) {
						previewPanel.add(noQuestionSelectedLabel);
					} else {
						previewedQuestion = gameEditor.getSelectedQuestionProperties().generateQuestion(false);
						
						if(previewedQuestion != null) {
							previewPanel.add(previewedQuestion.getQuestionDisplay(null));
						} else {
							previewPanel.add(incompleteQuestionLabel);
						}
					}				
					
					previewPanel.revalidate();
					previewPanel.repaint();
				}				
			} catch (Exception e) {}
		}).start();		
		
	}
	
	private void onGameEdit(Object... ignore) {
		if(gameEdited == false) {
			gameEdited = true;
			frame.setTitle(frame.getTitle() + "*");
		}
	}
	
	public void playGame() {
		BookItBabyGame game = gameEditor.createGame(false);
		GameManager manager = new GameManager(game);
		
		frame.setVisible(false);
		
		AudienceDisplay.launch(manager);
		ControlDisplay.launch(manager).addShutdownListener(() -> frame.setVisible(true));
	}
	
	public void openFile(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setDialogTitle("Open Book It, Baby! Game File");
		fileChooser.setFileFilter(new FileNameExtensionFilter("Book It Baby! Game Files", "bbgz"));
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setCurrentDirectory(new File(BookItBabyConfiguration.getLastOpenedGamePath()));
		
		int result = fileChooser.showOpenDialog(frame);
		
		if(result == JFileChooser.APPROVE_OPTION) {
			if(openGameFile == null || JOptionPane.showConfirmDialog(frame, "Openning a new game will clear everything in memory.\nDo you want to continue?", "Are You Sure?", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
				openFile(fileChooser.getSelectedFile());
				BookItBabyConfiguration.setLastOpenedGamePath(fileChooser.getSelectedFile().getParent());
			}			
		}		
	}
	public void openFile(File f) {
		try {
			NotificationDialog dialog = new NotificationDialog("Opening File", "Opening the game file:\n" + f.getName(), frame.getIconImage());
			
			BookItBabyGame game = BookItBabyGame.openFile(f);			
			gameEditor.openGame(game);		
			
			frame.setTitle("Book It, Baby! - " + f.getName());
			openGameFile = f;
			gameEdited = false;
			
			dialog.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveToFile(boolean promptForFile) {
		boolean allowDrafts = false;
		
		if(!gameEditor.isGameComplete()) {
			int option = JOptionPane.showConfirmDialog(frame, 
					"The current game setup contains question drafts.\n" + 
					"Do you want to save these questions drafts to file?", "Incomplete Questions", JOptionPane.YES_NO_CANCEL_OPTION);
		
			if(option == JOptionPane.YES_OPTION)
				allowDrafts = true;
			else if (option == JOptionPane.CANCEL_OPTION)
				return;
		}
		
		if(openGameFile == null || promptForFile) {
			JFileChooser fileChooser = new JFileChooser();
			
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setDialogTitle("Save Book It, Baby! Game File");
			fileChooser.setFileFilter(new FileNameExtensionFilter("Book It Baby! Game Files", "bbgz"));
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setCurrentDirectory(new File(BookItBabyConfiguration.getLastOpenedGamePath()));
			
			int result = fileChooser.showSaveDialog(frame);
			
			if(result == JFileChooser.APPROVE_OPTION) {
				File f = fileChooser.getSelectedFile();
				
				if(f.exists()) {
					int option = JOptionPane.showConfirmDialog(frame, 
							"The selected file already exists.\nDo you want to override it?",
							"File Exists", JOptionPane.YES_NO_CANCEL_OPTION);
				
					if(option != JOptionPane.YES_OPTION)
						return;
				}
				
				openGameFile = f;
				BookItBabyConfiguration.setLastOpenedGamePath(f.getParent());				
			} else
				return;
		}
		
		NotificationDialog dialog = new NotificationDialog("Saving File", "Saving the game file:\n" + openGameFile.getName(), frame.getIconImage());
		
		gameEditor.saveGameToFile(openGameFile, allowDrafts);
		
		gameEdited = false;
		frame.setTitle("Book It, Baby! - " + openGameFile.getName());
		
		dialog.close();
	}
	
	public void createNewGame(ActionEvent e) {
		if(JOptionPane.showConfirmDialog(frame, "Creating a new game will clear everything in memory.\nDo you want to continue?", "Are You Sure?", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
			gameEditor.clearGame();
			openGameFile = null;
			gameEdited = false;
			frame.setTitle("Book It, Baby!");
		}
	}
	
	private void onWindowClosing() {
		
		if(gameEdited) {
			int choice = JOptionPane.showConfirmDialog(frame, "The open game has unsaved changes."
					+ "\nWould you like to save them?", "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION);
			
			if(choice == JOptionPane.YES_OPTION)
				saveToFile(false);
			else if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION)
				return;
		}
		
		frame.dispose();
		//System.exit(0);
	}

	public static GameEditorDisplay launch(String[] args) {
		return new GameEditorDisplay(args);
	}
}