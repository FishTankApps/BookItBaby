package com.fishtankapps.bookitbaby.questions;

import java.util.Objects;

import javax.swing.JComponent;

import com.fishtankapps.animation.Animation;
import com.fishtankapps.animation.AnimationBuilder;
import com.fishtankapps.animation.AnimationGroup;
import com.fishtankapps.animation.ImagePaintable;
import com.fishtankapps.bookitbaby.game.GameEvent;
import com.fishtankapps.bookitbaby.game.GameManager;
import com.fishtankapps.bookitbaby.game.Question;
import com.fishtankapps.bookitbaby.images.ImageManager;

public class ChristInContextQuestion extends Question {

	private static final long serialVersionUID = -7664208188619366762L;
	private Question question;
	
	public ChristInContextQuestion(Question question) {
		this.question = question;
	}
	
	public Question getQuestion() {
		return question;
	}
	
	@Override
	public JComponent getQuestionDisplay(GameManager manager) {
		return question.getQuestionDisplay(manager);
	}

	@Override
	public AnimationGroup getTypeLogoAnimation() {
		AnimationGroup ag = question.getTypeLogoAnimation();
		
		Animation cicBadge = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.CHRIST_IN_CONTEXT_BADGE))
				.setPoints(-145, 45, -285, 95).setAlphas(0f, 1f).setRotations(0f, -10f).setTimeInterpolation(Animation.EASE_IN)
				.setScales(2.0f, 1.0f).setDurration(500).build();
		
		ag.addAnimationOnTop(cicBadge, 1_750);
		
		return ag;
	}

	@Override
	public void handleGameEvent(GameEvent gameEvent) {
		question.handleGameEvent(gameEvent);
	}

	@Override
	public int hashCode() {
		return Objects.hash(question);
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof ChristInContextQuestion))
			return false;
		
		return question.equals(((ChristInContextQuestion) o).question);
	}
	
	
	
	public String toString() {
		return question.toString();
	}

}
