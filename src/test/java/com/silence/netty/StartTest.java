package com.silence.netty;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import org.junit.Test;

public class StartTest {
  
  @Test
  public void startTest(){
    final Start.HttpServerOne server = new Start.HttpServerOne(8080);
    try {
      server.start();
    } catch (InterruptedException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
      System.out.println("8".repeat(10));
    }

    HttpClient client = HttpClient.newHttpClient();

    try {
      client.send(
        HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080"))
        .GET()
        .build(), 

        java.net.http.HttpResponse.BodyHandlers.ofString()
      );
    }catch(Exception e){
      e.printStackTrace();
      System.out.println("send error");
    }

    try{
      Thread.sleep(2000l);
    }catch(Exception e){
      e.printStackTrace();
    }
  }
}
