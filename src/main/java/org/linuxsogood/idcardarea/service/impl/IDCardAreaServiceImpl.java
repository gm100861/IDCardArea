/**
 * linuxsogood
 * Copyright (c) 2014-2024 linuxsogood.org,Inc.All Rights Reserved.
 */
package org.linuxsogood.idcardarea.service.impl;

import org.linuxsogood.idcardarea.entity.IdcardArea;
import org.linuxsogood.idcardarea.mapper.IdcardAreaMapper;
import org.linuxsogood.idcardarea.service.IDCardAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author gm100861, </br> gm100861@gmail.com
 * @see org.linuxsogood.idcardarea.service.impl.IDCardAreaServiceImpl
 * @version	V0.0.1-SNAPSHOT, 2015年5月8日 下午3:26:32
 * @description
 * 
 */
@Service
public class IDCardAreaServiceImpl implements IDCardAreaService {

	@Autowired
	private IdcardAreaMapper mapper;
	
	@Override
	public IdcardArea validate(String substring) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.linuxsogood.idcardarea.service.IDCardAreaService#queryArea(java.lang.String)
	 */
	@Override
	public IdcardArea queryArea(String cardNumber) {
		return mapper.selectByPrimaryKey(Integer.parseInt(cardNumber));
	}

}
