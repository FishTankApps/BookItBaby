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
import com.fishtankapps.bookitbaby.gui.OutlinedLabel;
import com.fishtankapps.bookitbaby.images.ImageManager;
import com.fishtankapps.bookitbaby.util.Constants;

public class Patching extends Question {

	private static final long serialVersionUID = 6021311676683375838L;
	
	private String question;
	private String answer;
	private boolean[] wordVisibilty;
	
	private transient OutlinedLabel answerLabel = null;

	public Patching(String question, String answer, boolean... wordVisibility) {
		this.question = question;
		this.answer = answer;
		this.wordVisibilty = wordVisibility;
	}
	public Patching(String question, String answer, ArrayList<Boolean> wordVisibility) {
		this.question = question;
		this.answer = answer;
		
		this.wordVisibilty = new boolean[wordVisibility.size()];
		for(int i = 0; i < wordVisibility.size(); i++)
			this.wordVisibilty[i] = wordVisibility.get(i);
	}

	public String getPrompt() {
		return question;
	}

	public String getAnswer() {
		return answer;
	}

	public boolean[] getWordVisibility() {
		return wordVisibilty;
	}
	
	public String getHiddenAnswer() {
		String[] split = answer.split("\\s");
		String hiddenAnswer = "";

		for (int index = 0; index < split.length; index++)
			hiddenAnswer += ((wordVisibilty[index]) ? split[index] : "________") + " ";

		return hiddenAnswer;
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
		answerLabel = new OutlinedLabel(getHiddenAnswer(),  new Font("Bahnschrift", Font.PLAIN, 50), 10);
		
		panel.add(promptLabel, ChainGBC.getInstance(0, 0).setFill(true, false).setPadding(10));
		panel.add(answerLabel, ChainGBC.getInstance(0, 1).setFill(true, true).setPadding(10, 10, 50, 10));
		
		return panel;
	}

	@Override
	public AnimationGroup getTypeLogoAnimation() {
		AnimationGroup animationGroup = new AnimationGroup();
		
		
		Animation redOval = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.PATCHING_RED_OVAL))
				.setPoints(-147.9, 505.8, -147.9, 5.8).setAlphas(1.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(1000).build();
		
		Animation yellowTriangle = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.PATCHING_YELLOW_TRIANGLE))
				.setPoints(114.1, 515.5, 114.1, 15.5).setAlphas(1.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(1000).build();		
		
		Animation blueSquare = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.PATCHING_BLUE_SQUARE))
				.setPoints(-19.9, 450.3, -19.9, -49.7).setAlphas(1f, 1f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(1000).build();
		
		Animation bigPatch = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.PATCHING_BIG_PATCH))
				.setPoints(-87.8, 533.1, -87.8, 33.1).setAlphas(1.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(1000).build();
		
		Animation littlePatch = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.PATCHING_LITTLE_PATCH))
				.setPoints(75.1, 556.4, 75.1, 56.4).setAlphas(1.0f, 1.0f).setRotations(0, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(1.0f, 1.0f).setDurration(1000).build();
		
		Animation text = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.PATCHING_TEXT))
				.setPoints(37.2, -36.4, 37.2, -36.4).setAlphas(0.0f, 1.0f).setRotations(-10, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(2.0f, 1.0f).setDurration(1000).build();

		animationGroup.addAnimationOnTop(redOval);
		animationGroup.addAnimationOnTop(yellowTriangle, 200);	
		animationGroup.addAnimationOnTop(blueSquare, 400);
		animationGroup.addAnimationOnTop(bigPatch, 600);
		animationGroup.addAnimationOnTop(littlePatch, 800);
		animationGroup.addAnimationOnTop(text, 800);
		
		return animationGroup;
	}

	@Override
	public void handleGameEvent(GameEvent gameEvent) {
		if(gameEvent == GameEvent.REVEAL_ANSWER && answerLabel != null) {
			answerLabel.setText(answer);
		}
	}


	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(wordVisibilty);
		result = prime * result + Objects.hash(answer, question);
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
		Patching other = (Patching) obj;
		return Objects.equals(answer, other.answer) && Objects.equals(question, other.question)
				&& Arrays.equals(wordVisibilty, other.wordVisibilty);
	}
	
	public String toString() {
		return question;
	}
}