package com.fishtankapps.bookitbaby.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;

import com.fishtankapps.bookitbaby.sounds.SoundPlayer;
import com.fishtankapps.bookitbaby.util.Constants;
import com.fishtankapps.bookitbaby.util.Utilities;

public class TimerDisplay extends JComponent {

	private static final long serialVersionUID = 1631247357275280165L;

	private static final Font DISPLAY_FONT = new Font("LCD", Font.PLAIN, 400);
	private static final BasicStroke OUTLINE_STROKE = new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	
	private long startTime = -1;
	private long length = 0;
	private boolean isTimeUp = false;
	
	private ScheduledThreadPoolExecutor threadPool;
	private Runnable repaintDisplay = () -> updateTimer();
	private ScheduledFuture<?> sf;
	
	
	public TimerDisplay(long length) {
		startTime = -1;
		this.length = length;
		
		threadPool = new ScheduledThreadPoolExecutor(1);
	}

	public void start() {
		if(isTimeUp)
			return;
		
		if(!Constants.RUNNING_ON_PI)
			SoundPlayer.loopClip(SoundPlayer.SLAPPYS_THEME_SONG_CLIP, sf == null);
		
		startTime = System.currentTimeMillis();
		sf = threadPool.scheduleAtFixedRate(repaintDisplay, 0, 1, TimeUnit.SECONDS);
	}
	
	public void pause() {
		if(!Constants.RUNNING_ON_PI)
			SoundPlayer.pauseClip(SoundPlayer.SLAPPYS_THEME_SONG_CLIP);
		
		length = getTimeLeft();
		startTime = -1;
		
		if(sf != null)
			sf.cancel(false);
	}

	public void shutdown() {
		if(!Constants.RUNNING_ON_PI)
			SoundPlayer.stopClip(SoundPlayer.SLAPPYS_THEME_SONG_CLIP);
		
		if(sf != null)
			sf.cancel(false);
		
		threadPool.shutdown();		
	}
	
	
	public long getTimeLeft() {
		if(startTime == -1) 
			return length;
		else
			return length - (System.currentTimeMillis() - startTime);
	}
	
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;		
		
		String text = Utilities.millisToString(Math.max(0, getTimeLeft()));
		
		g.setFont(DISPLAY_FONT);
		
		Rectangle2D bounds = g.getFontMetrics().getStringBounds(text, g);
		
		int x = (int) ( (getWidth()  - bounds.getWidth())  / 2 );
		int y = (int) ( (getHeight() - bounds.getHeight()) / 2 );

		y -= (int) bounds.getY();
		
		TextLayout textLayout = new TextLayout(text, DISPLAY_FONT, g.getFontRenderContext());
		Shape outline = textLayout.getOutline(AffineTransform.getTranslateInstance(x + 5, y + 5));		
		
		g.setStroke(OUTLINE_STROKE);
		g.setColor(Color.BLACK);
		g.draw(outline);
		
		g.setColor(Color.WHITE);
		g.drawString(text, x, y);

	}
	
	private void updateTimer() {
		
		if(getTimeLeft() < 0 && !isTimeUp) {
			isTimeUp = true;
			shutdown();
			
			if(!Constants.RUNNING_ON_PI)
				SoundPlayer.playClip(SoundPlayer.TIME_UP_SOUND_CLIP);
			
			getRootPane().repaint();
		} else if(getRootPane() != null) {
			this.getRootPane().repaint();
		} else
			shutdown();
	}
}
