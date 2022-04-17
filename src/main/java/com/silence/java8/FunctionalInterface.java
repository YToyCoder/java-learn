package com.silence.java8;

public class FunctionalInterface {
	public FunctionalInterface(){
	}
	
	public FunctionalInterface(String msg){
	}
	
	public interface DefaultFactory<T> {
		T create();
	}
	
	public interface OneParamFactory<T,P> {
		T create(P p);
	}
}
