package js.tools.commons.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

public class FilesUnitTest extends TestCase
{
  public void testBasename()
  {
    assertEquals("file", Files.basename("/tmp/file.txt"));
    assertEquals("file", Files.basename("/tmp/file"));
    assertEquals("file", Files.basename(new File("/tmp/file.txt")));
    assertEquals("file", Files.basename(new File("/tmp/file")));
    assertEquals("", Files.basename(""));
    assertNull(Files.basename((String)null));
    assertNull(Files.basename((File)null));
  }

  public void testDot2path()
  {
    if(File.separatorChar == '/') {
      assertEquals("js/tools/commons/util/FilesUnitTest", Files.dot2path(FilesUnitTest.class.getName()));
    }
    else {
      assertEquals("js\\tools\\commons\\util\\FilesUnitTest", Files.dot2path(FilesUnitTest.class.getName()));
    }
    assertNull(Files.dot2path(null));
  }

  public void testDot2pathWithExtension()
  {
    if(File.separatorChar == '/') {
      assertEquals("js/tools/commons/util/FilesUnitTest.java", Files.dot2path(FilesUnitTest.class.getName(), ".java"));
      assertEquals("js/tools/commons/util/FilesUnitTest", Files.dot2path(FilesUnitTest.class.getName(), null));
    }
    else {
      assertEquals("js\\tools\\commons\\util\\FilesUnitTest.java", Files.dot2path(FilesUnitTest.class.getName(), ".java"));
      assertEquals("js\\tools\\commons\\util\\FilesUnitTest", Files.dot2path(FilesUnitTest.class.getName(), null));
    }
    assertNull(Files.dot2path(null, ".java"));
    assertNull(Files.dot2path(null, null));
  }

  public void testDot2urlpath()
  {
    if(File.separatorChar == '/') {
      assertEquals("js/tools/commons/util/FilesUnitTest", Files.dot2urlpath(FilesUnitTest.class.getName()));
    }
    else {
      assertEquals("js/tools/commons/util/FilesUnitTest", Files.dot2urlpath(FilesUnitTest.class.getName()));
    }
    assertNull(Files.dot2urlpath(null));
  }

  public void testPath2Unix()
  {
    assertEquals("/temp", Files.path2unix("C:\\temp\\"));
    assertEquals("/temp/file.txt", Files.path2unix("C:\\temp\\file.txt"));
    assertEquals("/temp", Files.path2unix("C:\\\\temp"));
    assertEquals("/temp/C:", Files.path2unix("\\temp\\C:\\"));
    assertNull(Files.path2unix((String)null));
  }

  public void testClose()
  {
    final AtomicInteger probe = new AtomicInteger();
    Files.close(new Closeable()
    {
      @Override
      public void close() throws IOException
      {
        probe.incrementAndGet();
      }
    });
    assertEquals(1, probe.get());

    // close on null should not throw exception
    try {
      Files.close(null);
    }
    catch(Throwable t) {
      fail(t.getMessage());
    }
  }

  public void testGetExtension()
  {
    assertEquals("jpg", Files.getExtension(new File("image.jpg")));
    assertEquals("jpg", Files.getExtension(new File("image.JPG")));
    assertEquals("jpg", Files.getExtension(new File("relative/path/image.jpg")));
    assertEquals("jpg", Files.getExtension(new File("/absolute/path/image.JPG")));
    assertEquals("", Files.getExtension(new File("image")));
    assertEquals("", Files.getExtension(new File("image.")));
    assertEquals("", Files.getExtension(new File("relative/path/image")));
    assertEquals("", Files.getExtension(new File("/absolute/pathimage")));
    assertEquals("", Files.getExtension(new File("/etc/rc.d/file")));
    assertEquals("txt", Files.getExtension(new File("/etc/rc.d/readme.txt")));
    assertEquals("", Files.getExtension(new File("../readme")));

    assertNull(Files.getExtension((String)null));
    assertNull(Files.getExtension((File)null));
  }

  private static final byte[] EXPECTED_FILE_DIGEST = new byte[]
  {
      73, 89, 53, 124, -16, 127, -82, -125, -124, -101, 59, -98, 14, -106, 60, 84
  };

  public void testGetFileDigest() throws Exception
  {
    byte[] fileDigest = Files.getFileDigest(new File("fixture/util/image.png"));
    assertEquals(16, fileDigest.length);
    assertTrue(Arrays.equals(EXPECTED_FILE_DIGEST, fileDigest));
  }

  public void testCopyReaderWriter() throws IOException
  {
    File tmp = createTempFile();
    Files.copy(new FileReader("fixture/util/image.png"), new FileWriter(tmp));
    assertFile(tmp);

    try {
      Files.copy((FileReader)null, new FileWriter(tmp));
      fail("Null reader should rise illegal argument.");
    }
    catch(IllegalArgumentException e) {}
    try {
      Files.copy(new FileReader("fixture/util/image.png"), (FileWriter)null);
      fail("Null writer should rise illegal argument.");
    }
    catch(IllegalArgumentException e) {}
  }

  public void testCopyFileOutputStream() throws IOException
  {
    File tmp = createTempFile();
    Files.copy(new File("fixture/util/image.png"), new FileOutputStream(tmp));
    assertFile(tmp);

    try {
      Files.copy((File)null, new FileOutputStream(tmp));
      fail("Null input file should rise illegal argument.");
    }
    catch(IllegalArgumentException e) {}
    try {
      Files.copy(new File("fixture/util/image.png"), (FileOutputStream)null);
      fail("Null output stream should rise illegal argument.");
    }
    catch(IllegalArgumentException e) {}
  }

  private static File createTempFile() throws IOException
  {
    File tmp = File.createTempFile("test", null);
    tmp.deleteOnExit();
    return tmp;
  }

  private void assertFile(File tmp) throws IOException
  {
    String expected = loadString(new File("fixture/util/image.png"));
    String concrete = loadString(tmp);
    assertEquals(expected, concrete);
  }

  private static String loadString(File file) throws IOException
  {
    Reader reader = new FileReader(file);
    Writer writer = new StringWriter();
    try {
      char[] buffer = new char[1024];
      int count = buffer.length;
      for(;;) {
        int readChars = reader.read(buffer, 0, count);
        if(readChars <= 0) {
          break;
        }
        writer.write(buffer, 0, readChars);
        count -= readChars;
      }
    }
    finally {
      Files.close(reader);
      Files.close(writer);
    }
    return writer.toString();
  }
}
