package com.fishtankapps.bookitbaby.questions;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.fishtankapps.bookitbaby.gui.MultipyingChoiceDisplay;
import com.fishtankapps.bookitbaby.gui.OutlinedLabel;
import com.fishtankapps.bookitbaby.images.ImageManager;
import com.fishtankapps.bookitbaby.util.Constants;
import com.fishtankapps.bookitbaby.util.Utilities;

public class MultiplingChoice extends Question {

	private static final long serialVersionUID = -9209571555696120253L;
	
	private String question;
	private String correctAnswer;
	private String[] answers;
	
	private transient MultipyingChoiceDisplay display = null;
	
	public MultiplingChoice(String question, String... answers) {
		this.question = question;
		correctAnswer = answers[0];
		this.answers = Utilities.shuffleArray(answers);
	}
	
	public MultiplingChoice(String question, String answer, ArrayList<String> incorrectAnswers) {
		this.question = question;
		correctAnswer = answer;
		
		String[] answers = new String[incorrectAnswers.size() + 1]; 
		answers[0] = answer;
		
		for(int i = 0; i < incorrectAnswers.size(); i++)
			answers[i + 1] = incorrectAnswers.get(i);
		
		this.answers = Utilities.shuffleArray(answers);
	}
	
	public String getPrompt() {
		return question;
	}
	
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	
	public String[] getAnswers() {
		return answers;
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
		
		OutlinedLabel promptLabel = new OutlinedLabel(question,  new Font("Bahnschrift", Font.PLAIN, Constants.PROMPT_FONT_SIZE), Constants.PROMPT_FONT_SIZE/4);
		
		display = new MultipyingChoiceDisplay(answers, correctAnswer);
		
		panel.add(promptLabel, ChainGBC.getInstance(0, 0).setFill(true, false).setPadding(10));
		panel.add(display, ChainGBC.getInstance(0, 1).setFill(true, true).setPadding(10));
		
		return panel;
	}

	@Override
	public AnimationGroup getTypeLogoAnimation() {
		AnimationGroup animationGroup = new AnimationGroup();
		
		Animation redOval = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_RED_OVAL))
				.setPoints(-91, 95.8, -91, 95.8).setAlphas(1.0f, 1.0f).setRotations(360, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(0.0f, 1.0f).setDurration(700).build();
		
		Animation yellowTriangle = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_YELLOW_TRIANGLE))
				.setPoints(-8.8, -25.4, -8.8, -25.4).setAlphas(1.0f, 1.0f).setRotations(-360, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(0.0f, 1.0f).setDurration(700).build();
		
		Animation blueSquare = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_BLUE_SQUARE))
				.setPoints(171.1, 25.2, 171.1, 25.2).setAlphas(1.0f, 1f).setRotations(-180, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(0.0f, 1.0f).setDurration(700).build();
		
		Animation text = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_TEXT))
				.setPoints(81.2, -18.7, 81.2, -18.7).setAlphas(0.0f, 1.0f).setRotations(10, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(2.0f, 1.0f).setDurration(700).build();
		
		
		Animation rabbit0 = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_RABBIT_0))
				.setPoints(281.2, 169.7, 281.2, 169.7).setAlphas(0.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(100).build();
		
		Animation rabbit1 = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_RABBIT_1))
				.setPoints(-51.7, 70.9, -51.7, 70.9).setAlphas(0.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(100).build();
		
		Animation rabbit2 = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_RABBIT_2))
				.setPoints(-124.4, 145.1, -124.4, 145.1).setAlphas(0.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(100).build();
		
		Animation rabbit3 = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_RABBIT_3))
				.setPoints(-212.1, 99.3, -212.1, 99.3).setAlphas(0.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(100).build();
		
		Animation rabbit4 = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_RABBIT_4))
				.setPoints(81.3, -66.1, 81.3, -66.1).setAlphas(0.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(100).build();
		
		Animation rabbit5 = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_RABBIT_5))
				.setPoints(161.4, 144.92, 161.4, 144.92).setAlphas(0.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(100).build();
		
		Animation rabbit6 = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_RABBIT_6))
				.setPoints(34.8, 148.8, 34.8, 148.8).setAlphas(0.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(100).build();
		
		Animation rabbit7 = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_RABBIT_7))
				.setPoints(349.3, -78.2, 349.3, -78.2).setAlphas(0.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(100).build();
		
		Animation rabbit8 = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_RABBIT_8))
				.setPoints(157.6, -147.9, 157.6, -147.9).setAlphas(0.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(100).build();
		
		Animation rabbit9 = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_RABBIT_9))
				.setPoints(264.9, 40.6, 264.9, 40.6).setAlphas(0.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(100).build();
		
		Animation rabbit10 = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_RABBIT_10))
				.setPoints(-297.3, 3.1, -297.3, 3.1).setAlphas(0.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(100).build();
		
		Animation rabbit11 = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_RABBIT_11))
				.setPoints(-42.3, -169.2, -42.3, -169.2).setAlphas(0.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(100).build();
		
		Animation rabbit12 = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.MULTIPLING_CHOICE_RABBIT_12))
				.setPoints(-127.4, -42.7, -127.4, -42.7).setAlphas(0.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(100).build();
		
		
		

		animationGroup.addAnimationOnTop(redOval);
		animationGroup.addAnimationOnTop(yellowTriangle);
		animationGroup.addAnimationOnTop(blueSquare);
		animationGroup.addAnimationOnTop(rabbit0, 900);
		animationGroup.addAnimationOnTop(rabbit1, 1300);
		animationGroup.addAnimationOnTop(rabbit2, 1700);
		animationGroup.addAnimationOnTop(rabbit3, 1900);
		animationGroup.addAnimationOnTop(rabbit4, 2000);
		animationGroup.addAnimationOnTop(rabbit5, 2100);
		animationGroup.addAnimationOnTop(rabbit6, 2200);
		animationGroup.addAnimationOnTop(rabbit7, 2300);
		animationGroup.addAnimationOnTop(rabbit8, 2400);
		animationGroup.addAnimationOnTop(rabbit9, 2500);
		animationGroup.addAnimationOnTop(rabbit10, 2550);
		animationGroup.addAnimationOnTop(rabbit11, 2600);
		animationGroup.addAnimationOnTop(rabbit12, 2650);
		animationGroup.addAnimationOnTop(text);
		
		return animationGroup;
	}

	@Override
	public void handleGameEvent(GameEvent gameEvent) {
		if(gameEvent == GameEvent.REVEAL_OPTION) {
			if(display == null)
				return;

			display.revealOption();
			
		} else if(gameEvent == GameEvent.REVEAL_ANSWER) {
			if(display == null)
				return;

			display.revealAnswer();
		}
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(answers);
		result = prime * result + Objects.hash(correctAnswer, question);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MultiplingChoice other = (MultiplingChoice) obj;
		return Arrays.equals(answers, other.answers) && Objects.equals(correctAnswer, other.correctAnswer)
				&& Objects.equals(question, other.question);
	}
	
	public String toString() {
		return question;
	}

}
