package don.demo.queryservices.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ParallelDataElementServiceTest.class, SimpleDataElementServiceTest.class,
		StreamDataElementServiceTest.class })
public class AllTestsQueryService {
}
