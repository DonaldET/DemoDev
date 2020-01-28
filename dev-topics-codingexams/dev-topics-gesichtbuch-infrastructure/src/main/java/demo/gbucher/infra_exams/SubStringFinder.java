package demo.gbucher.infra_exams;

import java.util.HashSet;
import java.util.Set;

//
//Given:
//	string - abacdcdddddddd
//	integer - k (e.g., k = 2) maximum count of unique characters
//
// We form substrings of non-zero length with contiguous characters. We are interested in the
// unique characters in each substring. Examples are:
//  o aba - a,b - 3  
//  o abc - not a substring 
//  o acd - a,c,d can not be a solution because  
//  o cdcd - c,d - 4
//  
// We want to find the length of longest substring such that it has at most k unique chars
//
public class SubStringFinder {

	static int longest(String s, int k) {
		int maxLth = Integer.MIN_VALUE;
		Set<Character> uniqChars = new HashSet<Character>();

		for (int low = 0; low < s.length(); low++) {
			uniqChars.clear();
			int longLth = s.length() - low;

			for (int subLth = 1; subLth <= longLth; subLth++) {
				uniqChars.add(s.charAt(low - 1 + subLth));
				int cunique = uniqChars.size();
				if (cunique <= k) {
					maxLth = Math.max(maxLth, subLth);
				}
			}
		}

		return maxLth;
	}

	public static void main(String[] args) {
		System.out.println("\nLongest Substring\n");

		String s = "a";
		int k = 1;
		int cnt = longest(s, k);
		System.out.println(s + " has " + cnt + " as maximum length for k=" + k);

		s = "abde";
		k = 3;
		cnt = longest(s, k);
		System.out.println(s + " has " + cnt + " as maximum length for k=" + k);

		s = "aaaabacd";
		k = 2;
		cnt = longest(s, k);
		System.out.println(s + " has " + cnt + " as maximum length for k=" + k);

		s = "aaaabacd";
		k = 3;
		cnt = longest(s, k);
		System.out.println(s + " has " + cnt + " as maximum length for k=" + k);

		s = "aaaabacd";
		k = 5;
		cnt = longest(s, k);
		System.out.println(s + " has " + cnt + " as maximum length for k=" + k);

		s = "abacdcdddddddd";
		k = 2;
		cnt = longest(s, k);
		System.out.println(s + " has " + cnt + " as maximum length for k=" + k);
	}
}
