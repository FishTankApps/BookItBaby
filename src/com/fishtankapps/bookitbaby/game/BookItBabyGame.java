package com.fishtankapps.bookitbaby.game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fishtankapps.bookitbaby.questions.ChristInContextQuestion;
import com.fishtankapps.bookitbaby.questions.DrewOrFalse;
import com.fishtankapps.bookitbaby.questions.GoFigure;
import com.fishtankapps.bookitbaby.questions.MultiplingChoice;
import com.fishtankapps.bookitbaby.questions.Patching;
import com.fishtankapps.bookitbaby.questions.PhilInTheBlank;
import com.fishtankapps.bookitbaby.questions.QuestionDraft;
import com.fishtankapps.bookitbaby.questions.SongAnswer;
import com.fishtankapps.bookitbaby.util.FileUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class BookItBabyGame implements Serializable {

	private static final long serialVersionUID = 42855058354190122L;

	private ArrayList<Question> questions;
	
	private transient File unzipLocation = null;
	
	
	public BookItBabyGame() {
		questions = new ArrayList<>();
	}
	
	public void shuffleQuestions() {
		Collections.shuffle(questions);
	}
	
	public void setUnzipLocation(File file) {
		unzipLocation = file;
	}
	
	public ArrayList<Question> getQuestions() {
		return questions;
	}
	
	public void removeQuestionDrafts() {
		for(int i = 0; i < questions.size(); i++)
			if(questions.get(i) instanceof QuestionDraft)
				questions.remove(i--);
	}
	
	public File getInternalFile(String name) {
		if(unzipLocation == null)
			return null;
		
		return new File(unzipLocation.getAbsolutePath() + "/" + name);
	}
	
	public ArrayList<File> getInternalFiles() {
		ArrayList<File> files = new ArrayList<>();
		
		if(unzipLocation != null && unzipLocation.exists())
			for(File f : unzipLocation.listFiles((dir, name) -> {return !name.endsWith(".cfg");}))
				files.add(f);
		
		return files;
	}
	
	
	public static void createGameFile(File[] files, BookItBabyGame game, File outputFile) {
		File zipLocation = FileUtils.createTempDir("to-zip");
		
		File gameConfigurationFile = new File(zipLocation.getAbsolutePath() + "/game.cfg");
		
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(gameConfigurationFile);
			createXMLReaderAndWriter().toXML(game, outputStream);
			outputStream.close();
			
			
			for(File f : files)
				FileUtils.copyFile(f, zipLocation.getAbsolutePath() + "/" + f.getName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		FileUtils.zipFolder(zipLocation, outputFile.getAbsolutePath());	
		FileUtils.deleteFolder(zipLocation);
	}
	public static void createGameFile(List<File> files, BookItBabyGame game, File outputFile) {
		File zipLocation = FileUtils.createTempDir("to-zip");
		
		File gameConfigurationFile = new File(zipLocation.getAbsolutePath() + "/game.cfg");
		
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(gameConfigurationFile);
			createXMLReaderAndWriter().toXML(game, outputStream);
			outputStream.close();
			
			
			for(File f : files)
				FileUtils.copyFile(f, zipLocation.getAbsolutePath() + "/" + f.getName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		FileUtils.zipFolder(zipLocation, outputFile.getAbsolutePath());	
		FileUtils.deleteFolder(zipLocation);
	}
	
	public static BookItBabyGame openFile(File file) {
		
		if(!file.getName().endsWith(".bbgz"))	
			return null;
		
		File unzipLocation = FileUtils.createTempDir("game-configuration");
		FileUtils.unzipFolder(file, unzipLocation.getAbsolutePath());
		
		File gameConfigurationFile = new File(unzipLocation.getAbsolutePath() + "/game.cfg");
		
		XStream stream = createXMLReaderAndWriter();
		BookItBabyGame game = (BookItBabyGame) stream.fromXML(gameConfigurationFile);
		
		game.setUnzipLocation(unzipLocation);
		
		return game;
	}
	
	private static XStream createXMLReaderAndWriter() {
		XStream xStream = new XStream(new DomDriver());
		
		xStream.allowTypes(new Class[] {BookItBabyGame.class, DrewOrFalse.class, MultiplingChoice.class,
				                        Patching.class, PhilInTheBlank.class, SongAnswer.class, 
				                        ChristInContextQuestion.class, QuestionDraft.class, GoFigure.class});
		
		return xStream;
	}

	
	
	public int hashCode() {
		return Objects.hash(questions);
	}

}
