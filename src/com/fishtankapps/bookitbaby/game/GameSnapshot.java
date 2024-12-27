package com.fishtankapps.bookitbaby.game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;

import com.fishtankapps.bookitbaby.util.Constants;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class GameSnapshot implements Serializable {

	private static final long serialVersionUID = 5728646893014784822L;

	private String redTeamName, blueTeamName;
	private int redTeamScore, blueTeamScore;
	private boolean[] questionUsed;
	
	private long gameHash;

	
	public GameSnapshot(String redTeamName, String blueTeamName, int redTeamScore, int blueTeamScore,
			boolean[] questionUsed, long gameHash) {
		this.redTeamName = redTeamName;
		this.blueTeamName = blueTeamName;
		this.redTeamScore = redTeamScore;
		this.blueTeamScore = blueTeamScore;
		this.questionUsed = questionUsed;
		this.gameHash = gameHash;
	}

	public String getRedTeamName() {
		return redTeamName;
	}

	public String getBlueTeamName() {
		return blueTeamName;
	}

	public int getRedTeamScore() {
		return redTeamScore;
	}

	public int getBlueTeamScore() {
		return blueTeamScore;
	}

	public boolean[] getQuestionUsed() {
		return questionUsed;
	}

	public long getGameHash() {
		return gameHash;
	}
	
	public static File saveSnapshot(GameSnapshot snapshot) {
		File f = GameSnapshot.createSnapshotFile(snapshot);
		
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(f);
			createXMLReaderAndWriter().toXML(snapshot, outputStream);
			outputStream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return f;
	}
	
	public static GameSnapshot openSnapshot(File f) {
		
		try {
			Object o = createXMLReaderAndWriter().fromXML(f);

			return (GameSnapshot) o;			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static XStream createXMLReaderAndWriter() {
		XStream xStream = new XStream(new DomDriver());		
		xStream.allowTypes(new Class[] {GameSnapshot.class});
		return xStream;
	}

	

	static {
		cleanSnapshotFolder();
	}
	
	public static void cleanSnapshotFolder() {
		long time = System.currentTimeMillis();
		
		for(File snapshotFile : getSnapshotFolder().listFiles()) {
			String fileName = snapshotFile.getName();
			long creationTime = Long.parseLong(fileName.split("\\.")[0]);
			
			if(time - creationTime > Constants.MAX_SNAPSHOT_AGE)
				snapshotFile.delete();
		}
	}
	
	public static File getSnapshotFolder() {
		File appDataFolder = new File(System.getenv("APPDATA") + "/BookItBaby/snapshots");
		
		if(!appDataFolder.exists())
			appDataFolder.mkdirs();
		
		return appDataFolder;
	}
	
	public static File createAppDataFolder() {
		File appDataFolder = new File(System.getenv("APPDATA") + "/BookItBaby");
		
		if(!appDataFolder.exists())
			appDataFolder.mkdirs();
		
		return appDataFolder;
	}
	
	public static File createSnapshotFile(GameSnapshot snapshot) {
		return new File(getSnapshotFolder() + "/" + System.currentTimeMillis() + "." + snapshot.getGameHash() +  ".bbgz.snapshot");
	}

	public static File[] checkForPreviousSnapshots(GameManager manager) {
		return getSnapshotFolder().listFiles();
	}
}
