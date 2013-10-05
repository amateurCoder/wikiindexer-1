package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.Serializable;

public class PostingNode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int value;
	private int frequency;
	
	public PostingNode(int valueId, int numOccurances) {
		this.value= valueId;
		this.frequency = numOccurances;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
}
