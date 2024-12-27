package com.fishtankapps.animation;

import java.awt.Point;
import java.awt.geom.Point2D;

public class AnimationBuilder {

	private Animation animation;
	
	private AnimationBuilder(Paintable paintable) {
		animation = new Animation(paintable);
	}
	
	
	public AnimationBuilder setStartingPoint(float x, float y) {
		animation.startX = x;
		animation.startY = y;
		return this;
	}	
	public AnimationBuilder setStartingPoint(double x, double y) {
		return setStartingPoint((float) x, (float) y);
	}
	public AnimationBuilder setStartingPoint(Point p) {
		return setStartingPoint(p.getX(), p.getY());
	}	
	public AnimationBuilder setStartingPoint(Point2D p) {
		return setStartingPoint(p.getX(), p.getY());
	}
	
	public AnimationBuilder setEndingPoint(float x, float y) {
		animation.endX = x;
		animation.endY = y;
		return this;
	}	
	public AnimationBuilder setEndingPoint(double x, double y) {
		return setEndingPoint((float) x, (float) y);
	}
	public AnimationBuilder setEndingPoint(Point p) {
		return setEndingPoint(p.getX(), p.getY());
	}	
	public AnimationBuilder setEndingPoint(Point2D p) {
		return setEndingPoint(p.getX(), p.getY());
	}
	
	public AnimationBuilder setPoints(float startingX, float startingY, float endingX, float endingY) {
		animation.startX = startingX;
		animation.startY = startingY;
		animation.endX = endingX;
		animation.endY = endingY;
		return this;
	}	
	public AnimationBuilder setPoints(double startingX, double startingY, double endingX, double endingY) {
		return setPoints((float) startingX, (float) startingY, (float) endingX, (float) endingY);
	}
	public AnimationBuilder setPoints(Point start, Point end) {
		return setPoints(start.getX(), start.getY(), end.getX(), end.getY());
	}	
	public AnimationBuilder setPoints(Point2D start, Point2D end) {
		return setPoints(start.getX(), start.getY(), end.getX(), end.getY());
	}
	
	
	
	public AnimationBuilder setStartingScale(float scale) {
		animation.startS = scale;
		return this;
	}
	public AnimationBuilder setStartingScale(double scale) {
		return setStartingScale((float) scale);
	}	
	
	public AnimationBuilder setEndingScale(float scale) {
		animation.endS = scale;
		return this;
	}
	public AnimationBuilder setEndingScale(double scale) {
		return setEndingScale((float) scale);
	}
	
	public AnimationBuilder setScales(float startingScale, float endingScale) {
		animation.startS = startingScale;
		animation.endS = endingScale;
		return this;
	}
	public AnimationBuilder setScales(double startingScale, double endingScale) {
		return setScales((float) startingScale, (float) endingScale);
	}

	
	
	public AnimationBuilder setStartingRotation(float degrees) {
		animation.start0 = degrees;
		return this;
	}
	public AnimationBuilder setStartingRotation(double degrees) {
		return setStartingRotation((float) degrees);
	}

	public AnimationBuilder setEndingRotation(float degrees) {
		animation.end0 = degrees;
		return this;
	}
	public AnimationBuilder setEndingRotation(double degrees) {
		return setEndingRotation((float) degrees);
	}

	public AnimationBuilder setRotations(float startingDegrees, float endingDegrees) {
		animation.start0 = startingDegrees;
		animation.end0 = endingDegrees;
		return this;
	}
	public AnimationBuilder setRotations(double startingDegrees, double endingDegrees) {
		return setRotations((float) startingDegrees, (float) endingDegrees);
	}

	
	public AnimationBuilder setStartingAlpha(float alpha) {
		animation.startA = Math.min(Math.max(alpha, 0), 1);
		return this;
	}
	public AnimationBuilder setStartingAlpha(double alpha) {
		return setStartingAlpha((float) alpha);
	}
	
	public AnimationBuilder setEndingAlpha(float alpha) {
		animation.endA = Math.min(Math.max(alpha, 0), 1);
		return this;
	}
	public AnimationBuilder setEndingAlpha(double alpha) {
		return setEndingAlpha((float) alpha);
	}
	
	public AnimationBuilder setAlphas(float startingAlpha, float endingAlpha) {
		animation.startA = Math.min(Math.max(startingAlpha, 0), 1);
		animation.endA = Math.min(Math.max(endingAlpha, 0), 1);
		return this;
	}
	public AnimationBuilder setAlphas(double startingAlpha, double endingAlpha) {
		return setAlphas((float) startingAlpha, (float) endingAlpha);
	}

	
	public AnimationBuilder setDurration(long durration) {
		animation.durration = durration;
		return this;
	}
	
	
	public AnimationBuilder setTimeInterpolation(TimeInterpolation ti) {
		animation.timeInterpolation = ti;
		return this;
	}
	
	
		
	public Animation build() {
		return animation;
	}
		
	
	public static AnimationBuilder getInstance(Paintable paintable) {
		return new AnimationBuilder(paintable);
	}
}
