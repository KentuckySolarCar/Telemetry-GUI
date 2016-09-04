package com.telemetry.util;

import java.util.LinkedList;

/**
 * Sized-Queue for when calculating latest 100 data points
 * @author Weilian Song
 *
 * @param <E>
 */
public class SizedQueue<E> extends LinkedList<E> {

	private static final long serialVersionUID = -7440475923928179235L;
	private int queue_size;
	
	public SizedQueue(int size) {
		super();
		queue_size = size;
	}
	
	@Override
	public boolean add(E e) {
		this.addLast(e);
		if(this.size() > queue_size) {
			this.pop();
		}
		return true;
	}	
}
