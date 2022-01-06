package demo.don.apple.compoundstruct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This is a data structure that supports the following operations in constant
 * time O(1):
 * <ul>
 * <li>void insert(int val)</li>
 * <li>void remove(int val)</li>
 * <li>int getRandom() -> return a random value in the data structure</li>
 * </ul>
 * The data structure will hold integer values. The getRandom method will return
 * a value stored in the data structure such that all values have equally likely
 * probability of being selected with each invocation.
 * <p>
 * Here is an example of the getRandom call:
 * 
 * <pre>
 * insert(2);
 * insert(3);
 * insert(4);
 * System.out.println(getRandom()) -> 2, 3 or 4 are drawn from a uniform
 * distribution
 * </pre>
 * 
 * @author Donald Trummell
 */
public class Struct {
	private Random rand = new Random();
	private Map<Integer, Integer> selector = new HashMap<>();
	private List<Integer> remover = new ArrayList<>();

	public Struct() {
	}

	public int size() {
		int lth = selector.size();
		assert lth == remover.size();
		return lth;
	}

	public void insert(int val) {
		if (selector.containsKey(val)) {
			return;
		}

		remover.add(val);
		selector.put(val, remover.size() - 1);
	}

	public void remove(int val) {
		if (!selector.containsKey(val)) {
			return;
		}

		int index = selector.get(val);
		selector.remove(val);

		int rlth = remover.size();
		Collections.swap(remover, rlth - 1, index);
		remover.remove(rlth - 1);
	}

	public int getRandom() {
		int lth = remover.size();
		return lth > 0 ? remover.get(rand.nextInt(lth)) : Integer.MIN_VALUE;
	}
}
