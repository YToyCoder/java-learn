package com.silence;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class NioTest {

  static String project_root_dir = System.getProperty("user.dir");
  static final String Readme = full_path("README.md");

  @Test
  public void read_file(){
    byte[] bytes = new byte[20];
    try(FileInputStream fileInputStream = new FileInputStream(Readme)){
      FileChannel channel = fileInputStream.getChannel();
      ByteBuffer buffer = ByteBuffer.wrap(bytes);
      while (channel.read(buffer) > 0){
        buffer.flip();
        while(buffer.position() != buffer.limit())
          System.out.print((char)buffer.get());
        buffer.clear();
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void bytebuffer_test(){
    ByteBuffer buffer = ByteBuffer.wrap(new byte[1028]);
    byte[] bs = "hello, world".getBytes(StandardCharsets.UTF_8);
//    "".getBytes(StandardCharsets.US_ASCII)
//    new StringReader("hello, world")
    try( ReadableByteChannel channel = Channels.newChannel(
        new ByteArrayInputStream(bs)
    )) {
      System.out.println(buffer.position());
      System.out.println(buffer.limit());
      channel.read(buffer);
      System.out.println(buffer.position());
      System.out.println(buffer.limit());
      buffer.flip();
      System.out.println(buffer.position());
      System.out.println(buffer.limit());
      System.out.println(buffer.get(0));
      System.out.println(buffer.position());
      System.out.println(buffer.limit());

      System.out.println("clear");
      buffer.clear();
      System.out.println(buffer.position());
      System.out.println(buffer.limit());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void char_test(){
    System.out.println(Integer.valueOf('h'));
    System.out.println(Integer.toBinaryString( 'h' & 0x0F));
    System.out.println(Integer.toBinaryString(Integer.valueOf('h')));
    System.out.println(Objects.equals(Character.toString(39), "'"));
  }


  static String full_path(String name){
    return project_root_dir + "/" + name;
  }

}
