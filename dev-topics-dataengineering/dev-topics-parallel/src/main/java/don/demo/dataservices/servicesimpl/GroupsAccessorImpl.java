package don.demo.dataservices.servicesimpl;

import java.util.List;
import java.util.stream.Collectors;

import don.demo.dataservices.GroupsAccessor;
import don.demo.mockdata.MockDataGenerator;

/**
 * Obtains groups by combinations of business date and segment. Note: this class
 * is thread safe.
 */
public class GroupsAccessorImpl extends AbstractAccessor implements GroupsAccessor {

	public GroupsAccessorImpl() {
		super();
	}

	public GroupsAccessorImpl(long milliDelay) {
		super(milliDelay);
	}

	@Override
	public List<GroupIdentification> getGroups(BusinessDateHolder businessDateHolder) {
		this.sleepAccessor("getGroups(BusinessDateHolder)");
		final long busDate = businessDateHolder.businessDate();
		return MockDataGenerator.groups.entrySet().stream()
				.filter((s) -> s.getValue().businessStartDate() <= busDate && busDate <= s.getValue().businessEndDate())
				.map((g) -> new GroupIdentification(g.getValue().groupId(), g.getValue().segmentId()))
				.collect(Collectors.toList());
	}

	@Override
	public List<GroupIdentification> getGroups(SegmentHolder segmentHolder) {
		this.sleepAccessor("getGroups(SegmentHolder)");
		final int seg = segmentHolder.segmentID();
		return MockDataGenerator.groups.entrySet().stream().filter((s) -> s.getValue().segmentId() == seg)
				.map((g) -> new GroupIdentification(g.getValue().groupId(), g.getValue().segmentId()))
				.collect(Collectors.toList());
	}

	@Override
	public List<GroupIdentification> getGroups(BusinessDateHolder businessDateHolder, SegmentHolder segmentHolder) {
		this.sleepAccessor("getGroups(businessDateHolder, SegmentHolder)");
		long busDate = businessDateHolder.businessDate();
		final int seg = segmentHolder.segmentID();
		return MockDataGenerator.groups.entrySet().stream()
				.filter((s) -> s.getValue().businessStartDate() <= busDate && busDate <= s.getValue().businessEndDate())
				.filter((s) -> s.getValue().segmentId() == seg)
				.map((g) -> new GroupIdentification(g.getValue().groupId(), g.getValue().segmentId()))
				.collect(Collectors.toList());
	}
}
