package com.silence.ObserverPattern;

import java.util.ArrayList;
import java.util.List;

public class AbstractSubject implements Subject {
	List<Observer>  observers = new ArrayList<>();

	/**
	 * 注册一个观察者
	 */
	@Override
	public void registerObserver(Observer observer){
		if(!observers.contains(observer)){
			// 避免重复注册
			observers.add(observer);
			observer.regist(this);
		} 
	}

	/**
	 * 移除一个观察者
	 */
	@Override
	public void removeObserver(Observer observer){
		int ind = observers.indexOf(observer);
		if(ind != -1){
			observers.remove(ind);
			observer.unRegist(this);
		}
	}
	
	@Override
	public void notifyObservers(){
		for(Observer observer : observers){
			observer.update(this);
		}
	}
}
