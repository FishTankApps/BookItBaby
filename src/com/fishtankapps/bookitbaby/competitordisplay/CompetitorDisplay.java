package com.fishtankapps.bookitbaby.competitordisplay;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.fishtankapps.animation.AnimationGroup;
import com.fishtankapps.bookitbaby.game.GameEvent;
import com.fishtankapps.bookitbaby.game.GameManager;
import com.fishtankapps.bookitbaby.game.Question;
import com.fishtankapps.bookitbaby.game.Team;
import com.fishtankapps.bookitbaby.images.ImageManager;
import com.fishtankapps.bookitbaby.questions.DrewOrFalse;
import com.fishtankapps.bookitbaby.sounds.SoundPlayer;
import com.fishtankapps.bookitbaby.util.Constants;
import com.fishtankapps.communication.ConnectionStateListener;
import com.fishtankapps.communication.Server;

public class CompetitorDisplay {

	private JDialog dialog;
	
	private GameManager manager;
	private JPanel mainPanel;
	
	private NoConnectionDisplay noConnection;
	private QuestionPickerDisplay questionPicker;
	private RandomTeamPickerDisplay randomTeamPicker;
	
	private AnimationGroup currentAnimationGroup;
	private boolean validBuzzerMoment;
	
	private CompetitorDisplay(GameManager manager) {
		this.manager = manager;
		validBuzzerMoment = false;
		
		initDialog();
		initConnection();		
		
		manager.addCurrentQuestionListener(this::handleCurrentQuestionEvent);
		manager.addGameEventListener(this::handleGameEvent);
		manager.addBuzzerListener(team -> dialog.repaint());
	}
	
	private void initDialog() {
		dialog = new JDialog();
		
		dialog.setSize(800, 444);
		dialog.setLocation(0, 0);
		dialog.setUndecorated(true);	
		dialog.setAlwaysOnTop(true);
		dialog.setFocusable(true);
		
		dialog.add(initMainPanel());
		
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		for(GraphicsDevice device : devices)
			if(device.equals(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()) && Constants.RUNNING_ON_PI) {
				device.setFullScreenWindow(dialog);
				break;
			}
		
		dialog.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				handleKeyPress(e);
			}

			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}			
		});
		
		dialog.revalidate();
		dialog.repaint();
		
		dialog.setVisible(true);
		
		manager.addRepaintListener(() -> dialog.repaint());
	}
	private JPanel initMainPanel() {
		mainPanel = new JPanel(new GridLayout(1,1)) {

			private static final long serialVersionUID = 1L;

			private final Color DARK_BLUE = new Color(0,  0, 60);
			private final Color DARK_RED = new Color(60, 0, 0);
			
			public void paint(Graphics graphics) {
				Graphics2D g = (Graphics2D) graphics;
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				
				super.paint(g);
				
				if(manager.isBlueBuzzedIn()) {
					g.setPaint(new GradientPaint(0, 0, DARK_BLUE, 
							getWidth()/2, getHeight()/2, Color.BLUE, true));		
					g.fillRect(0, 0, getWidth(), getHeight());
				} else if (manager.isRedBuzzedIn()) {
					g.setPaint(new GradientPaint(0, 0, DARK_RED, 
							getWidth()/2, getHeight()/2, Color.RED, true));		
					g.fillRect(0, 0, getWidth(), getHeight());
				}
			}
			
			public void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				Graphics2D g = (Graphics2D) graphics;
				
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				
				GradientPaint backgroundPaint = new GradientPaint(0f, 0f, Color.GRAY.darker().darker(), getWidth()/2f, getHeight()/2f, Color.GRAY, true);
				g.setPaint(backgroundPaint);
				g.fillRect(0, 0, getWidth(), getHeight());
				
				BufferedImage logo = ImageManager.BOOKIT_BABY_LOGO;
				
				double scale = getHeight() / (double) logo.getHeight();
				int scaledHeight = (int) (logo.getHeight() * scale);
				int scaledWidth  = (int) (logo.getWidth() * scale);
				
				Composite c = g.getComposite();
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, .8f));			
				g.drawImage(logo, (getWidth() - scaledWidth)/2, 0, scaledWidth, scaledHeight, null);
				g.setComposite(c);
			}
			
		};
		
		noConnection = new NoConnectionDisplay();
		questionPicker = new QuestionPickerDisplay(manager);
		randomTeamPicker = new RandomTeamPickerDisplay();
		randomTeamPicker.addOnTeamPickedListener(questionPicker::setPicker);
		
		mainPanel.add(noConnection);
		
		return mainPanel;
	}
	
	private void initConnection() {
		Server server = new Server(Constants.CONNECTION_PORT);

		server.addConnectionStateListener(new ConnectionStateListener() {
			
			public void onConnectionOpenned() {
				manager.connectToRemoteManger(server, false);
				
				mainPanel.removeAll();				
				mainPanel.revalidate();
				dialog.repaint();
			}

			public void onConnectionClosed() {
				mainPanel.removeAll();
				mainPanel.add(new NoConnectionDisplay());
				mainPanel.revalidate();
				dialog.repaint();
				
				new Thread(() -> server.openConnection()).start();
			}
			
		});
		new Thread(() -> server.openConnection()).start();
		
	}
	

	private void handleCurrentQuestionEvent(Question question) {
		if(question == null) {
			mainPanel.removeAll();
			mainPanel.add(questionPicker);
			mainPanel.revalidate();
			dialog.repaint();
			
			validBuzzerMoment = false;			
		} else {
			validBuzzerMoment = false;
			
			if(currentAnimationGroup != null)
				currentAnimationGroup.stop();
			
			currentAnimationGroup = question.getTypeLogoAnimation();
			mainPanel.removeAll();
			mainPanel.add(currentAnimationGroup.createDisplayComponent());
			mainPanel.revalidate();
			dialog.repaint();
			
			currentAnimationGroup.start();
		}
	}
	private void handleGameEvent(GameEvent event) {
		if(event == GameEvent.REVEAL_QUESTION) {
			mainPanel.removeAll();
			mainPanel.add(manager.getCurrentQuestion().getQuestionDisplay(manager));
			mainPanel.revalidate();
			dialog.repaint();
			validBuzzerMoment = true;	
		} else if (event == GameEvent.SHOW_QUESTION_PICKER) {
			mainPanel.removeAll();
			mainPanel.add(questionPicker);
			mainPanel.revalidate();
			dialog.repaint();
		}  else if (event == GameEvent.SHOW_TEAM_PICKER) {
			mainPanel.removeAll();
			mainPanel.add(randomTeamPicker);
			mainPanel.revalidate();
			dialog.repaint();
		}
		
		Question question = manager.getCurrentQuestion();
		if(question != null)
			question.handleGameEvent(event);
	}
	
	private void handleKeyPress(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			dialog.dispose();
			manager.sendGameEvent(GameEvent.SHUTDOWN);
			System.exit(0);
		} 
		
		if(isBuzzValid()) {
			if (e.getKeyCode() == KeyEvent.VK_R) {
				manager.buzzInRed();
				SoundPlayer.playRandomBuzzerSound();
			} else if (e.getKeyCode() == KeyEvent.VK_B) {
				manager.buzzInBlue();
				SoundPlayer.playRandomBuzzerSound();
			}
			
			dialog.repaint();
		}	
	}
	
	private boolean isBuzzValid() {
		return validBuzzerMoment && manager.getBuzzedInTeam() == Team.NONE &&
				manager.getCurrentQuestion() != null && 
				!(manager.getCurrentQuestion() instanceof DrewOrFalse);
	}

	public static CompetitorDisplay launch(GameManager manager) {
		return new CompetitorDisplay(manager);
	}
}
