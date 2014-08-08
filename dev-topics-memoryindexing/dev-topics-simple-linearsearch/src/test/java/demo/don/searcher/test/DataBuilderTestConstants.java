package demo.don.searcher.test;

import demo.don.searcher.FLName;
import demo.don.searcher.UserID;

public class DataBuilderTestConstants
{
  public static final int TEST_FIRST_NAME_COUNT = 278;
  public static final int TEST_FULL_NAME_COUNT = 165;

  public static final String FIRST_NAME_FILE = "/firstnames.txt";
  public static final String FULL_NAME_FILE = "/winners.txt";

  public static final FLName o1 = new FLName("Daffy", "Duck");
  public static final FLName o2 = new FLName("Donald", "Duck");
  public static final FLName o3 = new FLName("Minni", "Mouse");
  public static final FLName o4 = new FLName("Roger", "Rabbit");
  public static final FLName o4a = new FLName("Roger", "Rabbit");

  public static final UserID uo1 = new UserID("Daffy", "Duck", "dd@foo.com");
  public static final UserID uo2 = new UserID("Donald", "Duck", "detdk@bar.com");
  public static final UserID uo3 = new UserID("Minni", "Mouse",
      "mmousek@pub.uk");
  public static final UserID uo4 = new UserID("Roger", "Rabbit",
      "roger_rabbit@pixar.org");
  public static final UserID uo4a = new UserID("Roger", "Rabbit",
      "roger_rabbit@pixar.org");
  public static final UserID uo4b = new UserID("Roger", "Rabbit", "aa@bb.cc");

  public static final FLName[] ordered = new FLName[] { o1, o2, o3, o4, o4a };

  public static final UserID[] uidOrdered = new UserID[] { uo1, uo2, uo3, uo4,
      uo4a, uo4b };

  public static final FLName[] unordered = new FLName[] { o4a, o4, o3, o2, o1 };

  public static final UserID[] uidUnordered = new UserID[] { uo4b, uo4a, uo4,
      uo3, uo2, uo1 };
}
