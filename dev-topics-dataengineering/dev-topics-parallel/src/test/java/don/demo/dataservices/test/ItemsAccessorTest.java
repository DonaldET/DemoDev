package don.demo.dataservices.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import don.demo.dataservices.ItemsAccessor;
import don.demo.dataservices.ItemsAccessor.GroupIdHolder;
import don.demo.dataservices.ItemsAccessor.Items;
import don.demo.dataservices.servicesimpl.ItemsAccessorImpl;

public class ItemsAccessorTest extends AbstractAccessorTest {
	private ItemsAccessor accessor = null;

	class SortByItemId implements Comparator<Items> {
		public int compare(Items a, Items b) {
			return (int) (a.itemId() - b.itemId());
		}
	}

	@Before
	public void setUp() throws Exception {
		accessor = new ItemsAccessorImpl();
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
	public void testItemsByGroupIdSmall() {
		GroupIdHolder testGroupId = new GroupIdHolder(0);
		List<Items> items = accessor.getItems(testGroupId);
		assertNotNull("test groupId " + testGroupId + " is null", items);
		assertEquals(testGroupId + " not empty, is " + items.size(), 0, items.size());
	}

	@Test
	public void testItemsByGroupId() {
		GroupIdHolder testGroupId = new GroupIdHolder(73);
		List<Items> items = accessor.getItems(testGroupId);
		assertNotNull("test groupId " + testGroupId + " is null", items);
		assertFalse("test date " + testGroupId + " is empty", items.size() < 1);

		List<Items> expList = new ArrayList<>();
		expList.add(new Items(1318, 73, 373260, 1, 497525, false, 0));
		expList.add(new Items(3143, 73, 394532, 1, 453370, false, 0));
		expList.add(new Items(3918, 73, 377752, 1, 126502, true, 377762));
		expList.add(new Items(3954, 73, 118427, 1, 481411, false, 0));
		expList.add(new Items(4048, 73, 422618, 1, 463216, true, 422628));
		expList.add(new Items(4518, 73, 141572, 1, 372397, false, 0));
		expList.add(new Items(5298, 73, 265222, 1, 426601, false, 0));
		expList.add(new Items(5468, 73, 422514, 1, 363545, true, 422524));
		expList.add(new Items(6631, 73, 316242, 1, 76045, true, 316252));

		assertEquals("result items list size differs from expected items list size:", expList.size(), items.size());

		expList.sort(new SortByItemId());
		items.sort(new SortByItemId());
		assertEquals("the expected and actual lists, of size " + expList.size() + ", differ:", expList, items);
	}
}
