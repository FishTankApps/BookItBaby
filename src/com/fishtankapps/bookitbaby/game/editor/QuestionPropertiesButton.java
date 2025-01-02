package com.fishtankapps.bookitbaby.game.editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.fishtankapps.bookitbaby.game.Question.QuestionType;
import com.fishtankapps.bookitbaby.gui.ChainGBC;

public class QuestionPropertiesButton extends JPanel {

	private static final long serialVersionUID = 1121027296323261306L;
	
	private static final Border UNSELECTED_BORDER = BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(0xD0D0D0)),
			BorderFactory.createEmptyBorder(5, 5, 5, 5));
	
	private static final Border SELECTED_BORDER = BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(0x48c0f9)), 
			BorderFactory.createEmptyBorder(5, 5, 5, 5));
	
	private static final Color HOVER_COLOR_VALID =     new Color(0xFFFFFF);
	private static final Color NOT_HOVER_COLOR_VALID = new Color(0xF0F0F0);
	
	private static final Color HOVER_COLOR_INVALID =     new Color(0xFFE0E0);
	private static final Color NOT_HOVER_COLOR_INVALID = new Color(0xF0E0E0);
	
	private JLabel titleLabel, promptLabel;
	
	private OnClickListener listener;
	private QuestionType type;
	private int qpIndex;
	private boolean isQuestionValid = false;
	
	public QuestionPropertiesButton(int qpIndex, QuestionType type, String prompt) {
		super(new GridBagLayout());
		
		this.type = type;
		this.qpIndex = qpIndex;
		
		listener = null;
		
		setBorder(UNSELECTED_BORDER);
		setBackground(NOT_HOVER_COLOR_INVALID);
		
		titleLabel = new JLabel("Question #" + (qpIndex + 1) + " - " + type.toString(), JLabel.LEFT);
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		promptLabel = new JLabel(prompt, JLabel.LEFT);
		
		add(titleLabel,  ChainGBC.getInstance(0, 0).setFill(true, false).setPadding(2, 2, 0, 2));
		add(promptLabel, ChainGBC.getInstance(0, 1).setFill(true, false).setPadding(2, 2, 0, 2));
		
		this.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				if(listener != null)
					listener.onClick();
			}

			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {
				setBackground(isQuestionValid ? HOVER_COLOR_VALID : HOVER_COLOR_INVALID);
			}
			public void mouseExited(MouseEvent e) {
				setBackground(isQuestionValid ? NOT_HOVER_COLOR_VALID : NOT_HOVER_COLOR_INVALID);
			}			
		});
	}
	
	public void updateIndex(int qpIndex) {
		this.qpIndex = qpIndex;
		titleLabel.setText("Question #" + (qpIndex + 1) + " - " + type.toString());
	}
	public void updateType(QuestionType type) {
		this.type = type;
		titleLabel.setText("Question #" + (qpIndex + 1) + " - " + type.toString());
	}
	public void updatePrompt(String prompt) {
		promptLabel.setText(prompt);
	}
	public void updateQuestionValidity(boolean validity) {
		this.isQuestionValid = validity;
		setBackground(isQuestionValid ? NOT_HOVER_COLOR_VALID : NOT_HOVER_COLOR_INVALID);
	}
	
	public void setIsSelected(boolean isSelected) {
		setBorder((isSelected) ? SELECTED_BORDER : UNSELECTED_BORDER);
	}
	
	public void setOnClickListener(OnClickListener listener) {
		this.listener = listener;
	}
	

	public static interface OnClickListener {
		public void onClick();
	}
}
