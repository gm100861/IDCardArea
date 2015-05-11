/**
 * linuxsogood
 * Copyright (c) 2014-2024 linuxsogood.org,Inc.All Rights Reserved.
 */
package org.linuxsogood.idcardarea.common;

/**
 * 
 * @author gm100861, </br> gm100861@gmail.com
 * @see org.linuxsogood.idcardarea.common.Result
 * @version	V0.0.1-SNAPSHOT, 2015��5��8�� ����2:38:43
 * @description ��װ����ֵ��ʵ����
 * 
 */
public class Result {
	
	private int code;	//���ص�״̬��,200��ʾ�ɹ�,��������ʾʧ��
	private String cardNumber;	//���֤����
	private String msg;	//����鵽,�򷵻����֤�����ĵ���
	
	private String gender; //�Ա�
	private String area; //���֤��������
	
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
