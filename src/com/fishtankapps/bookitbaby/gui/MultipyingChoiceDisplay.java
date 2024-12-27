package com.fishtankapps.bookitbaby.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JComponent;

import com.fishtankapps.bookitbaby.util.BoundedString;
import com.fishtankapps.bookitbaby.util.Constants;

public class MultipyingChoiceDisplay extends JComponent {

	private static final long serialVersionUID = -3243538943494140689L;
	
	private static final int MAXIMUM_RETRIES = 1_000;
	private static final Font FONT = new Font("Bahnschrift", Font.PLAIN, Constants.RUNNING_ON_PI ? 40 : 70);
	private static final BasicStroke OUTLINE_STROKE = new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	
	
	private boolean addAnswerOnRepaint;
	private boolean revealAnswer;
	
	private String correctAnswer;
	private ArrayList<String> unusedAnswers;
	private ArrayList<BoundedString> usedAnswers;
	
	public MultipyingChoiceDisplay(String[] answers, String correctAnswer) {
		this.correctAnswer = correctAnswer;
		addAnswerOnRepaint = false;
		revealAnswer = false;
		
		usedAnswers = new ArrayList<>();
		
		unusedAnswers = new ArrayList<>();
		for(String answer : answers)
			unusedAnswers.add(answer);		
	}
	
	public void revealOption() {
		addAnswerOnRepaint = true;
		getRootPane().repaint();		
	}
	
	public void revealAnswer() {
		revealAnswer = true;
		getRootPane().repaint();		
	}
	
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		g.setFont(FONT);
		
		if(addAnswerOnRepaint) {
			addAnswer(g);
			addAnswerOnRepaint = false;
		}
		
		FontRenderContext frc = g.getFontRenderContext();
		TextLayout textLayout;
		Shape outline;
		
		g.setStroke(OUTLINE_STROKE);
		for(BoundedString b : usedAnswers) {
			textLayout = new TextLayout(b.getString(), FONT, frc);
			outline = textLayout.getOutline(AffineTransform.getTranslateInstance(b.getX(), b.getY()));
			
			g.setColor(getTextColor(b));
			g.draw(outline);
			
			g.setColor(Color.BLACK);
			g.drawString(b.getString(), b.getX(), b.getY());
		}
	}
	
	private Color getTextColor(BoundedString string) {
		if(!revealAnswer)
			return string.getColor();
		else if(string.getString().equals(correctAnswer))
			return Color.GREEN;
		else
			return Color.RED;
	}
	
	private void addAnswer(Graphics2D g) {
		if(unusedAnswers.size() == 0)
			return;
		
		String answer = unusedAnswers.remove(0);
		Rectangle2D answerBounds = g.getFontMetrics().getStringBounds(answer, g);
		Rectangle2D adjustedBounds = answerBounds;
		
		boolean valid;
		int x = (int) (Math.random() * (getWidth()  - answerBounds.getWidth()));
		int y = (int) (Math.random() * (getHeight() - answerBounds.getHeight() + answerBounds.getY()) - answerBounds.getY());
		
		for(int count = 0; count < MAXIMUM_RETRIES; count++) {
			valid = true;
			x = (int) (Math.random() * (getWidth()  - answerBounds.getWidth()));
			y = (int) (Math.random() * (getHeight() - answerBounds.getHeight() + answerBounds.getY()) - answerBounds.getY());
			adjustedBounds = new Rectangle2D.Double(answerBounds.getX() + x,
													answerBounds.getY() + y,
													answerBounds.getWidth(),
													answerBounds.getHeight());
			
			for(BoundedString b : usedAnswers)
				valid = valid && !b.getBounds().intersects(adjustedBounds);
			
			if(valid) break;
		}
		
		Color randomColor = new Color(
				(int) (Math.random() * 155) + 100,
				(int) (Math.random() * 155) + 100,
				(int) (Math.random() * 155) + 100);
		
		usedAnswers.add(new BoundedString(adjustedBounds, x, y, answer, randomColor));
	}
}
