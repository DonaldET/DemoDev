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

import don.demo.dataservices.GroupsAccessor;
import don.demo.dataservices.GroupsAccessor.BusinessDateHolder;
import don.demo.dataservices.GroupsAccessor.GroupIdentification;
import don.demo.dataservices.GroupsAccessor.SegmentHolder;
import don.demo.dataservices.servicesimpl.GroupsAccessorImpl;

public class GroupAccessorTest extends AbstractAccessorTest {
	private GroupsAccessor accessor = null;

	class SortByGroup implements Comparator<GroupIdentification> {

		public int compare(GroupIdentification a, GroupIdentification b) {
			int d = a.groupId() - b.groupId();
			if (d == 0) {
				d = a.segmentId() - b.segmentId();
			}
			return d;
		}
	}

	@Before
	public void setUp() throws Exception {
		accessor = new GroupsAccessorImpl();
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
	public void testGroupsByBusDateTooSmall() {
		long testDate = 1L;
		List<GroupIdentification> groups = accessor.getGroups(new BusinessDateHolder(testDate));
		assertNotNull("test date " + testDate + " is null", groups);
		assertEquals(testDate + " not empty, is " + groups.size(), 0, groups.size());
	}

	@Test
	public void testGroupsByBusDate() {
		long testDate = 328951L;
		List<GroupIdentification> groups = accessor.getGroups(new BusinessDateHolder(testDate));
		assertNotNull("test date " + testDate + " is null", groups);
		assertFalse("test date " + testDate + " is empty", groups.size() < 1);

		List<GroupIdentification> expList = new ArrayList<>();
		expList.add(new GroupIdentification(2, 2));
		expList.add(new GroupIdentification(13, 1));
		expList.add(new GroupIdentification(21, 1));
		expList.add(new GroupIdentification(22, 2));
		expList.add(new GroupIdentification(42, 3));
		expList.add(new GroupIdentification(53, 4));
		expList.add(new GroupIdentification(80, 1));
		expList.add(new GroupIdentification(87, 4));
		expList.add(new GroupIdentification(101, 5));
		expList.add(new GroupIdentification(106, 5));
		expList.add(new GroupIdentification(145, 2));
		expList.add(new GroupIdentification(166, 2));
		expList.add(new GroupIdentification(167, 1));
		expList.add(new GroupIdentification(179, 6));
		expList.add(new GroupIdentification(180, 1));
		expList.add(new GroupIdentification(188, 1));
		expList.add(new GroupIdentification(189, 2));
		expList.add(new GroupIdentification(190, 3));
		expList.add(new GroupIdentification(191, 4));
		expList.add(new GroupIdentification(195, 1));
		expList.add(new GroupIdentification(211, 5));
		expList.add(new GroupIdentification(224, 2));
		expList.add(new GroupIdentification(235, 1));
		expList.add(new GroupIdentification(240, 2));
		expList.add(new GroupIdentification(248, 2));
		expList.add(new GroupIdentification(249, 3));
		expList.add(new GroupIdentification(253, 3));
		expList.add(new GroupIdentification(266, 4));
		expList.add(new GroupIdentification(270, 3));
		expList.add(new GroupIdentification(281, 1));
		expList.add(new GroupIdentification(297, 6));
		expList.add(new GroupIdentification(300, 3));
		expList.add(new GroupIdentification(312, 3));
		expList.add(new GroupIdentification(332, 4));
		expList.add(new GroupIdentification(339, 3));
		expList.add(new GroupIdentification(340, 1));
		expList.add(new GroupIdentification(354, 1));
		expList.add(new GroupIdentification(364, 1));
		expList.add(new GroupIdentification(366, 3));
		expList.add(new GroupIdentification(376, 3));
		expList.add(new GroupIdentification(385, 3));
		expList.add(new GroupIdentification(415, 5));
		expList.add(new GroupIdentification(416, 1));
		expList.add(new GroupIdentification(427, 1));
		expList.add(new GroupIdentification(428, 2));
		expList.add(new GroupIdentification(429, 3));
		expList.add(new GroupIdentification(434, 5));
		expList.add(new GroupIdentification(437, 2));
		expList.add(new GroupIdentification(446, 1));
		expList.add(new GroupIdentification(455, 3));
		expList.add(new GroupIdentification(456, 4));
		expList.add(new GroupIdentification(469, 6));
		expList.add(new GroupIdentification(473, 3));
		expList.add(new GroupIdentification(477, 2));
		expList.add(new GroupIdentification(486, 6));
		expList.add(new GroupIdentification(495, 3));
		expList.add(new GroupIdentification(504, 3));
		expList.add(new GroupIdentification(509, 4));
		expList.add(new GroupIdentification(510, 5));
		expList.add(new GroupIdentification(519, 1));
		expList.add(new GroupIdentification(532, 3));
		expList.add(new GroupIdentification(539, 1));
		expList.add(new GroupIdentification(542, 1));
		expList.add(new GroupIdentification(544, 3));
		expList.add(new GroupIdentification(553, 5));
		expList.add(new GroupIdentification(562, 1));
		expList.add(new GroupIdentification(568, 4));
		expList.add(new GroupIdentification(572, 2));
		expList.add(new GroupIdentification(573, 3));
		expList.add(new GroupIdentification(581, 2));
		expList.add(new GroupIdentification(589, 3));
		expList.add(new GroupIdentification(597, 6));
		expList.add(new GroupIdentification(606, 4));
		expList.add(new GroupIdentification(607, 5));
		expList.add(new GroupIdentification(610, 2));
		expList.add(new GroupIdentification(622, 1));
		expList.add(new GroupIdentification(624, 3));
		expList.add(new GroupIdentification(625, 4));
		expList.add(new GroupIdentification(629, 4));
		expList.add(new GroupIdentification(637, 2));
		expList.add(new GroupIdentification(640, 5));
		expList.add(new GroupIdentification(643, 2));
		expList.add(new GroupIdentification(661, 1));
		expList.add(new GroupIdentification(663, 3));
		expList.add(new GroupIdentification(667, 4));
		expList.add(new GroupIdentification(669, 1));
		expList.add(new GroupIdentification(676, 4));
		expList.add(new GroupIdentification(683, 3));
		expList.add(new GroupIdentification(687, 2));
		expList.add(new GroupIdentification(690, 1));
		expList.add(new GroupIdentification(716, 4));
		expList.add(new GroupIdentification(717, 5));
		expList.add(new GroupIdentification(723, 5));
		expList.add(new GroupIdentification(726, 3));

		assertEquals("result group list size differs from expected group list size:", expList.size(), groups.size());

		expList.sort(new SortByGroup());
		List<GroupIdentification> actList = new ArrayList<>(groups);
		actList.sort(new SortByGroup());
		assertEquals("the expected and actual lists, of size " + expList.size() + ", differ:", expList, actList);
	}

	///////////////////////////////////////////////

	@Test
	public void testGroupsBySegmentTooBig() {
		int testSeqment = 11;
		List<GroupIdentification> groups = accessor.getGroups(new SegmentHolder(testSeqment));
		assertNotNull("test segment " + testSeqment + " is null", groups);
		assertEquals(testSeqment + " not empty, is " + groups.size(), 0, groups.size());
	}

	@Test
	public void testGroupsBySeqment() {
		int testSegmnt = 6;
		List<GroupIdentification> groups = accessor.getGroups(new SegmentHolder(testSegmnt));
		assertNotNull("test segment " + testSegmnt + " is null", groups);
		assertFalse("test segment " + testSegmnt + " is empty", groups.size() < 1);

		List<GroupIdentification> expList = new ArrayList<>();
		expList.add(new GroupIdentification(39, 6));
		expList.add(new GroupIdentification(55, 6));
		expList.add(new GroupIdentification(79, 6));
		expList.add(new GroupIdentification(89, 6));
		expList.add(new GroupIdentification(107, 6));
		expList.add(new GroupIdentification(113, 6));
		expList.add(new GroupIdentification(119, 6));
		expList.add(new GroupIdentification(128, 6));
		expList.add(new GroupIdentification(142, 6));
		expList.add(new GroupIdentification(149, 6));
		expList.add(new GroupIdentification(156, 6));
		expList.add(new GroupIdentification(179, 6));
		expList.add(new GroupIdentification(187, 6));
		expList.add(new GroupIdentification(193, 6));
		expList.add(new GroupIdentification(204, 6));
		expList.add(new GroupIdentification(212, 6));
		expList.add(new GroupIdentification(222, 6));
		expList.add(new GroupIdentification(256, 6));
		expList.add(new GroupIdentification(280, 6));
		expList.add(new GroupIdentification(297, 6));
		expList.add(new GroupIdentification(303, 6));
		expList.add(new GroupIdentification(328, 6));
		expList.add(new GroupIdentification(334, 6));
		expList.add(new GroupIdentification(345, 6));
		expList.add(new GroupIdentification(369, 6));
		expList.add(new GroupIdentification(388, 6));
		expList.add(new GroupIdentification(410, 6));
		expList.add(new GroupIdentification(426, 6));
		expList.add(new GroupIdentification(435, 6));
		expList.add(new GroupIdentification(458, 6));
		expList.add(new GroupIdentification(469, 6));
		expList.add(new GroupIdentification(486, 6));
		expList.add(new GroupIdentification(511, 6));
		expList.add(new GroupIdentification(524, 6));
		expList.add(new GroupIdentification(554, 6));
		expList.add(new GroupIdentification(570, 6));
		expList.add(new GroupIdentification(597, 6));
		expList.add(new GroupIdentification(608, 6));
		expList.add(new GroupIdentification(618, 6));
		expList.add(new GroupIdentification(641, 6));
		expList.add(new GroupIdentification(655, 6));
		expList.add(new GroupIdentification(718, 6));

		assertEquals("result group list size differs from expected group list size:", expList.size(), groups.size());

		expList.sort(new SortByGroup());
		List<GroupIdentification> actList = new ArrayList<>(groups);
		actList.sort(new SortByGroup());
		assertEquals("the expected and actual lists, of size " + expList.size() + ", differ:", expList, actList);
	}

	///////////////////////////////////////////////

	@Test
	public void testGroupsByBusDateAndSegment() {
		long testDate = 328951L;
		int testSegmnt = 6;

		List<GroupIdentification> groups = accessor.getGroups(new BusinessDateHolder(testDate),
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
