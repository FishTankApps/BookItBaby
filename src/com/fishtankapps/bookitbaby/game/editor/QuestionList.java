package com.fishtankapps.bookitbaby.game.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;

import com.fishtankapps.bookitbaby.game.editor.QuestionProperties.Property;
import com.fishtankapps.bookitbaby.gui.ChainGBC;
import com.fishtankapps.bookitbaby.gui.ScrollablePanel;

public class QuestionList extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3668227733120930223L;

	private JPanel questionListPanel;
	private GameEditor gameEditor;
	
	private QuestionLabel selectedLabel = null;
	private int selectedLabelIndex = -1;
	
	
	public QuestionList(GameEditor gameEditor) {
		super(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Question List"));
		
		this.gameEditor = gameEditor;
		
		setMinimumSize(new Dimension(250, 250));
		setPreferredSize(new Dimension(250, 250));		
		
		add(initQuestionPane(), ChainGBC.getInstance(0, 0).setFill(false, true).setPadding(2));
		add(initToolBarPanel(), ChainGBC.getInstance(0, 1).setFill(true, false).setPadding(0));
		
		gameEditor.addQuestionPropertiesListChangeListener(this::updateQuestionList);
		gameEditor.addQuestionPropertiesChangeListener((qp, index, p) -> {
						if(p == Property.PROMPT) this.updateQuestionList();	});
	}
	
	private JPanel initToolBarPanel() {
		JPanel toolBarPanel = new JPanel(new GridBagLayout());
		
		JButton addQuestion = new JButton("+");
		JButton removeQuestion = new JButton("-");
		
		toolBarPanel.add(addQuestion, ChainGBC.getInstance(0, 0).setFill(false).setPadding(0));
		toolBarPanel.add(Box.createHorizontalGlue(), ChainGBC.getInstance(1, 0).setFill(true, false).setPadding(0));
		toolBarPanel.add(removeQuestion, ChainGBC.getInstance(2, 0).setFill(false).setPadding(0));
				
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
		
		for(String prompt : gameEditor.getQuestionPrompts()) {
			final int thisIndex = index;
			String text = "Question #" + (index + 1) + ": " + prompt;
			QuestionLabel label = new QuestionLabel(text);
			label.setActionListener((e) -> {
				
				if(selectedLabel != null)
					selectedLabel.setSelected(false);
				
				label.setSelected(true);
				selectedLabel = label;
				
				gameEditor.selectQuestionProperties(thisIndex);
				selectedLabelIndex = thisIndex;
			});
			
			if(index == selectedLabelIndex) {
				label.setSelected(true);
				selectedLabel = label;
			}
			
			questionListPanel.add(label, ChainGBC.getInstance(0, index++).setFill(true, false).setPadding(3, 3, 5, 5));
		}
		
		questionListPanel.add(Box.createVerticalGlue(), ChainGBC.getInstance(0, index).setFill(true));
		questionListPanel.revalidate();
		questionListPanel.repaint();
	}


	private static class QuestionLabel extends JLabel {
		
		private static final long serialVersionUID = -6158636148955612867L;

		private static final CompoundBorder UNSELECTED_BORDER = BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(0xD0D0D0)), 
				BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		private static final CompoundBorder SELECTED_BORDER = BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(0x48c0f9)), 
				BorderFactory.createEmptyBorder(5, 5, 5, 5));

		private ActionListener l;
		
		public QuestionLabel(String text) {
			super(text, JLabel.LEFT);
			setFocusable(true);
			setBorder(UNSELECTED_BORDER);
			
			addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(l != null)
						l.actionPerformed(null);
				}

				public void mousePressed(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}				
			});
		}
		
		public void setActionListener(ActionListener l) {
			this.l = l;
		}
		
		public void setSelected(boolean selected) {
			setBorder((selected) ? SELECTED_BORDER : UNSELECTED_BORDER);
		}

	}
}
