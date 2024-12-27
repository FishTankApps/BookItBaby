package com.fishtankapps.animation;

import java.awt.Graphics2D;
import java.awt.Image;

public class ImagePaintable implements Paintable{

	private Image image;
	
	public ImagePaintable(Image image) {
		this.image = image;
	}

	public void paint(Graphics2D g, int width, int height) {
		g.drawImage(image, -image.getWidth(null)/2, -image.getHeight(null)/2, image.getWidth(null), image.getHeight(null), null);
	}
}