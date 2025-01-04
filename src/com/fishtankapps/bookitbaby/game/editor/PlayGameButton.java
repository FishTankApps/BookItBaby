package com.fishtankapps.bookitbaby.game.editor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;

import com.fishtankapps.bookitbaby.images.ImageManager;

public class PlayGameButton extends JComponent implements MouseListener {

	private static final long serialVersionUID = -3087495408505329951L;
	
	private static final Dimension PERFERED_SIZE = new Dimension(200, 200);
	
	private ArrayList<ActionListener> actionListeners;
	
	private boolean isHovering;
	private boolean isHeld;
	
	public PlayGameButton() {
		isHovering = false;
		isHeld = false;
		
		actionListeners = new ArrayList<>();
		
		setPreferredSize(PERFERED_SIZE);
		setMinimumSize(PERFERED_SIZE);		
		
		addMouseListener(this);
	}
	
	
	public void addActionListeners(ActionListener listener) {
		actionListeners.add(listener);
	}
	
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		BufferedImage img;
		
		if(isHeld)
			img = ImageManager.PLAY_GAME_BUTTON_DOWN;
		else
			img = isHovering ? ImageManager.PLAY_GAME_BUTTON_UP : ImageManager.PLAY_GAME_BUTTON_UP_DARK;
		
		double scale = Math.min(((double) getWidth()) / img.getWidth(), ((double) getHeight()) / img.getHeight());
		
		int width = (int) (img.getWidth() * scale);
		int height = (int) (img.getHeight() * scale);
		int x = (getWidth() - width) / 2;
		int y = (getHeight() - height) / 2;
		
		g.drawImage(img, x, y, width, height, null);
	}	
	

	public void mousePressed(MouseEvent e) {
		isHeld = true;
		repaint();
	}

	public void mouseReleased(MouseEvent e) {
		isHeld = false;
		repaint();
		
		if(isHovering) {
			ActionEvent ev = new ActionEvent(this, 1, "click", System.currentTimeMillis(), 0);
			for(ActionListener l : actionListeners)
				l.actionPerformed(ev);
		}		
	}

	public void mouseEntered(MouseEvent e) {
		isHovering = true;
		repaint();
	}

	public void mouseExited(MouseEvent e) {
		isHovering = false;
		repaint();
	}
	
	public void mouseClicked(MouseEvent e) {}
}
