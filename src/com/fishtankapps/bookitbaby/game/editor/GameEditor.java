package com.fishtankapps.bookitbaby.game.editor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.fishtankapps.bookitbaby.game.BookItBabyGame;
import com.fishtankapps.bookitbaby.game.Question;
import com.fishtankapps.bookitbaby.game.editor.QuestionProperties.OnChangeListener;
import com.fishtankapps.bookitbaby.game.editor.QuestionProperties.Property;
import com.fishtankapps.bookitbaby.util.FileUtils;

public class GameEditor {
	
	private ArrayList<QuestionPropertiesListChangeListener> questionPropertiesListChangeListeners;
	private ArrayList<SelectedQuestionPropertiesListener> selectedQuestionPropertiesListeners;
	private ArrayList<QuestionPropertiesChangeListener> questionPropertiesChangeListeners;

	
	private ArrayList<QuestionProperties> questionProperties;
	private ArrayList<File> files;
	
	private QuestionProperties selectedProperties;
	
	private final OnChangeListener ON_QUESTION_PROPETIES_CHANGED = (qp, p) -> {
		for(QuestionPropertiesChangeListener l : questionPropertiesChangeListeners)
			l.onQuestionPropertiesChanged(qp, questionProperties.indexOf(qp), p);
	};
	
	private JFrame frame;
	
	public GameEditor(JFrame frame) {
		questionProperties = new ArrayList<>();
		files = new ArrayList<>();
		this.frame = frame;
		
		questionPropertiesListChangeListeners = new ArrayList<>();
		selectedQuestionPropertiesListeners = new ArrayList<>();
		questionPropertiesChangeListeners = new ArrayList<>();
	}
	
	public void openGame(BookItBabyGame game) {
		questionProperties.clear();
		
		for(Question q : game.getQuestions()) {
			QuestionProperties qp = new QuestionProperties(q);
			
			qp.addOnChangeListener(ON_QUESTION_PROPETIES_CHANGED);
			questionProperties.add(qp);
		}
		
		files = game.getInternalFiles();
		
		for(QuestionPropertiesListChangeListener l : questionPropertiesListChangeListeners)
			l.onListChange();
	}
	
	public void addQuestionProperties(QuestionProperties qp) {
		qp.addOnChangeListener(ON_QUESTION_PROPETIES_CHANGED);
		questionProperties.add(qp);
		
		for(QuestionPropertiesListChangeListener l : questionPropertiesListChangeListeners)
			l.onListChange();
	}
	
	public void removeQuestionProperties(int index) {
		questionProperties.remove(index);
		
		for(QuestionPropertiesListChangeListener l : questionPropertiesListChangeListeners)
			l.onListChange();
	}
	public void removeQuestionProperties(QuestionProperties qp) {
		questionProperties.remove(qp);
		
		for(QuestionPropertiesListChangeListener l : questionPropertiesListChangeListeners)
			l.onListChange();
	}
	public void removeSelectedQuestionProperties() {
		removeQuestionProperties(selectedProperties);
		
		for(QuestionPropertiesListChangeListener l : questionPropertiesListChangeListeners)
			l.onListChange();
	}
		
	public void selectQuestionProperties(int index) {
		selectedProperties = questionProperties.get(index);
		
		for(SelectedQuestionPropertiesListener l : selectedQuestionPropertiesListeners)
			l.onSelectedQuestionPropertiesChanged(selectedProperties);
	}
	public void selectQuestionProperties(QuestionProperties qp) {
		int index = questionProperties.indexOf(qp);
		if(index < 0)
			clearSelectedQuestionProperties();
		else
			selectQuestionProperties(index);
	}
	public void clearSelectedQuestionProperties() {
		selectedProperties = null;
		
		for(SelectedQuestionPropertiesListener l : selectedQuestionPropertiesListeners)
			l.onSelectedQuestionPropertiesChanged(selectedProperties);
	}
	
	
	public QuestionProperties getSelectedQuestionProperties() {
		return selectedProperties;
	}
	public int getSelectedQuestionPropertiesIndex() {
		return questionProperties.indexOf(selectedProperties);
	}
	public String[] getQuestionPrompts() {
		String[] prompts = new String[questionProperties.size()];
		
		for(int i = 0; i < prompts.length; i++)
			prompts[i] = questionProperties.get(i).getPrompt();

		return prompts;
	}
	public String[] getFileNames() {
		String[] prompts = new String[files.size()];
		
		for(int i = 0; i < prompts.length; i++)
			prompts[i] = files.get(i).getName();

		return prompts;
	}
	
	public void addFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Chooser Audio File");
		fileChooser.showOpenDialog(frame);
		
		File f = fileChooser.getSelectedFile();
		
		if(f == null || !f.exists())
			return;
		
		File tempFile = FileUtils.createTempFile(f.getName(), "", "game-configuration", false);
		try {
			FileUtils.copyFile(f, tempFile.getAbsolutePath());
			files.add(tempFile);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "There was an error trying to load the file:\n" + e.getMessage(), 
					"Error Loading File", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void addSelectedQuestionPropertiesListener(SelectedQuestionPropertiesListener l) {
		selectedQuestionPropertiesListeners.add(l);
	}
	public void addQuestionPropertiesChangeListener(QuestionPropertiesChangeListener l) {
		questionPropertiesChangeListeners.add(l);
	}
	public void addQuestionPropertiesListChangeListener(QuestionPropertiesListChangeListener l) {
		questionPropertiesListChangeListeners.add(l);
	}
	
	
	public BookItBabyGame createGame() {
		BookItBabyGame game = new BookItBabyGame();
		
		for(QuestionProperties qp : questionProperties) {
			Question question = qp.generateQuestion();
			
			if(question != null)
				game.getQuestions().add(question);
			else
				System.out.println("Question was unsucessully generated.");
		}
		
		game.shuffleQuestions();
		
		return game;
	}
	
	public static interface SelectedQuestionPropertiesListener {
		public void onSelectedQuestionPropertiesChanged(QuestionProperties qp);
	}
	public static interface QuestionPropertiesChangeListener {
		public void onQuestionPropertiesChanged(QuestionProperties qp, int index, Property p);
	}
	public static interface QuestionPropertiesListChangeListener {
		public void onListChange();
	}
}
