package com.example.draganddropphotos.views;

import java.util.LinkedList;

public class LinkedLimitedList<T> extends LinkedList<T> {

	private static final long serialVersionUID = 3209044135708646058L;

	@Override
	public boolean add(T object) {
		if(size()<2) {
			return super.add(object);
		} else {
			removeLast();
			return super.add(object);
		}
	}
	
}
