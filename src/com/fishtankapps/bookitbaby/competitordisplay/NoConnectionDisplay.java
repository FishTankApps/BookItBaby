package com.fishtankapps.bookitbaby.competitordisplay;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import javax.swing.JComponent;

public class NoConnectionDisplay extends JComponent {

	private static final long serialVersionUID = -3497582069089678007L;
	private static final String MESSAGE = "No Connection";

	private static final Font FONT = new Font("Bahnschrift", Font.PLAIN, 90);
	private static final BasicStroke OUTLINE_STROKE = new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	
	private String ip = "Error getting IP";
	
	
	public NoConnectionDisplay() {
		try {
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
			while(e.hasMoreElements())
			{
			    NetworkInterface n = (NetworkInterface) e.nextElement();
			    Enumeration<InetAddress> ee = n.getInetAddresses();
			    while (ee.hasMoreElements())
			    {
			        String possibleIP = ee.nextElement().getHostAddress();
			        if(possibleIP.startsWith("192.") || possibleIP.startsWith("10."))
			        	ip = possibleIP;
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setFont(FONT);
		g.setStroke(OUTLINE_STROKE);
		
		FontRenderContext frc = g.getFontRenderContext();
		FontMetrics fm = g.getFontMetrics();
		TextLayout textLayout;
		Shape outline;
		
		int width, x, y;
		
		width = fm.stringWidth(MESSAGE);
		x = (getWidth() - width)/2;
		y = 150;
		
		textLayout = new TextLayout(MESSAGE, FONT, frc);
		outline = textLayout.getOutline(AffineTransform.getTranslateInstance(x, y));
		
		g.setColor(Color.BLACK);
		g.draw(outline);
		
		g.setColor(Color.WHITE);
		g.drawString(MESSAGE, x, y);
		
		
		width = fm.stringWidth(ip);
		x = (getWidth() - width)/2;
		y = 325;
		
		textLayout = new TextLayout(ip, FONT, frc);
		outline = textLayout.getOutline(AffineTransform.getTranslateInstance(x, y));
		
		g.setColor(Color.BLACK);
		g.draw(outline);
		
		g.setColor(Color.WHITE);
		g.drawString(ip, x, y);
	}
}
