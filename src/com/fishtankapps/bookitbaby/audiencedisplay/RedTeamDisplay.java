package com.fishtankapps.bookitbaby.audiencedisplay;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;

import com.fishtankapps.bookitbaby.game.GameManager;
import com.fishtankapps.bookitbaby.gui.BlendComposite;
import com.fishtankapps.bookitbaby.images.ImageManager;
import com.fishtankapps.bookitbaby.util.Constants;

public class RedTeamDisplay extends JComponent {

	private static final long serialVersionUID = -5470602460692292211L;
	private static final Font TEAM_NAME_FONT = new Font("VTF Redzone Classic", Font.PLAIN, 37);
	private static final Color TEAM_NAME_OUTLINE_COLOR = new Color(222,222,222);
	private static final BasicStroke TEAM_NAME_OUTLINE_STROKE = new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	
	private static final Font SCORE_FONT = new Font("VTF Redzone Classic", Font.PLAIN, 150);
	private static final BasicStroke SCORE_OUTLINE_STROKE = new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	
	private static final int[] X_CORDS = {0, 400, 400,   0};
	private static final int[] Y_CORDS = {0,   0,  98, 168};
	private static final Polygon SHADING_POLYGON = new Polygon(X_CORDS, Y_CORDS, 4);
	
	
	private GameManager manager;
	
	public RedTeamDisplay(GameManager manager) {
		this.setPreferredSize(new Dimension(400, 300));
		this.manager = manager;
		
		manager.addTeamRenameListener((n1, n2) -> repaint());
		manager.addRedTeamScoreListener((score) -> repaint());
	}
	
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		super.paintComponent(g);

		g.drawImage(ImageManager.RED_TEAM_BACKGROUND, 0, 0,
				ImageManager.RED_TEAM_BACKGROUND.getWidth(),
				ImageManager.RED_TEAM_BACKGROUND.getHeight(), null);
		
		drawTeamName(g);
		drawTeamScore(g);
		
		if(manager.isBlueBuzzedIn()) {
			Composite backup = g.getComposite();
			
			g.setComposite(BlendComposite.Multiply);
			g.setColor(Constants.BUZZED_OUT_SHADING);
			g.fill(SHADING_POLYGON);
			
			g.setComposite(backup);
		}	
	}
	
	private void drawTeamName(Graphics2D g) { 
		AffineTransform backup = g.getTransform();
		AffineTransform rotationTransform = g.getTransform();
		rotationTransform.setToRotation(Math.toRadians(-10));
		g.transform(rotationTransform);
		
		
		g.setFont(TEAM_NAME_FONT);
		g.setColor(TEAM_NAME_OUTLINE_COLOR);
		g.setStroke(TEAM_NAME_OUTLINE_STROKE);
		 
		String[] teamNameLines = getTeamNameLines(g.getFontMetrics());		
		
		
		FontRenderContext frc = g.getFontRenderContext();
		
	    TextLayout topLineText = new TextLayout(teamNameLines[0], TEAM_NAME_FONT, frc);
	    Shape topOutline = topLineText.getOutline(null);
	    
	    Shape bottomOutline = null;
	    if(teamNameLines[1].length() > 0) {
	    	TextLayout bottomLineText = new TextLayout(teamNameLines[1], TEAM_NAME_FONT, frc);
		    bottomOutline = bottomLineText.getOutline(null);
	    }
	    
	   
	    AffineTransform topLineTransform = g.getTransform();
	    topLineTransform.setToRotation(0);
	    topLineTransform.translate(-10, 106);
	    
	    g.transform(topLineTransform);
	    g.draw(topOutline);
	    
	    if(teamNameLines[1].length() > 0) {
		    g.translate(0, 44);
		    g.draw(bottomOutline);
	    }
	    
	    g.setTransform(backup);
	    g.rotate(Math.toRadians(-10));
	    
	    g.setColor(Color.BLACK);
	    g.drawString(teamNameLines[0],    -10, 106);
		g.drawString(teamNameLines[1],    -10, 150);
		
		g.setTransform(backup);
	}
	private String[] getTeamNameLines(FontMetrics fm) {
		String[] teamNameParts = manager.getRedTeamName().split("\\s");
		String topLine = "";
		String bottomLine = "";
		int i;
		
		for(i = 0; i < teamNameParts.length; i++) {
			if(fm.stringWidth(topLine + teamNameParts[i]) > 400) break;
			topLine += teamNameParts[i] + " ";
		}
		
		for(; i < teamNameParts.length; i++) {
			if(fm.stringWidth(bottomLine + teamNameParts[i]) > 400) break;
			bottomLine += teamNameParts[i] + " ";
		}
		
		return new String[]{topLine, bottomLine};
	}

	private void drawTeamScore(Graphics2D g) {
		
		g.setFont(SCORE_FONT);
		
		String score = Integer.toString(manager.getRedTeamScore());
		
		int width = g.getFontMetrics().stringWidth(score);	
		
		FontRenderContext frc = g.getFontRenderContext();
		
	    TextLayout text = new TextLayout(score, SCORE_FONT, frc);
	    Shape outline = text.getOutline(null);
	    
	    g.translate((getWidth() - width)/2, 240);
	    g.setColor(manager.isBlueBuzzedIn() ? Color.GRAY : Color.WHITE);
	    g.setStroke(SCORE_OUTLINE_STROKE);
	    g.draw(outline);
	    
	    
	    g.setColor(Color.BLACK);
		g.drawString(score, 0, 0);	
		
		g.translate(-(getWidth() - width)/2, -240);
	}
}
