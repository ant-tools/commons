package js.tools.commons.rmi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import js.tools.commons.BugError;
import js.tools.commons.rmi.MockConnectionFactory.OpenConnectionListener;
import js.tools.commons.util.Classes;
import js.tools.commons.util.GType;
import junit.framework.TestCase;

public class HttpRmiUnitTest extends TestCase implements OpenConnectionListener
{
  private MockConnectionFactory factory;
  private MockHttpURLConnection connection;
  private HttpRmi rmi;

  @Override
  public void onConnectionOpened(MockHttpURLConnection connection)
  {
    this.connection = connection;
  }

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    factory = new MockConnectionFactory(this);
  }

  public void testIsJSON() throws Exception
  {
    assertTrue((boolean)Classes.invoke(HttpRmi.class, "isJSON", "application/json"));
    // JSON media type does not require parameters and charset is ignored but implementation accept it
    assertTrue((boolean)Classes.invoke(HttpRmi.class, "isJSON", "application/json; charset=UTF-8"));
    // null content type is accepted as default
    assertTrue((boolean)Classes.invoke(HttpRmi.class, "isJSON", (String)null));
    assertFalse((boolean)Classes.invoke(HttpRmi.class, "isJSON", ""));
    assertFalse((boolean)Classes.invoke(HttpRmi.class, "isJSON", "application/xml"));
  }

  public void testReadJsonObject() throws Exception
  {
    String json = "{\"id\":1964,\"text\":\"message\"}";
    Notification notification = Classes.invoke(HttpRmi.class, "readJsonObject", new ByteArrayInputStream(json.getBytes()), Notification.class);
    assertNotNull(notification);
    assertEquals(1964, notification.id);
    assertEquals("message", notification.text);
    assertNull(notification.timestamp);
  }

  public void testJsonRequest() throws Throwable
  {
    rmi = new HttpRmi(factory, "http://localhost/test/", Service.class);
    rmi.setReturnType(Boolean.class);

    factory.responseStatusCode = 200;
    factory.responseContentType = "application/json";
    factory.responseBody = "true";

    boolean value = rmi.invoke("method", 1, 2);

    assertTrue(value);
    assertEquals("http://localhost/test/js/tools/commons/rmi/HttpRmiUnitTest$Service/method.rmi", connection.getURL().toExternalForm());
    assertEquals("POST", connection.getRequestMethod());
    assertEquals("application/json", connection.getRequestContentType());
    assertEquals("[1,2]", connection.getRequestBody());
  }

  public void testStreamRequest() throws Throwable
  {
    rmi = new HttpRmi(factory, "http://localhost/test/", Service.class);
    rmi.setReturnType(Boolean.class);

    factory.responseStatusCode = 200;
    factory.responseContentType = "application/json";
    factory.responseBody = "true";

    boolean value = rmi.invoke("method", new StreamHandler<OutputStream>(OutputStream.class)
    {
      @Override
      protected void handle(OutputStream outputStream) throws IOException
      {
        outputStream.write("stream".getBytes("UTF-8"));
      }
    });

    assertTrue(value);
    assertEquals("http://localhost/test/js/tools/commons/rmi/HttpRmiUnitTest$Service/method.rmi", connection.getURL().toExternalForm());
    assertEquals("POST", connection.getRequestMethod());
    assertEquals("application/octet-stream", connection.getRequestContentType());
    assertEquals("stream", connection.getRequestBody());
  }

  public void testMixedRequest() throws Throwable
  {
    rmi = new HttpRmi(factory, "http://localhost/test/", Service.class);
    rmi.setReturnType(Boolean.class);

    factory.responseStatusCode = 200;
    factory.responseContentType = "application/json";
    factory.responseBody = "true";

    boolean value = rmi.invoke("method", 1964, new StreamHandler<OutputStream>(OutputStream.class)
    {
      @Override
      protected void handle(OutputStream outputStream) throws IOException
      {
        outputStream.write("stream".getBytes("UTF-8"));
      }
    });

    Object argumentsEncoder = Classes.getFieldValue(rmi, "argumentsWriter");
    String boundary = Classes.getFieldValue(argumentsEncoder, "boundary");

    assertTrue(value);
    assertEquals("http://localhost/test/js/tools/commons/rmi/HttpRmiUnitTest$Service/method.rmi", connection.getURL().toExternalForm());
    assertEquals("POST", connection.getRequestMethod());
    assertEquals(String.format("multipart/mixed; boundary=\"%s\"", boundary), connection.getRequestContentType());

    String expectedRequestBody = "" + //
        "\r\n" + //
        "--%1$s\r\n" + //
        "Content-Disposition: form-data; name=\"0\"\r\n" + //
        "Content-Type: application/json\r\n" + //
        "\r\n" + //
        "1964\r\n" + //
        "--%1$s\r\n" + //
        "Content-Disposition: form-data; name=\"1\"\r\n" + //
        "Content-Type: application/octet-stream\r\n" + //
        "\r\n" + //
        "stream\r\n" + //
        "--%1$s--";
    assertEquals(String.format(expectedRequestBody, boundary), connection.getRequestBody());
  }

  public void testStringsListResponse() throws Exception {
    rmi = new HttpRmi(factory, "http://localhost/test/", Service.class);
    rmi.setReturnType(new GType(List.class, String.class));

    factory.responseStatusCode = 200;
    factory.responseContentType = "application/json";
    factory.responseBody = "[\"index.htm\",\"index.css\",\"index.js\"]";

    List<String> strings = rmi.invoke("method");

    assertNotNull(strings);
    assertEquals(3, strings.size());
    assertEquals("index.htm", strings.get(0));
    assertEquals("index.css", strings.get(1));
    assertEquals("index.js", strings.get(2));
    
    assertEquals("http://localhost/test/js/tools/commons/rmi/HttpRmiUnitTest$Service/method.rmi", connection.getURL().toExternalForm());
    assertEquals("POST", connection.getRequestMethod());
    assertNull(connection.getRequestContentType());
    assertEquals("", connection.getRequestBody());
  }
  
  public void testForbiden() throws Throwable
  {
    rmi = new HttpRmi(factory, "http://localhost/test/", Service.class);

    factory.responseStatusCode = 403;
    factory.responseContentType = null;
    factory.responseBody = null;

    try {
      rmi.invoke("method");
    }
    catch(RmiException e) {
      if(e.getMessage().startsWith("Server refuses to process request")) {
        return;
      }
    }
    fail("Server response 403 should rise RMI exception.");
  }

  public void testCheckedRemoteException() throws Throwable
  {
    rmi = new HttpRmi(factory, "http://localhost/test/", Service.class);
    rmi.setExceptions(new Class<?>[]
    {
      IOException.class
    });

    factory.responseStatusCode = 500;
    factory.responseContentType = "application/json";
    factory.responseBody = "{\"cause\":\"java.io.IOException\",\"message\":\"server exception\"}";

    try {
      rmi.invoke("method");
    }
    catch(IOException e) {
      assertEquals("server exception", e.getMessage());
      return;
    }
    fail("Checked remote exception should rise checked exception.");
  }

  public void testUnauthorized() throws Throwable
  {
    rmi = new HttpRmi(factory, "http://localhost/test/", Service.class);

    factory.responseStatusCode = 401;
    factory.responseContentType = null;
    factory.responseBody = null;

    try {
      rmi.invoke("method");
    }
    catch(RmiException e) {
      if(e.getMessage().startsWith("Attempt to access private")) {
        return;
      }
    }
    fail("Server response 401 should rise RMI exception.");
  }

  public void testNotFound() throws Throwable
  {
    rmi = new HttpRmi(factory, "http://localhost/test/", Service.class);

    factory.responseStatusCode = 404;
    factory.responseContentType = null;
    factory.responseBody = null;

    try {
      rmi.invoke("method");
    }
    catch(RmiException e) {
      if(e.getMessage().startsWith("Method |http://localhost/test/js/tools/commons/rmi/HttpRmiUnitTest$Service/method.rmi| not found")) {
        return;
      }
    }
    fail("Server response 404 should rise RMI exception.");
  }

  public void testBadRequest() throws Throwable
  {
    rmi = new HttpRmi(factory, "http://localhost/test/", Service.class);

    factory.responseStatusCode = 400;
    factory.responseContentType = "application/json";
    factory.responseBody = "{\"errorCode\":1964}";

    try {
      rmi.invoke("method");
    }
    catch(BugError e) {
      assertTrue(e.getMessage().startsWith("HTTP-RMI server execution error"));
      return;
    }
    fail("Checked remote exception should rise checked exception.");
  }

  /**
   * Service not available - 503 is observed to be sent back when front-end HTTP server is up but back-end web server is
   * down.
   */
  public void testServiceUnavailable() throws Throwable
  {
    rmi = new HttpRmi(factory, "http://localhost/test/", Service.class);

    factory.responseStatusCode = 503;
    factory.responseContentType = null;
    factory.responseBody = null;

    try {
      rmi.invoke("method");
    }
    catch(RmiException e) {
      if(e.getMessage().startsWith("Front-end HTTP server is up but back-end is down.")) {
        return;
      }
    }
    fail("Server response 503 should rise RMI exception.");
  }

  /** Server responds with 500 and an exception declared into method signature. */
  public void testInternalServerErrorCheckedException() throws Throwable
  {
    rmi = new HttpRmi(factory, "http://localhost/test/", Service.class);
    rmi.setExceptions(new Class<?>[]
    {
      IOException.class
    });

    factory.responseStatusCode = 500;
    factory.responseContentType = "application/json";
    factory.responseBody = "{\"cause\":\"java.io.IOException\",\"message\":\"Error message.\"}";

    try {
      rmi.invoke("method");
    }
    catch(IOException e) {
      if(e.getMessage().equals("Error message.")) {
        return;
      }
    }
    fail("Server response 500 should rethrow original remote exception.");
  }

  /** Server responds with 500 and an exception not present into method signature. */
  public void testInternalServerErrorUncheckedException() throws Throwable
  {
    rmi = new HttpRmi(factory, "http://localhost/test/", Service.class);

    factory.responseStatusCode = 500;
    factory.responseContentType = "application/json";
    factory.responseBody = "{\"cause\":\"java.io.IOException\",\"message\":\"Error message.\"}";

    try {
      rmi.invoke("method");
    }
    catch(BugError e) {
      assertTrue(e.getMessage().startsWith("HTTP-RMI server execution error"));
      return;
    }
    fail("Unchecked remote exception should rise RMI exception.");
  }

  public void testUnexpectedErrorCode() throws Throwable
  {
    rmi = new HttpRmi(factory, "http://localhost/test/", Service.class);

    factory.responseStatusCode = 567;
    factory.responseContentType = "text/plain;charset=UTF-8";
    factory.responseBody = "Unknown code.";

    try {
      rmi.invoke("method");
    }
    catch(RmiException e) {
      assertTrue(e.getMessage().startsWith("HTTP-RMI error on"));
      return;
    }
    fail("Server response 567 should rise RMI exception.");
  }

  // ------------------------------------------------------
  // FIXTURE

  private static interface Service
  {
  }
}
