package com.fishtankapps.bookitbaby.game.editor;

import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fishtankapps.bookitbaby.game.Question.QuestionType;
import com.fishtankapps.bookitbaby.gui.ChainGBC;

public class QuestionEditor extends JPanel {

	private static final long serialVersionUID = 1829346933966990243L;

	private GameEditor gameEditor;
	
	public QuestionEditor(GameEditor gameEditor) {
		super(new GridBagLayout());
		
		this.gameEditor = gameEditor;

		new JPanel(new GridBagLayout());
		gameEditor.addSelectedQuestionPropertiesListener((qp) -> updateGUI());
		
		updateGUI();
	}
	
	private void updateGUI() {
		this.removeAll();
		QuestionProperties qp = gameEditor.getSelectedQuestionProperties();
		
		if(qp == null)
			return;
		
		JLabel label = new JLabel("Question #" + (gameEditor.getSelectedQuestionPropertiesIndex() + 1));
		label.setFont(getFont().deriveFont(16f));
		
		JComboBox<QuestionType> questionType = new JComboBox<>(QuestionType.values());
		
		questionType.setSelectedItem(qp.getQuestionType());	
		questionType.addItemListener(this::onQuestionTypeChanged);
		
		JCheckBox isChristInContextCheckbox = new JCheckBox("Is Christ in Context Question");
		isChristInContextCheckbox.setSelected(qp.isChristInContextQuestion());
		isChristInContextCheckbox.addActionListener(e -> qp.setIsChristInContextQuestion(isChristInContextCheckbox.isSelected()));
		
		add(label, ChainGBC.getInstance(0, 0).setFill(false).setWidthAndHeight(2, 1));
		add(new JLabel("Question Type:"), ChainGBC.getInstance(0, 1).setFill(false));
		add(questionType,                 ChainGBC.getInstance(1, 1).setFill(false));
		add(isChristInContextCheckbox,    ChainGBC.getInstance(2, 1).setFill(false));
		add(Box.createHorizontalGlue(),   ChainGBC.getInstance(3, 1).setFill(true, false));
		
		add(new QuestionPropertiesEditor(qp, gameEditor), ChainGBC.getInstance(0, 2).setFill(true, false).setWidthAndHeight(4, 1));
		this.revalidate();
	}
		
	private void onQuestionTypeChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED) {
			QuestionProperties qp = gameEditor.getSelectedQuestionProperties();
			qp.setQuestionType((QuestionType) e.getItem());
		}
	}
}