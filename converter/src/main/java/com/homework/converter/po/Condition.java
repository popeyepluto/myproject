package com.homework.converter.po;

import java.util.List;

import com.homework.converter.po.OperatorType;

public class Condition {
	
	private String field;
	private OperatorType operator;
	private List values;
	
	
	public List getValues() {
		return values;
	}
	public void setValues(List values) {
		this.values = values;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public OperatorType getOperator() {
		return operator;
	}
	public void setOperator(OperatorType operator) {
		this.operator = operator;
	}



}
