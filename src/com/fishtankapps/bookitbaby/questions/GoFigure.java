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
import com.fishtankapps.bookitbaby.images.ImageManager;
import com.fishtankapps.bookitbaby.util.Constants;

public class GoFigure extends Question {

	private static final long serialVersionUID = 6021311676683375838L;
	
	private String prompt;
	
	private transient OutlinedLabel answerLabel = null;

	public GoFigure(String prompt) {
		this.prompt = prompt;
	}

	public String getPrompt() {
		return prompt;
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
		
		OutlinedLabel promptLabel = new OutlinedLabel("Try to guess the acting!",  
				new Font("Bahnschrift", Font.PLAIN, 80), 10);
		
		answerLabel = new OutlinedLabel("",  new Font("Bahnschrift", Font.PLAIN, 50), 10);
		panel.add(promptLabel, ChainGBC.getInstance(0, 0).setFill(true, false).setPadding(10, 10, 50, 10));
		panel.add(answerLabel, ChainGBC.getInstance(0, 1).setFill(true, true).setPadding(10, 10, 50, 10));
		
		return panel;
	}

	@Override
	public AnimationGroup getTypeLogoAnimation() {
		AnimationGroup animationGroup = new AnimationGroup();
		
		
		Animation redOval = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.GO_FIGURE_RED_OVAL))
				.setPoints(-180.2, 11.9, 180.2, 11.9).setAlphas(0.0f, 1.0f).setRotations(180, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(0f, 1.0f).setDurration(1000).build();
		
		Animation yellowTriangle = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.GO_FIGURE_YELLOW_TRIANGLE))
				.setPoints(-58.9, 38.3, 58.9, 38.3).setAlphas(0.0f, 1.0f).setRotations(180, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(0f, 1.0f).setDurration(1000).build();		
		
		Animation blueSquare = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.GO_FIGURE_BLUE_SQUARE))
				.setPoints(140.6, 29.6, -140.6, 29.6).setAlphas(0.0f, 1.0f).setRotations(180, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(0f, 1.0f).setDurration(1000).build();
		
		Animation text = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.GO_FIGURE_TEXT))
				.setPoints(0, 79.7, 0, 79.7).setAlphas(0.0f, 1.0f).setRotations(-180, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(0f, 1.0f).setDurration(1000).build();
		
		Animation plot = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.GO_FIGURE_FOURIER_PLOT))
				.setPoints(152.7, -43.5, -152.7, -43.5).setAlphas(0.0f, 1.0f).setRotations(-180, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(0f, 1.0f).setDurration(1000).build();
		
		Animation stickFigure = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.GO_FIGURE_STICK_FIGURE))
				.setPoints(-800, 300, 223.2, -8.1).setAlphas(1.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(500).build();

		animationGroup.addAnimationOnTop(blueSquare);
		animationGroup.addAnimationOnTop(yellowTriangle);	
		animationGroup.addAnimationOnTop(redOval);
		animationGroup.addAnimationOnTop(plot);
		animationGroup.addAnimationOnTop(text);
		animationGroup.addAnimationOnTop(stickFigure, 1000);

		
		return animationGroup;
	}

	@Override
	public void handleGameEvent(GameEvent gameEvent) {
		if(gameEvent == GameEvent.REVEAL_ANSWER && answerLabel != null) {
			answerLabel.setText(prompt);
		} else if(gameEvent == GameEvent.REVEAL_PROMPT && Constants.RUNNING_ON_PI && answerLabel != null) {
			answerLabel.setText(prompt);
			
			new Thread(() -> {
				try {
					Thread.sleep(10_000);
				} catch (InterruptedException e) {}
				
				answerLabel.setText("");
			}).start();
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(prompt);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoFigure other = (GoFigure) obj;
		return Objects.equals(prompt, other.prompt);
	}

	public String toString() {
		return prompt;
	}
}