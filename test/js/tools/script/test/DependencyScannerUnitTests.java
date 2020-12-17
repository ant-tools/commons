package js.tools.script.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;

import js.tools.commons.dep.Dependency;
import js.tools.commons.dep.DependencyScanner;
import junit.framework.TestCase;

public class DependencyScannerUnitTests extends TestCase
{
  public void testDependencyScanner() throws Exception
  {
    Collection<Dependency> deps = getDependencies("dependency-sample.js", "js.tools.scanner.test.DependencySample");
    assertEquals(28, deps.size());

    assertDependency("js.lang.Operator", Dependency.Type.STRONG, deps);
    assertDependency("js.format.Number", Dependency.Type.STRONG, deps);
    assertDependency("js.net.RMI", Dependency.Type.WEAK, deps);
    assertDependency("js.lang.Types", Dependency.Type.WEAK, deps);
    assertDependency("js.net.Method", Dependency.Type.STRONG, deps);
    assertDependency("js.lang.Strings", Dependency.Type.WEAK, deps);
    assertDependency("js.net.XHR", Dependency.Type.WEAK, deps);
    assertDependency("js.lang.Log", Dependency.Type.STRONG, deps);
    assertDependency("js.event.Types", Dependency.Type.WEAK, deps);
    assertDependency("js.util.UUID", Dependency.Type.WEAK, deps);
    assertDependency("js.format.FullDateTime", Dependency.Type.WEAK, deps);
    assertDependency("js.format.AbstractDateTime", Dependency.Type.WEAK, deps);
    assertDependency("js.ua.Regional", Dependency.Type.WEAK, deps);
    assertDependency("js.ua.System", Dependency.Type.WEAK, deps);
    assertDependency("js.format.Currency", Dependency.Type.STRONG, deps);
    assertDependency("js.format.ShortDateTime", Dependency.Type.STRONG, deps);
    assertDependency("js.lang.LogFactory", Dependency.Type.STRONG, deps);
    assertDependency("js.net.ReadyState", Dependency.Type.STRONG, deps);
    assertDependency("js.util.Timeout", Dependency.Type.STRONG, deps);
    assertDependency("js.util.Timer", Dependency.Type.WEAK, deps);
    assertDependency("js.lang.Object", Dependency.Type.STRONG, deps);
    assertDependency("js.ua.Engine", Dependency.Type.STRONG, deps);
    assertDependency("js.util.Rand", Dependency.Type.WEAK, deps);
    assertDependency("js.lang.NOP", Dependency.Type.WEAK, deps);
    assertDependency("js.dom.Node", Dependency.Type.WEAK, deps);
    assertDependency("js.widget.Description", Dependency.Type.WEAK, deps);
    assertDependency("js.lang.Exception", Dependency.Type.WEAK, deps);
  }

  public void test3ptyApiDependencyScanner() throws Exception
  {
    Collection<Dependency> deps = getDependencies("3pty-api.js", "js.tools.scanner.test.DependencySample");
    assertEquals(2, deps.size());

    assertDependency("js.lang.Operator", Dependency.Type.STRONG, deps);
    assertDependency("http://maps.google.com/maps/api/js?sensor=false", Dependency.Type.THIRD_PARTY, deps);
  }

  public void testFormUnitTests() throws Exception
  {
    Collection<Dependency> deps = getDependencies("FormUnitTests.js");
    assertEquals(10, deps.size());

    assertDependency("js.lang.Operator", Dependency.Type.STRONG, deps);
    assertDependency("js.dom.Form", Dependency.Type.WEAK, deps);
    assertDependency("js.format.ShortDate", Dependency.Type.STRONG, deps);
  }

  public void testPrivateStaticMethod() throws Exception
  {
    Collection<Dependency> deps = getDependencies("private-static-method.js");

    assertEquals(1, deps.size());
    assertDependency("js.ua.System", Dependency.Type.STRONG, deps);
  }

  public void testPublicStaticConstant() throws Exception
  {
    Collection<Dependency> deps = getDependencies("public-static-constant.js");
    assertEquals(6, deps.size());

    assertDependency("js.ua.UserAgent", Dependency.Type.WEAK, deps);
    assertDependency("js.ua.Engine", Dependency.Type.WEAK, deps);
    assertDependency("js.ua.Message", Dependency.Type.WEAK, deps);
    assertDependency("js.ua.Style", Dependency.Type.WEAK, deps);
    assertDependency("js.ua.Layout", Dependency.Type.STRONG, deps);
    assertDependency("js.ua.TypeFace", Dependency.Type.STRONG, deps);
  }

  public void testLegacyPseudoOperator() throws Exception
  {
    Collection<Dependency> deps = getDependencies("legacy-pseudo-operator.js");
    assertEquals(4, deps.size());

    assertDependency("js.ua.Engine", Dependency.Type.STRONG, deps);
    assertDependency("js.ua.Layout", Dependency.Type.STRONG, deps);
    assertDependency("js.ua.TypeFace", Dependency.Type.STRONG, deps);
    assertDependency("js.net.XHR", Dependency.Type.STRONG, deps);
  }

  public void testWinMain() throws Exception
  {
    Collection<Dependency> deps = getDependencies("win-main.js");

    assertEquals(1, deps.size());
    assertDependency("js.ua.Window", Dependency.Type.STRONG, deps);
  }

  // ------------------------------------------------------
  // Utility Functions

  private static Collection<Dependency> getDependencies(String fileName, String... excludes) throws FileNotFoundException
  {
    File sourceFile = new File("fixture/" + fileName);
    DependencyScanner scanner = new DependencyScanner();
    return scanner.getDependencies(sourceFile, excludes);
  }

  private static void assertDependency(String expectedName, Dependency.Type expectedType, Collection<Dependency> dependencies) throws Exception
  {
    Dependency expected = null;
    for(Dependency dependecy : dependencies) {
      if(dependecy.getName().equals(expectedName)) {
        expected = dependecy;
        break;
      }
    }
    assertNotNull(expected);
    assertEquals(expectedType, expected.getType());
  }
}
