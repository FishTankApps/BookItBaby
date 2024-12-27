package com.fishtankapps.bookitbaby.competitordisplay;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import com.fishtankapps.bookitbaby.game.GameManager;
import com.fishtankapps.bookitbaby.game.Team;
import com.fishtankapps.bookitbaby.images.ImageManager;
import com.fishtankapps.bookitbaby.sounds.SoundPlayer;
import com.fishtankapps.bookitbaby.util.Constants;

public class QuestionPickerDisplay extends JComponent implements MouseListener {


	private static final long serialVersionUID = -1576506362132399162L;
	
	private static final int QUESTION_BOX_PADDING = 5;
	private static final int SIDE_PADDING = 20;
	private static final int TOP_PADDING = 10;
	private static final int QUESTION_BOX_SIZE = 90;
	
	private static final Font QUESTION_BOX_FONT = new Font("Impact", Font.PLAIN, 45);
	private static final Color USED_OVERLAY_COLOR = new Color(0, 0, 0, 100);
	private static final BasicStroke ACTIVE_OUTLINE_STROKE = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	
	private static final Font WHOSE_TURN_FONT = new Font("Bahnschrift", Font.PLAIN, 80);
	private static final BasicStroke WHOSE_TURN_STROKE = new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	
	private enum State {
		UNUSED, USED, ACTIVE
	}
	
	private Team picker;
	
	private GameManager manager;
	private Rectangle[] buttonBounds = null;
	
	public QuestionPickerDisplay(GameManager manager) {
		this.manager = manager;
		addMouseListener(this);
		
		this.picker = Team.NONE;
	}
	
	public void setPicker(Team team) {
		picker = team;
	}
	
	
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		
		boolean[] questionUsed = manager.getQuestionsUsed();
		int currentQuestionIndex = manager.getCurrentQuestionIndex();
		int maxNumberOfBoxes = ((getWidth() - SIDE_PADDING*2) / (QUESTION_BOX_SIZE + QUESTION_BOX_PADDING));
		int[] xpadding = new int[(int) Math.ceil(((double) questionUsed.length) / maxNumberOfBoxes)];

		int i, x, y, column;
		boolean hasUnusedQuestion = false;
		State state;
		
		for(i = 0, column = 0; i < xpadding.length; i++) {
			column += maxNumberOfBoxes;
			
			if(column > questionUsed.length) // Last Row
				xpadding[i] = (getWidth() - SIDE_PADDING*2 - (QUESTION_BOX_PADDING + (questionUsed.length % maxNumberOfBoxes) * (QUESTION_BOX_SIZE + QUESTION_BOX_PADDING)))/2;
			else
				xpadding[i] = (getWidth() - SIDE_PADDING*2 - (QUESTION_BOX_PADDING + maxNumberOfBoxes * (QUESTION_BOX_SIZE + QUESTION_BOX_PADDING)))/2;
		}
		
		for(boolean b : questionUsed)
			hasUnusedQuestion = hasUnusedQuestion || !b;
		
		if(!hasUnusedQuestion)
			return;
		
		buttonBounds = new Rectangle[questionUsed.length];
		for(i = 0; i < questionUsed.length; i++) {		
			
			column = (i / maxNumberOfBoxes);
			x = xpadding[column] + (i % maxNumberOfBoxes) * (QUESTION_BOX_SIZE + QUESTION_BOX_PADDING) + SIDE_PADDING + QUESTION_BOX_PADDING;
			y = QUESTION_BOX_PADDING + column * (QUESTION_BOX_SIZE + QUESTION_BOX_PADDING) + TOP_PADDING;
			
			if(questionUsed[i])
				buttonBounds[i] = null;
			else
				buttonBounds[i] = new Rectangle(x, y, QUESTION_BOX_SIZE, QUESTION_BOX_SIZE);			
			
			if(i == currentQuestionIndex)
				state = State.ACTIVE;
			else 
				state = (questionUsed[i]) ? State.USED : State.UNUSED;
			
			paintQuestionBox(g, i + 1, state, x, y);
		}
		
		
		if(Constants.DISPLAY_WHOSE_TURN && picker != Team.NONE && hasUnusedQuestion) {
			g.setColor(picker == Team.RED ? Color.RED : Color.BLUE);
			g.setFont(WHOSE_TURN_FONT);
			g.setStroke(WHOSE_TURN_STROKE);
			
			String text = (picker == Team.RED) ? "Red's Turn" : "Blue's Turn";
			
			x = (getWidth() - g.getFontMetrics().stringWidth(text))/2;
			y = getHeight() - 50;
						
			TextLayout textLayout = new TextLayout(text, WHOSE_TURN_FONT, g.getFontRenderContext());
			Shape outline = textLayout.getOutline(AffineTransform.getTranslateInstance(x, y));
			
			g.draw(outline);
			
			g.setColor(Color.BLACK);
			g.drawString(text, x, y);
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
		    Shape outline = textlayout.getOutline(AffineTransform.getTranslateInstance(x + (int) (QUESTION_BOX_SIZE - bounds.getWidth()) / 2, y + 62));

		    g.setColor(Color.WHITE);
		    g.setStroke(ACTIVE_OUTLINE_STROKE);
		    g.draw(outline);
		}
		
		g.setColor((state == State.USED) ? Color.GRAY : Color.BLACK);
		g.drawString(text, x + (int) (QUESTION_BOX_SIZE - bounds.getWidth()) / 2, 
				           y + 62);
		
		if(state == State.USED) {
			g.setColor(USED_OVERLAY_COLOR);
			g.fillRoundRect(x, y, QUESTION_BOX_SIZE, QUESTION_BOX_SIZE, 20, 20);
		}
	}

	
	

	public void mouseClicked(MouseEvent e) {
		
	}


	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {
		Point p = e.getPoint();
		if(Constants.RUNNING_ON_PI)
			p = new Point(getWidth() - e.getX(), getHeight() - e.getY());
		
		if(buttonBounds != null) {
			for(int i = 0; i < buttonBounds.length; i++) {
				if(buttonBounds[i] != null && buttonBounds[i].contains(p)) {
					SoundPlayer.playClip(SoundPlayer.BUTTON_PRESS_CLIP);
					manager.setCurrentQuestion(i);
					
					picker = (picker == Team.RED) ? Team.BLUE : Team.RED;
				}
			}
		}
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
}
