package com.fishtankapps.bookitbaby.util;

import java.awt.Color;
import java.awt.GradientPaint;

public class Constants {

	private Constants() {}
	
	public enum OperatingSystem {
		OS_X, WINDOWS, LINUX
	}	
	public static final OperatingSystem OPERATING_SYSTEM = (System.getProperty("os.name").contains("Windows")) 
			? OperatingSystem.WINDOWS : ((System.getProperty("os.name").contains("Linux")) 
					              ? OperatingSystem.LINUX : OperatingSystem.OS_X);
	
	public static final boolean RUNNING_ON_PI = OPERATING_SYSTEM == OperatingSystem.LINUX;
	
	
	public static final boolean DISPLAY_CURTAIN = true; //true;
	public static final boolean DISPLAY_SPOTLIGHT = true; //true;
	
	public static final int CONNECTION_PORT = 6_000;
	
	public static final boolean DISPLAY_WHOSE_TURN = true;
	
	public static final float ANIMATION_FPS = 24;
	
	public static final int PROMPT_FONT_SIZE = RUNNING_ON_PI ? 40 : 60;
	
	public static final Color TRANSPARENT = new Color(0,0,0,0);
	public static final Color BUZZED_OUT_SHADING = Color.GRAY; //new Color(150,150,150);
	public static final Color SEMI_TRANSPARENT_BLACK = new Color(0,0,0,126);
	
	public static final GradientPaint QUESTION_DISPLAY_GRADIENT = new GradientPaint(0, 50, Constants.TRANSPARENT, 
			0, 200, new Color(0, 0, 0, 50));

	public static final long MAX_SNAPSHOT_AGE = 604800000; // 7 Days old

	
}
