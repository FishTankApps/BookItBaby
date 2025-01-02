package com.fishtankapps.bookitbaby.game.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import com.fishtankapps.bookitbaby.gui.ChainGBC;
import com.fishtankapps.bookitbaby.gui.ScrollablePanel;

public class QuestionList extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3668227733120930223L;

	private JPanel questionListPanel;
	private GameEditor gameEditor;
	
	private QuestionPropertiesButton selectedButton = null;	
	
	public QuestionList(GameEditor gameEditor) {
		super(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Question List"));
		
		this.gameEditor = gameEditor;
		
		setMinimumSize(new Dimension(250, 250));
		setPreferredSize(new Dimension(250, 250));		
		
		add(initQuestionPane(), ChainGBC.getInstance(0, 0).setFill(false, true).setPadding(2));
		add(initToolBarPanel(), ChainGBC.getInstance(0, 1).setFill(true, false).setPadding(0));
		
		gameEditor.addQuestionPropertiesListChangeListener(this::updateQuestionList);
	}
	
	private JPanel initToolBarPanel() {
		JPanel toolBarPanel = new JPanel(new GridBagLayout());
		
		JButton addQuestion = new JButton("+");
		
		addQuestion.addActionListener((e) -> gameEditor.addNewQuestionPropertiesAfterSelected());
		
		JButton removeQuestion = new JButton("-");
		removeQuestion.addActionListener((e) -> gameEditor.removeSelectedQuestionProperties());
		
		JButton shuffleQuestions = new JButton("Shuffle Questions");
		shuffleQuestions.addActionListener(e -> gameEditor.shuffleQuestions());
		
		toolBarPanel.add(addQuestion,      ChainGBC.getInstance(0, 0).setFill(false).setPadding(0));
		toolBarPanel.add(Box.createHorizontalGlue(), ChainGBC.getInstance(1, 0).setFill(true, false).setPadding(0));
		toolBarPanel.add(shuffleQuestions, ChainGBC.getInstance(2, 0).setFill(false).setPadding(0));
		toolBarPanel.add(Box.createHorizontalGlue(), ChainGBC.getInstance(3, 0).setFill(true, false).setPadding(0));
		toolBarPanel.add(removeQuestion,    ChainGBC.getInstance(4, 0).setFill(false).setPadding(0));
				
		return toolBarPanel;
	}
	
	private JScrollPane initQuestionPane() {

		questionListPanel = new ScrollablePanel(new GridBagLayout());
		
		JScrollPane questionListPane = new JScrollPane(questionListPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		questionListPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		questionListPanel.setBackground(new Color(0xfAfAfA));
		
		updateQuestionList();
		
		return questionListPane;
	}
	
	public void updateQuestionList() {
		
		questionListPanel.removeAll();
		int index = 0;
		
		if(gameEditor == null)
			return;
		
		for(QuestionPropertiesButton button : gameEditor.getQuestionPropertiesButtons()) {
			final int finalIndex = index;
			button.updateIndex(finalIndex);
			button.setOnClickListener(() -> {
				if(selectedButton != null) selectedButton.setIsSelected(false);
				(selectedButton = button).setIsSelected(true);
				gameEditor.selectQuestionProperties(finalIndex);
			});
			
			questionListPanel.add(button, ChainGBC.getInstance(0, index++).setFill(true, false).setPadding(3, 3, 5, 5));
		}
		
		questionListPanel.add(Box.createVerticalGlue(), ChainGBC.getInstance(0, index).setFill(true));
		questionListPanel.revalidate();
		questionListPanel.repaint();
	}
}
