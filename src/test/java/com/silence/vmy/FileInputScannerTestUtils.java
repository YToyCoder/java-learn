package com.silence.vmy;

import com.silence.Utils;

import java.io.FileNotFoundException;
import java.util.function.Consumer;

public class FileInputScannerTestUtils {
  private FileInputScannerTestUtils(){}

  public static void do_with_instance(String file, Consumer<Scripts.FileInputScanner> scanner_consumer){
    try(Scripts.FileInputScanner scanner = new Scripts.FileInputScanner(file)) {
      scanner_consumer.accept(scanner);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public static String ofScript(String _name){
    return String.format("%s/%s", Utils.get_dir_of_project("scripts" ), _name);
  }

  public static Consumer<Scripts.FileInputScanner> build_with_scanner(){
    return scanner -> AST.build(scanner);
  }
}
