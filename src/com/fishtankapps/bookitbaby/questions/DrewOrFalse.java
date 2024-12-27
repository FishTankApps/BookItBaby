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
import com.fishtankapps.bookitbaby.gui.TimerDisplay;
import com.fishtankapps.bookitbaby.images.ImageManager;
import com.fishtankapps.bookitbaby.util.Constants;

public class DrewOrFalse extends Question {

	private static final long serialVersionUID = 8876068927545499784L;
	
	private String prompt;
	private long timerLength;
	
	private transient TimerDisplay display = null;
	
	
 	public DrewOrFalse(String prompt, long timerLength) {
		this.prompt = prompt;
		this.timerLength = timerLength;
	}

	public String getPrompt() {
		return prompt;
	}
	
	public long getTimerLength() {
		return timerLength;
	}
	
	
	

	@Override
	public JComponent getQuestionDisplay(GameManager manager) {
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
		
		OutlinedLabel promptLabel = new OutlinedLabel(prompt,  new Font("Bahnschrift", Font.PLAIN, Constants.PROMPT_FONT_SIZE), Constants.PROMPT_FONT_SIZE/4);
		display = new TimerDisplay(timerLength);
		
		panel.add(promptLabel, ChainGBC.getInstance(0, 0).setFill(true, false).setPadding(10));
		panel.add(display,     ChainGBC.getInstance(0, 1).setFill(true, true).setPadding(10));
		
		return panel;
	}

	@Override
	public AnimationGroup getTypeLogoAnimation() {
		AnimationGroup animationGroup = new AnimationGroup();
		
		Animation blueSquare = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.DREW_OR_FALSE_BLUE_SQUARE))
				.setPoints(-500, 17, -140, 17).setAlphas(0f, 1f).setRotations(-90, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(0.5f, 1.0f).setDurration(1000).build();
		
		Animation yellowTriangle = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.DREW_OR_FALSE_YELLOW_TRIANGLE))
				.setPoints(500, -26, 125, -26).setAlphas(0.0f, 1.0f).setRotations(90, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(0.5f, 1.0f).setDurration(1000).build();
		
		Animation redOval = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.DREW_OR_FALSE_RED_OVAL))
				.setPoints(-53, 400, -53, 35).setAlphas(0.0f, 1.0f).setRotations(180, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(0.5f, 1.0f).setDurration(1000).build();
		
		Animation pencil = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.DREW_OR_FALSE_PENCIL))
				.setPoints(-16, 20, -16, 20).setAlphas(1.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(0.0f, 1.0f).setDurration(1000).build();
		
		Animation text = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.DREW_OR_FALSE_TEXT))
				.setPoints(-42, 2, -42, 2).setAlphas(0.0f, 1.0f).setRotations(10, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(2.0f, 1.0f).setDurration(1000).build();

		animationGroup.addAnimationOnTop(blueSquare);
		animationGroup.addAnimationOnTop(yellowTriangle);
		animationGroup.addAnimationOnTop(redOval);
		animationGroup.addAnimationOnTop(pencil);
		animationGroup.addAnimationOnTop(text, 100);
		
		return animationGroup;
	}
	
	public void handleGameEvent(GameEvent gameEvent) {
		if(display == null)
			return;
		
		if(gameEvent == GameEvent.START_TIMER)
			display.start();
		else if (gameEvent== GameEvent.PAUSE_TIMER)
			display.pause();
		else if (gameEvent == GameEvent.SHUTDOWN)
			display.shutdown();
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(prompt, timerLength);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DrewOrFalse other = (DrewOrFalse) obj;
		return Objects.equals(prompt, other.prompt) && timerLength == other.timerLength;
	}

	public String toString() {
		return prompt;
	}
}