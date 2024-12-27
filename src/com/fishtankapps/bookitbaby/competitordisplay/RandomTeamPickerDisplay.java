package com.fishtankapps.bookitbaby.competitordisplay;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;

import com.fishtankapps.animation.Animation;
import com.fishtankapps.animation.AnimationBuilder;
import com.fishtankapps.animation.ImagePaintable;
import com.fishtankapps.bookitbaby.game.Team;
import com.fishtankapps.bookitbaby.images.ImageManager;

public class RandomTeamPickerDisplay extends JComponent implements MouseListener {

	private static final long serialVersionUID = -9124815434169342854L;

	private ArrayList<OnTeamPickedListener> onTeamPickedListeners;
	
	private Thread animationThread;	
	private Animation animation;
	private long startTime;
	
	private Team pickedTeam;
	
	
	public RandomTeamPickerDisplay() {
		onTeamPickedListeners = new ArrayList<>();
		addMouseListener(this);
		
		startTime = -1;
		
		pickedTeam = Math.random() > 0.5 ? Team.RED : Team.BLUE;
		
		animation = AnimationBuilder.getInstance(new ImagePaintable(ImageManager.TEAM_COLOR_WHEEL))
				.setDurration(8_000)
				.setRotations(0, 360*10 + ((pickedTeam == Team.BLUE) ? 45 : -45))
				.setAlphas(1, 1)
				.setPoints(0,0, 0,0)
				.setScales(1, 1)
				.setTimeInterpolation(Animation.EASE_IN_OUT)
				.build();
		
		animationThread = new Thread(() -> {
				try {					
					while(true) {
						Thread.sleep(42);
						getRootPane().repaint();
					}
				} catch (Exception e) {}
			});
	}
	
	public void addOnTeamPickedListener(OnTeamPickedListener l) {
		onTeamPickedListeners.add(l);
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		
		if(startTime == -1)
			animation.paint(g, 1, getWidth(), getHeight());
		else
			animation.paint(g, System.currentTimeMillis() - startTime, getWidth(), getHeight());
		
		
		BufferedImage pointer = ImageManager.TEAM_COLOR_WHEEL_POINTER;
		g.drawImage(pointer, (getWidth() - pointer.getWidth())/2, 
				             (getHeight() - pointer.getHeight())/2 - 153,
				             pointer.getWidth(), pointer.getHeight(), null);
	}

	public void mouseClicked(MouseEvent e) {
		for(OnTeamPickedListener l : onTeamPickedListeners)
			l.teamPicked(pickedTeam);
		
		if(!animationThread.isAlive() && startTime == -1)
			animationThread.start();
		
		startTime = System.currentTimeMillis();
	}
	
	
	public interface OnTeamPickedListener {
		public void teamPicked(Team team);
	}


	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
