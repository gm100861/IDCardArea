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
 * @version	V0.0.1-SNAPSHOT, 2015��5��8�� ����2:31:15
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
				log.debug("��ʼ�������֤�����ѯ��������Ϣ,Ҫ��ѯ�����֤������ :"+cardNumber);
			}
			if(StringUtils.isBlank(cardNumber)){
				result.setCode(0);
				result.setCardNumber(cardNumber);
				result.setMsg("���֤���벻��Ϊ��");
				return result;
			}
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
}
