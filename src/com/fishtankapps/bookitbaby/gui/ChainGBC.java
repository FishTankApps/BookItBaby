package com.fishtankapps.bookitbaby.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class ChainGBC extends GridBagConstraints {
	private static final long serialVersionUID = -665560057212809379L;

	private ChainGBC() {}
	
	public static ChainGBC gbc = null;
	public static ChainGBC getInstance(int x, int y) {
		if(gbc == null)
			gbc = new ChainGBC();
		
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		
		gbc.insets = new Insets(5,5,5,5);
		gbc.anchor = GridBagConstraints.WEST;// or NORTH, EAST, SOUTH, WEST, SOUTHWEST etc.
		gbc.fill   = GridBagConstraints.BOTH;
		
		gbc.weightx = 1;
		gbc.weighty = 1;	
		
		return gbc;
	}
	
	public ChainGBC setWidthAndHeight(int width, int height) {
		this.gridwidth = width;
		this.gridheight = height;
		
		return this;
	}
	
	public ChainGBC setPadding(int padding) {
		this.insets = new Insets(padding,padding,padding,padding);
		return this;
	}
	
	public ChainGBC setPadding(int left, int right, int top, int botton) {
		this.insets = new Insets(top,left,botton,right);
		return this;
	}
	
	public ChainGBC setFill(boolean fill) {
		this.weightx = (fill) ? 1 : 0;
		this.weighty = (fill) ? 1 : 0;
		return this;
	}
	
	public ChainGBC setFill(boolean xFill, boolean yFill) {
		this.weightx = (xFill) ? 1 : 0;
		this.weighty = (yFill) ? 1 : 0;
		return this;
	}
	
	public ChainGBC setFill(double xFill, double yFill) {
		this.weightx = xFill;
		this.weighty = yFill;
		return this;
	}
}
