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
import com.fishtankapps.bookitbaby.gui.AudioDisplay;
import com.fishtankapps.bookitbaby.gui.ChainGBC;
import com.fishtankapps.bookitbaby.gui.OutlinedLabel;
import com.fishtankapps.bookitbaby.images.ImageManager;
import com.fishtankapps.bookitbaby.util.Constants;

public class SongAnswer extends Question {

	private static final long serialVersionUID = 8917697554237586334L;
	
	private String prompt;
	private String songName;
	private String songFileName;
	
	private transient AudioDisplay display = null;
	
	public SongAnswer(String prompt, String songName, String songFileName) {
		this.prompt = prompt;
		this.songName = songName;
		this.songFileName = songFileName;
	}
 
	public String getPrompt() {
		return prompt;
	}
	
	public String getSongName() {
		return songName;
	}
	
	public String getSongFileName() {
		return songFileName;
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
		
		OutlinedLabel promptLabel = new OutlinedLabel(prompt,  new Font("Bahnschrift", Font.PLAIN, Constants.PROMPT_FONT_SIZE), Constants.PROMPT_FONT_SIZE/4);
		display = new AudioDisplay(songName, manager.getInternalFile(songFileName));
		
		panel.add(promptLabel, ChainGBC.getInstance(0, 0).setFill(true, false).setPadding(10));
		panel.add(display,     ChainGBC.getInstance(0, 1).setFill(true, true).setPadding(10));
		
		return panel;
	}

	@Override
	public AnimationGroup getTypeLogoAnimation() {
		AnimationGroup animationGroup = new AnimationGroup();
		
		Animation blueSquare = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.SONG_ANSWER_BLUE_SQUARE))
				.setPoints(-405.7, 389.8, 5.7, -10.2).setAlphas(1f, 1f).setRotations(-45, 0).setTimeInterpolation(Animation.EASE_OUT)
				.setScales(0.5f, 1.0f).setDurration(1000).build();
		
		Animation yellowTriangle = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.SONG_ANSWER_YELLOW_TRIANGLE))
				.setPoints(-707.3, 402.4, -207.3, 2.4).setAlphas(1.0f, 1.0f).setRotations(45, 0).setTimeInterpolation(Animation.EASE_OUT)
				.setScales(0.5f, 1.0f).setDurration(1000).build();
		
		Animation redOval = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.SONG_ANSWER_RED_OVAL))
				.setPoints(-82.4, 336.7, 217.6, -63.3).setAlphas(1.0f, 1.0f).setRotations(180, 0).setTimeInterpolation(Animation.EASE_OUT)
				.setScales(0.5f, 1.0f).setDurration(1000).build();
		
		Animation music = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.SONG_ANSWER_MUSIC))
				.setPoints(296, 424.2, -4, 24.2).setAlphas(1.0f, 1.0f).setRotations(45, 0).setTimeInterpolation(Animation.EASE_OUT)
				.setScales(1.0f, 1.0f).setDurration(1000).build();
		
		Animation text = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.SONG_ANSWER_TEXT))
				.setPoints(-15.1, -77.5, -15.1, -77.5).setAlphas(0.0f, 1.0f).setRotations(10, 0).setTimeInterpolation(Animation.EASE_IN_OUT)
				.setScales(2.0f, 1.0f).setDurration(1000).build();

		animationGroup.addAnimationOnTop(blueSquare);
		animationGroup.addAnimationOnTop(yellowTriangle);
		animationGroup.addAnimationOnTop(redOval);
		animationGroup.addAnimationOnTop(music);
		animationGroup.addAnimationOnTop(text, 100);
		
		return animationGroup;
	}

	@Override
	public void handleGameEvent(GameEvent gameEvent) {
		if(display == null)
			return;
		
		if(gameEvent == GameEvent.PLAY_SONG)
			display.start();
		else if (gameEvent== GameEvent.PAUSE_SONG)
			display.pause();
		else if (gameEvent == GameEvent.SHUTDOWN)
			display.shutdown();
		else if (gameEvent == GameEvent.REVEAL_ANSWER)
			display.revealAnswer();
		else if (gameEvent== GameEvent.RED_BUZZ_IN)
			display.pause();
		else if (gameEvent== GameEvent.BLUE_BUZZ_IN)
			display.pause();
		else if (gameEvent== GameEvent.BUZZ_IN_CLEAR)
			display.start();
	}


	

	@Override
	public int hashCode() {
		return Objects.hash(prompt, songFileName, songName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SongAnswer other = (SongAnswer) obj;
		return Objects.equals(prompt, other.prompt) && Objects.equals(songFileName, other.songFileName)
				&& Objects.equals(songName, other.songName);
	}
	
	public String toString() {
		return prompt;
	}
}