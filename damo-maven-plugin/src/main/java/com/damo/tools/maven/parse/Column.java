package com.damo.tools.maven.parse;

public class Column extends Table {
	DataType type;

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Column [name=" + name + ", commnet="
				+ commnet + ", type=" + type + "]";
	}
}
