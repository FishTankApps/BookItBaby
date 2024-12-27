package com.fishtankapps.bookitbaby.util;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class BoundedString {

	private Rectangle2D bounds;
	private String string;
	private Color color;
	
	private int x, y;
	
	public BoundedString(Rectangle2D bounds, int x, int y, String string, Color color) {
		super();
		this.bounds = bounds;
		this.string = string;
		this.color = color;
		this.x = x;
		this.y = y;
	}

	public Rectangle2D getBounds() {
		return bounds;
	}

	public String getString() {
		return string;
	}
	
	public Color getColor() {
		return color;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
}
