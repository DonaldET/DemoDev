package don.demo.dataservices.servicesimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import don.demo.dataservices.ItemStringAcccessor;
import don.demo.mockdata.MockDataGenerator;

/**
 * Obtains lists of item strings by record identifier, optionally filtered by a
 * predicate. Note: this class is thread safe.
 */
public class ItemStringsAccessorImpl extends AbstractAccessor implements ItemStringAcccessor {

	public ItemStringsAccessorImpl() {
		super();
	}

	public ItemStringsAccessorImpl(long milliDelay) {
		super(milliDelay);
	}

	@Override
	public List<ItemStrings> getItemStrings(long itemStrId) {
		this.sleepAccessor("getItemStrings(itemStrId)");
		List<ItemStrings> recordList = MockDataGenerator.itemStrings.get(itemStrId);
		return (recordList != null) ? recordList : new ArrayList<ItemStrings>();
	}

	@Override
	public List<ItemStrings> getItemStrings(long itemStrId, Predicate<ItemStrings> predicate) {
		this.sleepAccessor("getItemStrings(itemStrId, predicate)");
		return this.getItemStrings(itemStrId).stream().filter(predicate).collect(Collectors.toList());
	}
}