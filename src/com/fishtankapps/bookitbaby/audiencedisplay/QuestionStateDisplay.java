package com.fishtankapps.bookitbaby.audiencedisplay;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import com.fishtankapps.bookitbaby.game.GameManager;
import com.fishtankapps.bookitbaby.gui.BlendComposite;
import com.fishtankapps.bookitbaby.images.ImageManager;
import com.fishtankapps.bookitbaby.util.Constants;

public class QuestionStateDisplay extends JComponent implements MouseListener {

	private static final long serialVersionUID = 6103329232074428277L;
	private static final int QUESTION_BOX_PADDING = 5;
	private static final int SIDE_PADDING = 20;
	private static final int TOP_PADDING = 10;
	private static final int QUESTION_BOX_SIZE = 50;
	
	private static final Font QUESTION_BOX_FONT = new Font("Impact", Font.PLAIN, 25);
	private static final Color USED_OVERLAY_COLOR = new Color(0, 0, 0, 100);
	private static final BasicStroke ACTIVE_OUTLINE_STROKE = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	
	private enum State {
		UNUSED, USED, ACTIVE
	}
	
	private GameManager manager;
	private Rectangle[] buttonBounds = null;
	
	public QuestionStateDisplay(GameManager manager) {
		this.manager = manager;
		manager.addCurrentQuestionListener(l -> repaint());
		manager.addQuestionUsedMassChangeListener(ignore -> repaint());
		addMouseListener(this);
	}
	
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;

		paintTrails(g);
		
		boolean[] questionUsed = manager.getQuestionsUsed();
		int currentQuestionIndex = manager.getCurrentQuestionIndex();
		int maxNumberOfBoxes = ((getWidth() - SIDE_PADDING*2) / (QUESTION_BOX_SIZE + QUESTION_BOX_PADDING));
		int[] xpadding = new int[(int) Math.ceil(((double) questionUsed.length) / maxNumberOfBoxes)];

		int i, x, y, column;
		State state;
		
		for(i = 0, column = 0; i < xpadding.length; i++) {
			column += maxNumberOfBoxes;
			
			if(column > questionUsed.length) // Last Row
				xpadding[i] = (getWidth() - SIDE_PADDING*2 - (QUESTION_BOX_PADDING + (questionUsed.length % maxNumberOfBoxes) * (QUESTION_BOX_SIZE + QUESTION_BOX_PADDING)))/2;
			else
				xpadding[i] = (getWidth() - SIDE_PADDING*2 - (QUESTION_BOX_PADDING + maxNumberOfBoxes * (QUESTION_BOX_SIZE + QUESTION_BOX_PADDING)))/2;
		}
		
		buttonBounds = new Rectangle[questionUsed.length];
		for(i = 0; i < questionUsed.length; i++) {
			column = (i / maxNumberOfBoxes);
			x = xpadding[column] + (i % maxNumberOfBoxes) * (QUESTION_BOX_SIZE + QUESTION_BOX_PADDING) + SIDE_PADDING + QUESTION_BOX_PADDING;
			y = QUESTION_BOX_PADDING + column * (QUESTION_BOX_SIZE + QUESTION_BOX_PADDING) + TOP_PADDING;
			
			buttonBounds[i] = new Rectangle(x, y, QUESTION_BOX_SIZE, QUESTION_BOX_SIZE);			
			
			if(i == currentQuestionIndex)
				state = State.ACTIVE;
			else 
				state = (questionUsed[i]) ? State.USED : State.UNUSED;
			
			paintQuestionBox(g, i + 1, state, x, y);
		}
	}
	
	private void paintQuestionBox(Graphics2D g, int number, State state, int x, int y) {
		g.setFont(QUESTION_BOX_FONT);
		
		BufferedImage background = ImageManager.QUESTION_BOX_UNLIT_BACKGROUND;
		
		if(state == State.ACTIVE)
			background = ImageManager.QUESTION_BOX_LIT_BACKGROUND;
		
		g.drawImage(background, x, y, QUESTION_BOX_SIZE, QUESTION_BOX_SIZE, null);
		
		
		String text = "#" + number;
		Rectangle2D bounds = g.getFontMetrics().getStringBounds(text, g);
		
		if(state == State.ACTIVE) {
			FontRenderContext frc = g.getFontRenderContext();
			
		    TextLayout textlayout = new TextLayout(text, QUESTION_BOX_FONT, frc);
		    Shape outline = textlayout.getOutline(null);
		    
		    g.translate(x + (int) (QUESTION_BOX_SIZE - bounds.getWidth()) / 2, y + 35);
		    g.setColor(Color.WHITE);
		    g.setStroke(ACTIVE_OUTLINE_STROKE);
		    g.draw(outline);
		    g.translate(-(x + (int) (QUESTION_BOX_SIZE - bounds.getWidth()) / 2), -(y + 35));
		}
		
		g.setColor((state == State.USED) ? Color.GRAY : Color.BLACK);
		g.drawString(text, x + (int) (QUESTION_BOX_SIZE - bounds.getWidth()) / 2, 
				           y + 35);
		
		if(state == State.USED) {
			g.setColor(USED_OVERLAY_COLOR);
			g.fillRoundRect(x, y, QUESTION_BOX_SIZE, QUESTION_BOX_SIZE, 20, 20);
		}
	}
	
	private void paintTrails(Graphics2D g) {
		BufferedImage blueTrail = ImageManager.BLUE_TEAM_BACKGROUND_TRAIL;
		BufferedImage redTrail = ImageManager.RED_TEAM_BACKGROUND_TRAIL;
		
		g.drawImage(blueTrail, getWidth() - blueTrail.getWidth(), 0, blueTrail.getWidth(), blueTrail.getHeight(), null);
		g.drawImage(redTrail,  0, 0, redTrail.getWidth(), redTrail.getHeight(), null);
		
		
		
		if(manager.isRedBuzzedIn()) {
			int leftPoint = getWidth() - blueTrail.getWidth();
			int[] x = {leftPoint, leftPoint, getWidth(), getWidth()};
			int[] y = {28,        0,         0,          98};
			Polygon polygon = new Polygon(x, y, x.length);
			GradientPaint paint = new GradientPaint(getWidth(), 0, Constants.BUZZED_OUT_SHADING, 
							getWidth() - blueTrail.getWidth(), 0, Color.WHITE);

			Composite backup = g.getComposite();
			
			g.setPaint(paint);
			g.setComposite(BlendComposite.Multiply);
			g.fill(polygon);
			
			g.setComposite(backup);
			
		} else if (manager.isBlueBuzzedIn()) {
			int[] x = {0,  0, getWidth(), getWidth()};
			int[] y = {98, 0,          0,         28};
			Polygon polygon = new Polygon(x, y, x.length);
			GradientPaint paint = new GradientPaint(0, 0, Constants.BUZZED_OUT_SHADING, 
					redTrail.getWidth(), 0, Color.WHITE);

			Composite backup = g.getComposite();
			
			g.setPaint(paint);
			g.setComposite(BlendComposite.Multiply);
			g.fill(polygon);
			
			g.setComposite(backup);
		}
	}



	public void mouseClicked(MouseEvent e) {
		if(buttonBounds != null) {
			for(int i = 0; i < buttonBounds.length; i++) {
				if(buttonBounds[i].contains(e.getPoint())) {
					manager.setCurrentQuestion(i);
				}
			}
		}
	}


	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
}
