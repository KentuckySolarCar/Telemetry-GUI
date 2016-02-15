package com.telemetry.custom;

import java.util.LinkedList;

public class SizedQueue<E> extends LinkedList<E> {

	private static final long serialVersionUID = -7440475923928179235L;
	private static final int QUEUE_SIZE = 100;
	
	@Override
	public boolean add(E e) {
		this.addLast(e);
		if(this.size() > QUEUE_SIZE) {
			this.pop();
		}
		return true;
	}
}
