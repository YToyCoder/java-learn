package com.silence;

public class Utils {
  public static final String project_dir = System.getProperty("user.dir");

  public static final String get_dir_of_project(String name){
    return String.format("%s/%s", project_dir, name);
  }
}
