package com.silence;

import java.util.Collection;

public class Utils {
  public static final String project_dir = System.getProperty("user.dir");

  public static final String get_dir_of_project(String name){
    return String.format("%s/%s", project_dir, name);
  }

  public static <T> String collection_to_string(Collection<T> collection){
    final StringBuilder builder = new StringBuilder("[");
    for(T el : collection){
      builder.append(el.toString()).append(" ");
    }
    builder.append("]");
    return builder.toString();
  }
}
