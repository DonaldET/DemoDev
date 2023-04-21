package don.demo.queryservices.servicesimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import don.demo.dataservices.GroupsAccessor.GroupIdentification;
import don.demo.dataservices.ItemStringAcccessor.ItemStrings;
import don.demo.dataservices.ItemsAccessor.GroupIdHolder;
import don.demo.dataservices.ItemsAccessor.Items;
import don.demo.queryservices.UIDataRetrieval;

/**
 * Non-stream and Single-threaded retriever. <code>Accessors</code> are
 * &quot;services&quot; that are thread-safe.
 */
public class SimpleDataElementService extends AbstractDataElementService implements UIDataRetrieval {

	public SimpleDataElementService(boolean isParallel, long testingDelay) {
		super(false, testingDelay);
	}

	/**
	 * Non-stream single threaded data element builder; groupIdentifications are not
	 * empty
	 */
	@Override
	public List<UIDataElement> assembleDataElements(List<GroupIdentification> groupIdentifications,
			Predicate<ItemStrings> itemStringsPredicate) {
		final List<UIDataElement> dataElements = new ArrayList<UIDataElement>();

		for (GroupIdentification groupIdentification : groupIdentifications) {
			int grpId = groupIdentification.groupId();
			int segId = groupIdentification.segmentId();

			List<Items> items = ia.getItems(new GroupIdHolder(grpId));
			for (Items item : items) {
				int itmId = item.itemId();

				List<ItemStrings> itemStrings = isa.getItemStrings(itmId, itemStringsPredicate);
				for (ItemStrings itemStr : itemStrings) {
					UIDataElement uiDataElement = new UIDataElement(grpId, segId, itmId, item.itmDate(), item.recId(),
							item.reftime(), item.deleted(), item.dateDeleted(), itemStr.itemStrid(), itemStr.recordid(),
							itemStr.item_val(), itemStr.item_err());
					dataElements.add(uiDataElement);
				}
			}
		}

		return dataElements;
	}
}