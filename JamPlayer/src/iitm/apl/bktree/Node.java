package iitm.apl.bktree;

import iitm.apl.player.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Node {
	String nodestring;
	HashMap<Integer, Node> children;
	ArrayList<Song> matches;
	private LevenshtienDistance distance;

	public Node() {
		this.children = new HashMap<Integer, Node>();
		this.matches = new ArrayList<Song>();
		distance = new LevenshtienDistance();
	}

	public void AddMatchSong(Song song, String a) {
		this.matches.add(song);
		nodestring = a;
	}

	public void add(Song s, String a) {
		int dist = distance.FindDistance(a, this.nodestring);
		if (dist == 0) {
			this.matches.add(s);
		} else {
			Node child = children.get(dist);
			if (child != null) {
				child.add(s, a);
			} else {
				Node newnode = new Node();
				newnode.AddMatchSong(s, a);
				children.put(dist, newnode);
			}
		}
	}
	// to search for elements with in range specified
	public void search(String a, int range, Vector<Song> songlist) {
		int i;
		Song s;
		int dist = distance.FindDistance(a, this.nodestring);
		if (dist == range) {
			for (i = 0; i < matches.size(); i++) {
				s = matches.get(i);
				if (s.matched > dist) {
					s.matched = dist;
				}
				if (s.matched == 0) {
					songlist.add(s);
					s.matched = dist;
				}
			}
		}

		if (dist < range) {
			for (i = 0; i < matches.size(); i++) {
				s = matches.get(i);
				if (s.matched == 0) {
					songlist.add(s);
					s.matched = 1;
				}
			}
		}

		for (i = dist - range; i <= dist + range; i++) {
			Node child = children.get(i);
			if (child != null) {
				child.search(a, range, songlist);
			}
		}
		/*
		 * for(i = 0;i<songlist.size();i++){ songlist.get(i).matched = 0; }
		 */
	}
}
