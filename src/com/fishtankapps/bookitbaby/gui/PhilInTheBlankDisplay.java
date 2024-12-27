package com.fishtankapps.bookitbaby.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;

import com.fishtankapps.animation.Animation;
import com.fishtankapps.animation.AnimationBuilder;
import com.fishtankapps.animation.ImagePaintable;
import com.fishtankapps.bookitbaby.images.ImageManager;
import com.fishtankapps.bookitbaby.util.Constants;

public class PhilInTheBlankDisplay extends JComponent {

	private static final long serialVersionUID = -8298021903609260010L;

	private static final Font FONT = new Font("Bahnschrift", Font.PLAIN, Constants.RUNNING_ON_PI ? 90 : 150);
	private static final BasicStroke OUTLINE_STROKE = new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	
	private static final int BLANK_HEIGHT = 10;
	private static final int PADDING = Constants.RUNNING_ON_PI ? 15 : 30;
	
	private char[] answer;
	private int letterIndex;
	
	private int maximumWidth;
	
	private Thread animationThread;
	private Animation phil;
	private long startTime;
	
	private boolean revealAnswer;
	
	
	public PhilInTheBlankDisplay(String answer, int letterIndex) {
		this.answer = answer.toCharArray();
		this.letterIndex = letterIndex;
		
		revealAnswer = false;
		maximumWidth = -1;
		startTime = 0;
	}
	
	public void revealAnswer() {
		revealAnswer = true;
		getRootPane().repaint();
	}
	
	public void shutdown() {
		if(animationThread != null)
			animationThread.interrupt();
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setFont(FONT);
		
		if(maximumWidth == -1) {
			getMaximumWidth(g);
			setUpAnimation();
		}
		
		paintBlanks(g);
		phil.paint(g, System.currentTimeMillis() - startTime, getWidth(), getHeight());	
		
		if(revealAnswer)
			paintLetters(g);
	}
	private void paintBlanks(Graphics2D g) {
		
		int totalWidth = (answer.length * maximumWidth) + ((answer.length - 1) * PADDING);
		int leftPadding = (getWidth() - totalWidth)/2;
		int x;
		
		for(int i = 0; i < answer.length; i++) {
			
			
			x = i * (maximumWidth + PADDING) + leftPadding;
			
			g.setColor(Color.BLACK);
			g.fillRoundRect(x-5, getHeight() - 20 - BLANK_HEIGHT-5, maximumWidth+10, BLANK_HEIGHT+10, 5, 5);
			
			g.setColor((revealAnswer && i == letterIndex) ? Color.GREEN : Color.WHITE);
			g.fillRect(x, getHeight() - 20 - BLANK_HEIGHT, maximumWidth, BLANK_HEIGHT);
			
			
		}
	}
	private void paintLetters(Graphics2D g) {
		FontMetrics fm = g.getFontMetrics();
		FontRenderContext frc = g.getFontRenderContext();
		TextLayout textLayout;
		Shape outline;		
		
		g.setStroke(OUTLINE_STROKE);
		
		int totalWidth = (answer.length * maximumWidth) + ((answer.length - 1) * PADDING);
		int leftPadding = (getWidth() - totalWidth)/2;
		int x;
		
		for(int i = 0; i < answer.length; i++) {
			x = i * (maximumWidth + PADDING) + leftPadding - (fm.charWidth(answer[i]) - maximumWidth)/2;
			
			textLayout = new TextLayout(answer[i] + "", FONT, frc);
			outline = textLayout.getOutline(AffineTransform.getTranslateInstance(x, getHeight() - 30 - BLANK_HEIGHT));
			g.setColor(Color.BLACK);
			g.draw(outline);
			
			g.setColor((revealAnswer && i == letterIndex) ? Color.GREEN : Color.WHITE);
			g.drawString(answer[i] + "", x, getHeight() - 30 - BLANK_HEIGHT);
		}
	}

	private void getMaximumWidth(Graphics2D g) {
		FontMetrics fm = g.getFontMetrics();
		int width;
		
		for(char c : answer) {
			width = fm.charWidth(c);
			if(width > maximumWidth)
				maximumWidth = width;
		}
	}
	
	private void setUpAnimation() {
		int totalWidth = (answer.length * maximumWidth) + ((answer.length - 1) * PADDING);
		int leftPadding = (getWidth() - totalWidth)/2;
		
		
		float scale = Constants.RUNNING_ON_PI ? 0.5f : 0.7f;
		
		int x = letterIndex * (maximumWidth + PADDING) + leftPadding - getWidth()/2 + maximumWidth/2;
		int y = getHeight()/2 - 20 - BLANK_HEIGHT - 40 - (int) (ImageManager.PHIL_FRONT_VIEW.getHeight()*scale)/2;

		
		phil = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.PHIL_FRONT_VIEW))
				.setPoints(-700, y, x, y).setAlphas(1.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_OUT)
				.setScales(scale, scale).setDurration(2000).build();
		
		startTime = System.currentTimeMillis();
		
		animationThread = new Thread(() -> {
			try {
				while(System.currentTimeMillis() - startTime < phil.getDurration()) {
					getRootPane().repaint();
					Thread.sleep(42);
				}
			} catch (Exception e) {}
		});
		
		animationThread.start();
		
		
	}
}
