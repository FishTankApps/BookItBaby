package com.fishtankapps.bookitbaby.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class BookItBabyConfiguration implements Serializable {

	private static final long serialVersionUID = 5805023283475422193L;

	private static final String CONFIGURATION_PATH = System.getenv("APPDATA") + "/BookItBaby/configuration.xml";
	
	private String lastOpenedAudioPath;
	private String lastOpenedGamePath;
	private String lastConnectedIp;
	
	
	private BookItBabyConfiguration() {
		lastOpenedAudioPath = System.getProperty("user.home") + "/Music";
		lastOpenedGamePath = System.getProperty("user.home") + "/Documents";
		lastConnectedIp = "10.0.0.1";
	}
	
	private static BookItBabyConfiguration config;
	
	static {
		File configFile = new File(CONFIGURATION_PATH);
		
		if (!configFile.exists())
			config = new BookItBabyConfiguration();
		else {
			try {
				XStream stream = getXStream();
				config = (BookItBabyConfiguration) stream.fromXML(configFile);					
			} catch (Exception e) {
				config = new BookItBabyConfiguration();
			}
			
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			
			try (FileOutputStream outputStream = new FileOutputStream(configFile)) {
				XStream stream = getXStream();
				stream.toXML(config, outputStream);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));
	}	
	private static XStream getXStream() {
		XStream xStream = new XStream(new DomDriver());		
		xStream.allowTypes(new Class[] {BookItBabyConfiguration.class});
		
		xStream.alias("configuration", BookItBabyConfiguration.class);
		xStream.aliasField("last-audio-file-path", BookItBabyConfiguration.class, "lastOpenedAudioPath");
		xStream.aliasField("last-game-file-path", BookItBabyConfiguration.class, "lastOpenedGamePath");
		
		return xStream;
	}
	
	public static String getLastOpenedAudioPath() {
		return config.lastOpenedAudioPath;
	}
	public static String getLastOpenedGamePath() {
		return config.lastOpenedGamePath;
	}
	public static String getLastConnectedIp() {
		return config.lastConnectedIp;
	}
	
	public static void setLastOpenedAudioPath(String lastOpenedAudioPath) {
		config.lastOpenedAudioPath = lastOpenedAudioPath;
	}
	public static void setLastOpenedGamePath(String lastOpenedGamePath) {
		config.lastOpenedGamePath = lastOpenedGamePath;
	}
	public static void setLastConnectedIp(String ip) {
		config.lastConnectedIp = ip;
	}
}


