package js.tools.commons.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Date;

import js.tools.commons.NoSuchBeingException;
import junit.framework.TestCase;

public class ClassesUnitTest extends TestCase
{
  public void testGetResourceAsReader() throws IOException
  {
    assertNotNull(Classes.getResourceAsReader("js/tools/commons/util/TestsSuite.class"));

    Reader reader = Classes.getResourceAsReader("js/tools/commons/util/resource.txt");
    Writer writer = new StringWriter();
    Files.copy(reader, writer);
    assertEquals("resource.txt", writer.toString());
  }

  public void testGetResourceAsString() throws IOException
  {
    assertNotNull(Classes.getResourceAsString("js/tools/commons/util/TestsSuite.class"));

    String string = Classes.getResourceAsString("js/tools/commons/util/resource.txt");
    assertEquals("resource.txt", string);
  }

  public void testNewInstance() throws ClassNotFoundException
  {
    Object arg = Classes.newInstance("js.tools.commons.util.ClassesUnitTest$InnerClass");
    Object obj = Classes.newInstance("js.tools.commons.util.ClassesUnitTest$InnerClass", arg);
    assertNotNull(obj);
    assertTrue(obj instanceof InnerClass);
  }

  public void testNewInstanceByClass() throws ClassNotFoundException
  {
    Object arg = Classes.newInstance("js.tools.commons.util.ClassesUnitTest$InnerClass");
    Object obj = Classes.newInstance(InnerClass.class, arg);
    assertNotNull(obj);
    assertTrue(obj instanceof InnerClass);
  }

  public void testNewInstanceWithNullArgument() throws ClassNotFoundException
  {
    Object obj = Classes.newInstance("js.tools.commons.util.ClassesUnitTest$InnerClass", (Object)null);
    assertNotNull(obj);
    assertTrue(obj instanceof InnerClass);
  }

  public void testNewInstanceWithLastArgumentOfBadType() throws ClassNotFoundException
  {
    try {
      Object innerInterface = Classes.newInstance("js.tools.commons.util.ClassesUnitTest$InnerClass");
      Classes.newInstance("js.tools.commons.util.ClassesUnitTest$InnerClass", innerInterface, 123);
      TestCase.fail("Missing expected exception.");
    }
    catch(NoSuchBeingException ignore) {}
  }

  public void testGetFieldValue()
  {
    InnerClass object = new InnerClass();
    assertEquals("string", Classes.getFieldValue(object, "string"));
  }

  public void testSetFieldValue() throws IllegalArgumentException, IllegalAccessException
  {
    InnerClass object = new InnerClass();
    Classes.setFieldValue(object, "string", "value");
    assertEquals("value", object.string);
  }

  public void testInvoke() throws Exception
  {
    InnerClass object = new InnerClass();
    assertEquals(6, Classes.invoke(object, "addIntegers", 1, 2, 3));
  }

  public void testInvokeSuperclass() throws Exception
  {
    SuperClass object = new SuperClass();
    assertEquals(6, Classes.invoke(object, InnerClass.class, "addIntegers", 1, 2, 3));
  }

  public void testGetParameterTypes()
  {
    Type[] types = Classes.getParameterTypes(new Object[]
    {
        1964, "John Doe", true, new Date()
    });

    assertEquals(4, types.length);
    assertEquals(Integer.class, types[0]);
    assertEquals(String.class, types[1]);
    assertEquals(Boolean.class, types[2]);
    assertEquals(Date.class, types[3]);
  }

  public void testLoadService()
  {
    InnerInterface service = Classes.loadService(InnerInterface.class);
    assertNotNull(service);
    assertTrue(service instanceof InnerClass);
    assertEquals("string", Classes.getFieldValue(service, "string"));
  }

  // ------------------------------------------------------
  // FIXTURE

  public static interface InnerInterface
  {
  }

  public static class InnerClass implements InnerInterface
  {
    private String string = "string";

    public InnerClass()
    {
    }

    public InnerClass(InnerInterface ii)
    {
    }

    public InnerClass(InnerInterface ii, String s)
    {
    }

    Integer addIntegers(Integer i1, Integer i2, Integer i3)
    {
      return i1 + i2 + i3;
    }

    Integer addInts(int i1, int i2)
    {
      return i1 + i2;
    }

    Integer addNumbers(Number n1, Number n2)
    {
      return n1.intValue() + n2.intValue();
    }

    void method(String name, int age)
    {
    }
  }

  public static class SuperClass extends InnerClass
  {

  }
}
