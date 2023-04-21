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

import don.demo.dataservices.GroupsAccessor.BusinessDateHolder;
import don.demo.dataservices.GroupsAccessor.GroupIdentification;
import don.demo.dataservices.GroupsAccessor.SegmentHolder;
import don.demo.dataservices.ItemStringAcccessor.ItemStrings;
import don.demo.dataservices.servicesimpl.GroupsAccessorImpl;

public class RetrievalServicesTest extends AbstractAccessorTest {
	class SortByGroup implements Comparator<GroupIdentification> {

		public int compare(GroupIdentification a, GroupIdentification b) {
			int d = a.groupId() - b.groupId();
			if (d == 0) {
				d = a.segmentId() - b.segmentId();
			}
			return d;
		}
	}

	class SortByCompoundKey implements Comparator<ItemStrings> {
		public int compare(ItemStrings a, ItemStrings b) {
			int d = (int) (a.itemStrid() - b.itemStrid());
			return (d == 0) ? (int) (a.recordid() - b.recordid()) : d;
		}
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMockDataCheckSum() {
		assertEquals("mock data checksum varies", 1149274328L, checkSum());
	}

	@Test
	public void testGroupsByBusDateAndSegment() {
		long testDate = 328951L;
		int testSegmnt = 6;

		List<GroupIdentification> groups = new GroupsAccessorImpl().getGroups(new BusinessDateHolder(testDate),
				new SegmentHolder(testSegmnt));
		assertNotNull("test segment " + testSegmnt + " is null", groups);
		assertFalse("test segment " + testSegmnt + " is empty", groups.size() < 1);

		List<GroupIdentification> expList = new ArrayList<>();
		expList.add(new GroupIdentification(179, 6));
		expList.add(new GroupIdentification(297, 6));
		expList.add(new GroupIdentification(469, 6));
		expList.add(new GroupIdentification(486, 6));
		expList.add(new GroupIdentification(597, 6));

		assertEquals("result group list size differs from expected group list size:", expList.size(), groups.size());

		expList.sort(new SortByGroup());
		List<GroupIdentification> actList = new ArrayList<>(groups);
		actList.sort(new SortByGroup());
		assertEquals("the expected and actual lists, of size " + expList.size() + ", differ:", expList, actList);
	}
}
