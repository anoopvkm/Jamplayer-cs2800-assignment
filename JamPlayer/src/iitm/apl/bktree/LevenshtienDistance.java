package iitm.apl.bktree;
/* Author: Anoop R Santhosh
 * CLass tofind levingston distance
 */
public class LevenshtienDistance {
	/* Function name: FindDistance
	 * Parameters:string,string
	 * return type:int
	 * Returns the levenshtien distance between two strings
	 */
	public int FindDistance(String string1, String string2) {
		int len1, len2;
		int cost;
		int min;
		int Valabove, Valleft, Valdiag;
		char ch1, ch2;
		len1 = string1.length();
		len2 = string2.length();
		// if either of strings if blank clearly distance is the length of the other
		if (len1 == 0) {
			return len2;
		} else if (len2 == 0) {
			return len1;
		}

		int distmatrix[][] = new int[len1 + 1][len2 + 1];  // to store various intermediate values

		for (int i = 0; i <= len1; i++) {
			distmatrix[i][0] = i;
		}
		for (int i = 0; i <= len2; i++) {
			distmatrix[0][i] = i;
		}
		// Finding out using algorithm
		for (int i = 1; i <= len1; i++) {
			ch1 = string1.charAt(i - 1);

			for (int j = 1; j <= len2; j++) {
				ch2 = string2.charAt(j - 1);

				if (ch1 == ch2) {
					cost = 0;
				} else {
					cost = 1;
				}
				Valabove = distmatrix[i - 1][j] + 1;
				Valleft = distmatrix[i][j - 1] + 1;
				Valdiag = distmatrix[i - 1][j - 1] + cost;

				min = Valabove;
				if (Valleft < min) {
					min = Valleft;
				}
				if (Valdiag < min) {
					min = Valdiag;
				}
				distmatrix[i][j] = min;
			}
		}
		return distmatrix[len1][len2];
	}

}
