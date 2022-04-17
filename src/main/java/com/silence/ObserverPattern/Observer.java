package com.silence.ObserverPattern;

public interface Observer {
	void update(Subject subject);
	void regist(Subject subject);
	void unRegist(Subject subject);
}
