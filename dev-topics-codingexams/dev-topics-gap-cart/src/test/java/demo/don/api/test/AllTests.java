package demo.don.api.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GapCartImplTest.class, RegularPricerImplTest.class,
    RegularPricingRuleImplTest.class, SpecialPricerImplTest.class,
    SpecialPricingRuleImplTest.class })
public class AllTests
{

}
