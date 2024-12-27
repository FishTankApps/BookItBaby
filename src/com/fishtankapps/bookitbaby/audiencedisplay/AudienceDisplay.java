package com.fishtankapps.bookitbaby.audiencedisplay;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.fishtankapps.animation.AnimationGroup;
import com.fishtankapps.bookitbaby.game.GameEvent;
import com.fishtankapps.bookitbaby.game.GameManager;
import com.fishtankapps.bookitbaby.game.Question;
import com.fishtankapps.bookitbaby.gui.ChainGBC;
import com.fishtankapps.bookitbaby.images.ImageManager;
import com.fishtankapps.bookitbaby.util.Constants;

public class AudienceDisplay {
	
	private JDialog dialog;
	
	private Thread animationThread = null;
	private int animationFrame = 0;
	private boolean displayCurtain = Constants.DISPLAY_CURTAIN;
	private boolean fadeOut = false;
	
	private GameManager manager;
	private JPanel questionDisplayPanel;
	private AnimationGroup currentAnimationGroup;
	
 	public AudienceDisplay(GameManager manager) {
		this.manager = manager;
		currentAnimationGroup = null;
		
		System.setProperty("sun.java2d.opengl", "true");
		
		initDialog();
		manager.addGameEventListener(this::handleGameEvent);
		manager.addCurrentQuestionListener(this::handleCurrentQuestion);
		manager.addBuzzerListener((team) -> dialog.repaint());
	}

	private void initDialog() {
		dialog = new JDialog();
		dialog.setSize(1366, 768); // SS TV = [1360x768]
		dialog.setLocation(0, 0);
		dialog.setUndecorated(true);	
		
		dialog.add(initMainPanel());
		
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		if(devices.length == 1) {
			Rectangle r = devices[0].getDefaultConfiguration().getBounds();
			dialog.setSize(r.width, r.height);
		} else {
			for(GraphicsDevice device : devices)
				if(!device.equals(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice())) {
					device.setFullScreenWindow(dialog);
					break;
				}
		}
		
		
		dialog.setFocusable(true);
		
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
	}
	private JPanel initMainPanel() {
		JPanel mainPanel = new JPanel(new GridBagLayout()) {
			
			private static final long serialVersionUID = 2699931860572492631L;
			private static final int SPOTLIGHT_SIZE = 275;
			private static final double ANIMATION_TIME_SCALE = 0.00115;
			
			private final long START_TIME = System.currentTimeMillis();
			
			public void paint(Graphics graphics) {
				Graphics2D g = (Graphics2D) graphics;
				super.paint(g);
				
				if(displayCurtain)
					paintCurtain(g);				
				else if(fadeOut)
					paintFadeOut(g);
			}
			
			private void paintCurtain(Graphics2D g) {
				g.drawImage(ImageManager.CURTAIN_OPENNING_ANIMATION[animationFrame], 0, 0, getWidth(), getHeight(), null);
			
				double time1 = (System.currentTimeMillis() - START_TIME) * ANIMATION_TIME_SCALE;
				double time2 = (System.currentTimeMillis() - START_TIME) * ANIMATION_TIME_SCALE + .1;
				int x1, x2, y1, y2;
				
				x1 = (int) (Math.sin(time1) * (getWidth()/2 - SPOTLIGHT_SIZE/1.8) + getWidth()/2);
				x2 = (int) (Math.sin(-time2) * (getWidth()/2 - SPOTLIGHT_SIZE/1.8) + getWidth()/2);
				y1 = (int) (Math.sin(time1 * 2) * (getHeight()/2 - SPOTLIGHT_SIZE/2 - 50) + getHeight()/2);
				y2 = (int) (Math.sin(time2 * 2) * (getHeight()/2 - SPOTLIGHT_SIZE/2 - 50) + getHeight()/2);
				
				
				Rectangle2D rect = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
				Ellipse2D spotlight1 = new Ellipse2D.Double(x1 - SPOTLIGHT_SIZE/2, y1 - SPOTLIGHT_SIZE/2, SPOTLIGHT_SIZE, SPOTLIGHT_SIZE);
				Ellipse2D spotlight2 = new Ellipse2D.Double(x2 - SPOTLIGHT_SIZE/2, y2 - SPOTLIGHT_SIZE/2, SPOTLIGHT_SIZE, SPOTLIGHT_SIZE);
				
				Area area = new Area(rect);
				area.subtract(new Area(spotlight1));
				area.subtract(new Area(spotlight2));
				
				g.setColor(new Color(0, 0, 0, 127 - animationFrame));
				g.fill(area);
				
				g.setColor(new Color(150, 150, 150, 127 - animationFrame));
				g.fill(spotlight1);
				g.fill(spotlight2);
			}
			
			private void paintFadeOut(Graphics2D g) {
				int alpha = Math.min(Math.max(animationFrame * 2, 0), 255);
				
				g.setColor(new Color(0, 0, 0, alpha));
				g.fillRect(0, 0, getWidth(), getHeight());
			}
			
			public void paintComponent(Graphics graphics) {
				Graphics2D g = (Graphics2D) graphics;
				super.paintComponent(g);
				
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				
				GradientPaint backgroundPaint = new GradientPaint(0f, 0f, Color.GRAY.darker().darker(), getWidth()/2f, getHeight()/2f, Color.GRAY, true);
				g.setPaint(backgroundPaint);
				g.fillRect(0, 0, getWidth(), getHeight());
				
				BufferedImage logo = ImageManager.BOOKIT_BABY_LOGO;
				
				double scale = getHeight() / (double) logo.getHeight();
				int scaledHeight = (int) (logo.getHeight() * scale);
				int scaledWidth  = (int) (logo.getWidth() * scale);
				
				g.drawImage(logo, (getWidth() - scaledWidth)/2, 0, scaledWidth, scaledHeight, null);
			}
		};
		
		animationThread = new Thread(() -> {
			displayCurtain = Constants.DISPLAY_CURTAIN;
			
			try {
				while(true) {
					dialog.repaint();
					Thread.sleep(42);
				}				
			} catch (Exception e) {}
		});
		animationThread.start();
		
		
		RedTeamDisplay redTeamDisplay = new RedTeamDisplay(manager);
		BlueTeamDisplay blueTeamDisplay = new BlueTeamDisplay(manager);
		
		mainPanel.add(redTeamDisplay, ChainGBC.getInstance(0, 0).setFill(false).setPadding(0));		
		mainPanel.add(new QuestionStateDisplay(manager), ChainGBC.getInstance(1, 0).setFill(true, false).setPadding(0));		
		mainPanel.add(blueTeamDisplay, ChainGBC.getInstance(2, 0).setFill(false).setPadding(0));
		
		questionDisplayPanel = new JPanel(new GridLayout(1,1)) {
			private static final long serialVersionUID = 9038495469270656904L;

			public void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				Graphics2D g = (Graphics2D) graphics;
				
				g.setPaint(Constants.QUESTION_DISPLAY_GRADIENT);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		questionDisplayPanel.setBackground(Constants.TRANSPARENT);
		
		mainPanel.add(questionDisplayPanel, ChainGBC.getInstance(0, 1).setFill(true, true).setWidthAndHeight(3, 1).setPadding(0));
		
		return mainPanel;
	}
	
	private void handleCurrentQuestion(Question question) {
		if(currentAnimationGroup != null)
			currentAnimationGroup.stop();
		
		questionDisplayPanel.removeAll();

		if(question != null) {
			currentAnimationGroup = question.getTypeLogoAnimation();
			
			if(currentAnimationGroup != null) {
				questionDisplayPanel.add(currentAnimationGroup.createDisplayComponent());
				currentAnimationGroup.start();
			}
		}
		
		questionDisplayPanel.revalidate();
		dialog.repaint();
	}
	
	private void openCurtain() {
		if(animationThread != null)
			animationThread.interrupt();
		
		animationThread = new Thread(() -> {
			displayCurtain = true;
			
			try {
				for(animationFrame = 0; animationFrame < ImageManager.CURTAIN_OPENNING_ANIMATION.length; animationFrame++) {
					dialog.repaint();
					Thread.sleep(42);
				}
				
				displayCurtain = false;
			} catch (Exception e) {
				displayCurtain = false;
			}
		});
		
		animationThread.start();
	}
	private void resetCurtain() {
		if(animationThread != null)
			animationThread.interrupt();
		
		animationThread = new Thread(() -> {
			displayCurtain = true;
			
			try {
				while(true) {
					displayCurtain = true;
					animationFrame = 0;
					dialog.repaint();
					Thread.sleep(42);
				}				
			} catch (Exception e) {}
		});
		animationThread.start();
	}
	private void fadeOut() {
		if(animationThread != null)
			animationThread.interrupt();
		
		animationThread = new Thread(() -> {
			displayCurtain = false;
			fadeOut = true;
			
			try {
				for(animationFrame = 0; animationFrame < 400; animationFrame++) {
					dialog.repaint();
					Thread.sleep(42);
				}
			} catch (Exception e) {
				fadeOut = false;
				animationFrame = 0;
				dialog.repaint();
			}
		});
		
		animationThread.start();
	}
	private void clearDisplayEffects() {
		if(animationThread != null)
			animationThread.interrupt();
		
		displayCurtain = false;
		fadeOut = false;
		animationFrame = 0;
		dialog.repaint();
	}
	
	private void handleKeyPress(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			manager.sendGameEvent(GameEvent.SHUTDOWN);
			System.exit(0);
		} else if(e.getKeyCode() == KeyEvent.VK_C)
			openCurtain();
	}
	
	private void handleGameEvent(GameEvent event) {
	
		if(event == GameEvent.SHUTDOWN) {
			dialog.dispose();
			if(animationThread != null)
				animationThread.interrupt();
		} else if (event == GameEvent.OPEN_CURTAIN)
			openCurtain();
		else if (event == GameEvent.RESET_CURTAIN)
			resetCurtain();
		else if (event == GameEvent.FADE_OUT_DISPLAY)
			fadeOut();
		else if (event == GameEvent.CLEAR_DISPLAY_EFFECTS)
			clearDisplayEffects();
		else if(event == GameEvent.REVEAL_QUESTION) {
			questionDisplayPanel.removeAll();
			questionDisplayPanel.add(manager.getCurrentQuestion().getQuestionDisplay(manager));
			questionDisplayPanel.revalidate();
			dialog.repaint();
		}
		
		if(manager.getCurrentQuestion() != null)
			manager.getCurrentQuestion().handleGameEvent(event);
	}
}