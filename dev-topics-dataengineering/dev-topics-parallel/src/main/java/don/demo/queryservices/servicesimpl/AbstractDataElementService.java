package don.demo.queryservices.servicesimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import don.demo.dataservices.GroupsAccessor;
import don.demo.dataservices.GroupsAccessor.BusinessDateHolder;
import don.demo.dataservices.GroupsAccessor.GroupIdentification;
import don.demo.dataservices.GroupsAccessor.SegmentHolder;
import don.demo.dataservices.ItemStringAcccessor;
import don.demo.dataservices.ItemStringAcccessor.ItemStrings;
import don.demo.dataservices.ItemsAccessor;
import don.demo.dataservices.servicesimpl.GroupsAccessorImpl;
import don.demo.dataservices.servicesimpl.ItemStringsAccessorImpl;
import don.demo.dataservices.servicesimpl.ItemsAccessorImpl;
import don.demo.queryservices.UIDataRetrieval;

/**
 * Base class that creates mock data access.
 */
abstract class AbstractDataElementService implements UIDataRetrieval {

	private boolean isParallel = false;
	private long testingDelay = 0;

	protected final GroupsAccessor ga;
	protected final ItemsAccessor ia;
	protected final ItemStringAcccessor isa;

	protected AbstractDataElementService(boolean isParallel, long testingDelay) {
		super();
		this.isParallel = isParallel;
		if (testingDelay < 0) {
			throw new IllegalArgumentException("testing delay " + testingDelay + " is negative");
		}
		this.testingDelay = testingDelay;

		ga = new GroupsAccessorImpl(testingDelay);
		ia = new ItemsAccessorImpl(testingDelay);
		isa = new ItemStringsAccessorImpl(testingDelay);
	}

	@Override
	public List<UIDataElement> getDataElements(BusinessDateHolder businessDateHolder,
			Predicate<ItemStrings> optionalPredicate) {
		return getDataElementsImpl(ga.getGroups(businessDateHolder), optionalPredicate);
	}

	@Override
	public List<UIDataElement> getDataElements(SegmentHolder segmentHolder, Predicate<ItemStrings> optionalPredicate) {
		return getDataElementsImpl(ga.getGroups(segmentHolder), optionalPredicate);
	}

	@Override
	public List<UIDataElement> getDataElements(BusinessDateHolder businessDateHolder, SegmentHolder segmentHolder,
			Predicate<ItemStrings> optionalPredicate) {
		return getDataElementsImpl(ga.getGroups(businessDateHolder, segmentHolder), optionalPredicate);
	}

	/**
	 * Create the join of the ITEMS and ITEM_STR tables selected by the group table,
	 * thereby creating the UI data element.
	 */
	private List<UIDataElement> getDataElementsImpl(List<GroupIdentification> groupIdentifications,
			Predicate<ItemStrings> optionalPredicate) {
		return (groupIdentifications == null || groupIdentifications.isEmpty()) ? new ArrayList<UIDataElement>()
				: assembleDataElements(groupIdentifications,
						optionalPredicate == null ? str -> true : optionalPredicate);
	}

	public abstract List<UIDataElement> assembleDataElements(List<GroupIdentification> groupIdentifications,
			Predicate<ItemStrings> itemStringsPredicate);

	public boolean isParallel() {
		return isParallel;
	}

	public long getTestingDelay() {
		return testingDelay;
	}
}