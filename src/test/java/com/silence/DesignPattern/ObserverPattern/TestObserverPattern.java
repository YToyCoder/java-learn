package com.silence.DesignPattern.ObserverPattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestObserverPattern {

	@Test
	public void sunrisePersonWakingup(){
		Person tom = new Person();
		Sunrise sunrise = new Sunrise();
		tom.regist(sunrise);
		assertTrue("tom is sleeping when created", tom.isSleeping());
		sunrise.rise();
		assertTrue("tom wakeup when sun rises", tom.awake());
		tom.sleep();
		assertTrue("manu make tom sleep", tom.isSleeping());
		Person tony = new Person();
		tony.regist(sunrise);
		assertTrue("tony is sleeping when created", tony.isSleeping());
		sunrise.rise();
		assertTrue("people wakeup whene sun rise", tony.awake() && tom.awake());
		assertFalse("people", tony.isSleeping() && tom.isSleeping());
	}

}
