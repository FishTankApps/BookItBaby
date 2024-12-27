package com.fishtankapps.bookitbaby.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JComponent;

public class OutlinedLabel extends JComponent {

	private static final long serialVersionUID = 7932287260962307561L;
	
	private static final int LINE_PADDING = 0;
	private static final Font STANDARD_FONT = new Font("", Font.PLAIN, 42);
	
	private Font font;
	private Color textColor, outlineColor;
	private String string;
	private BasicStroke outlineStroke = new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	
	public OutlinedLabel() {
		this("");
	}
	
	public OutlinedLabel(String string) {
		this(string, STANDARD_FONT);
	}
	
	public OutlinedLabel(String string, Font font) {
		this(string, font, 5);
	}
	
	public OutlinedLabel(String string, Font font, float outlineSize) {
		this(string, Color.WHITE, Color.BLACK, font, outlineSize);
	}
	
	public OutlinedLabel(String string, Color textColor, Color outlineColor) {
		this(string, textColor, outlineColor, STANDARD_FONT);
	}
	
	public OutlinedLabel(String string, Color textColor, Color outlineColor, float outlineSize) {
		this(string, textColor, outlineColor, STANDARD_FONT, outlineSize);
	}
	
	public OutlinedLabel(String string, Color textColor, Color outlineColor, Font font) {
		this(string, textColor, outlineColor, font, 5);
	}
	
	public OutlinedLabel(String string, Color textColor, Color outlineColor, Font font, float outlineSize) {
		this.outlineColor = outlineColor;
		this.textColor = textColor;
		this.string = string;
		this.font = font;
		
		outlineStroke = new BasicStroke(outlineSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		
		this.setMinimumSize(new Dimension(200, font.getSize() + 20));
	}
	
	
	public void setFont(Font font) {
		this.font = font;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
	}

	public void setText(String string) {
		this.string = string;
		this.getRootPane().repaint();
	}

	
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		g.setFont(font);
		g.setStroke(outlineStroke);
		
		FontMetrics fm = g.getFontMetrics();
		FontRenderContext frc = g.getFontRenderContext();
		ArrayList<String> lines = getTextLines(fm);
		TextLayout textLayout;
		Shape outline;
		
		Rectangle2D bounds;
		int height = 0;
		
		for(int i = 0; i < lines.size(); i++) {
			
			if(lines.get(i) == null || lines.get(i).equals(""))
				continue;
			
			bounds = fm.getStringBounds(lines.get(i), g);
			int x = (int) ( (getWidth() - bounds.getWidth()) / 2 );
			int y = (int) ( (i + 1) * (bounds.getHeight() + LINE_PADDING) );
			height = (int) bounds.getHeight();
			
			textLayout = new TextLayout(lines.get(i), font, frc);
			outline = textLayout.getOutline(AffineTransform.getTranslateInstance(x, y));
			
			g.setColor(outlineColor);
			g.draw(outline);
			
			g.setColor(textColor);
			g.drawString(lines.get(i), x, y);
		}
		
		height = (int) ( lines.size() * (height + LINE_PADDING + 20) );

		if(height != getHeight()) {
			this.setPreferredSize(new Dimension(200, height));
			this.invalidate();
			
			this.getRootPane().revalidate();
			this.getRootPane().repaint();
		}	
	}
	
	
	private ArrayList<String> getTextLines(FontMetrics fm) {
		ArrayList<String> spacedLines = new ArrayList<>();
		
		String[] lines = string.split("\n");
		String[] words;
		String spacedLine;
		
		for(String line : lines) {
			words = line.split("\\s");
			spacedLine = "";
			
			for(String word : words) {
				if(fm.stringWidth(spacedLine + word) > getWidth()) {
					spacedLines.add(spacedLine);
					spacedLine = word + " ";
				} else
					spacedLine += word + " ";
			}
			
			spacedLines.add(spacedLine.trim());
		}
		
		return spacedLines;
	}
}
