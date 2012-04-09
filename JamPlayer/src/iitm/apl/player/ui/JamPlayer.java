package iitm.apl.player.ui;

import iitm.apl.bktree.BKTree;
import iitm.apl.player.Song;
import iitm.apl.player.ThreadedPlayer;
import iitm.apl.player.ThreadedPlayer.State;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

/**
 * The JamPlayer Main Class Sets up the UI, and stores a reference to a threaded
 * player that actually plays a song.
 * 
 * TODO: a) Implement the search functionality b) Implement a play-list
 * generation feature
 */
public class JamPlayer {

	// UI Items
	private JFrame mainFrame;
	private PlayerPanel pPanel;

	private JTable libraryTable;
	private LibraryTableModel libraryModel;

	private Thread playerThread = null;
	private ThreadedPlayer player = null;

	private BKTree title = new BKTree();
	private BKTree album = new BKTree();
	private BKTree artist = new BKTree();

	ImageIcon equi = new ImageIcon("eq1.gif");
	JLabel imag = new JLabel(equi);

	int count = 0;
	char str[] = new char[100];

	public JamPlayer() {
		// Create the player
		player = new ThreadedPlayer();
		playerThread = new Thread(player);
		playerThread.start();
	}

	/**
	 * Create a file dialog to choose MP3 files to add
	 */
	private Vector<Song> addFileDialog() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal != JFileChooser.APPROVE_OPTION)
			return null;

		File selectedFile = chooser.getSelectedFile();
		// Read files as songs
		Vector<Song> songs = new Vector<Song>();
		if (selectedFile.isFile()
				&& selectedFile.getName().toLowerCase().endsWith(".mp3")) {
			songs.add(new Song(selectedFile));
			return songs;
		} else if (selectedFile.isDirectory()) {
			for (File file : selectedFile.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".mp3");
				}
			}))
				songs.add(new Song(file));
		}

		return songs;
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private void createAndShowGUI() {
		// Create and set up the window.
		mainFrame = new JFrame("JamPlayer");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setMinimumSize(new Dimension(1000, 600));

		// Create and set up the content pane.
		Container pane = mainFrame.getContentPane();
		pane.setBackground(Color.black);
		// pane.add(imag,BorderLayout.BEFORE_LINE_BEGINS);
		pane.add(createMenuBar(), BorderLayout.NORTH);
		pane.add(imag, BorderLayout.LINE_END);
		pane.add(Box.createHorizontalStrut(30), BorderLayout.EAST);
		pane.add(Box.createHorizontalStrut(30), BorderLayout.WEST);
		pane.add(Box.createVerticalStrut(30), BorderLayout.SOUTH);

		JPanel mainPanel = new JPanel();
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		mainPanel.setBackground(Color.DARK_GRAY);

		String[] keystring = { "Title", "Artist", "Album" };
		final JComboBox key = new JComboBox(keystring);
		key.setSelectedIndex(1);

		pPanel = new PlayerPanel(player);
		pPanel.setBackground(Color.black);
		JLabel searchLabel = new JLabel("Search: ");
		searchLabel.setFont(new Font("Courier New", Font.BOLD, 12));
		searchLabel.setBackground(Color.blue);
		final JTextField searchText = new JTextField(200);
		searchText.setBackground(Color.orange);
		searchText.setMaximumSize(new Dimension(200, 20));
		searchText.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				char c = arg0.getKeyChar();
				if (c == '\b' && count > 0) {
					count--;
				} else {
					str[count] = c;
					count++;
				}
				String s = tostring(str, count);
				Vector<Song> match;
				int range = 1;
				do {
					range++;

					if (key.getSelectedIndex() == 0) {
						match = title.search(s, range);
					} else if (key.getSelectedIndex() == 1) {
						match = artist.search(s, range);
					} else {
						match = album.search(s, range);
					}
				} while (match.size() == 0);
				System.out.println(match.size());
				libraryModel.deletelist();
				libraryModel.add(match);
				libraryModel.resetIdx();
				libraryModel.fireTableDataChanged();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});

		libraryModel = new LibraryTableModel();
		libraryTable = new JTable(libraryModel);
		libraryTable.setBackground(Color.black);
		libraryTable.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() > 1) {
					Song song = libraryModel.get(libraryTable.getSelectedRow());
					if (song != null) {
						player.setSong(song);
						pPanel.setSong(song);
					}
				}
			}
		});

		JButton shuffleButton = new JButton("Shuffle Playlist");
		shuffleButton.setBackground(Color.gray);
		shuffleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				libraryModel.shuffle();
			}
		});

		JButton mostButton = new JButton("Most Played");
		mostButton.setBackground(Color.gray);
		mostButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				libraryModel.MostPlayed();
			}
		});

		libraryTable
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane libraryPane = new JScrollPane(libraryTable);

		layout.setHorizontalGroup(layout
				.createParallelGroup(Alignment.CENTER)
				.addComponent(pPanel)
				.addGroup(
						layout.createSequentialGroup().addContainerGap()
								.addGap(20).addComponent(searchLabel)
								.addComponent(searchText).addGap(50)
								.addComponent(key).addComponent(shuffleButton)
								.addComponent(mostButton).addContainerGap())
				.addComponent(libraryPane));

		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addComponent(pPanel)
				.addContainerGap()
				.addGroup(
						layout.createParallelGroup(Alignment.CENTER)
								.addComponent(searchLabel)
								.addComponent(searchText).addComponent(key)
								.addComponent(shuffleButton)
								.addComponent(mostButton))

				.addComponent(libraryPane));

		pane.add(mainPanel, BorderLayout.CENTER);

		// Display the window.
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	public String tostring(char[] str, int count) {
		String s = "";
		for (int i = 0; i < count; i++) {
			s = s + str[i];
		}
		return s;
	}

	private JMenuBar createMenuBar() {
		JMenuBar mbar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem addSongs = new JMenuItem("Add new files to library");
		addSongs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Vector<Song> songs = addFileDialog();
				if (songs != null)
					libraryModel.add(songs);
				title.add(songs, 1);

				album.add(songs, 2);

				artist.add(songs, 3);
			}
		});
		file.add(addSongs);

		JMenuItem createPlaylist = new JMenuItem("Create playlist");
		createPlaylist.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createPlayListHandler();
			}
		});
		file.add(createPlaylist);

		JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
			}
		});
		file.add(quitItem);

		mbar.add(file);

		return mbar;
	}

	protected void createPlayListHandler() {
		// TODO: Create a dialog window allowing the user to choose length of
		// play list, and a play list you create that best fits the time
		// specified
		PlayListMakerDialog dialog = new PlayListMakerDialog(this);
		dialog.setVisible(true);
	}

	public Vector<Song> getSongList() {
		Vector<Song> songs = new Vector<Song>();
		for (int i = 0; i < libraryModel.getRowCount(); i++)
			songs.add(libraryModel.get(i));
		return songs;
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JamPlayer player = new JamPlayer();
				player.createAndShowGUI();
			}
		});
	}
}
