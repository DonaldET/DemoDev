package don.demo.queryservices;

import java.util.List;
import java.util.function.Predicate;

import don.demo.dataservices.GroupsAccessor.BusinessDateHolder;
import don.demo.dataservices.GroupsAccessor.SegmentHolder;
import don.demo.dataservices.ItemStringAcccessor.ItemStrings;

public interface UIDataRetrieval {
	public static record UIDataElement(int groupId, int segmentId, int itemId, long itmDate, int recId, long reftime,
			boolean deleted, long dateDeleted, long itemStrid, long recordid, String item_val, String item_err) {
	}

	public List<UIDataElement> getDataElements(BusinessDateHolder businessDateHolder,
			Predicate<ItemStrings> optionalPredicate);

	public List<UIDataElement> getDataElements(SegmentHolder segmentHolder, Predicate<ItemStrings> optionalPredicate);

	public List<UIDataElement> getDataElements(BusinessDateHolder businessDateHolder, SegmentHolder segmentHolder,
			Predicate<ItemStrings> optionalPredicate);
}
