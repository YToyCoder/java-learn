package com.silence.DesignPattern.ObserverPattern;

import com.silence.ObserverPattern.AbstractSubject;

public class Sunrise extends AbstractSubject {
	public void rise(){
		notifyObservers();
	}
}
