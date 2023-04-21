package don.demo.dataservices.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GroupAccessorTest.class, ItemsAccessorTest.class, ItemStringsAccessorTest.class,
		RetrievalServicesTest.class })
public class AllTestsDataService {
}
