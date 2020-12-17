package js.tools.commons.util;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class StringsUnitTest extends TestCase
{
  public void testSplitStringByChar()
  {
    assertEquals(Strings.split("one,two,three,four", ','));
    assertEquals(Strings.split(",one,two,three,four", ','));
    assertEquals(Strings.split("one,two,three,four,", ','));
    assertEquals(Strings.split(",one,two,three,four,", ','));
  }

  public void testSplitTrim()
  {
    assertEquals(Strings.split("  one, two, three, four  ", ','));
    assertEquals(Strings.split("  one  ,  two , three ,  four   ", ','));
  }

  private static void assertEquals(List<String> strings)
  {
    assertEquals(4, strings.size());
    assertEquals("one", strings.get(0));
    assertEquals("two", strings.get(1));
    assertEquals("three", strings.get(2));
    assertEquals("four", strings.get(3));
  }

  public void testSplitPathSegmentsByChar()
  {
    for(String path : new String[]
    {
        "compo/discography", "/compo/discography", "compo/discography/", "/compo/discography/"
    }) {
      List<String> segments = Strings.split(path, '/');
      assertEquals(2, segments.size());
      assertEquals("compo", segments.get(0));
      assertEquals("discography", segments.get(1));
    }
  }

  public void testJoin() throws Throwable
  {
    String[] strings = new String[]
    {
        "", null, "1", null, null, "2", null, "3"
    };
    assertEquals("1 2 3", Strings.join(Arrays.asList(strings), ' '));
    assertEquals("1 2 3", Strings.join(Arrays.asList(strings), " "));
    assertEquals("1 2 3", Strings.join(Arrays.asList(strings), null));
    assertEquals("1,2,3", Strings.join(Arrays.asList(strings), ','));
    assertEquals("1,2,3", Strings.join(Arrays.asList(strings), ","));
    assertEquals("1, 2, 3", Strings.join(Arrays.asList(strings), ", "));
  }

  public void testConcat()
  {
    assertEquals("", Strings.concat());
    assertEquals("", Strings.concat(null, null));
    assertEquals("123", Strings.concat(null, "1", null, null, "2", null, "3"));
  }

  public void testFirstWord()
  {
    assertNull(Strings.firstWord(null));
    assertEquals("", Strings.firstWord(""));
    assertEquals("One", Strings.firstWord("One."));

    assertEquals("One.two", Strings.firstWord("One.two three"));
    assertEquals("One.two", Strings.firstWord("One.two. three"));
    assertEquals("One...", Strings.firstWord("One... two three"));
    assertEquals("(One)", Strings.firstWord("(One) two three"));
    assertEquals("[One]", Strings.firstWord("[One] two three"));
    assertEquals("{One}", Strings.firstWord("{One} two three"));
    assertEquals("<One>", Strings.firstWord("<One> two three"));
    assertEquals("{One. two}", Strings.firstWord("{One. two} three"));

    String[] sentences = new String[]
    {
        "One two three.", "One\ttwo three.", "One\rtwo three.", "One\ntwo three.", "One, two three.", "One: two three.", "One; two three.", "One(two) three.", "One{two} three.", "One[two] three.", "One<two> three."
    };
    for(String s : sentences) {
      assertEquals("One", Strings.firstWord(s));
    }
  }

  public void testRemoveFirstWord()
  {
    assertEquals("two three", Strings.removeFirstWord("One two three"));
    assertEquals("two three", Strings.removeFirstWord("One, two three"));
    assertEquals("two three", Strings.removeFirstWord("One \r\n two three"));
    assertEquals("two three", Strings.removeFirstWord("One\ttwo three"));
  }

  public void testFirstSentence()
  {
    String phrase = "Managed class. Managed class used by...";
    assertEquals("Managed class.", Strings.firstSentence(phrase));

    phrase = "Managed utility.class. Managed class used by...";
    assertEquals("Managed utility.class.", Strings.firstSentence(phrase));

    phrase = "Managed {@link utility.class}. Managed class used by...";
    assertEquals("Managed {@link utility.class}.", Strings.firstSentence(phrase));
  }

  public void testLast()
  {
    assertNull(Strings.last(null, '.'));
    assertTrue(Strings.last("", '.').isEmpty());
    assertEquals("exe", Strings.last("cmd.exe", '.'));
  }

  public void testToMemberName()
  {
    assertEquals("thisIsAString", Strings.toMemberName("this-is-a-string"));
    assertEquals("thisIsAString", Strings.toMemberName("this-is--a-string"));
  }
}
