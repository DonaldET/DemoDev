package demo.don.amazon.invoice.parser;

public interface ExtractStrategy
{
  public static final int FIELD_SIZE = 75;
  public static final String SPACE = " ";
  public static final String SEPERATOR = ", ";
  public static final String QUOTE = "\"";
  public static final String BOLD_CLOSE_TAG = "</b>";
  public static final String A_TAG_BEGIN = "<a href=\"";
  public static final String A_TAG_CLOSE = "\">";
  public static final String A_TAG_END = "</a>";
  public static final String KINDLE_EDITION = "[Kindle Edition]";
  public static final String DETAILS_FOR_ORDER = "Details for Order # ";
  public static final String FINAL_DETAILS_FOR_ORDER = "Final Details for Order #";
  public static final String DIGITAL_ORDER = "Digital Order: ";
  public static final String GRAND_TOTAL = "<b>Grand Total: $";
  public static final String ORDER_SUMMARY = "Order Summary";
  public static final String NOT_ORDER_SUMMARY = "<tr><td valign=\"top\" align=\"left\"><b>";

  public abstract void extractDetails(final String fileName,
      final String fileInfo, final StringBuilder data, final int start);
}