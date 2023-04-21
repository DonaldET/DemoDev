package don.demo.queryservices.servicesimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import don.demo.dataservices.GroupsAccessor.GroupIdentification;
import don.demo.dataservices.ItemStringAcccessor.ItemStrings;
import don.demo.dataservices.ItemsAccessor.GroupIdHolder;
import don.demo.dataservices.ItemsAccessor.Items;
import don.demo.queryservices.UIDataRetrieval;

/**
 * Stream-based Single and multi-threaded retriever. <code>Accessors</code> are
 * &quot;services&quot; that are thread-safe. Note that the common forkjoin pool
 * can be configured for number of threads by using
 * <code>-Djava.util.concurrent.ForkJoinPool.common.parallelism=20</code> on the
 * Java command line.
 */
public class StreamDataElementService extends AbstractDataElementService implements UIDataRetrieval {

	public StreamDataElementService(boolean isParallel, long milliDelay) {
		super(isParallel, milliDelay);
	}

	public static record MergedGIandItems(GroupIdentification groupIdentification, Items items) {
	};

	/**
	 * Stream based UI data element builder; groupIdentifications not empty.
	 */
	@Override
	public List<UIDataElement> assembleDataElements(List<GroupIdentification> groupIdentifications,
			Predicate<ItemStrings> predicate) {
		Stream<GroupIdentification> gidStr = isParallel() ? groupIdentifications.stream().parallel()
				: groupIdentifications.stream();

		Stream<UIDataElement> uiDataElements = gidStr.map(s -> mergeItems(s)).flatMap(Function.identity())
				.map(t -> mergeItemStrings(t, predicate)).flatMap(Function.identity());
		if (isParallel()) {
			if (!uiDataElements.isParallel()) {
				throw new IllegalStateException("uiDataElements stream is not parallel");
			}
		}
		return uiDataElements.collect(Collectors.toList());
	}

	private Stream<MergedGIandItems> mergeItems(GroupIdentification groupIdentification) {
		int grpId = groupIdentification.groupId();

		List<Items> items = ia.getItems(new GroupIdHolder(grpId));
		if (items == null || items.isEmpty()) {
			return new ArrayList<MergedGIandItems>().stream();
		}

		Stream<Items> itemStream = isParallel() ? items.stream().parallel() : items.stream();
		Stream<MergedGIandItems> mergedGiAndItems = itemStream.map(s -> new MergedGIandItems(groupIdentification, s));
		return isParallel() ? mergedGiAndItems.parallel() : mergedGiAndItems;
	}

	private Stream<UIDataElement> mergeItemStrings(MergedGIandItems mergeItem,
			Predicate<ItemStrings> itemStringsPredicate) {
		GroupIdentification groupIdent = mergeItem.groupIdentification;
		Items item = mergeItem.items();

		List<ItemStrings> itemStrings = isa.getItemStrings(item.itemId(), itemStringsPredicate);
		if (itemStrings == null || itemStrings.isEmpty()) {
			return new ArrayList<UIDataElement>().stream();
		}

		Stream<ItemStrings> itmStrStream = isParallel() ? itemStrings.stream().parallel() : itemStrings.stream();
		return itmStrStream.map(itemString -> new UIDataElement(groupIdent.groupId(), groupIdent.segmentId(),
				item.itemId(), item.itmDate(), item.recId(), item.reftime(), item.deleted(), item.dateDeleted(),
				itemString.itemStrid(), itemString.recordid(), itemString.item_val(), itemString.item_err()));
	}
}