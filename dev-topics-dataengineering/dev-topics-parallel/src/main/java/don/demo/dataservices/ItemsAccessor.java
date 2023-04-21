package don.demo.dataservices;

import java.util.List;

public interface ItemsAccessor {
	public static record Items(int itemId, int groupId, long itmDate, int recId, long reftime, boolean deleted,
			long dateDeleted) {
	}

	public static record GroupIdHolder(int groupId) {
	}

	public abstract List<Items> getItems(GroupIdHolder groupIdHolder);
}
