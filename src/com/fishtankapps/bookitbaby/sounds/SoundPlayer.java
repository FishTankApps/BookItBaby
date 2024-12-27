package com.fishtankapps.bookitbaby.sounds;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundPlayer {

	public static final String[] BUZZER_FILES = {
			"buzzer-elephant.wav",
			"buzzer-glockenspiel.wav",
			"buzzer-light-mechanical.wav",
			"buzzer-magic.wav",
			"buzzer-mechanical.wav",
			"buzzer-monster.wav",
			"buzzer-silly.wav"
	};
	
	public static Clip CHEERING_SOUND_CLIP;
	public static Clip DISAPPOINTED_SOUND_CLIP;
	public static Clip LAUGHTER_SOUND_CLIP;
	
	public static Clip TIME_UP_SOUND_CLIP;
	
	public static Clip BUTTON_PRESS_CLIP;
	
	public static Clip SLAPPYS_THEME_SONG_CLIP;
	private static Clip THEME_SONG_CLIP;
	
	private static Thread themeSongFadeOutThread = null;
	
	static {
		try {
			AudioInputStream cheeringInputStream = AudioSystem.getAudioInputStream(SoundPlayer.class.getResource("cheering.wav"));
			CHEERING_SOUND_CLIP = AudioSystem.getClip();
			CHEERING_SOUND_CLIP.open(cheeringInputStream); 
			
			AudioInputStream timeUpInputStream = AudioSystem.getAudioInputStream(SoundPlayer.class.getResource("times-up.wav"));
			TIME_UP_SOUND_CLIP = AudioSystem.getClip();
			TIME_UP_SOUND_CLIP.open(timeUpInputStream); 
			
			AudioInputStream buttonPressInputStream = AudioSystem.getAudioInputStream(SoundPlayer.class.getResource("button-press.wav"));
			BUTTON_PRESS_CLIP = AudioSystem.getClip();
			BUTTON_PRESS_CLIP.open(buttonPressInputStream); 

			AudioInputStream disappointedInputStream = AudioSystem.getAudioInputStream(SoundPlayer.class.getResource("disappointed.wav"));
			DISAPPOINTED_SOUND_CLIP = AudioSystem.getClip();
			DISAPPOINTED_SOUND_CLIP.open(disappointedInputStream); 

			AudioInputStream laughterInputStream = AudioSystem.getAudioInputStream(SoundPlayer.class.getResource("laughter.wav"));
			LAUGHTER_SOUND_CLIP = AudioSystem.getClip();
			LAUGHTER_SOUND_CLIP.open(laughterInputStream); 
			
			AudioInputStream themeSongInputStream = AudioSystem.getAudioInputStream(SoundPlayer.class.getResource("theme-song.wav"));
			THEME_SONG_CLIP = AudioSystem.getClip();
			THEME_SONG_CLIP.open(themeSongInputStream); 
			
			AudioInputStream slappysThemeSongInputStream = AudioSystem.getAudioInputStream(SoundPlayer.class.getResource("slappys-theme.wav"));
			SLAPPYS_THEME_SONG_CLIP = AudioSystem.getClip();
			SLAPPYS_THEME_SONG_CLIP.open(slappysThemeSongInputStream); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}
	
	private SoundPlayer() {}
	
	public static void load() {}
	
	public static void playClip(Clip clip) {
		playClip(clip, true);
	}
	
	public static void playClip(Clip clip, boolean reset) {
		clip.stop();
		clip.loop(0);
		if(reset)
			clip.setFramePosition(0);
		clip.start();
	}
	
	public static void loopClip(Clip clip, boolean reset) {
		clip.stop();
		if(reset)
			clip.setFramePosition(0);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public static void stopClip(Clip clip) {
		clip.stop();
		clip.setFramePosition(0);
	}
	
	public static void pauseClip(Clip clip) {
		clip.stop();
	}
	
	public static void playThemeSong() {
		if(themeSongFadeOutThread != null)
			themeSongFadeOutThread.interrupt();
		
		THEME_SONG_CLIP.stop();
		THEME_SONG_CLIP.setFramePosition(0);
		
		FloatControl gainControl = (FloatControl) THEME_SONG_CLIP.getControl(FloatControl.Type.MASTER_GAIN);   
		gainControl.setValue(0);		
		
		THEME_SONG_CLIP.start();
	}
	
	public static void fadeOutThemeSong() {
		if(themeSongFadeOutThread != null)
			themeSongFadeOutThread.interrupt();
		
		themeSongFadeOutThread = new Thread(() -> {
			try {
				float volume = 1f;
				int count = 0;
				FloatControl gainControl = (FloatControl) THEME_SONG_CLIP.getControl(FloatControl.Type.MASTER_GAIN);   	
				
				while(count++ < 150) {
					gainControl.setValue((float) (20 * Math.log10(volume /= 1.05)));
					Thread.sleep(25);
				}

			} catch (Exception e) {e.printStackTrace();} 
			finally {THEME_SONG_CLIP.stop();}
		});
		themeSongFadeOutThread.start();
	}

	public static void stopAllSounds() {
		if(themeSongFadeOutThread != null)
			themeSongFadeOutThread.interrupt();
		
		THEME_SONG_CLIP.stop();
		LAUGHTER_SOUND_CLIP.stop();
		CHEERING_SOUND_CLIP.stop();
		DISAPPOINTED_SOUND_CLIP.stop();
	}

	public static void playRandomBuzzerSound() {
		String name = BUZZER_FILES[(int) (Math.random() * BUZZER_FILES.length)];
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(SoundPlayer.class.getResource(name));
			Clip clip = AudioSystem.getClip();
			clip.open(stream); 
			
			clip.start();
			
			new Thread(() -> {
				
				try {
					while(clip.isRunning()) Thread.sleep(100);
					clip.close();
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		} catch (Exception e) {
			
		}
	}
}
