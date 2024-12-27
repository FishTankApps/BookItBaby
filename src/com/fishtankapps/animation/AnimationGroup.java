package com.fishtankapps.animation;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;

public class AnimationGroup {

	private static final int DEFAULT_ANIMATION_FPS = 24;
	
	private ArrayList<Animation> animations;
	private ArrayList<Long> timingOffsets;
	private long time;
	private long durration;
	private JComponent displayComponent;
	private Thread animationThread;
	
	private int animationFPS;
	
	public AnimationGroup() {
		animations = new ArrayList<>();
		timingOffsets = new ArrayList<>();
		time = 0;
		durration = 0;
		animationFPS = DEFAULT_ANIMATION_FPS;
		
		displayComponent = new JComponent() {

			private static final long serialVersionUID = 3523264408450130604L;

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				AnimationGroup.this.paint((Graphics2D) g, getWidth(), getHeight());
			}
		};
	
		animationThread = new Thread(() -> {
			try {
				final int time = (int) (1000 / animationFPS);
				
				while (!isComplete()) {
					advance(time);
					Thread.sleep(time);
				}

			} catch (Exception e) {
				advance(1_000_000);
			}
		});
	}
	
	public JComponent createDisplayComponent() {
		return displayComponent;
	}

	public void start() {	
		animationThread.start();
	}
	public void stop() {
		animationThread.interrupt();
	}

	public void setFPS(int fps) {
		animationFPS = fps;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	public long getTime() {
		return time;
	}
	
	public void advance(long amount) {
		time += amount;
		
		if(displayComponent.getRootPane() != null)
			displayComponent.getRootPane().repaint();
		else
			stop();
	}
	
	public long getDurration() {
		return durration;
	}
		
	public boolean isComplete() {
		return time > durration;
	}
	
	
	
	public void paint(Graphics2D g, int width, int height) {
		paint(g, time, width, height);
	}	
	public void paint(Graphics2D g, long time, int width, int height) {
		for(int i = 0; i < animations.size(); i++)
			animations.get(i).paint(g, time - timingOffsets.get(i), width, height);
	}
	
	public void addAnimationOnTop(Animation a) {
		addAnimationOnTop(a, 0);
	}
	public void addAnimationOnTop(Animation a, long timingOffset) {
		animations.add(a);
		timingOffsets.add(timingOffset);

		if(a.getDurration() + timingOffset > durration)
			durration = a.getDurration() + timingOffset;
	}
	
	public void addAnimationOnBottom(Animation a) {
		addAnimationOnBottom(a, 0);
	}
	public void addAnimationOnBottom(Animation a, long timingOffset) {
		animations.add(0, a);
		timingOffsets.add(0, timingOffset);
		
		if(a.getDurration() + timingOffset > durration)
			durration = a.getDurration() + timingOffset;
	}
}