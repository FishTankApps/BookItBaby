package com.fishtankapps.bookitbaby.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;

import javax.sound.sampled.Clip;
import javax.swing.JComponent;

import com.fishtankapps.bookitbaby.util.Constants;
import com.fishtankapps.bookitbaby.util.RawAudioFile;

public class AudioDisplay extends JComponent {

	private static final long serialVersionUID = -1923444970564188801L;

	private static final Font FONT = new Font("Bahnschrift", Font.PLAIN, 70);
	private static final BasicStroke OUTLINE_STROKE = new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private static final BasicStroke AUDIO_STROKE = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	
	private Clip clip;
	private boolean revealAnswer;
	private String songName;
	
	private short[] audioData;
	private Thread animationThread;
	
	public AudioDisplay(String songName, File songFile) {
		this.songName = songName;
		revealAnswer = false;
		
		audioData = null;
		
		animationThread = new Thread(() -> {
			try {
				while(true) {
					Thread.sleep(42);
					getRootPane().repaint();
				}
			} catch (Exception e) {}
			shutdown();
		});
		
		
		try {
			RawAudioFile raw = new RawAudioFile(songFile);
			clip = raw.getPlayableClip();
			
			raw.getAudioFormat().getSampleRate();
			audioData = raw.getChannel(1);
		} catch (Exception e) {
			clip = null;
			audioData = null;
			animationThread = null;
		}
	}
	
	public void revealAnswer() {
		revealAnswer = true;
		getRootPane().repaint();
	}
	
	public void start() {
		if(clip != null) {
			clip.start();
			
			if(animationThread.isInterrupted() || !animationThread.isAlive())
				animationThread.start();
		}		
	}
	
	public void pause() {
		if(clip != null)
			clip.stop();
	}
	
	public void shutdown() {
		if(clip != null)
			clip.close();
		
		if(animationThread != null)
			animationThread.interrupt();
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setFont(FONT);
		g.setStroke(OUTLINE_STROKE);
		
		if(revealAnswer) {
			FontMetrics fm = g.getFontMetrics();
			FontRenderContext frc = g.getFontRenderContext();
			TextLayout textLayout;
			Shape outline;
			Rectangle2D bounds = fm.getStringBounds(songName, g);
			
			int x = (int) ( (getWidth()  - bounds.getWidth())/2 );
			int y = (int) ( (getHeight() - bounds.getHeight())/2 );
			
			textLayout = new TextLayout(songName, FONT, frc);
			outline = textLayout.getOutline(AffineTransform.getTranslateInstance(x, y));
			
			g.setColor(Color.WHITE);
			g.draw(outline);
			
			g.setColor(Color.BLACK);
			g.drawString(songName, x, y);
		} else
			paintSound(g);
	}
	
	private void paintSound(Graphics2D g) {
		if(clip == null)
			return;
		
		int currentPosition = 0;
		g.setPaint(new GradientPaint(10,0, Constants.TRANSPARENT, getWidth()/2,0,  Color.WHITE, true));
		g.setStroke(AUDIO_STROKE);
		
		if(clip != null) 
			currentPosition = clip.getFramePosition();
		
		double scale = ((getHeight() / 2.0) / Short.MAX_VALUE);
		
		short value;
		for(int i = 0; i < getWidth() - 10; i++) {
			
			if(i*2 + currentPosition - getWidth()/2 >= audioData.length || i*2 + currentPosition - getWidth()/2 < 0) value = 0;
			else value = audioData[i*2 + currentPosition - getWidth()/2];
			
			value = (short) (value * scale);
			
			g.drawLine(i + 5, getHeight()/2 + value, i + 5, getHeight()/2 - value);
		}
	}
}
