/**
 * linuxsogood
 * Copyright (c) 2014-2024 linuxsogood.org,Inc.All Rights Reserved.
 */
package org.linuxsogood.idcardarea.controller;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.linuxsogood.idcardarea.common.Result;
import org.linuxsogood.idcardarea.entity.IdcardArea;
import org.linuxsogood.idcardarea.service.IDCardAreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author gm100861, </br> gm100861@gmail.com
 * @see org.linuxsogood.idcardarea.controller.IdCardAreaController
 * @version	V0.0.1-SNAPSHOT, 2015��5��8�� ����2:31:15
 * @description
 * 
 */
@Controller
public class IdCardAreaController {
	
	@Autowired
	private IDCardAreaService idService;
	
	private static final Logger log = LoggerFactory.getLogger(IdCardAreaController.class);
	
	/**
	 * @description �������֤��ѯ���֤�Ĺ�����
	 * @param cardNumber
	 * @return
	 */
	@RequestMapping(value="/queryArea",method=RequestMethod.POST,params={"cardNumber"})
	@ResponseBody
	public Result queryIdCardArea(String cardNumber){
		Result result = new Result();
		try {
			if(log.isDebugEnabled()){
				log.debug("��ʼ�������֤�����ѯ��������Ϣ,Ҫ��ѯ�����֤������ :"+cardNumber);
			}
			if(StringUtils.isBlank(cardNumber)){
				result.setCode(0);
				result.setCardNumber(cardNumber);
				result.setMsg("���֤���벻��Ϊ��");
				return result;
			}
			cardNumber= cardNumber.trim();
			IdcardArea card = idService.queryArea(cardNumber.substring(0, 6));
			if(card == null){
				result.setCode(0);
				result.setCardNumber(cardNumber);
				result.setMsg("�鲻�������֤�Ĺ�����,��ȷ�ϸ����֤�ǺϷ���");
				return result;
			}
			result.setCode(200);
			result.setCardNumber(cardNumber);
			result.setMsg(card.getArea());
			return result;
		} catch (Exception e) {
			if(log.isErrorEnabled()){
				log.error("�������֤�����ѯ���֤�ŵ�ʱ��,�����쳣,Ҫ��ѯ�����֤������:"+cardNumber);
			}
			result.setCode(0);
			result.setMsg("�е㶫��������");
			result.setCardNumber(cardNumber);
			return result;
		}
	}
	
	/**
	 * @description ���ݡ��л����񹲺͹����ұ�׼ GB 11643-1999�����йع�����ݺ���Ĺ涨
	 * ������ݺ�������������룬��ʮ��λ���ֱ������һλ����У������ɡ�
	 * ����˳�������������Ϊ����λ���ֵ�ַ�룬��λ���ֳ��������룬��λ����˳�����һλ����У���롣
	 * ��ַ���ʾ�������ס����������(�С��졢��)�������������롣
	 * �����������ʾ�������������ꡢ�¡��գ������������λ���ֱ�ʾ���ꡢ�¡���֮�䲻�÷ָ�����
	 * ˳�����ʾͬһ��ַ������ʶ������Χ�ڣ���ͬ�ꡢ�¡��ճ�������Ա�ඨ��˳��š�˳����������ָ����ԣ�ż���ָ�Ů�ԡ�
	 *  У�����Ǹ���ǰ��ʮ��λ�����룬����ISO 7064:1983.MOD 11-2У�����������ļ����롣
	 *  �������ڼ��㷽����
	 *  15λ�����֤�������Ȱѳ�������չΪ4λ���򵥵ľ�������һ��19��18,�����Ͱ���������1800-1999���������;
	 *  2000�������Ŀ϶�����18λ����û��������գ�����1800��ǰ������,��ɶ��ʱӦ�û�û���֤������������ѩn��b��...
	 *  ������������ʽ:
	 *   ��������1800-2099  (18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])
	 *   ���֤������ʽ /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i            
	 *   15λУ����� 6λ��ַ����+6λ��������+3λ˳���
	 *   18λУ����� 6λ��ַ����+8λ��������+3λ˳���+1λУ��λ
	 * У��λ����     ��ʽ:��(ai��Wi)(mod 11)����������������������������(1)
	 *   ��ʽ(1)�У� 
	 *   i----��ʾ�����ַ������������У�������ڵ�λ����ţ�
	 *    ai----��ʾ��iλ���ϵĺ����ַ�ֵ��
	 *    Wi----ʾ��iλ���ϵļ�Ȩ���ӣ�����ֵ���ݹ�ʽWi=2^(n-1)(mod 11)����ó���
	 *    i 18 17 16 15 14 13 12 11 10 9 8 7 6 5 4 3 2 1
	 *    Wi 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 1
	 * @param cardNumber ���֤����
	 * @return
	 */
	@RequestMapping(value="/validateCard",method=RequestMethod.POST,params={"cardNumber"})
	@ResponseBody
	public Result vaildateCard(String cardNumber){
		Result result = new Result();
		try {
			if(log.isDebugEnabled()){
				log.debug("��ʼ�������֤����Ч��:"+cardNumber);
			}
			if(StringUtils.isNoneBlank(cardNumber)){
				cardNumber=cardNumber.trim();
				String re1 = "^([0-9]{15})|([0-9]{17}[x|X|0-9])$";
				Pattern p = Pattern.compile(re1,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
				Matcher m = p.matcher(cardNumber);
				if(!m.matches()){
					result.setCardNumber(cardNumber);
					result.setCode(0);
					result.setMsg("���֤����Ƿ�---");
					return result;
				}
				//���֤��Ϊ��,��ʼ�����������֤�ĺϷ��� 
				Integer[] Wi = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };// ��Ȩ���� 
				Integer[] ValideCode = {1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2};// ���֤��֤λֵ.10����X 
				if(cardNumber.length() == 15){ //����15λ���֤����֤,��֤�������Ƿ�Ϸ�
					int year = Integer.parseInt(cardNumber.substring(6,8));
					int month = Integer.parseInt(cardNumber.substring(8, 10));
					int day = Integer.parseInt(cardNumber.substring(10, 12));
					LocalDate localDate = new LocalDate(year,month,day);
					if(localDate.getYear()!= year || localDate.getMonthOfYear() != month || localDate.getDayOfMonth() != day){
						result.setCardNumber(cardNumber);
						result.setCode(0);
						result.setMsg("���֤���Ϸ�");
						return result;
					}else {
						result.setCardNumber(cardNumber);
						result.setCode(200);
						result.setMsg("���֤�Ϸ�");
						return result;
					}
				}else if(cardNumber.length() == 18){ //����18λ���֤�Ļ�����֤�͵�18λ����֤
					//��֤18λ���֤�������֤���Ƿ���ȷ
					int sum = 0;
					//��ȡ�Ľ����,����ĵ�һλ�ǿյ�,����Ĳ���ֵ
					String[] array = cardNumber.split("");
					if(array[18].equalsIgnoreCase("x")){
						array[18] = "10";
					}
					for (int i = 1; i < 18; i++) {
						sum += Wi[i-1] * Integer.parseInt(array[i]);
					}
					int codePosition = sum%11;
					if((array[18]).equalsIgnoreCase((ValideCode[codePosition])+"")){
						result.setCode(200);
						result.setCardNumber(cardNumber);
						result.setMsg("���֤�Ϸ�");
						return result;
					}else {
						result.setCardNumber(cardNumber);
						result.setCode(0);
						result.setMsg("���֤���Ϸ�");
						return result;
					}
				}
			}
		} catch (Exception e) {
			if(log.isErrorEnabled()){
				log.error("����ִ��ʱ�����쳣:"+e);
				log.error(e.getMessage());
				return result;
			}
		}
		return result;
	}
	
	/**
	 * @description �������֤�����ȡ�Ա�
	 * @param cardNumber
	 * @return
	 */
	@RequestMapping(value="/getGender",method=RequestMethod.POST,params={"cardNumber"})
	@ResponseBody
	public Result checkGender(String cardNumber){
		Result result = new Result();
		try {
			if(StringUtils.isNoneBlank(cardNumber)){
				result = vaildateCard(cardNumber);
				if(result.getCode() != 200) {
					result.setCardNumber(cardNumber);
					result.setCode(0);
					result.setMsg("�봫��Ϸ������֤����");
					return result;
				}
				cardNumber=cardNumber.trim();
				if(cardNumber.length() == 15){
					if(Integer.parseInt(cardNumber.substring(14, 15))%2 == 0){
						result.setCode(200);
						result.setCardNumber(cardNumber);
						result.setGender("female");
						result.setMsg("Ů��");
						return result;
					}else {
						result.setCode(200);
						result.setCardNumber(cardNumber);
						result.setGender("male");
						result.setMsg("����");
						return result;
					}
				}else if(cardNumber.length() == 18){
					if(Integer.parseInt(cardNumber.substring(14, 17))%2 == 0){
						result.setCode(200);
						result.setCardNumber(cardNumber);
						result.setGender("female");
						result.setMsg("Ů��");
						return result;
					}else {
						result.setCode(200);
						result.setCardNumber(cardNumber);
						result.setGender("male");
						result.setMsg("����");
						return result;
					}
				}else {
					result.setCode(200);
					result.setCardNumber(cardNumber);
					result.setGender("");
					result.setMsg("�޷�ȷ���Ա�");
					return result;
				}
			}
		} catch (Exception e) {
			if(log.isErrorEnabled()){
				log.error("����ִ��ʱ�����쳣:"+e);
				log.error(e.getMessage());
				return result;
			}
		}
		return result;
	}
}
