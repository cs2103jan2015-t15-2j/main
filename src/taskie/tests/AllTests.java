package taskie.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CommandTest.class, FunctionalTest.class, ModelTest.class,
		ParserTest.class, StorageTest.class, UITest.class })
public class AllTests {

}
