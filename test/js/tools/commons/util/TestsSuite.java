package js.tools.commons.util;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestsSuite extends TestCase
{
  public static TestSuite suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(StringsUnitTest.class);
    suite.addTestSuite(TypesUnitTest.class);
    suite.addTestSuite(FilesUnitTest.class);
    suite.addTestSuite(ClassesUnitTest.class);
    return suite;
  }
}
