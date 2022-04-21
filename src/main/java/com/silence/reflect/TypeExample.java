package com.silence.reflect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeExample<T, V extends @Custom Number & Serializable> {
	private Number number;
	public T t;
	public V v;
	public String str;
	public List<T> list = new ArrayList<>();
	public Map<String, T> map = new HashMap<>();
	
	public T[] tArray;
	public List<T>[] ltArray;
	
	public TypeExample typeExample;
	public TypeExample<T, Integer> typeExample2;
	
	public Map<? super String, ? extends Number> mapWithWildcard;
	
	public <X extends Number> TypeExample(X x, T t){
		number = x;
		this.t = t;
	}

	public <Y extends T> void method(Y y){
		t = y;
	}
}
