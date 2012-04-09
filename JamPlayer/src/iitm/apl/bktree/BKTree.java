/* Author: Anoop R Santhosh CS10B002
 *  Class implementing BK tree datastructure
 */
package iitm.apl.bktree;


import java.io.BufferedReader;

import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import iitm.apl.player.Song;

public class BKTree {
	private Node root;
	private LevenshtienDistance distance;

	public Vector<Song> songlist;

	public BKTree() {
		songlist = new Vector<Song>();
	}
	/* Function name: add
	 * Parameters:Vector<song>,int
	 * Adds a vector of songs to tree serachkey tells which parameter to use to contruct the tree 
	 */
	public void add(Vector<Song> list, int searchkey) {
		Song s;
		int i = 0;
		while (i < list.size()) {
			s = list.get(i);
			addsong(s, searchkey);
			i++;
		}
		System.out.println(i);
	}

	public void addsong(Song s, int key) {
		String songname;
		if (key == 1) {
			songname = s.getTitle();
		} else if (key == 2) {
			songname = s.getAlbum();
		} else {
			songname = s.getArtist();
		}
		for (int i = 0; i < songname.length(); i++) {
			addsongs(s, songname.substring(0, i));
		}
		/*
		 * StringTokenizer st = new StringTokenizer(songname); while
		 * (st.hasMoreTokens()) { addsongs(s,st.nextToken()); }
		 */
	}
	/* Add a single song */
	public void addsongs(Song s, String a) {
		System.out.println(a);
		if (root == null) {
			root = new Node();
			root.AddMatchSong(s, a);
		} else {
			root.add(s, a);
		}
	}
	/* Function name: Vector<song>
	 * returns the list of songs which comes the range specified
	 */
	public Vector<Song> search(String a, int range) {
		songlist.clear();
		root.search(a, range, songlist);

		Collections.sort(songlist, new Comparator<Song>() {
			public int compare(Song st1, Song st2) {
				return st1.matched - st2.matched;
			}
		});

		for (int i = 0; i < songlist.size(); i++) {
			songlist.get(i).matched = 0;
		}
		return songlist;
	}

}
