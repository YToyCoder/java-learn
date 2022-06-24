package com.silence.DesignPattern.ObserverPattern;

import java.util.ArrayList;
import java.util.List;

import com.silence.ObserverPattern.Observer;
import com.silence.ObserverPattern.Subject;

public class Person implements Observer{
	private int status = 0;
	private List<Subject> subjects= new ArrayList<>();

	@Override
	public void update(Subject subject) {
		if(subject instanceof Sunrise){
			wakeup();
		}
	}
	
	public boolean awake(){
		return status == 1;
	}
	
	public void wakeup(){
		status = 1;
	}
	
	public void sleep(){
		status = 0;
	}
	
	public boolean isSleeping(){
		return status == 0;
	}

	@Override
	public void regist(Subject subject) {
		if(!subjects.contains(subject)){
			subject.registerObserver(this);
			subjects.add(subject);
		}
	}

	@Override
	public void unRegist(Subject subject) {
		if(subjects.contains(subject)){
			subject.registerObserver(this);
			subjects.remove(subject);
		}
	}
	
}
