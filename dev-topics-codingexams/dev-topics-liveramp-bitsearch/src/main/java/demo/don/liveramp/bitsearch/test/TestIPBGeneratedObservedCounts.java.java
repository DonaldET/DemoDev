package demo.don.liveramp.bitsearch.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo.don.liveramp.bitsearch.BitSearcher.SubnetPopulationParameters;

class TestIPBGenerateObservedCounts {
	private int failures = 0;
	private final List<SubnetPopulationParameters> params;

	public TestIPBGenerateObservedCounts(final List<SubnetPopulationParameters> params) {
		this.params = params;
	}

	public int runAll() {
		System.out.println("\n**** Testing IPBuilder method generateObservedCounts;");
		final int ntotal = params.stream().mapToInt((x) -> x.count).reduce(0, Integer::sum);

		failures = 0;
		Map<Integer, Integer> population = IPBuilder.generateObservedCounts(params);
		final int npop = population.size();
		if (ntotal != npop) {
			System.err.println("--- expected " + ntotal + " entries but found " + npop);
			failures++;
		} else {
			Map<Integer, Integer> popCounts = getSubPopulationCounts(population);
			int nSubPop = popCounts.entrySet().stream().mapToInt((x) -> x.getValue()).reduce(0, Integer::sum);
			if (ntotal != npop) {
				System.err.println(
						"--- expected " + ntotal + " entries but found " + nSubPop + " sub-population entries");
				failures++;
			}

			for (SubnetPopulationParameters param : params) {
				int testKey = param.mask & param.pattern;
				Integer counts = popCounts.get(testKey);
				if (counts == null | counts != param.count) {
					System.err
							.println("--- expected " + param.count + " entries for key " + Integer.toHexString(testKey)
									+ " but found " + String.valueOf(counts) + " sub-population entries");
					failures++;
				}
			}
		}

		return failures;
	}

	private Map<Integer, Integer> getSubPopulationCounts(Map<Integer, Integer> population) {
		Map<Integer, Integer> popCounts = new HashMap<Integer, Integer>();
		for (SubnetPopulationParameters param : params) {
			popCounts.put(param.mask & param.pattern, 0);
		}

		for (Map.Entry<Integer, Integer> e : population.entrySet()) {
			Integer group = findParamGroup(e.getKey(), params);
			if (group == null) {
				System.err.println("--- population key " + Integer.toHexString(e.getKey()) + " no in " + params);
				failures++;
			} else {
				popCounts.put(group, popCounts.get(group) + 1);
			}
		}

		return popCounts;
	}

	private Integer findParamGroup(int popKey, List<SubnetPopulationParameters> params) {
		Integer group = null;
		for (SubnetPopulationParameters param : params) {
			int candidate = param.mask & param.pattern;
			if ((popKey & param.mask) == candidate) {
				group = candidate;
				break;
			}
		}
		return group;
	}
}