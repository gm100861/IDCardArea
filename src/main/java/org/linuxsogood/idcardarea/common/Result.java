/**
 * linuxsogood
 * Copyright (c) 2014-2024 linuxsogood.org,Inc.All Rights Reserved.
 */
package org.linuxsogood.idcardarea.common;


/**
 * 
 * @author gm100861, </br> gm100861@gmail.com
 * @see org.linuxsogood.idcardarea.common.Result
 * @version	V0.0.1-SNAPSHOT, 2015年5月8日 下午2:38:43
 * @description 封装返回值的实体类
 * 
 */
public class Result {
	
	private int code;	//返回的状态码,200表示成功,其它都表示失败
	private String cardNumber;	//身份证号码
	private String msg;	//如果查到,则返回身份证所属的地区
	
	private String gender; //性别
	private String area; //身份证所属区域
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getCardNumber() {
		return cardNumber;
	}
	
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
