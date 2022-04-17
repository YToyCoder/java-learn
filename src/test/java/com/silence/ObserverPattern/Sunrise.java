package com.silence.ObserverPattern;

public class Sunrise extends AbstractSubject {
	public void rise(){
		notifyObservers();
	}
}
