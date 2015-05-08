/**
 * linuxsogood
 * Copyright (c) 2014-2024 linuxsogood.org,Inc.All Rights Reserved.
 */
package org.linuxsogood.idcardarea.controller;


import org.apache.commons.lang3.StringUtils;
import org.linuxsogood.idcardarea.common.Result;
import org.linuxsogood.idcardarea.entity.IdcardArea;
import org.linuxsogood.idcardarea.service.IDCardAreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	
	@RequestMapping(value="/validateIdcard",method=RequestMethod.POST)
	@ResponseBody
	public Result validateIDCard(String cardNumber){
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
}
