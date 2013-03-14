package learn;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author haojian
 * 
 */
public class MyClassLoader1 extends ClassLoader {

 public MyClassLoader1(ClassLoader parent) {
  super(parent);
 }

 public Class<?> findClass1(String name) throws Exception {
  byte[] bytes = loadClassBytes(name);
  Class theClass = defineClass(null, bytes, 0, bytes.length);
  if (theClass == null)
   throw new ClassFormatError();
  return theClass;
 }

 private byte[] loadClassBytes(String classFile) throws Exception {
//   String classFile = getClassFile();
   FileInputStream fis = new FileInputStream(classFile);
   FileChannel fileC = fis.getChannel();
   ByteArrayOutputStream baos = new ByteArrayOutputStream();
   WritableByteChannel outC = Channels.newChannel(baos);
   ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
   while (true) {
    int i = fileC.read(buffer);
    if (i == 0 || i == -1) {
     break;
    }
    buffer.flip();
    outC.write(buffer);
    buffer.clear();
   }
   fis.close();
   return baos.toByteArray();
  
 }


}

