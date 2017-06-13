package com.baselibrary.api;


public  class Result<T> {
	private T result;
	private int code;
	private String message;

	public Result(T result, int code, String message) {
		this.result = result;
		this.code = code;
		this.message = message;
	}

	public Result(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public Result() {
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}


	public void setCode(int code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}