package com.damo.tools.maven.parse;


public class DataType {
	String name;
	int length;
	int point;
	
	public DataType(String name) {
		this(name,0);
	}
	public DataType(String name, int length) {
		this(name,length,0);
	}
	public DataType(String name, int length, int point) {
		super();
		this.name = name;
		this.length = length;
		this.point = point;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	@Override
	public String toString() {
		return "DataType [name=" + name + ", length=" + length + ", point="
				+ point + "]";
	}	
}
