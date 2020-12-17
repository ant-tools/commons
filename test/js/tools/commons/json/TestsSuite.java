package js.tools.commons.json;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestsSuite extends TestCase
{
  public static TestSuite suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(LexerUnitTest.class);
    suite.addTestSuite(ParserUnitTest.class);
    suite.addTestSuite(SerializerUnitTest.class);
    return suite;
  }
}
