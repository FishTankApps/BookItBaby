package com.fishtankapps.bookitbaby.game.editor;

import java.awt.GridBagLayout;
import java.awt.event.InputEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.fishtankapps.bookitbaby.game.BookItBabyGame;
import com.fishtankapps.bookitbaby.gui.ChainGBC;
import com.fishtankapps.bookitbaby.images.ImageManager;

public class GameEditorDisplay {

	private JFrame frame;
	private GameEditor gameEditor;
	
	public GameEditorDisplay() {
		gameEditor = new GameEditor(frame);
		initJFrame();		
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
		
		fileMenu.add(createNewGameItem);
		fileMenu.add(openFileMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(saveGameItem);
		fileMenu.add(saveGameAsItem);
		fileMenu.add(exitItem);		
		
		menuBar.add(fileMenu);
		
		return menuBar;
	}
	private JPanel initEditPanel() {
		JPanel editPanel = new JPanel(new GridBagLayout());
		
		QuestionList questionSelector = new QuestionList(gameEditor);
		
		QuestionEditor questionEditor = new QuestionEditor(gameEditor);
		questionEditor.setBorder(BorderFactory.createTitledBorder("Question Editor"));
		
		JPanel otherPanel = new JPanel(new GridBagLayout());
		otherPanel.setBorder(BorderFactory.createTitledBorder("Display Preview"));
		
		editPanel.add(questionSelector, ChainGBC.getInstance(0, 0).setFill(false, true).setWidthAndHeight(1, 2));
		editPanel.add(questionEditor,   ChainGBC.getInstance(1, 0).setFill(true, false));
		editPanel.add(otherPanel,       ChainGBC.getInstance(1, 1).setFill(true, false));
		
		return editPanel;
	}

	
	public void openFile(File f) {
		try {
			BookItBabyGame game = BookItBabyGame.openFile(f);			
			gameEditor.openGame(game);		
			
			frame.setTitle("Book It, Baby! - " + f.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void onWindowClosing() {
		frame.dispose();
		System.exit(0);
	}
}