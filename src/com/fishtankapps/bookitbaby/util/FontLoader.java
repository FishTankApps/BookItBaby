package com.fishtankapps.bookitbaby.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

public class FontLoader {

	private FontLoader() {}
	
	public static void loadFonts() {
		try {
		     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, FontLoader.class.getResourceAsStream("BAHNSCHRIFT.TTF")));
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, FontLoader.class.getResourceAsStream("LCD.TTF")));
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, FontLoader.class.getResourceAsStream("IMPACT.TTF")));
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, FontLoader.class.getResourceAsStream("VTFRedzone-Classic.ttf")));
		     
		} catch (IOException|FontFormatException e) {
		     e.printStackTrace();
		}
	}
	
}
