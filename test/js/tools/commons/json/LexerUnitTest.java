package js.tools.commons.json;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import js.tools.commons.util.Classes;
import js.tools.commons.util.Strings;
import junit.framework.TestCase;

public class LexerUnitTest extends TestCase
{
  private static final Class<?> tokenClass = Classes.forName("js.tools.commons.json.Token");
  private static final int LEFT_BRACE = Classes.getFieldValue(tokenClass, "LEFT_BRACE");
  private static final int RIGHT_BRACE = Classes.getFieldValue(tokenClass, "RIGHT_BRACE");
  private static final int LEFT_SQUARE = Classes.getFieldValue(tokenClass, "LEFT_SQUARE");
  private static final int RIGHT_SQUARE = Classes.getFieldValue(tokenClass, "RIGHT_SQUARE");
  private static final int NAME = Classes.getFieldValue(tokenClass, "NAME");
  private static final int VALUE = Classes.getFieldValue(tokenClass, "VALUE");
  private static final int COLON = Classes.getFieldValue(tokenClass, "COLON");
  private static final int ITEM = Classes.getFieldValue(tokenClass, "ITEM");
  private static final int COMMA = Classes.getFieldValue(tokenClass, "COMMA");
  private static final int EOF = Classes.getFieldValue(tokenClass, "EOF");

  public void testDelimiters() throws Exception
  {
    for(String json : new String[]
    {
        "{[:,]}", " \r\n\t{ \r\n\t[ \r\n\t: \r\n\t, \r\n\t] \r\n\t} \r\n\t"
    }) {
      TokenValue[] tokens = exercise(json);

      assertEquals(LEFT_BRACE, tokens[0].token);
      assertEquals(LEFT_SQUARE, tokens[1].token);
      assertEquals(COLON, tokens[2].token);
      assertEquals(COMMA, tokens[3].token);
      assertEquals(RIGHT_SQUARE, tokens[4].token);
      assertEquals(RIGHT_BRACE, tokens[5].token);

      for(TokenValue token : tokens) {
        assertNull(token.value);
      }
    }
  }

  public void testStringValue() throws Exception
  {
    for(String json : new String[]
    {
        "\"John Doe\"", " \r\n\t\"John Doe\" \r\n\t"
    }) {
      TokenValue[] tokens = exercise(json);

      assertEquals(1, tokens.length);
      assertEquals(VALUE, tokens[0].token);
      assertEquals("John Doe", tokens[0].value);
    }
  }

  public void testEscapedStringValue() throws Exception
  {
    String json = "\"\\\"\\/\\b\\f\\n\\r\\t\\u00A9\"";
    TokenValue[] tokens = exercise(json);

    assertEquals(1, tokens.length);
    assertEquals(VALUE, tokens[0].token);
    assertEquals("\"/\b\f\n\r\t©", tokens[0].value);
  }

  public void testNumberValue() throws Exception
  {
    for(String json : new String[]
    {
        "1234.56", " \r\n\t1234.56 \r\n\t"
    }) {
      TokenValue[] tokens = exercise(json);

      assertEquals(1, tokens.length);
      assertEquals(VALUE, tokens[0].token);
      assertEquals("1234.56", tokens[0].value);
    }
  }

  public void testHexadecimalNumberValue() throws Exception
  {
    for(String json : new String[]
    {
        "0x1234", " \r\n\t0x1234 \r\n\t"
    }) {
      TokenValue[] tokens = exercise(json);

      assertEquals(1, tokens.length);
      assertEquals(VALUE, tokens[0].token);
      assertEquals("0x1234", tokens[0].value);
    }
  }

  public void testBooleanValue() throws Exception
  {
    for(String json : new String[]
    {
        "true", " \r\n\ttrue \r\n\t"
    }) {
      TokenValue[] tokens = exercise(json);
      assertEquals(1, tokens.length);
      assertEquals(VALUE, tokens[0].token);
      assertEquals("true", tokens[0].value);
    }

    for(String json : new String[]
    {
        "false", " \r\n\tfalse \r\n\t"
    }) {
      TokenValue[] tokens = exercise(json);
      assertEquals(1, tokens.length);
      assertEquals(VALUE, tokens[0].token);
      assertEquals("false", tokens[0].value);
    }
  }

  public void testNullValue() throws Exception
  {
    for(String json : new String[]
    {
        "null", " \r\n\tnull \r\n\t"
    }) {
      TokenValue[] tokens = exercise(json);

      assertEquals(1, tokens.length);
      assertEquals(VALUE, tokens[0].token);
      assertNull(tokens[0].value);
    }
  }

  public void testFlatObject() throws Exception
  {
    for(String json : new String[]
    {
        "{\"name\":\"John Doe\",\"picture\":\"picture.png\"}", "{name:\"John Doe\",picture:\"picture.png\"}", " \r\n\t{ \r\n\t\"name\" \r\n\t: \r\n\t\"John Doe\" \r\n\t, \r\n\t\"picture\" \r\n\t: \r\n\t\"picture.png\" \r\n\t} \r\n\t", " \r\n\t{ \r\n\tname \r\n\t: \r\n\t\"John Doe\" \r\n\t, \r\n\tpicture \r\n\t: \r\n\t\"picture.png\" \r\n\t} \r\n\t"
    }) {
      TokenValue[] tokens = exercise(json);

      assertEquals(9, tokens.length);

      assertEquals(LEFT_BRACE, tokens[0].token);
      assertNull(tokens[0].value);

      assertEquals(NAME, tokens[1].token);
      assertEquals("name", tokens[1].value);

      assertEquals(COLON, tokens[2].token);
      assertNull(tokens[2].value);

      assertEquals(VALUE, tokens[3].token);
      assertEquals("John Doe", tokens[3].value);

      assertEquals(COMMA, tokens[4].token);
      assertNull(tokens[4].value);

      assertEquals(NAME, tokens[5].token);
      assertEquals("picture", tokens[5].value);

      assertEquals(COLON, tokens[6].token);
      assertNull(tokens[6].value);

      assertEquals(VALUE, tokens[7].token);
      assertEquals("picture.png", tokens[7].value);

      assertEquals(RIGHT_BRACE, tokens[8].token);
      assertNull(tokens[8].value);
    }
  }

  public void testFlatArray() throws Exception
  {
    for(String json : new String[]
    {
        "[\"John Doe\",\"picture.png\"]", " \r\n\t[ \r\n\t\"John Doe\" \r\n\t, \r\n\t\"picture.png\" \r\n\t] \r\n\t"
    }) {
      TokenValue[] tokens = exercise(json);

      assertEquals(5, tokens.length);

      assertEquals(LEFT_SQUARE, tokens[0].token);
      assertNull(tokens[0].value);

      assertEquals(ITEM, tokens[1].token);
      assertEquals("John Doe", tokens[1].value);

      assertEquals(COMMA, tokens[2].token);
      assertNull(tokens[2].value);

      assertEquals(ITEM, tokens[3].token);
      assertEquals("picture.png", tokens[3].value);

      assertEquals(RIGHT_SQUARE, tokens[4].token);
      assertNull(tokens[4].value);
    }
  }

  public void testNestedObjects() throws Exception
  {
    for(String json : new String[]
    {
        "{\"person\":{\"name\":\"John Doe\",\"picture\":\"picture.png\"}}", "{person:{name:\"John Doe\",picture:\"picture.png\"}}", " \r\n\t{ \r\n\t\"person\" \r\n\t: \r\n\t{ \r\n\t\"name\" \r\n\t: \r\n\t\"John Doe\" \r\n\t, \r\n\t\"picture\" \r\n\t: \r\n\t\"picture.png\" \r\n\t} \r\n\t} \r\n\t", " \r\n\t{ \r\n\tperson \r\n\t: \r\n\t{ \r\n\tname \r\n\t: \r\n\t\"John Doe\" \r\n\t, \r\n\tpicture \r\n\t: \r\n\t\"picture.png\" \r\n\t} \r\n\t} \r\n\t"
    }) {
      TokenValue[] tokens = exercise(json);

      assertEquals(13, tokens.length);

      assertEquals(LEFT_BRACE, tokens[0].token);
      assertNull(tokens[0].value);

      assertEquals(NAME, tokens[1].token);
      assertEquals("person", tokens[1].value);

      assertEquals(COLON, tokens[2].token);
      assertNull(tokens[2].value);

      assertEquals(LEFT_BRACE, tokens[3].token);
      assertNull(tokens[3].value);

      assertEquals(NAME, tokens[4].token);
      assertEquals("name", tokens[4].value);

      assertEquals(COLON, tokens[5].token);
      assertNull(tokens[5].value);

      assertEquals(VALUE, tokens[6].token);
      assertEquals("John Doe", tokens[6].value);

      assertEquals(COMMA, tokens[7].token);
      assertNull(tokens[7].value);

      assertEquals(NAME, tokens[8].token);
      assertEquals("picture", tokens[8].value);

      assertEquals(COLON, tokens[9].token);
      assertNull(tokens[9].value);

      assertEquals(VALUE, tokens[10].token);
      assertEquals("picture.png", tokens[10].value);

      assertEquals(RIGHT_BRACE, tokens[11].token);
      assertNull(tokens[11].value);

      assertEquals(RIGHT_BRACE, tokens[12].token);
      assertNull(tokens[12].value);
    }
  }

  public void testNestedArrays() throws Exception
  {
    for(String json : new String[]
    {
        "[\"person\",[\"John Doe\",\"picture.png\"]]", " \r\n\t[ \r\n\t\"person\" \r\n\t, \r\n\t[ \r\n\t\"John Doe\" \r\n\t, \r\n\t\"picture.png\" \r\n\t] \r\n\t] \r\n\t"
    }) {
      TokenValue[] tokens = exercise(json);

      assertEquals(9, tokens.length);

      assertEquals(LEFT_SQUARE, tokens[0].token);
      assertNull(tokens[0].value);

      assertEquals(ITEM, tokens[1].token);
      assertEquals("person", tokens[1].value);

      assertEquals(COMMA, tokens[2].token);
      assertNull(tokens[2].value);

      assertEquals(LEFT_SQUARE, tokens[3].token);
      assertNull(tokens[3].value);

      assertEquals(ITEM, tokens[4].token);
      assertEquals("John Doe", tokens[4].value);

      assertEquals(COMMA, tokens[5].token);
      assertNull(tokens[5].value);

      assertEquals(ITEM, tokens[6].token);
      assertEquals("picture.png", tokens[6].value);

      assertEquals(RIGHT_SQUARE, tokens[7].token);
      assertNull(tokens[7].value);

      assertEquals(RIGHT_SQUARE, tokens[8].token);
      assertNull(tokens[8].value);
    }
  }

  public void testComplexGraph() throws Exception
  {
    String[] probes = new String[2];
    probes[0] = "{\"person\":{\"name\":\"John Doe\",\"picture\":\"picture.png\"},\"children\":[\"Adam\",{\"name\":\"Eva\",\"genre\":\"female\"}],\"age\":120,\"id\":0x10}";
    probes[1] = Strings.load(new File("fixture/json/person.json"));

    for(String json : probes) {
      TokenValue[] tokens = exercise(json);

      assertEquals(37, tokens.length);

      assertEquals(LEFT_BRACE, tokens[0].token);
      assertNull(tokens[0].value);

      assertEquals(NAME, tokens[1].token);
      assertEquals("person", tokens[1].value);

      assertEquals(COLON, tokens[2].token);
      assertNull(tokens[2].value);

      assertEquals(LEFT_BRACE, tokens[3].token);
      assertNull(tokens[3].value);

      assertEquals(NAME, tokens[4].token);
      assertEquals("name", tokens[4].value);

      assertEquals(COLON, tokens[5].token);
      assertNull(tokens[5].value);

      assertEquals(VALUE, tokens[6].token);
      assertEquals("John Doe", tokens[6].value);

      assertEquals(COMMA, tokens[7].token);
      assertNull(tokens[7].value);

      assertEquals(NAME, tokens[8].token);
      assertEquals("picture", tokens[8].value);

      assertEquals(COLON, tokens[9].token);
      assertNull(tokens[9].value);

      assertEquals(VALUE, tokens[10].token);
      assertEquals("picture.png", tokens[10].value);

      assertEquals(RIGHT_BRACE, tokens[11].token);
      assertNull(tokens[11].value);

      assertEquals(COMMA, tokens[12].token);
      assertNull(tokens[12].value);

      assertEquals(NAME, tokens[13].token);
      assertEquals("children", tokens[13].value);

      assertEquals(COLON, tokens[14].token);
      assertNull(tokens[14].value);

      assertEquals(LEFT_SQUARE, tokens[15].token);
      assertNull(tokens[15].value);

      assertEquals(ITEM, tokens[16].token);
      assertEquals("Adam", tokens[16].value);

      assertEquals(COMMA, tokens[17].token);
      assertNull(tokens[17].value);

      assertEquals(LEFT_BRACE, tokens[18].token);
      assertNull(tokens[18].value);

      assertEquals(NAME, tokens[19].token);
      assertEquals("name", tokens[19].value);

      assertEquals(COLON, tokens[20].token);
      assertNull(tokens[20].value);

      assertEquals(VALUE, tokens[21].token);
      assertEquals("Eva", tokens[21].value);

      assertEquals(COMMA, tokens[22].token);
      assertNull(tokens[22].value);

      assertEquals(NAME, tokens[23].token);
      assertEquals("genre", tokens[23].value);

      assertEquals(COLON, tokens[24].token);
      assertNull(tokens[24].value);

      assertEquals(VALUE, tokens[25].token);
      assertEquals("female", tokens[25].value);

      assertEquals(RIGHT_BRACE, tokens[26].token);
      assertNull(tokens[26].value);

      assertEquals(RIGHT_SQUARE, tokens[27].token);
      assertNull(tokens[27].value);

      assertEquals(COMMA, tokens[28].token);
      assertNull(tokens[28].value);

      assertEquals(NAME, tokens[29].token);
      assertEquals("age", tokens[29].value);

      assertEquals(COLON, tokens[30].token);
      assertNull(tokens[30].value);

      assertEquals(VALUE, tokens[31].token);
      assertEquals("120", tokens[31].value);

      assertEquals(COMMA, tokens[32].token);
      assertNull(tokens[32].value);

      assertEquals(NAME, tokens[33].token);
      assertEquals("id", tokens[33].value);

      assertEquals(COLON, tokens[34].token);
      assertNull(tokens[34].value);

      assertEquals(VALUE, tokens[35].token);
      assertEquals("0x10", tokens[35].value);

      assertEquals(RIGHT_BRACE, tokens[36].token);
      assertNull(tokens[36].value);
    }
  }

  /**
   * Exercise lexer. Returns array of token value instances; do not use token directly because token enumeration
   * instance is singleton, that is a single instance per constant - token type and on multiple tokens of the same type
   * all will have the same value.
   * 
   * @param json
   * @return
   * @throws ClassNotFoundException
   */
  private static TokenValue[] exercise(String json) throws Exception
  {
    List<TokenValue> tokens = new ArrayList<TokenValue>();
    Object lexer = Classes.newInstance("js.tools.commons.json.Lexer", new StringReader(json));
    try {
      for(;;) {
        Object token = Classes.invoke(lexer, "read");
        int ordinal = Classes.getFieldValue(token, "ordinal");
        if(ordinal == EOF) break;
        tokens.add(new TokenValue(token));
      }
    }
    catch(Throwable e) {
      e.printStackTrace();
    }
    return (TokenValue[])tokens.toArray(new TokenValue[tokens.size()]);
  }

  private static enum Token
  {
    NAME;
    String value;
  }

  public void testEnumBehaviour()
  {
    // this test case does not actually test lexer functionality but prove token syntax rationale

    // enumeration user defined fields have instance scope, of course if not declared static
    // every constant is an instance; in bellow snippet t1 and t2 are equals - the same instance
    // so t1.value and t2.value are in fact the same and the second assignment override the first
    // despite the appearance that we assign to different object

    Token t1 = Token.NAME;
    t1.value = "one";

    Token t2 = Token.NAME;
    t2.value = "two";

    assertEquals("two", t1.value);
    assertEquals("two", t1.value);
    assertEquals(t1.value, t2.value);
  }

  public void testWhiteSpaces() throws Exception
  {
    String json = "{\r\n\t\"name\" : \"John Doe\",\r\n\t\"picture\": \"picture.png\",\r\n\t\"id\" : 0x10\r\n}";
    TokenValue[] tokens = exercise(json);

    assertEquals(13, tokens.length);

    assertEquals(LEFT_BRACE, tokens[0].token);
    assertNull(tokens[0].value);

    assertEquals(NAME, tokens[1].token);
    assertEquals("name", tokens[1].value);

    assertEquals(COLON, tokens[2].token);
    assertNull(tokens[2].value);

    assertEquals(VALUE, tokens[3].token);
    assertEquals("John Doe", tokens[3].value);

    assertEquals(COMMA, tokens[4].token);
    assertNull(tokens[4].value);

    assertEquals(NAME, tokens[5].token);
    assertEquals("picture", tokens[5].value);

    assertEquals(COLON, tokens[6].token);
    assertNull(tokens[6].value);

    assertEquals(VALUE, tokens[7].token);
    assertEquals("picture.png", tokens[7].value);

    assertEquals(COMMA, tokens[8].token);
    assertNull(tokens[8].value);

    assertEquals(NAME, tokens[9].token);
    assertEquals("id", tokens[9].value);

    assertEquals(COLON, tokens[10].token);
    assertNull(tokens[10].value);

    assertEquals(VALUE, tokens[11].token);
    assertEquals("0x10", tokens[11].value);

    assertEquals(RIGHT_BRACE, tokens[12].token);
    assertNull(tokens[12].value);
  }
}

class TokenValue
{
  int token;
  String value;

  TokenValue(Object token)
  {
    this.token = Classes.getFieldValue(token, "ordinal");
    this.value = Classes.getFieldValue(token, "value");
  }
}