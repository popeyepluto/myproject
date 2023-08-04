package com.homework.converter.po;

public enum OperatorType {

	like("like"), 
	notLike("like"),
	equals("="),
	notEquals("!="),
	lessThan("<"),
	lessThanOrEqualTo("<="),
	greaterThan(">"),
	greaterThanOrEqualTo(">=");

	private final String operator;
	OperatorType(String operator) {
		this.operator = operator;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public boolean isValidOperator(){
        return this.equals(like) ||
                this.equals(notLike) ||
                this.equals(equals) ||
                this.equals(notEquals) ||
                this.equals(lessThan) ||
                this.equals(lessThanOrEqualTo) ||
                this.equals(greaterThan) ||
                this.equals(greaterThanOrEqualTo) ;
    }
}
