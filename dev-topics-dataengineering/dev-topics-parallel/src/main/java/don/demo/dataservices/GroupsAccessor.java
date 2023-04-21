package don.demo.dataservices;

import java.util.List;

public interface GroupsAccessor {

	public static record BusinessDateHolder(long businessDate) {

	}

	public static record SegmentHolder(int segmentID) {

	}

	public static record GroupIdentification(int groupId, int segmentId) {
	}

	public static record Groups(int groupId, int segmentId, long businessStartDate, long businessEndDate) {
	}

	public List<GroupIdentification> getGroups(BusinessDateHolder businessDateHolder);

	public List<GroupIdentification> getGroups(SegmentHolder segmentHolder);

	public List<GroupIdentification> getGroups(BusinessDateHolder businessDateHolder, SegmentHolder segmentHolder);
}
