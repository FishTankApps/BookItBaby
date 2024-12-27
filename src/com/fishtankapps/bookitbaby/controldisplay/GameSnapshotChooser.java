package com.fishtankapps.bookitbaby.controldisplay;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.fishtankapps.bookitbaby.game.GameManager;
import com.fishtankapps.bookitbaby.game.GameSnapshot;
import com.fishtankapps.bookitbaby.gui.ChainGBC;
import com.fishtankapps.bookitbaby.gui.ScrollablePanel;
import com.fishtankapps.bookitbaby.images.ImageManager;

public class GameSnapshotChooser extends JDialog {

	private static final long serialVersionUID = -7413027609323514773L;

	private ArrayList<SnapshotLoadRequestListener> snapshotLoadListeners;
	
	private ScrollablePanel snapshotListPanel;
	private JCheckBox onlyShowHashMatch;
	
	private File[] snapshotFiles;
	private GameManager manager;
	
	public GameSnapshotChooser(File[] snapshotFiles, GameManager manager) {		
		snapshotLoadListeners = new ArrayList<>();
		this.snapshotFiles = snapshotFiles;
		this.manager = manager;	
		
		Arrays.sort(snapshotFiles, (f1, f2) -> {
			return -f1.getName().compareTo(f2.getName());
		});
		
		this.setSize(440, 580);
		this.setTitle("Game Snapshots");
		this.setIconImage(ImageManager.BOOKIT_BABY_LOGO);
		this.setLocationRelativeTo(null);
		
		this.setModalityType(ModalityType.TOOLKIT_MODAL);
		
		this.add(initMainPanel());
	}
	
	public void showDialog() {
		this.setVisible(true);
	}
	
	private JPanel initMainPanel() {
		JPanel mainPanel = new JPanel(new GridBagLayout());

		onlyShowHashMatch = new JCheckBox("Only Show Hash Matches", true);
		onlyShowHashMatch.addActionListener(e -> refreshSnapshotButtons());
		
		JLabel gameHashLabel = new JLabel("Game Hash: " + manager.getGameHash(), JLabel.RIGHT);
		
		
		snapshotListPanel = new ScrollablePanel(new GridBagLayout());
		snapshotListPanel.setBackground(new Color(0xfAfAfA));
		
		JScrollPane questionListPane = new JScrollPane(snapshotListPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		questionListPane.setBorder(BorderFactory.createTitledBorder("Previous Snapshots"));
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> dispose());
		
		
		mainPanel.add(onlyShowHashMatch,          ChainGBC.getInstance(0, 0).setFill(false, false));
		mainPanel.add(gameHashLabel,              ChainGBC.getInstance(1, 0).setFill(false, false).setWidthAndHeight(4, 1));
		mainPanel.add(questionListPane,           ChainGBC.getInstance(0, 1).setFill(true,  true ).setWidthAndHeight(5, 1));
		mainPanel.add(Box.createHorizontalGlue(), ChainGBC.getInstance(0, 2).setFill(true,  false).setWidthAndHeight(4, 1));
		mainPanel.add(cancelButton,               ChainGBC.getInstance(4, 2).setFill(false, false));
		
		
		refreshSnapshotButtons();
		
		return mainPanel;
	}
	
	private void refreshSnapshotButtons() {
		snapshotListPanel.removeAll();		
		
		if(snapshotFiles.length == 0) {
			
			JLabel noSnapshotsLabel = new JLabel("No Snapshots Exist", JLabel.CENTER);
			noSnapshotsLabel.setFont(new Font("", Font.ITALIC, 16));
			snapshotListPanel.add(noSnapshotsLabel, ChainGBC.getInstance(0, 0).setFill(true));
		} else {
			int index = 0;
			
			for(File f : snapshotFiles) {
				int hash = Integer.parseInt(f.getName().split("\\.")[1]);

				if(!onlyShowHashMatch.isSelected() || hash == manager.getGameHash()) {
					GameSnapshotPanel button = new GameSnapshotPanel(f);
					button.addSnapshotLoadRequestListener(snapshot -> onSnapshotLoadRequest(snapshot));
					
					snapshotListPanel.add(button, ChainGBC.getInstance(0, index++).setFill(true, false));
				}				
			}
		}	
		
		
		snapshotListPanel.revalidate();
	}
	
	private void onSnapshotLoadRequest(GameSnapshot snapshot) {
		for(SnapshotLoadRequestListener l : snapshotLoadListeners)
			l.onSnapshotLoadRequest(snapshot);
		
		dispose();
	}
	
	public void addSnapshotLoadRequestListener(SnapshotLoadRequestListener l) {
		snapshotLoadListeners.add(l);
	}
	
	public static interface SnapshotLoadRequestListener {
		public void onSnapshotLoadRequest(GameSnapshot snapshot);
	}
}
