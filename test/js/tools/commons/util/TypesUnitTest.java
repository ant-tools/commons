package js.tools.commons.util;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

public class TypesUnitTest extends TestCase
{
  public void testEqualsAny()
  {
    assertTrue(Types.equalsAny(Date.class, Object.class, Date.class));
    assertFalse(Types.equalsAny(String.class, Object.class, Date.class));
    assertFalse(Types.equalsAny(null, Object.class));
  }

  public void testIsVoid()
  {
    assertTrue(Types.isVoid(Void.class));
    assertTrue(Types.isVoid(Void.TYPE));
    assertTrue(Types.isVoid(void.class));
    assertFalse(Types.isVoid(Object.class));
  }

  public void testIsInstanceOf()
  {
    assertTrue(Types.isInstanceOf(new Date()
    {
      private static final long serialVersionUID = -5044225237647579457L;
    }, Date.class));

    assertTrue(Types.isInstanceOf(new Boolean("true"), boolean.class));
    assertTrue(Types.isInstanceOf(true, Boolean.class));
    assertTrue(Types.isInstanceOf(new Boolean("true"), Boolean.class));

    assertTrue(Types.isInstanceOf(new Byte((byte)1), byte.class));
    assertTrue(Types.isInstanceOf((byte)1, Byte.class));
    assertTrue(Types.isInstanceOf(new Byte((byte)1), Byte.class));

    assertTrue(Types.isInstanceOf(new Character('a'), char.class));
    assertTrue(Types.isInstanceOf('a', Character.class));
    assertTrue(Types.isInstanceOf(new Character('a'), Character.class));

    assertTrue(Types.isInstanceOf(new Short((short)1), short.class));
    assertTrue(Types.isInstanceOf((short)1, Short.class));
    assertTrue(Types.isInstanceOf(new Short((short)1), Short.class));

    assertTrue(Types.isInstanceOf(new Integer(1), int.class));
    assertTrue(Types.isInstanceOf(1, Integer.class));
    assertTrue(Types.isInstanceOf(new Integer(1), Integer.class));

    assertTrue(Types.isInstanceOf(new Long(1L), long.class));
    assertTrue(Types.isInstanceOf(1L, Long.class));
    assertTrue(Types.isInstanceOf(new Long(1L), Long.class));

    assertTrue(Types.isInstanceOf(new Float(1.0F), float.class));
    assertTrue(Types.isInstanceOf(1.0F, Float.class));
    assertTrue(Types.isInstanceOf(new Float(1.0F), Float.class));

    assertTrue(Types.isInstanceOf(new Double(1.0), double.class));
    assertTrue(Types.isInstanceOf(1.0, Double.class));
    assertTrue(Types.isInstanceOf(new Double(1.0), Double.class));

    assertFalse(Types.isInstanceOf(null, boolean.class));
    assertFalse(Types.isInstanceOf(null, Boolean.class));
  }

  public void testIsBoolean()
  {
    assertTrue(Types.isBoolean(true));
    assertTrue(Types.isBoolean(false));
    assertFalse(Types.isBoolean(""));
    assertFalse(Types.isBoolean(new Date()));
    assertFalse(Types.isBoolean(null));
    assertTrue(Types.isBoolean(boolean.class));
    assertTrue(Types.isBoolean(Boolean.class));
    assertFalse(Types.isBoolean(Object.class));
  }

  public void testIsNumber()
  {
    assertTrue(Types.isNumber((byte)1));
    assertTrue(Types.isNumber((short)1));
    assertTrue(Types.isNumber(1));
    assertTrue(Types.isNumber(1L));
    assertTrue(Types.isNumber(1.0F));
    assertTrue(Types.isNumber(1.0));
    assertTrue(Types.isNumber(Number.class));
    assertTrue(Types.isNumber(Byte.class));
    assertTrue(Types.isNumber(Integer.class));
    assertTrue(Types.isNumber(Long.class));
    assertTrue(Types.isNumber(Float.class));
    assertTrue(Types.isNumber(Double.class));
    assertTrue(Types.isNumber(BigDecimal.class));

    assertFalse(Types.isNumber('c'));
    assertFalse(Types.isNumber(""));
    assertFalse(Types.isNumber(null));
    assertFalse(Types.isNumber(new Object()));
    assertFalse(Types.isNumber(Object.class));
  }

  public void testIsCharacter()
  {
    assertTrue(Types.isCharacter(char.class));
    assertTrue(Types.isCharacter(Character.class));
    assertFalse(Types.isCharacter(Object.class));
    assertFalse(Types.isCharacter(null));
  }

  public void testIsEnum()
  {
    assertTrue(Types.isEnum(Order.class));
    assertFalse(Types.isEnum(Enum.class));
    assertFalse(Types.isEnum(null));
    assertFalse(Types.isEnum(Object.class));
  }

  public void testIsDate()
  {
    assertTrue(Types.isDate(Date.class));
    assertTrue(Types.isDate(java.sql.Date.class));
    assertTrue(Types.isDate(Time.class));
    assertTrue(Types.isDate(Timestamp.class));
    assertTrue(Types.isDate(new Date()
    {
      private static final long serialVersionUID = -5044225237647579457L;
    }.getClass()));

    assertFalse(Types.isDate(null));
  }

  public void testIsPrimitiveLike()
  {
    assertTrue(Types.isPrimitiveLike(Number.class));
    assertTrue(Types.isPrimitiveLike(Byte.class));
    assertTrue(Types.isPrimitiveLike(Integer.class));
    assertTrue(Types.isPrimitiveLike(Long.class));
    assertTrue(Types.isPrimitiveLike(Float.class));
    assertTrue(Types.isPrimitiveLike(Double.class));
    assertTrue(Types.isPrimitiveLike(BigDecimal.class));

    assertTrue(Types.isPrimitiveLike(boolean.class));
    assertTrue(Types.isPrimitiveLike(Boolean.class));

    assertTrue(Types.isPrimitiveLike(Order.class));

    assertTrue(Types.isPrimitiveLike(Date.class));
    assertTrue(Types.isPrimitiveLike(java.sql.Date.class));
    assertTrue(Types.isPrimitiveLike(Time.class));
    assertTrue(Types.isPrimitiveLike(Timestamp.class));
    assertTrue(Types.isPrimitiveLike(new Date()
    {
      private static final long serialVersionUID = -5044225237647579457L;
    }.getClass()));

    assertTrue(Types.isPrimitiveLike(char.class));
    assertTrue(Types.isPrimitiveLike(Character.class));

    assertTrue(Types.isPrimitiveLike(String.class));

    assertFalse(Types.isPrimitiveLike(Object.class));
    assertFalse(Types.isPrimitiveLike(Enum.class));
    assertFalse(Types.isPrimitiveLike(null));
  }

  public void testIsNullPrimitive()
  {
    assertFalse(Types.isBoolean(null));
    assertFalse(Types.isCharacter(null));
    assertFalse(Types.isDate(null));
    assertFalse(Types.isEnum(null));
    assertFalse(Types.isNumber(null));
  }

  public void testIsArray()
  {
    assertTrue(Types.isArray(int[].class));
    assertFalse(Types.isArray(List.class));
    assertFalse(Types.isArray(null));
  }

  @SuppressWarnings("rawtypes")
  public void testIsArrayLike()
  {
    int[] array = new int[1];
    assertTrue(Types.isArrayLike(array));
    assertTrue(Types.isArrayLike(array.getClass()));

    Collection collection = Collections.emptyList();
    assertTrue(Types.isArrayLike(collection));
    assertTrue(Types.isArrayLike(collection.getClass()));

    List list = new ArrayList();
    assertTrue(Types.isArrayLike(list));
    assertTrue(Types.isArrayLike(list.getClass()));

    Set set = new HashSet();
    assertTrue(Types.isArrayLike(set));
    assertTrue(Types.isArrayLike(set.getClass()));

    Map map = new HashMap();
    assertFalse(Types.isArrayLike(map));
    assertFalse(Types.isArrayLike(map.getClass()));
  }

  public void testIsCollection()
  {
    assertTrue(Types.isCollection(List.class));
    assertTrue(Types.isCollection(Collections.emptyList()));
    assertTrue(Types.isCollection(ArrayList.class));
    assertFalse(Types.isCollection(Map.class));
    assertFalse(Types.isCollection(new int[0]));
    assertFalse(Types.isCollection(int[].class));
    assertFalse(Types.isCollection(null));
    assertFalse(Types.isCollection(""));
  }

  public void testIsMap()
  {
    assertTrue(Types.isMap(Map.class));
    assertTrue(Types.isMap(HashMap.class));
    assertTrue(Types.isMap(Collections.emptyMap()));
    assertFalse(Types.isMap(List.class));
    assertFalse(Types.isMap(null));
    assertFalse(Types.isMap(""));
  }

  public void testAsIterable()
  {
    Iterator<?> it = Types.asIterable(new int[]
    {
        1, 2
    }).iterator();
    assertEquals(1, (int)it.next());
    assertEquals(2, (int)it.next());
    try {
      it.next();
      fail("Overbounds iteration should rise exception.");
    }
    catch(ArrayIndexOutOfBoundsException e) {}

    List<Integer> list = new ArrayList<Integer>();
    list.add(1);
    list.add(2);

    it = Types.asIterable(list).iterator();
    assertEquals(1, (int)it.next());
    assertEquals(2, (int)it.next());
    assertFalse(it.hasNext());

    it = Types.asIterable(true).iterator();
    assertFalse(it.hasNext());

    it = Types.asIterable(null).iterator();
    assertFalse(it.hasNext());
  }

  private static enum Order
  {
    ONE, TWO
  }
}
