package js.tools.commons.json;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import js.tools.commons.util.Classes;
import js.tools.commons.util.GAType;
import js.tools.commons.util.GType;
import junit.framework.TestCase;

public class ParserUnitTest extends TestCase
{
  public void testStringValue() throws Throwable
  {
    String json = "\"John Doe\"";
    String instance = exercise(json, String.class);
    assertEquals("John Doe", instance);
    assertNull(exercise("null", Boolean.class));
  }

  public void testNumberValue() throws Throwable
  {
    String json = "123.00";
    assertEquals((byte)123, (byte)exercise(json, byte.class));
    assertEquals((short)123, (short)exercise(json, short.class));
    assertEquals((int)123, (int)exercise(json, int.class));
    assertEquals((long)123, (long)exercise(json, long.class));
    assertEquals((float)123, (float)exercise(json, float.class));
    assertEquals((double)123, (double)exercise(json, double.class));
    assertEquals((byte)123, (byte)exercise(json, Byte.class));
    assertEquals((short)123, (short)exercise(json, Short.class));
    assertEquals((int)123, (int)exercise(json, Integer.class));
    assertEquals((long)123, (long)exercise(json, Long.class));
    assertEquals((float)123, (float)exercise(json, Float.class));
    assertEquals((double)123, (double)exercise(json, Double.class));
    assertNull(exercise("null", Boolean.class));
  }

  public void testHexadecimalNumberValue() throws Throwable
  {
    String json = "0x7B";
    assertEquals((byte)123, (byte)exercise(json, byte.class));
    assertEquals((short)123, (short)exercise(json, short.class));
    assertEquals((int)123, (int)exercise(json, int.class));
    assertEquals((long)123, (long)exercise(json, long.class));
    assertEquals((float)123, (float)exercise(json, float.class));
    assertEquals((double)123, (double)exercise(json, double.class));
    assertEquals((byte)123, (byte)exercise(json, Byte.class));
    assertEquals((short)123, (short)exercise(json, Short.class));
    assertEquals((int)123, (int)exercise(json, Integer.class));
    assertEquals((long)123, (long)exercise(json, Long.class));
    assertEquals((float)123, (float)exercise(json, Float.class));
    assertEquals((double)123, (double)exercise(json, Double.class));
    assertNull(exercise("null", Boolean.class));
  }

  public void testBooleanValue() throws Throwable
  {
    assertTrue(exercise("true", boolean.class));
    assertFalse(exercise("false", boolean.class));
    assertTrue(exercise("on", boolean.class));
    assertFalse(exercise("off", boolean.class));
    assertTrue(exercise("1", boolean.class));
    assertFalse(exercise("0", boolean.class));
    assertTrue(exercise("yes", boolean.class));
    assertFalse(exercise("no", boolean.class));

    assertTrue(exercise("true", Boolean.class));
    assertFalse(exercise("false", Boolean.class));

    assertFalse(exercise("fake", boolean.class));
    assertNull(exercise("null", Boolean.class));
  }

  public void testNullValue() throws Throwable
  {
    String json = "null";
    assertNull(exercise(json, Person.class));
  }

  public void testFlatObject() throws Throwable
  {
    String json = "{\"name\":\"John Doe\",\"age\":50}";
    Person person = exercise(json, Person.class);
    assertNotNull(person);
    assertEquals("John Doe", person.name);
    assertEquals(50, person.age);
  }

  public void testNestedObjects() throws Throwable
  {
    String json = "{\"name\":\"Baby.NET\",\"leader\":{\"name\":\"John Doe\"}}";
    Organization organization = exercise(json, Organization.class);
    assertNotNull(organization);
    assertEquals("Baby.NET", organization.name);
    assertEquals("John Doe", organization.leader.name);
  }

  public void testEmptyObject() throws Throwable
  {
    String json = "{\"name\":\"Baby.NET\",\"leader\":{}}";
    Organization organization = exercise(json, Organization.class);
    assertNotNull(organization);
    assertEquals("Baby.NET", organization.name);
    assertNotNull(organization.leader);
  }

  public void testArrayOfStrings() throws Throwable
  {
    String json = "[\"John Doe\",\"picture.png\"]";
    String[] strings = exercise(json, String[].class);
    assertNotNull(strings);
    assertEquals(2, strings.length);
    assertEquals("John Doe", strings[0]);
    assertEquals("picture.png", strings[1]);
  }

  public void testArrayOfIntegers() throws Throwable
  {
    String json = "[1234,5678]";

    Integer[] integers = exercise(json, Integer[].class);
    assertNotNull(integers);
    assertEquals(2, integers.length);
    assertEquals(1234, integers[0].intValue());
    assertEquals(5678, integers[1].intValue());

    int[] ints = exercise(json, int[].class);
    assertNotNull(ints);
    assertEquals(2, ints.length);
    assertEquals(1234, ints[0]);
    assertEquals(5678, ints[1]);
  }

  public void testArrayOfBooleans() throws Throwable
  {
    String json = "[false,true]";

    Boolean[] booleans = exercise(json, Boolean[].class);
    assertNotNull(booleans);
    assertEquals(2, booleans.length);
    assertFalse(booleans[0]);
    assertTrue(booleans[1]);

    boolean[] bools = exercise(json, boolean[].class);
    assertNotNull(bools);
    assertEquals(2, bools.length);
    assertFalse(bools[0]);
    assertTrue(bools[1]);
  }

  public void testArrayOfPersons() throws Throwable
  {
    String json = "[{\"name\":\"WALLE\"},{\"name\":\"EVA\"}]";
    Person[] persons = exercise(json, Person[].class);
    assertNotNull(persons);
    assertEquals(2, persons.length);
    assertEquals("WALLE", persons[0].name);
    assertEquals("EVA", persons[1].name);
  }

  public void testArrayOfArrayOfStrings() throws Throwable
  {
    String json = "[[\"WALLE\",\"walle.png\"],[\"EVA\",\"eva.png\"]]";
    String[][] persons = exercise(json, String[][].class);
    assertNotNull(persons);
    assertEquals(2, persons.length);
    assertEquals("WALLE", persons[0][0]);
    assertEquals("walle.png", persons[0][1]);
    assertEquals("EVA", persons[1][0]);
    assertEquals("eva.png", persons[1][1]);
  }

  public void testListOfStrings() throws Throwable
  {
    String json = "[\"John Doe\",null,\"picture.png\"]";
    List<String> strings = exercise(json, new GType(List.class, String.class));
    assertNotNull(strings);
    assertTrue(strings instanceof ArrayList);
    assertEquals(3, strings.size());
    assertEquals("John Doe", strings.get(0));
    assertNull(strings.get(1));
    assertEquals("picture.png", strings.get(2));
  }

  public void testListOfIntegers() throws Throwable
  {
    String json = "[1234,5678]";
    List<Integer> integers = exercise(json, new GType(List.class, Integer.class));
    assertNotNull(integers);
    assertTrue(integers instanceof ArrayList);
    assertEquals(2, integers.size());
    assertEquals(1234, integers.get(0).intValue());
    assertEquals(5678, integers.get(1).intValue());
  }

  public void testListOfEnums() throws Throwable
  {
    String json = "[\"LIGER\",\"TIGON\"]";
    List<Integer> cats = exercise(json, new GType(List.class, Cats.class));
    assertNotNull(cats);
    assertTrue(cats instanceof ArrayList);
    assertEquals(2, cats.size());
    assertEquals(Cats.LIGER, cats.get(0));
    assertEquals(Cats.TIGON, cats.get(1));
  }

  public void testListOfBooleans() throws Throwable
  {
    String json = "[false,true]";
    List<Boolean> booleans = exercise(json, new GType(List.class, Boolean.class));
    assertNotNull(booleans);
    assertTrue(booleans instanceof ArrayList);
    assertEquals(2, booleans.size());
    assertFalse(booleans.get(0));
    assertTrue(booleans.get(1));
  }

  public void testListOfLists() throws Throwable
  {
    String json = "[[{\"name\":\"John Doe\",\"age\":50}],[{\"name\":\"Jane Doe\",\"age\":40}]]";
    List<List<Person>> persons = exercise(json, new GType(List.class, new GType(List.class, Person.class)));
    assertNotNull(persons);
    assertEquals(2, persons.size());
    assertEquals(1, persons.get(0).size());
    assertEquals(1, persons.get(1).size());
    assertEquals("John Doe", persons.get(0).get(0).name);
    assertEquals(50, persons.get(0).get(0).age);
    assertEquals("Jane Doe", persons.get(1).get(0).name);
    assertEquals(40, persons.get(1).get(0).age);
  }

  public void testArrayOfLists() throws Throwable
  {
    // although Java forbids generic array instantiation parser allows it
    // List<String>[] a1 = new List[10]; // allowed
    // List<String>[] a2 = new List<String>[10]; // verboten

    String json = "[[{\"name\":\"John Doe\",\"age\":50}],[{\"name\":\"Jane Doe\",\"age\":40}]]";
    List<Person>[] persons = exercise(json, new GAType(new GType(List.class, Person.class)));
    assertNotNull(persons);
    assertEquals(2, persons.length);
    assertEquals(1, persons[0].size());
    assertEquals(1, persons[1].size());
    assertEquals("John Doe", persons[0].get(0).name);
    assertEquals(50, persons[0].get(0).age);
    assertEquals("Jane Doe", persons[1].get(0).name);
    assertEquals(40, persons[1].get(0).age);
  }

  public void testEmptyList() throws Throwable
  {
    String json = "{\"name\":\"Research\",\"employees\":[]}";
    Department department = exercise(json, Department.class);
    assertNotNull(department);
    assertEquals("Research", department.name);
    assertEquals(0, department.employees.size());
  }

  public void testMapOfStrings() throws Throwable
  {
    String json = "{\"name\":\"John Doe\",\"picture\":\"picture.png\"}";
    Map<String, String> person = exercise(json, new GType(Map.class, String.class, String.class));
    assertNotNull(person);
    assertTrue(person instanceof HashMap);
    assertEquals("John Doe", person.get("name"));
    assertEquals("picture.png", person.get("picture"));
  }

  public void testMapOfStringsWithDash() throws Throwable
  {
    String json = "{\"user-name\":\"John Doe\",\"user-picture\":\"john-picture.png\"}";
    Map<String, String> person = exercise(json, new GType(Map.class, String.class, String.class));
    assertNotNull(person);
    assertTrue(person instanceof HashMap);
    assertEquals("John Doe", person.get("user-name"));
    assertEquals("john-picture.png", person.get("user-picture"));
  }

  public void testMapOfIntegers() throws Throwable
  {
    String json = "{\"first\":1234,\"second\":5678}";
    Map<String, Integer> integers = exercise(json, new GType(Map.class, String.class, Integer.class));
    assertNotNull(integers);
    assertTrue(integers instanceof HashMap);
    assertEquals(1234, integers.get("first").intValue());
    assertEquals(5678, integers.get("second").intValue());
  }

  public void testMapOfBooleans() throws Throwable
  {
    String json = "{\"first\":false,\"second\":true}";
    Map<String, Boolean> booleans = exercise(json, new GType(Map.class, String.class, Boolean.class));
    assertNotNull(booleans);
    assertTrue(booleans instanceof HashMap);
    assertFalse(booleans.get("first"));
    assertTrue(booleans.get("second"));
  }

  public void testMapOfPersons() throws Throwable
  {
    String json = "{\"worker\":{\"name\":\"WALLE\"},\"specialist\":{\"name\":\"EVA\"}}";
    Map<String, Person> map = exercise(json, new GType(Map.class, String.class, Person.class));
    assertNotNull(map);
    assertTrue(map instanceof HashMap);
    assertEquals("WALLE", map.get("worker").name);
    assertEquals("EVA", map.get("specialist").name);
  }

  public void testMapObjectKey() throws Throwable
  {
    String json = "{{\"name\":\"WALLE\",\"age\":1964}:\"worker\",{\"name\":\"EVA\"}:\"specialist\",null:\"expert\"}";
    Map<Person, String> map = exercise(json, new GType(Map.class, Person.class, String.class));
    assertNotNull(map);
    assertTrue(map instanceof HashMap);
    assertEquals("worker", map.get(new Person("WALLE", 1964)));
    assertEquals("specialist", map.get(new Person("EVA", 0)));
    assertEquals("expert", map.get(null));
  }

  public void testEmptyMap() throws Throwable
  {
    String json = "{}";
    Map<String, Person> map = exercise(json, new GType(Map.class, String.class, Person.class));
    assertNotNull(map);
    assertTrue(map instanceof HashMap);
    assertTrue(map.isEmpty());
  }

  public void testMapOfEmptyObject() throws Throwable
  {
    String json = "{\"worker\":{},\"specialist\":{\"name\":\"EVA\"}}";
    Map<String, Person> map = exercise(json, new GType(Map.class, String.class, Person.class));
    assertNotNull(map);
    assertTrue(map instanceof HashMap);
    assertNull(map.get("worker").name);
    assertEquals("EVA", map.get("specialist").name);
  }

  public void testMapEmptyObjectKey() throws Throwable
  {
    String json = "{{}:\"worker\"}";
    Map<Person, String> map = exercise(json, new GType(Map.class, Person.class, String.class));
    assertNotNull(map);
    assertTrue(map instanceof HashMap);
    assertEquals("worker", map.get(new Person(null, 0)));
  }

  public void testMapIntegerKey() throws Throwable
  {
    String json = "{1964:\"worker\",0:\"specialist\"}";
    Map<Integer, String> map = exercise(json, new GType(Map.class, Integer.class, String.class));
    assertNotNull(map);
    assertTrue(map instanceof HashMap);
    assertEquals("worker", map.get(1964));
    assertEquals("specialist", map.get(0));
  }

  public void testMapDoubleKey() throws Throwable
  {
    String json = "{1.5:\"worker\",2.0:\"specialist\"}";
    Map<Double, String> map = exercise(json, new GType(Map.class, Double.class, String.class));
    assertNotNull(map);
    assertTrue(map instanceof HashMap);
    assertEquals("worker", map.get(1.5));
    assertEquals("specialist", map.get(2.0));
  }

  public void testComplexGraph() throws Throwable
  {
    String json = "{\"name\":\"Baby.NET\\u00A9\\\"2013\",\"leader\":{\"name\":\"Mr. Leader\"},\"departments\":[{\"name\":\"Waste Disposal\",\"employees\":[{\"name\":\"WALLE\"},{\"name\":\"EVA\"}]},{\"name\":\"Manager\",\"employees\":[{\"name\":\"John Doe\"}]}]}";
    Organization organization = exercise(json, Organization.class);
    assertNotNull(organization);
    assertEquals("Baby.NET©\"2013", organization.name);
    assertEquals("Mr. Leader", organization.leader.name);
  }

  public void testObjectWithNewLineBeforeEndBracet() throws Throwable
  {
    String json = "{aliases:[\"alias\"]\r\n}";
    User user = exercise(json, User.class);
    assertNotNull(user);
    assertEquals(1, user.aliases.length);
    assertEquals("alias", user.aliases[0]);
  }

  public void testEnum() throws Throwable
  {
    String json = "\"LIGER\"";
    Cats cats = exercise(json, Cats.class);
    assertNotNull(cats);
    assertEquals(Cats.LIGER, cats);
  }

  public void testEnumArray() throws Throwable
  {
    String json = "[\"LIGER\",\"TIGON\"]";
    Cats[] cats = exercise(json, Cats[].class);
    assertNotNull(cats);
    assertEquals(Cats.LIGER, cats[0]);
    assertEquals(Cats.TIGON, cats[1]);
  }

  public void testEnumWithoutQuotes() throws Throwable
  {
    String json = "LIGER";
    Cats cats = exercise(json, Cats.class);
    assertNotNull(cats);
    assertEquals(Cats.LIGER, cats);
  }

  public void testEnumArrayWithoutQuotes() throws Throwable
  {
    String json = "[LIGER,TIGON]";
    Cats[] cats = exercise(json, Cats[].class);
    assertNotNull(cats);
    assertEquals(Cats.LIGER, cats[0]);
    assertEquals(Cats.TIGON, cats[1]);
  }

  // ----------------------------------------------------

  private static <T> T exercise(String json, Class<T> clazz) throws Throwable
  {
    return exercise(json, (Type)clazz);
  }

  private static <T> T exercise(String json, Type type) throws Throwable
  {
    StringReader reader = new StringReader(json);
    return Classes.invoke(parser(), "parse", reader, type);
  }

  private static Object parser() throws ClassNotFoundException
  {
    return Classes.newInstance("js.tools.commons.json.Parser");
  }

  private static class Organization
  {
    String name;
    Person leader;
    @SuppressWarnings("unused")
    Department[] departments;
  }

  private static class Department
  {
    String name;
    List<Person> employees;
  }

  private static class Person
  {
    String name;
    int age;

    @SuppressWarnings("unused")
    Person()
    {
    }

    Person(String name, int age)
    {
      this.name = name;
      this.age = age;
    }

    @Override
    public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = prime * result + age;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      if(this == obj) return true;
      if(obj == null) return false;
      if(getClass() != obj.getClass()) return false;
      Person other = (Person)obj;
      if(age != other.age) return false;
      if(name == null) {
        if(other.name != null) return false;
      }
      else if(!name.equals(other.name)) return false;
      return true;
    }
  }

  private static class User
  {
    String[] aliases;
  }

  private static enum Cats
  {
    LIGER, TIGON
  }
}
