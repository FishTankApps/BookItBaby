package com.fishtankapps.animation;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Animation {

	public static final TimeInterpolation LINEAR = new LinearInterpolation();
	public static final TimeInterpolation EASE_IN = new CubicBezierInterpolation(0.25f, 0);
	public static final TimeInterpolation EASE_OUT = new CubicBezierInterpolation(0, 0.25f);
	public static final TimeInterpolation EASE_IN_OUT = new CubicBezierInterpolation(0.25f, 0.25f);
	
	float startX, startY, endX, endY;
	float start0, startS, end0, endS, startA, endA;
	
	long durration;
	private Paintable paintable;
	
	TimeInterpolation timeInterpolation; 
	
	Animation(Paintable paintable) {
		this.paintable = paintable;
		
		this.timeInterpolation = LINEAR;
		
		this.startX = 0;
		this.startY = 0;
		this.startA = 1f;
		this.endX = 0;
		this.endY = 0;
		this.endA = 1f;
		this.durration = 1000;
		this.start0 = 0;
		this.startS = 1;
		this.end0 = 0;
		this.endS = 1;
	}

	public void paint(Graphics2D g, long time, int width, int height) {
		AffineTransform backup = g.getTransform();
		Composite composite = g.getComposite();
		
		float progress = timeInterpolation.applyInterpolation(((float) time / durration));

		float currentX = (startX*(1-progress) + endX*(progress));
		float currentY = (startY*(1-progress) + endY*(progress));
		
		float current0 = (start0*(1-progress) + end0*(progress));
		float currentS = (startS*(1-progress) + endS*(progress));		
		
		float currentA = (startA*(1-progress) + endA*(progress));
		
		g.translate(width/2, height/2);		
		g.translate(currentX, currentY);
		g.rotate(Math.toRadians(current0));
		g.scale(currentS, currentS);
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currentA));
		
		paintable.paint(g, width, height);
		
		g.setTransform(backup);
		g.setComposite(composite);
	}

	public long getDurration() {
		return durration;
	}
}