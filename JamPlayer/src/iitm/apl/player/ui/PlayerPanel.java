package iitm.apl.player.ui;

import iitm.apl.player.Song;
import iitm.apl.player.ThreadedPlayer;
import iitm.apl.player.ThreadedPlayer.State;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * PlayerPanel Contains player controls, and raises appropriate events
 * 
 * @author Arun Tejasvi Chaganty <arunchaganty@gmail.com>
 * 
 */
public class PlayerPanel extends JPanel {
	private static final long serialVersionUID = -5264313656161958408L;

	private JLabel songLabel;
	private Song currentSong;

	private ThreadedPlayer player;
	ImageIcon prev = new ImageIcon("prev.png");
	ImageIcon play = new ImageIcon("play.jpg");
	ImageIcon stop = new ImageIcon("stop.png");
	ImageIcon next = new ImageIcon("next.png");

	public PlayerPanel(ThreadedPlayer player_) {
		// Call the parent constructor
		super();
		this.player = player_;

		// Set layout manager
		setLayout(new FlowLayout(FlowLayout.CENTER));

		songLabel = new JLabel("");
		songLabel.setMinimumSize(new Dimension(60, 30));

		// Add buttons
		JButton prevButton = new JButton("Prev");
		prevButton.setIcon(prev);
		prevButton.setBackground(Color.black);
		prevButton.setEnabled(false);
		prevButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		add(prevButton);

		JButton playPauseButton = new JButton("Play/Pause");
		playPauseButton.setIcon(play);
		playPauseButton.setBackground(Color.black);
		playPauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (player.getState() == State.PLAY)
					player.setState(State.PAUSE);
				else if (player.getState() == State.PAUSE)
					player.setState(State.PLAY);
			}
		});
		add(playPauseButton);

		JButton stopButton = new JButton("Stop");
		stopButton.setIcon(stop);
		stopButton.setBackground(Color.black);
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.setState(State.STOP);
			}
		});
		add(stopButton);

		JButton nextButton = new JButton("Next");
		nextButton.setIcon(next);
		nextButton.setBackground(Color.black);
		nextButton.setEnabled(false);
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		add(nextButton);
	}

	public void setSong(Song song) {
		currentSong = song;
		String lbl = currentSong.toString();
		songLabel.setText(lbl);
	}

}
