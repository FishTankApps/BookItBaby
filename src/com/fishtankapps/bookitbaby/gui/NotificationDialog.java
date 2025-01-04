package com.fishtankapps.bookitbaby.gui;

import java.awt.Image;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class NotificationDialog {

	private JDialog dialog;
	
	public NotificationDialog(String title, String message, Image iconImage) {		
		JOptionPane pane = new JOptionPane("", JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_OPTION, null,
				new Object[] {"Cancel"});
		
		pane.setMessage(message);
		
		dialog = pane.createDialog(title);
		dialog.setIconImage(iconImage);
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);

		new Thread(() -> dialog.setVisible(true)).start();
	}
	
	public void close() {
		dialog.dispose();
	}
}