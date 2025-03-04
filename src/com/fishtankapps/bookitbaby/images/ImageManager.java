package com.fishtankapps.bookitbaby.images;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import com.fishtankapps.bookitbaby.util.Constants;

public class ImageManager {

	public static BufferedImage BOOKIT_BABY_LOGO = null;
	
	public static BufferedImage TEAM_COLOR_WHEEL = null;
	public static BufferedImage TEAM_COLOR_WHEEL_POINTER = null;
	
	public static BufferedImage RED_TEAM_BACKGROUND = null;
	public static BufferedImage BLUE_TEAM_BACKGROUND = null;
	public static BufferedImage RED_TEAM_BACKGROUND_TRAIL = null;
	public static BufferedImage BLUE_TEAM_BACKGROUND_TRAIL = null;
	
	public static BufferedImage QUESTION_BOX_LIT_BACKGROUND = null;
	public static BufferedImage QUESTION_BOX_UNLIT_BACKGROUND = null;
	
	public static BufferedImage DREW_OR_FALSE_BLUE_SQUARE = null;
	public static BufferedImage DREW_OR_FALSE_PENCIL = null;
	public static BufferedImage DREW_OR_FALSE_RED_OVAL = null;
	public static BufferedImage DREW_OR_FALSE_TEXT = null;
	public static BufferedImage DREW_OR_FALSE_YELLOW_TRIANGLE = null;
	
	public static BufferedImage GO_FIGURE_BLUE_SQUARE = null;
	public static BufferedImage GO_FIGURE_RED_OVAL = null;
	public static BufferedImage GO_FIGURE_TEXT = null;
	public static BufferedImage GO_FIGURE_YELLOW_TRIANGLE = null;
	public static BufferedImage GO_FIGURE_STICK_FIGURE = null;
	public static BufferedImage GO_FIGURE_FOURIER_PLOT = null;
	
	public static BufferedImage PATCHING_BLUE_SQUARE = null;
	public static BufferedImage PATCHING_BIG_PATCH = null;
	public static BufferedImage PATCHING_LITTLE_PATCH = null;
	public static BufferedImage PATCHING_RED_OVAL = null;
	public static BufferedImage PATCHING_TEXT = null;
	public static BufferedImage PATCHING_YELLOW_TRIANGLE = null;
	
	public static BufferedImage MULTIPLING_CHOICE_BLUE_SQUARE = null;
	public static BufferedImage MULTIPLING_CHOICE_RED_OVAL = null;
	public static BufferedImage MULTIPLING_CHOICE_TEXT = null;
	public static BufferedImage MULTIPLING_CHOICE_YELLOW_TRIANGLE = null;
	public static BufferedImage MULTIPLING_CHOICE_RABBIT_0 = null;
	public static BufferedImage MULTIPLING_CHOICE_RABBIT_1 = null;
	public static BufferedImage MULTIPLING_CHOICE_RABBIT_2 = null;
	public static BufferedImage MULTIPLING_CHOICE_RABBIT_3 = null;
	public static BufferedImage MULTIPLING_CHOICE_RABBIT_4 = null;
	public static BufferedImage MULTIPLING_CHOICE_RABBIT_5 = null;
	public static BufferedImage MULTIPLING_CHOICE_RABBIT_6 = null;
	public static BufferedImage MULTIPLING_CHOICE_RABBIT_7 = null;
	public static BufferedImage MULTIPLING_CHOICE_RABBIT_8 = null;
	public static BufferedImage MULTIPLING_CHOICE_RABBIT_9 = null;
	public static BufferedImage MULTIPLING_CHOICE_RABBIT_10 = null;
	public static BufferedImage MULTIPLING_CHOICE_RABBIT_11 = null;
	public static BufferedImage MULTIPLING_CHOICE_RABBIT_12 = null;
	
	public static BufferedImage SONG_ANSWER_BLUE_SQUARE = null;
	public static BufferedImage SONG_ANSWER_MUSIC = null;
	public static BufferedImage SONG_ANSWER_RED_OVAL = null;
	public static BufferedImage SONG_ANSWER_TEXT = null;
	public static BufferedImage SONG_ANSWER_YELLOW_TRIANGLE = null;
	
	public static BufferedImage PHIL_IN_THE_BLANK_BLUE_SQUARE = null;
	public static BufferedImage PHIL_IN_THE_BLANK_RED_OVAL = null;
	public static BufferedImage PHIL_IN_THE_BLANK_TEXT = null;
	public static BufferedImage PHIL_IN_THE_BLANK_YELLOW_TRIANGLE = null;
	
	public static BufferedImage PHIL_FRONT_VIEW = null;
	public static BufferedImage PHIL_SIDE_VIEW = null;
	
	public static BufferedImage CHRIST_IN_CONTEXT_BADGE = null;
	
	public static BufferedImage PLAY_GAME_BUTTON_UP = null;
	public static BufferedImage PLAY_GAME_BUTTON_DOWN = null;
	public static BufferedImage PLAY_GAME_BUTTON_UP_DARK = null;
	
	
	public static BufferedImage[] CURTAIN_OPENNING_ANIMATION = null;

	static {

		for(Field f : ImageManager.class.getDeclaredFields()) {
			
			if(f.getType() == BufferedImage.class) {
				String name = f.getName().toLowerCase().replace('_', '-') + ".png";
				
				try {
					f.set(null, ImageIO.read(ImageManager.class.getResourceAsStream(name)));
				} catch (IOException | IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}		
		
		if(Constants.DISPLAY_CURTAIN && !Constants.RUNNING_ON_PI) {
			try { 
				ImageInputStream iis = new MemoryCacheImageInputStream(ImageManager.class.getResourceAsStream("curtain-openning.gif"));
				
				ImageReader reader = ImageIO.getImageReadersByFormatName("GIF").next();
				reader.setInput(iis); 
				
				int total = reader.getNumImages(true);
				CURTAIN_OPENNING_ANIMATION = new BufferedImage[total];
					  
				for (int i = 0; i < total; i++) 
					CURTAIN_OPENNING_ANIMATION[i] = reader.read(i); 
				
			} catch (Exception e) {
				e.printStackTrace(); 
			}
		}
	}

	private ImageManager() {}

}
