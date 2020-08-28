package com.damo.tools.maven.parse;

import java.util.LinkedHashMap;

public class Table {
	String name;
	String commnet;
	private LinkedHashMap<String,Column> columns;
	public Table() {
		columns = new LinkedHashMap<String,Column>();
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCommnet() {
		return commnet;
	}

	public void setCommnet(String commnet) {
		this.commnet = commnet;
	}

    public LinkedHashMap<String,Column> getColumns() {
        return columns;
    }
    
    public void addColumn(Column col){
    	columns.put(col.getName(), col);
    }
	@Override
	public String toString() {
		return "Table [name=" + name + ", commnet=" + commnet + ", columns =" + columns +"]";
	}
}
