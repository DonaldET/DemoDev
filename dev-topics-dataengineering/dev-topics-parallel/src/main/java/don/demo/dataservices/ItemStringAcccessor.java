package don.demo.dataservices;

import java.util.List;
import java.util.function.Predicate;

public interface ItemStringAcccessor {
	public static record ItemStrings(long itemStrid, long recordid, String item_val, String item_err) {

	}

	/**
	 * Using ITEMS.PK_ITEMID and ITEMS_STR.PK2_RECORDID_FK for join.
	 * 
	 * @param itemStrId
	 * @return list of item strings for join
	 */
	public abstract List<ItemStrings> getItemStrings(long itemStrId);

	/**
	 * Using ITEMS.PK_ITEMID and ITEMS_STR.PK2_RECORDID_FK for join and a predicate
	 * to select item string records.
	 * 
	 * @param itemStrId
	 * @param predicate
	 * @return list of item strings for join and filter predicate
	 */
	public abstract List<ItemStrings> getItemStrings(long itemStrId, Predicate<ItemStrings> predicate);
}
