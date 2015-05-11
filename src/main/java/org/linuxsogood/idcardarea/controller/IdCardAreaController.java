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
 * @version	V0.0.1-SNAPSHOT, 2015年5月8日 下午2:31:15
 * @description
 * 
 */
@Controller
public class IdCardAreaController {
	
	@Autowired
	private IDCardAreaService idService;
	
	private static final Logger log = LoggerFactory.getLogger(IdCardAreaController.class);
	
	/**
	 * @description 根据身份证查询身份证的归属地
	 * @param cardNumber
	 * @return
	 */
	@RequestMapping(value="/queryArea",method=RequestMethod.POST,params={"cardNumber"})
	@ResponseBody
	public Result queryIdCardArea(String cardNumber){
		Result result = new Result();
		try {
			if(log.isDebugEnabled()){
				log.debug("开始根据身份证号码查询归属地信息,要查询的身份证号码是 :"+cardNumber);
			}
			if(StringUtils.isBlank(cardNumber)){
				result.setCode(0);
				result.setCardNumber(cardNumber);
				result.setMsg("身份证号码不能为空");
				return result;
			}
			cardNumber= cardNumber.trim();
			IdcardArea card = idService.queryArea(cardNumber.substring(0, 6));
			if(card == null){
				result.setCode(0);
				result.setCardNumber(cardNumber);
				result.setMsg("查不到此身份证的归属地,请确认该身份证是合法的");
				return result;
			}
			result.setCode(200);
			result.setCardNumber(cardNumber);
			result.setMsg(card.getArea());
			return result;
		} catch (Exception e) {
			if(log.isErrorEnabled()){
				log.error("根据身份证号码查询身份证号的时候,出现异常,要查询的身份证号码是:"+cardNumber);
			}
			result.setCode(0);
			result.setMsg("有点东西不正常");
			result.setCardNumber(cardNumber);
			return result;
		}
	}
	
	/**
	 * @description 根据〖中华人民共和国国家标准 GB 11643-1999〗中有关公民身份号码的规定
	 * 公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
	 * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
	 * 地址码表示编码对象常住户口所在县(市、旗、区)的行政区划代码。
	 * 出生日期码表示编码对象出生的年、月、日，其中年份用四位数字表示，年、月、日之间不用分隔符。
	 * 顺序码表示同一地址码所标识的区域范围内，对同年、月、日出生的人员编定的顺序号。顺序码的奇数分给男性，偶数分给女性。
	 *  校验码是根据前面十七位数字码，按照ISO 7064:1983.MOD 11-2校验码计算出来的检验码。
	 *  出生日期计算方法。
	 *  15位的身份证编码首先把出生年扩展为4位，简单的就是增加一个19或18,这样就包含了所有1800-1999年出生的人;
	 *  2000年后出生的肯定都是18位的了没有这个烦恼，至于1800年前出生的,那啥那时应该还没身份证号这个东东，⊙﹏⊙b汗...
	 *  下面是正则表达式:
	 *   出生日期1800-2099  (18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])
	 *   身份证正则表达式 /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i            
	 *   15位校验规则 6位地址编码+6位出生日期+3位顺序号
	 *   18位校验规则 6位地址编码+8位出生日期+3位顺序号+1位校验位
	 * 校验位规则     公式:∑(ai×Wi)(mod 11)……………………………………(1)
	 *   公式(1)中： 
	 *   i----表示号码字符从由至左包括校验码在内的位置序号；
	 *    ai----表示第i位置上的号码字符值；
	 *    Wi----示第i位置上的加权因子，其数值依据公式Wi=2^(n-1)(mod 11)计算得出。
	 *    i 18 17 16 15 14 13 12 11 10 9 8 7 6 5 4 3 2 1
	 *    Wi 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 1
	 * @param cardNumber 身份证号码
	 * @return
	 */
	@RequestMapping(value="/validateCard",method=RequestMethod.POST,params={"cardNumber"})
	@ResponseBody
	public Result vaildateCard(String cardNumber){
		Result result = new Result();
		try {
			if(log.isDebugEnabled()){
				log.debug("开始检验身份证的有效性:"+cardNumber);
			}
			if(StringUtils.isNoneBlank(cardNumber)){
				cardNumber=cardNumber.trim();
				String re1 = "^([0-9]{15})|([0-9]{17}[x|X|0-9])$";
				Pattern p = Pattern.compile(re1,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
				Matcher m = p.matcher(cardNumber);
				if(!m.matches()){
					result.setCardNumber(cardNumber);
					result.setCode(0);
					result.setMsg("身份证号码非法---");
					return result;
				}
				//身份证不为空,开始检验数据身份证的合法性 
				Integer[] Wi = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };// 加权因子 
				Integer[] ValideCode = {1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2};// 身份证验证位值.10代表X 
				if(cardNumber.length() == 15){ //进行15位身份证的验证,验证年月日是否合法
					int year = Integer.parseInt(cardNumber.substring(6,8));
					int month = Integer.parseInt(cardNumber.substring(8, 10));
					int day = Integer.parseInt(cardNumber.substring(10, 12));
					LocalDate localDate = new LocalDate(year,month,day);
					if(localDate.getYear()!= year || localDate.getMonthOfYear() != month || localDate.getDayOfMonth() != day){
						result.setCardNumber(cardNumber);
						result.setCode(0);
						result.setMsg("身份证不合法");
						return result;
					}else {
						result.setCardNumber(cardNumber);
						result.setCode(200);
						result.setMsg("身份证合法");
						return result;
					}
				}else if(cardNumber.length() == 18){ //进行18位身份证的基本验证和第18位的验证
					//验证18位身份证的最后验证码是否正确
					int sum = 0;
					//截取的结果是,数组的第一位是空的,后面的才有值
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
						result.setMsg("身份证合法");
						return result;
					}else {
						result.setCardNumber(cardNumber);
						result.setCode(0);
						result.setMsg("身份证不合法");
						return result;
					}
				}
			}
		} catch (Exception e) {
			if(log.isErrorEnabled()){
				log.error("程序执行时出现异常:"+e);
				log.error(e.getMessage());
				return result;
			}
		}
		return result;
	}
	
	/**
	 * @description 根据身份证号码获取性别
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
					result.setMsg("请传入合法的身份证号码");
					return result;
				}
				cardNumber=cardNumber.trim();
				if(cardNumber.length() == 15){
					if(Integer.parseInt(cardNumber.substring(14, 15))%2 == 0){
						result.setCode(200);
						result.setCardNumber(cardNumber);
						result.setGender("female");
						result.setMsg("女性");
						return result;
					}else {
						result.setCode(200);
						result.setCardNumber(cardNumber);
						result.setGender("male");
						result.setMsg("男性");
						return result;
					}
				}else if(cardNumber.length() == 18){
					if(Integer.parseInt(cardNumber.substring(14, 17))%2 == 0){
						result.setCode(200);
						result.setCardNumber(cardNumber);
						result.setGender("female");
						result.setMsg("女性");
						return result;
					}else {
						result.setCode(200);
						result.setCardNumber(cardNumber);
						result.setGender("male");
						result.setMsg("男性");
						return result;
					}
				}else {
					result.setCode(200);
					result.setCardNumber(cardNumber);
					result.setGender("");
					result.setMsg("无法确认性别");
					return result;
				}
			}
		} catch (Exception e) {
			if(log.isErrorEnabled()){
				log.error("程序执行时出现异常:"+e);
				log.error(e.getMessage());
				return result;
			}
		}
		return result;
	}
}
