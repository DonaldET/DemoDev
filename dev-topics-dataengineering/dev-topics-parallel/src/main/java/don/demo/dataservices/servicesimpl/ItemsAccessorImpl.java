package don.demo.dataservices.servicesimpl;

import java.util.List;
import java.util.stream.Collectors;

import don.demo.dataservices.ItemsAccessor;
import don.demo.mockdata.MockDataGenerator;

/**
 * Obtains Items by group identifier. Note: this class is thread safe
 */
public class ItemsAccessorImpl extends AbstractAccessor implements ItemsAccessor {

	public ItemsAccessorImpl() {
		super();
	}

	public ItemsAccessorImpl(long milliDelay) {
		super(milliDelay);
	}

	@Override
	public List<Items> getItems(GroupIdHolder groupIdHolder) {
		this.sleepAccessor("getItems(GroupIdHolder)");
		final long groupId = groupIdHolder.groupId();
		return MockDataGenerator.items.entrySet().stream().filter((s) -> s.getValue().groupId() == groupId)
				.map((g) -> g.getValue()).collect(Collectors.toList());
	}
}
