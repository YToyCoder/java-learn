package com.silence.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TypeTest {
	Class<TypeTest> type = TypeTest.class;
	
	@Test
	public void getTypeName(){
		String typename = type.getTypeName();
		assertEquals("com.silence.reflect.TypeTest", typename);
	}

	@Test
	public void getTypeVariable() throws NoSuchFieldException, SecurityException{
		Field v = TypeExample.class.getField("v");
		assertTrue("Field v is TypeVariable 类型", v.getGenericType() instanceof TypeVariable);
		TypeVariable typeVariable = (TypeVariable) v.getGenericType();
		assertEquals("V", typeVariable.toString());
		// 类的上界
		// [class java.lang.Number, interface java.io.Serializable]
		System.out.println("TypeVariable: " + Arrays.asList(typeVariable.getBounds())); 
		// 类型变量的载体
		// class com.silence.reflect.TypeExample
		assertEquals("class com.silence.reflect.TypeExample", typeVariable.getGenericDeclaration().toString());
		//1.8 AnnotatedType: 如果这个这个泛型参数类型的上界用注解标记了，我们可以通过它拿到相应的注解
		//@com.silence.reflect.Custom() java.lang.Number, java.io.Serializable
		AnnotatedType[] annotatedTypes = typeVariable.getAnnotatedBounds();        
		// annotatedTypes[0].getAnnotations() -> @com.silence.reflect.Custom()
		// System.out.println("TypeVariable4:" + Arrays.asList(annotatedTypes) + " : " + Arrays.asList(annotatedTypes[0].getAnnotations()));
		// typeVariable.getName() -> V
		// System.out.println("TypeVariable5:" + typeVariable.getName());
	}
	
	// @Test(expected = TypeNotPresentException.class)
	@Test
	public void typeNotPresentException() throws NoSuchFieldException, SecurityException{
		Field str = TypeExample.class.getField("str");
		Class<?> strTypeVariable =(Class<?>) str.getGenericType();
		assertEquals("public final class java.lang.String", strTypeVariable.toGenericString());
	}

	@Test
	public void parameterizedType() throws NoSuchFieldException, SecurityException{
		Field field = TypeExample.class.getField("list");
		Type type  = field.getGenericType(); // 
		assertTrue("ParameterizedType", type instanceof ParameterizedType);
		Field map = TypeExample.class.getField("map");
		assertTrue("ParameterizedType: Map<K,V>", map.getGenericType() instanceof ParameterizedType);
		ParameterizedType mapType = (ParameterizedType) map.getGenericType();
		//[class java.lang.String, T] 
		System.out.println(Arrays.asList(mapType.getActualTypeArguments()));
		assertEquals("java.util.Map", mapType.getRawType().getTypeName());
		// 没有外部类
		assertNull(mapType.getOwnerType());
	}
	
	@Test
	public void genericArrayType() throws NoSuchFieldException, SecurityException{
		Field tArray = TypeExample.class.getField("tArray");
		assertTrue("tArray is GenericArrayType", tArray.getGenericType() instanceof GenericArrayType);
		GenericArrayType type = (GenericArrayType) tArray.getGenericType();
		assertTrue("tArray GenericComponentType", type.getGenericComponentType() instanceof TypeVariable);
	}

	@Test
	public void theClass(){
		Class<?> type = TypeExample.class;
		assertEquals("com.silence.reflect.TypeExample", type.getName());
		assertTrue("Class.isInstance", String.class.isInstance(" "));
		assertFalse("Class.isInstance", type.isInstance(" "));
		assertTrue("Class.isAssignableFrom", Number.class.isAssignableFrom(Integer.class));
		assertTrue("Class.isInterface", Map.class.isInterface());
		assertFalse("Class.isInterface", type.isInterface());
		assertTrue("Class.isArray : int[].class", int[].class.isArray());
		assertTrue("Class.isPrimitive : Integer.class", int.class.isPrimitive());
		assertFalse("Class.isPrimitive : TypeExample", type.isPrimitive());
		assertTrue("Class.isAnnotation : Override", Override.class.isAnnotation());
		assertEquals("Class.getSuperclass", Integer.class.getSuperclass(), Number.class);
		Class<?> hstype = HashMap.class;
		assertTrue("Class.getGenericSuperclass", hstype.getGenericSuperclass() instanceof ParameterizedType);
	}

	@Test
	public void  theClassEnclosingMethod(){
		class EnclosingMethod {}
		Class<?> cls = new EnclosingMethod().getClass();
		assertEquals("public void com.silence.reflect.TypeTest.theClassEnclosingMethod()", cls.getEnclosingMethod().toString());
	}

	@Test
	public void theClassEnclosingConstructor(){
		EnclosingConstructor enclosingConstructor = new EnclosingConstructor();
		Object obj = enclosingConstructor.val;
		Class<?> cls = obj.getClass();
		Constructor<?> constructor = cls.getEnclosingConstructor();
		assertEquals("public com.silence.reflect.TypeTest$EnclosingConstructor(com.silence.reflect.TypeTest)", constructor.toString());
	}

	class EnclosingConstructor {
		public Object val;
		public EnclosingConstructor(){
			class Inclass{}
			val = new Inclass();
		}
	}

	@Test
	public void getDeclaringClass(){
		Class<?> cls = new EnclosingConstructor().getClass();
		Class<?> declare = cls.getDeclaringClass();
		assertNotNull(declare);
	}
}
