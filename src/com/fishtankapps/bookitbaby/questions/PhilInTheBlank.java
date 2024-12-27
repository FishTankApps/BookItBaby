package com.fishtankapps.bookitbaby.questions;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.util.Objects;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.fishtankapps.animation.Animation;
import com.fishtankapps.animation.AnimationBuilder;
import com.fishtankapps.animation.AnimationGroup;
import com.fishtankapps.animation.ImagePaintable;
import com.fishtankapps.bookitbaby.game.GameEvent;
import com.fishtankapps.bookitbaby.game.GameManager;
import com.fishtankapps.bookitbaby.game.Question;
import com.fishtankapps.bookitbaby.gui.ChainGBC;
import com.fishtankapps.bookitbaby.gui.OutlinedLabel;
import com.fishtankapps.bookitbaby.gui.PhilInTheBlankDisplay;
import com.fishtankapps.bookitbaby.images.ImageManager;
import com.fishtankapps.bookitbaby.util.Constants;

public class PhilInTheBlank extends Question {

	private static final long serialVersionUID = 1388829917768815039L;
	
	private String question;
	private String answer;
	private int letterIndex;
	
	private transient PhilInTheBlankDisplay display = null;
	
	public PhilInTheBlank(String question, String answer, int letterIndex) {
		this.question = question;
		this.answer = answer;
		this.letterIndex = letterIndex;
	}

	public String getPrompt() {
		return question;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public int getLetterIndex() {
		return letterIndex;
	}
	
	
	@Override
	public JComponent getQuestionDisplay(GameManager manager)  {
		JPanel panel = new JPanel(new GridBagLayout()) {
			private static final long serialVersionUID = 3930178048104337725L;

			public void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				Graphics2D g = (Graphics2D) graphics;
				
				g.setPaint(Constants.QUESTION_DISPLAY_GRADIENT);
				g.fillRect(0, 0, getWidth(), getHeight());
				
				g.setColor(Constants.SEMI_TRANSPARENT_BLACK);
				g.fillRoundRect(10, 10, getWidth()-20, getHeight()-20, 20, 20);
			}
		};
		panel.setBackground(Constants.TRANSPARENT);
		
		OutlinedLabel promptLabel = new OutlinedLabel(question,  new Font("Bahnschrift", Font.PLAIN, Constants.PROMPT_FONT_SIZE - 10), Constants.PROMPT_FONT_SIZE/4);
		display = new PhilInTheBlankDisplay(answer, letterIndex);
		
		
		panel.add(promptLabel, ChainGBC.getInstance(0, 0).setFill(true, false).setPadding(10));
		panel.add(display,     ChainGBC.getInstance(0, 1).setFill(true, true).setPadding(10));
		
		return panel;
	}

	@Override
	public AnimationGroup getTypeLogoAnimation() {
		AnimationGroup animationGroup = new AnimationGroup();
		
		Animation blueSquare = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.PHIL_IN_THE_BLANK_BLUE_SQUARE))
				.setPoints(186.1, -451.6, 186.1, -51.6).setAlphas(1.0f, 1f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_OUT)
				.setScales(0.5f, 1.0f).setDurration(1000).build();
		
		Animation yellowTriangle = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.PHIL_IN_THE_BLANK_YELLOW_TRIANGLE))
				.setPoints(-213.0, -428.6, -213.0, -28.6).setAlphas(1.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_OUT)
				.setScales(0.5f, 1.0f).setDurration(1000).build();
		
		Animation redOval = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.PHIL_IN_THE_BLANK_RED_OVAL))
				.setPoints(-29.0, -494.8, -29.0, -94.8).setAlphas(1.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_OUT)
				.setScales(0.5f, 1.0f).setDurration(1000).build();
		
		Animation phil = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.PHIL_SIDE_VIEW))
				.setPoints(-925.1, 67.2, -25.1, 67.2).setAlphas(1.0f, 1.0f).setRotations(-680, 0).setTimeInterpolation(Animation.EASE_OUT)
				.setScales(0.25f, 1.0f).setDurration(1000).build();
		
		Animation text = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.PHIL_IN_THE_BLANK_TEXT))
				.setPoints(-1, -89.9, -1, -89.9).setAlphas(0.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(3.0f, 1.0f).setDurration(1000).build();

		animationGroup.addAnimationOnTop(blueSquare);
		animationGroup.addAnimationOnTop(yellowTriangle);
		animationGroup.addAnimationOnTop(redOval);
		animationGroup.addAnimationOnTop(phil);
		animationGroup.addAnimationOnTop(text, 100);
		
		return animationGroup;
	}

	@Override
	public void handleGameEvent(GameEvent gameEvent) {
		if(display != null) {
			if(gameEvent == GameEvent.REVEAL_ANSWER)
				display.revealAnswer();
			
			else if(gameEvent == GameEvent.SHUTDOWN)
				display.shutdown();
		}
	}


	

	@Override
	public int hashCode() {
		return Objects.hash(answer, letterIndex, question);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhilInTheBlank other = (PhilInTheBlank) obj;
		return Objects.equals(answer, other.answer) && letterIndex == other.letterIndex
				&& Objects.equals(question, other.question);
	}	
	
	public String toString() {
		return question;
	}
}