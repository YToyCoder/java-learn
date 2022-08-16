package com.silence.vmy;

import java.io.FileNotFoundException;
import java.util.function.Consumer;

public class FileInputScannerTestUtils {
  private FileInputScannerTestUtils(){}

  public static void do_with_instance(String file, Consumer<Scripts.FileInputScanner> scanner_consumer){
    try(Scripts.FileInputScanner scanner = new Scripts.FileInputScanner(file)) {
      scanner_consumer.accept(scanner);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
