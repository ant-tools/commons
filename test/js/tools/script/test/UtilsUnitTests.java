package js.tools.script.test;

import js.tools.commons.util.Classes;
import junit.framework.TestCase;

public class UtilsUnitTests extends TestCase
{
  public void testValidQualifiedClassNamePattern() throws Throwable
  {
    String[] names = new String[]
    {
        "package.ClassName", //
        "package.ClassName.InnerClass", //
        "a.b.c.ClassName", //
        "a1.b2.c3.ClassName", //
        "ro.bbnet.hc2.ReleasesPage", //
        "js.util.UUID"
    };

    Class<?> utilsClass = Class.forName("js.tools.commons.util.Utils");
    for(int i = 0; i < names.length; i++) {
      assertTrue("Invalid class name: " + names[i], (Boolean)Classes.invoke(utilsClass, "isQualifiedClassName", names[i]));
    }
  }

  public void testValidQualifiedNamePattern() throws Throwable
  {
    String[] names = new String[]
    {
        "package.ClassName", //
        "package.ClassName.InnerClass", //
        "a.b.c.ClassName", //
        "a1.b2.c3.ClassName", //
        "ro.bbnet.hc2.ReleasesPage", //
        "js.util.UUID.member", //
        "js.util.UUID.member.inner_member", //
        "js.util.UUID._member", //
        "js.util.UUID._member._inner_member", //
        "js.ua.UserAgent.TRIDENT", //
        "js.net.XHR", //
        "js.net.XHR.TIMEOUT"
    };

    Class<?> utilsClass = Class.forName("js.tools.commons.util.Utils");
    for(int i = 0; i < names.length; i++) {
      assertTrue("Invalid qualified name: " + names[i], (Boolean)Classes.invoke(utilsClass, "isDependencyName", names[i]));
    }
  }

  public void testGetClassName() throws Exception
  {
    assertClassName("js.util.Timer", "js.util.Timer");
    assertClassName("js.ua.System", "js.ua.System._getErrorMessage");
    assertClassName("js.lang.LogFactory", "js.lang.LogFactory.getLogger");
    assertClassName("js.ua.Engine", "js.ua.Engine.TRIDENT");
    assertClassName("js.net.XHR", "js.net.XHR.Upload");
    assertClassName("js.net.XHR", "js.net.XHR.Upload.BIG_SPEED");
  }

  private static void assertClassName(String expected, String className) throws Exception
  {
    Class<?> utilsClass = Class.forName("js.tools.commons.util.Utils");
    assertEquals(expected, Classes.invoke(utilsClass, "getDependencyQualifiedClassName", className));
  }
}
