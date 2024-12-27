package com.fishtankapps.bookitbaby.game.editor;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import com.fishtankapps.bookitbaby.game.Question.QuestionType;
import com.fishtankapps.bookitbaby.gui.ChainGBC;

public class QuestionPropertiesEditor extends JPanel {

	private static final long serialVersionUID = -4772111871020992963L;

	private QuestionProperties qp;
	private GameEditor editor;
	
	public QuestionPropertiesEditor(QuestionProperties qp, GameEditor editor) {
		super(new GridBagLayout());
		this.editor = editor;
		this.qp = qp;
				
		qp.addOnChangeListener((updatedQp, property) -> {
				if(property == QuestionProperties.Property.QUESTION_TYPE)
					refresh();
			});
		
		refresh();
	}
	
	public void refresh() {
		this.removeAll();
		
		if(qp.getQuestionType() == QuestionType.DREW_OR_FALSE)
			addDrewOrFalseComponents();
		else if(qp.getQuestionType() == QuestionType.MULTIPLYING_CHOICE)
			addMultipyingChoiceComponents();
		else if(qp.getQuestionType() == QuestionType.PATCHING)
			addPatchingComponents();
		else if(qp.getQuestionType() == QuestionType.PHIL_IN_THE_BLANK) {
			addPhilInTheBlankComponents();
		} else if(qp.getQuestionType() == QuestionType.SONG_ANSWER) {
			addSongAnswerComponents();
		}
	
		this.revalidate();
		this.repaint();
	}
	
	private void addDrewOrFalseComponents() {
		JLabel promptLabel = new JLabel("Drawing Prompt:", JLabel.RIGHT);
		JTextField promptField = new JTextField(qp.getPrompt());
		JLabel timeLimitLabel = new JLabel("Time Limit (in milliseconds):", JLabel.RIGHT);
		JIntField timeLimitField = new JIntField(qp.getTimeLimit() + "");
		
		promptField.getDocument().addDocumentListener(new PropertyDocumentListener() {
			public void onTextChanged(String s) { qp.setPrompt(s); }});
		
		timeLimitField.getDocument().addDocumentListener(new PropertyDocumentListener() {
			public void onTextChanged(String s) { qp.setTimeLimit(timeLimitField.getValue()); }});
		
		add(promptLabel,    ChainGBC.getInstance(0, 0).setFill(false));
		add(promptField,    ChainGBC.getInstance(1, 0).setFill(true, false));
		add(timeLimitLabel, ChainGBC.getInstance(0, 1).setFill(false));
		add(timeLimitField, ChainGBC.getInstance(1, 1).setFill(true, false));
	}
	private void addMultipyingChoiceComponents() {
		JLabel promptLabel = new JLabel("Prompt:", JLabel.RIGHT);
		JTextField promptField = new JTextField(qp.getPrompt());
		JLabel correctAnswerLabel = new JLabel("Correct Answer:", JLabel.RIGHT);
		JTextField correctAnswerField = new JTextField(qp.getAnswer());
		
		JLabel incorrectAnswerLabel = new JLabel("Incorrect Answers:", JLabel.RIGHT);
		JTextField incorrectAnswerField = new JTextField(qp.getIncorrectAnswersString());
		
		
		promptField.getDocument().addDocumentListener(new PropertyDocumentListener() {
			public void onTextChanged(String s) { qp.setPrompt(s); }});
		
		correctAnswerField.getDocument().addDocumentListener(new PropertyDocumentListener() {
			public void onTextChanged(String s) { qp.setAnswer(s); }});
		
		incorrectAnswerField.getDocument().addDocumentListener(new PropertyDocumentListener() {
			public void onTextChanged(String s) { qp.setIncorrectAnswers(s); }});
		
		
		add(promptLabel,    ChainGBC.getInstance(0, 0).setFill(false));
		add(promptField,    ChainGBC.getInstance(1, 0).setFill(true, false));
		add(correctAnswerLabel, ChainGBC.getInstance(0, 1).setFill(false));
		add(correctAnswerField, ChainGBC.getInstance(1, 1).setFill(true, false));
		add(incorrectAnswerLabel, ChainGBC.getInstance(0, 2).setFill(false));
		add(incorrectAnswerField, ChainGBC.getInstance(1, 2).setFill(true, false));
	}
	private void addPatchingComponents() {
		JLabel promptLabel = new JLabel("Prompt:", JLabel.RIGHT);
		JTextField promptField = new JTextField(qp.getPrompt());
		JLabel answerLabel = new JLabel("Answer:", JLabel.RIGHT);
		JTextField answerField = new JTextField(qp.getAnswer());
		
		JPanel wordVisibilityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		wordVisibilityPanel.setBorder(BorderFactory.createTitledBorder("Word Visibility"));
		
		answerField.addActionListener(e-> {
			wordVisibilityPanel.removeAll();
			
			String[] array = answerField.getText().split("\\s");
			for(String s : array)
				wordVisibilityPanel.add(new JCheckBox(s));
			
			wordVisibilityPanel.revalidate();
			wordVisibilityPanel.repaint();
		});
		
		String[] array = answerField.getText().split("\\s");
		for(int i = 0; i < array.length; i++) {
			final int index = i;
			
			if(i >= qp.getWordVisibility().size()) {
				qp.getWordVisibility().add(false);
			}
			
			JCheckBox box = new JCheckBox(array[i], qp.getWordVisibility().get(i));
			
			box.addActionListener(e -> {
				qp.getWordVisibility().set(index, box.isSelected());
			});
			
			wordVisibilityPanel.add(box); // TODO: This
		}
		
		wordVisibilityPanel.revalidate();
		
		
		promptField.getDocument().addDocumentListener(new PropertyDocumentListener() {
			public void onTextChanged(String s) { qp.setPrompt(s); }});
		
		answerField.getDocument().addDocumentListener(new PropertyDocumentListener() {
			public void onTextChanged(String s) {
				wordVisibilityPanel.removeAll();	
				
				String[] words = s.split("\\s");
				ArrayList<Boolean> newWordVis = remapWordVisibility(qp.getAnswer(), s, qp.getWordVisibility());
				
				for(int i = 0; i < words.length; i++) {
					final int index = i;
					JCheckBox box = new JCheckBox(words[i], newWordVis.get(i));
					
					box.addActionListener(e -> {
						qp.getWordVisibility().set(index, box.isSelected());
					});
					
					wordVisibilityPanel.add(box); // TODO: This
				}
				
				wordVisibilityPanel.revalidate();
				wordVisibilityPanel.repaint();
				
				qp.setAnswer(s);
				qp.setWordVisibility(newWordVis);
			}
		});
		
		add(promptLabel,    ChainGBC.getInstance(0, 0).setFill(false));
		add(promptField,    ChainGBC.getInstance(1, 0).setFill(true, false));
		add(answerLabel, ChainGBC.getInstance(0, 1).setFill(false));
		add(answerField, ChainGBC.getInstance(1, 1).setFill(true, false));
		add(wordVisibilityPanel, ChainGBC.getInstance(0, 2).setFill(true, false).setWidthAndHeight(2, 1));		
	}
	private void addPhilInTheBlankComponents() {
		JLabel promptLabel = new JLabel("Prompt:", JLabel.RIGHT);
		JTextField promptField = new JTextField(qp.getPrompt());
		JLabel answerLabel = new JLabel("Answer:", JLabel.RIGHT);
		JTextField answerField = new JTextField(qp.getAnswer());
		
		JLabel letterIndexLabel = new JLabel(getLetterIndexLabelText(qp.getLetterIndex()), JLabel.RIGHT);
		JIntField letterIndexField = new JIntField(qp.getLetterIndex() + "");
		
		letterIndexField.addActionListener(e -> {
				letterIndexLabel.setText(getLetterIndexLabelText(letterIndexField.getValue()));
			});
		
		
		promptField.getDocument().addDocumentListener(new PropertyDocumentListener() {
			public void onTextChanged(String s) { qp.setPrompt(s); }});
		
		answerField.getDocument().addDocumentListener(new PropertyDocumentListener() {
			public void onTextChanged(String s) { qp.setAnswer(s); }});
		
		
		letterIndexField.getDocument().addDocumentListener(new PropertyDocumentListener() {
			public void onTextChanged(String s) { qp.setLetterIndex(letterIndexField.getValue()); }});
		
		
		add(promptLabel,    ChainGBC.getInstance(0, 0).setFill(false));
		add(promptField,    ChainGBC.getInstance(1, 0).setFill(true, false));
		add(answerLabel, ChainGBC.getInstance(0, 1).setFill(false));
		add(answerField, ChainGBC.getInstance(1, 1).setFill(true, false));
		add(letterIndexLabel, ChainGBC.getInstance(0, 2).setFill(false));
		add(letterIndexField, ChainGBC.getInstance(1, 2).setFill(true, false));
	}
	private void addSongAnswerComponents() {
		JLabel promptLabel = new JLabel("Prompt:", JLabel.RIGHT);
		JTextField promptField = new JTextField(qp.getPrompt());
		JLabel songNameLabel = new JLabel("Song Name:", JLabel.RIGHT);
		JTextField songNameField = new JTextField(qp.getAnswer());
		

		JLabel songFileLabel = new JLabel("Audio File:", JLabel.RIGHT);
		JComboBox<String> songFileField = new JComboBox<>(editor.getFileNames());
		
		songFileField.setSelectedItem(qp.getSongFile());
		
		JButton openFileButton = new JButton("Add File...");
		openFileButton.addActionListener(e -> { 
			editor.addFile();
			
			String selectedFileName = (String) songFileField.getSelectedItem();			
			songFileField.removeAllItems();
			
			for(String name : editor.getFileNames())
				songFileField.addItem(name);
			
			songFileField.setSelectedItem(selectedFileName);
		});
		
		
		promptField.getDocument().addDocumentListener(new PropertyDocumentListener() {
			public void onTextChanged(String s) { qp.setPrompt(s); }});
		
		songNameField.getDocument().addDocumentListener(new PropertyDocumentListener() {
			public void onTextChanged(String s) { qp.setAnswer(s); }});
		
		songFileField.addActionListener(e -> qp.setSongFile((String) songFileField.getSelectedItem()));

		add(promptLabel,    ChainGBC.getInstance(0, 0).setFill(false));
		add(promptField,    ChainGBC.getInstance(1, 0).setFill(true, false).setWidthAndHeight(3, 1));
		add(songNameLabel,  ChainGBC.getInstance(0, 1).setFill(false));
		add(songNameField,  ChainGBC.getInstance(1, 1).setFill(true, false).setWidthAndHeight(3, 1));
		add(songFileLabel,  ChainGBC.getInstance(0, 2).setFill(false));
		add(songFileField,  ChainGBC.getInstance(1, 2).setFill(false));
		add(openFileButton, ChainGBC.getInstance(2, 2).setFill(false));
	}
	
	private String getLetterIndexLabelText(int index) {
		String s = "Letter Index";
		
		if(index >= 0 &&  index < qp.getAnswer().length())
			s += " (" + qp.getAnswer().charAt(index) + ") ";
		
		s += ":";
		return s;
	}

	
	
	private static ArrayList<Boolean> remapWordVisibility(String oldAnswer, String newAnswer, ArrayList<Boolean> oldWordVis) {
		String[] oldSplitAnswer = oldAnswer.split("\\s");
		String[] newSplitAnswer = newAnswer.split("\\s");
		
		double[][] matchMatrix = new double[oldSplitAnswer.length][newSplitAnswer.length];
		
		ArrayList<Boolean> newWordVis = new ArrayList<>();
		
		for(int i = 0; i < newSplitAnswer.length; i++)
			newWordVis.add(false);		
		
		for(int old = 0; old < oldSplitAnswer.length; old++) {
			for(int New = 0; New < newSplitAnswer.length; New++) {
				matchMatrix[old][New] = compareStrings(oldSplitAnswer[old], newSplitAnswer[New]);
				matchMatrix[old][New] *= 1.0 / (Math.abs(New-old)/16.0 + 1);
			}
		}
		
		double[] d = new double[newSplitAnswer.length];		
		for(int i = 0; i < oldSplitAnswer.length; i++) {
			
			double highestPercent = matchMatrix[i][0];
			int hightestIndex = 0;
			
			for(int j = 1; j < newSplitAnswer.length; j++) {
				if(matchMatrix[i][j] > highestPercent) {
					highestPercent = matchMatrix[i][j];
					hightestIndex = j;
				}
			}
			
			if(d[hightestIndex] < highestPercent) {
				d[hightestIndex] = highestPercent;
				newWordVis.set(hightestIndex, oldWordVis.get(i));
			}						
		}
		
		return newWordVis;
	}
	
	private static double compareStrings(String s1, String s2) {
		double percentPerLetter = 1.0 / Math.min(s1.length(), s2.length());
		double percentMatch = 0;
		int closestIndex;
		
		for (int i = 0; i < Math.min(s1.length(), s2.length()); i++) {
			closestIndex = Integer.MAX_VALUE;
			
			for(int j = i; j >= 0; j--) {
				if(s1.charAt(i) == s2.charAt(j)) {
					closestIndex = i - j;
					continue;
				}
			}
			
			for(int j = i; j < s2.length() && (j - i < closestIndex); j++) {
				if(s1.charAt(i) == s2.charAt(j)) {
					closestIndex = j - i;
					continue;
				}
			}
			
			if(closestIndex != Integer.MAX_VALUE)				
				percentMatch += (1.0 / (closestIndex/8.0 + 1)) * percentPerLetter;
		}
		
		return percentMatch;
	}
	
	
	private interface PropertyDocumentListener extends DocumentListener {

		public void onTextChanged(String s);
		
		public default void insertUpdate(DocumentEvent e) {
			try {
				onTextChanged(e.getDocument().getText(0, e.getDocument().getLength()));
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}

		public default void removeUpdate(DocumentEvent e) {
			try {
				onTextChanged(e.getDocument().getText(0, e.getDocument().getLength()));
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}

		public default void changedUpdate(DocumentEvent e) {
			try {
				onTextChanged(e.getDocument().getText(0, e.getDocument().getLength()));
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
		
	}
}
