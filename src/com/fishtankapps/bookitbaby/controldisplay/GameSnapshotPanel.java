package com.fishtankapps.bookitbaby.controldisplay;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.fishtankapps.bookitbaby.controldisplay.GameSnapshotChooser.SnapshotLoadRequestListener;
import com.fishtankapps.bookitbaby.game.GameSnapshot;
import com.fishtankapps.bookitbaby.gui.ChainGBC;

public class GameSnapshotPanel extends JPanel {

	private static final long serialVersionUID = 531172157039187790L;
	
	private ArrayList<SnapshotLoadRequestListener> snapshotLoadListeners;
	
	private long creationTime;	
	private GameSnapshot snapshot;
	private JButton load;
	
	public GameSnapshotPanel(File snapshotFile) {
		super(new GridBagLayout());
		
		snapshotLoadListeners = new ArrayList<>();
		
		String[] fileName = snapshotFile.getName().split("\\.");		
		creationTime = Long.parseLong(fileName[0]);
		snapshot = GameSnapshot.openSnapshot(snapshotFile);		
		
		initGUI();
	}
	
	private void initGUI() {
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
		String creationDate = fmt.format(new Date(creationTime));		 
		
		int questionsAsked = 0;
		for(boolean b : snapshot.getQuestionUsed())
			if(b) questionsAsked++;

		JLabel creationDateLabel = new JLabel(creationDate);
		JLabel hashLabel = new JLabel("Game Hash: " + snapshot.getGameHash());
		JLabel redTeamLabel = new JLabel("Red Team: '" + snapshot.getRedTeamName() + "' (" + snapshot.getRedTeamScore() + " points)");
		JLabel blueTeamLabel = new JLabel("Blue Team: '" + snapshot.getBlueTeamName() + "' (" + snapshot.getBlueTeamScore() + " points)");
		JLabel questionsAnswered = new JLabel(questionsAsked + " of " + snapshot.getQuestionUsed().length + " questions answered");
		
		load = new JButton("Load");
		load.addActionListener(e -> {
			
			int choice = JOptionPane.showConfirmDialog(this, "Loading this snapshot will override the current game.\nAre you sure you want to continue?", "Are you sure?", JOptionPane.YES_NO_OPTION);
			
			if(choice == JOptionPane.YES_OPTION)
				for(SnapshotLoadRequestListener l : snapshotLoadListeners)
					l.onSnapshotLoadRequest(snapshot);
		});
		
		Font bold = creationDateLabel.getFont().deriveFont(Font.BOLD);
		creationDateLabel.setFont(bold);
		questionsAnswered.setFont(bold);
		
		add(creationDateLabel, ChainGBC.getInstance(0, 0).setFill(true, false));
		add(hashLabel,         ChainGBC.getInstance(1, 0).setFill(false, false).setWidthAndHeight(2, 1));
		add(redTeamLabel,      ChainGBC.getInstance(0, 1).setFill(true, false).setWidthAndHeight(3, 1));
		add(blueTeamLabel,     ChainGBC.getInstance(0, 2).setFill(true, false).setWidthAndHeight(3, 1));
		add(questionsAnswered, ChainGBC.getInstance(0, 3).setFill(true, false).setWidthAndHeight(2, 1));
		add(load,              ChainGBC.getInstance(2, 3).setFill(false, false));
	}
	
	public void addSnapshotLoadRequestListener(SnapshotLoadRequestListener l) {
		snapshotLoadListeners.add(l);
	}
}
