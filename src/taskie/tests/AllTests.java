package taskie.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FunctionalTest.class, CommandTest.class, ModelTest.class,
		ParserTest.class, StorageTest.class })
public class AllTests {

}
