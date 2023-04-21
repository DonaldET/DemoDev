package don.demo.dataservices.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import don.demo.dataservices.ItemStringAcccessor;
import don.demo.dataservices.ItemStringAcccessor.ItemStrings;
import don.demo.dataservices.servicesimpl.ItemStringsAccessorImpl;

public class ItemStringsAccessorTest extends AbstractAccessorTest {
	private ItemStringAcccessor accessor = null;

	class SortByCompoundKey implements Comparator<ItemStrings> {
		public int compare(ItemStrings a, ItemStrings b) {
			int d = (int) (a.itemStrid() - b.itemStrid());
			return (d == 0) ? (int) (a.recordid() - b.recordid()) : d;
		}
	}

	@Before
	public void setUp() throws Exception {
		accessor = new ItemStringsAccessorImpl();
	}

	@After
	public void tearDown() throws Exception {
		accessor = null;
	}

	@Test
	public void testMockDataCheckSum() {
		assertEquals("mock data checksum varies", 1149274328L, checkSum());
	}

	@Test
	public void testItemsByBadRecordId() {
		long itemStrId = 0;
		List<ItemStrings> itemStrings = accessor.getItemStrings(itemStrId);
		assertNotNull("test recordId " + itemStrId + " is null", itemStrings);
		assertEquals(itemStrId + " not empty, is " + itemStrings.size(), 0, itemStrings.size());
	}

	@Test
	public void testItemsByRecordId() {
		long itemStrId = 33;
		List<ItemStrings> itemStrings = accessor.getItemStrings(itemStrId);
		assertNotNull("test recordId " + itemStrId + " is null", itemStrings);
		assertFalse("test date " + itemStrId + " is empty", itemStrings.size() < 1);

		List<ItemStrings> expList = new ArrayList<>();
		expList.add(new ItemStrings(33, 1, "val->[1,A]", "err->[1,A]"));
		expList.add(new ItemStrings(33, 2, "val->[2,B]", "err->[2,B]"));
		expList.add(new ItemStrings(33, 3, "val->[3,C]", "err->[3,C]"));
		expList.add(new ItemStrings(33, 4, "val->[4,D]", "err->[4,D]"));
		expList.add(new ItemStrings(33, 5, "val->[5,E]", "err->[5,E]"));

		assertEquals("result items list size differs from expected items list size:", expList.size(),
				itemStrings.size());

		expList.sort(new SortByCompoundKey());
		itemStrings.sort(new SortByCompoundKey());
		assertEquals("the expected and actual lists, of size " + expList.size() + ", differ:", expList, itemStrings);
	}

	@Test
	public void testItemsByRecordIdFiltered() {
		long itmStrId = 33;
		Predicate<ItemStrings> selectItemStrId = new Predicate<ItemStrings>() {

			@Override
			public boolean test(ItemStrings t) {
				if (t == null) {
					return false;
				}
				return t.recordid() % 2 == 1;
			}

		};

		List<ItemStrings> itemStrings = accessor.getItemStrings(itmStrId, selectItemStrId);
		assertNotNull("test recordId " + itmStrId + " is null", itemStrings);
		assertFalse("test date " + itmStrId + " is empty", itemStrings.size() < 1);

		List<ItemStrings> expList = new ArrayList<>();
		expList.add(new ItemStrings(33, 1, "val->[1,A]", "err->[1,A]"));
		expList.add(new ItemStrings(33, 3, "val->[3,C]", "err->[3,C]"));
		expList.add(new ItemStrings(33, 5, "val->[5,E]", "err->[5,E]"));

		assertEquals("result items list size differs from expected items list size:", expList.size(),
				itemStrings.size());

		expList.sort(new SortByCompoundKey());
		itemStrings.sort(new SortByCompoundKey());
		assertEquals("the expected and actual lists, of size " + expList.size() + ", differ:", expList, itemStrings);
	}
}
