package don.demo.queryservices.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import don.demo.dataservices.GroupsAccessor.BusinessDateHolder;
import don.demo.dataservices.GroupsAccessor.SegmentHolder;
import don.demo.dataservices.ItemStringAcccessor.ItemStrings;
import don.demo.queryservices.UIDataRetrieval;
import don.demo.queryservices.UIDataRetrieval.UIDataElement;
import don.demo.queryservices.servicesimpl.StreamDataElementService;

public class ParallelDataElementServiceTest extends AbstractDataElementServiceTest {

	private UIDataRetrieval retriever = null;

	@Before
	public void setUp() throws Exception {
		retriever = new StreamDataElementService(true, 1L);
	}

	@After
	public void tearDown() throws Exception {
		retriever = null;
	}

	@Test
	public void testMockDataCheckSum() {
		assertEquals("mock data checksum varies", 1149274328L, checkSum());
	}

	@Test
	public void testRetrieverByBusDateAndSegmentIdAndEmpty() {
		long testDate = 1L;
		int testSegmnt = 26;
		Predicate<ItemStrings> selectAll = (s) -> true;
		int expSize = 0;
		List<UIDataElement> elements = retriever.getDataElements(new BusinessDateHolder(testDate),
				new SegmentHolder(testSegmnt), selectAll);
		assertEquals("empty element count differs", expSize, elements.size());

		testDate = 328951L;
		testSegmnt = 6;
		Predicate<ItemStrings> selectNone = (s) -> false;
		expSize = 0;
		elements = retriever.getDataElements(new BusinessDateHolder(testDate), new SegmentHolder(testSegmnt),
				selectNone);
		assertEquals("empty element count differs2", expSize, elements.size());
	}

	@Test
	public void testRetrieverByBusDateAndSegmentIdAndUnique() {
		long testDate = 328951L;
		int testSegmnt = 6;
		Predicate<ItemStrings> selectAll = (s) -> true;
		int expSize = 292;
		List<UIDataElement> elements = retriever.getDataElements(new BusinessDateHolder(testDate),
				new SegmentHolder(testSegmnt), selectAll);
		assertEquals("element count differs", expSize, elements.size());

		Set<UIDataElement> testSet = new HashSet<>(elements);
		assertEquals("set size differs", testSet.size(), expSize);
	}

	@Test
	public void testRetrieverByBusDateAndSegmentIdAndFilter() {
		long testDate = 328951L;
		int testSegmnt = 6;
		String errMatch = "[6,F]";
		Predicate<ItemStrings> selectSample = (s) -> s.item_err().contains(errMatch);
		List<UIDataElement> filteredElements = retriever.getDataElements(new BusinessDateHolder(testDate),
				new SegmentHolder(testSegmnt), selectSample);
		assertEquals("filtered element count differs", 30, filteredElements.size());

		for (int i = 0; i < filteredElements.size(); i++) {
			assertTrue("content filter not in filtered element" + i,
					filteredElements.get(i).item_err().contains(errMatch));
		}

		List<UIDataElement> expected = new ArrayList<>();
		expected.add(
				new UIDataElement(179, 6, 121, 466817, 1, 250963, true, 466827, 121, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(179, 6, 1682, 206455, 1, 153, false, 0, 1682, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(179, 6, 1882, 103984, 1, 293726, false, 0, 1882, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(179, 6, 3561, 221961, 1, 111957, false, 0, 3561, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(179, 6, 5331, 258956, 1, 26307, false, 0, 5331, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(179, 6, 5572, 157007, 1, 41612, false, 0, 5572, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(179, 6, 6351, 221768, 1, 393047, false, 0, 6351, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(297, 6, 786, 130633, 1, 65634, false, 0, 786, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(
				new UIDataElement(297, 6, 3329, 295154, 1, 151102, true, 295164, 3329, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(297, 6, 3545, 357326, 1, 428003, false, 0, 3545, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(297, 6, 4944, 131347, 1, 34864, false, 0, 4944, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(469, 6, 1332, 426982, 1, 110091, false, 0, 1332, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(469, 6, 1373, 472892, 1, 257689, false, 0, 1373, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(469, 6, 1863, 373589, 1, 490828, false, 0, 1863, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(469, 6, 2339, 133925, 1, 102749, false, 0, 2339, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(469, 6, 2486, 336158, 1, 464723, false, 0, 2486, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(469, 6, 2656, 261777, 1, 54917, false, 0, 2656, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(469, 6, 2723, 364873, 1, 448689, false, 0, 2723, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(469, 6, 5300, 273315, 1, 195266, false, 0, 5300, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(469, 6, 6770, 341613, 1, 18570, false, 0, 6770, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(486, 6, 74, 231870, 1, 439309, false, 0, 74, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(486, 6, 3569, 244200, 1, 392989, false, 0, 3569, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(486, 6, 3608, 79374, 1, 264630, false, 0, 3608, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(
				new UIDataElement(486, 6, 4767, 431580, 1, 162847, true, 431590, 4767, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(
				new UIDataElement(486, 6, 5386, 279851, 1, 107310, true, 279861, 5386, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(486, 6, 6154, 371499, 1, 19665, false, 0, 6154, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(486, 6, 6598, 294967, 1, 492462, false, 0, 6598, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(597, 6, 909, 334933, 1, 335773, false, 0, 909, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(597, 6, 4771, 304083, 1, 79032, false, 0, 4771, 6, "val->[6,F]", "err->[6,F]"));
		expected.add(new UIDataElement(597, 6, 5232, 453393, 1, 276089, false, 0, 5232, 6, "val->[6,F]", "err->[6,F]"));
		assertEquals("expected result size differs", 30, expected.size(), filteredElements.size());

		assertEquals("selected data element itemid differ",
				expected.stream().mapToLong(s -> s.itemId()).reduce(0L, Long::sum),
				filteredElements.stream().mapToLong(s -> s.itemId()).reduce(0L, Long::sum));

		assertEquals("selected data element itemStrid differ",
				expected.stream().mapToLong(s -> s.itemStrid()).reduce(0L, Long::sum),
				filteredElements.stream().mapToLong(s -> s.itemStrid()).reduce(0L, Long::sum));

		assertEquals("selected data element itmDate differ",
				expected.stream().mapToLong(s -> s.itmDate()).reduce(0L, Long::sum),
				filteredElements.stream().mapToLong(s -> s.itmDate()).reduce(0L, Long::sum));
	}
}