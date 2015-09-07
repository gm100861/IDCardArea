/**
 * linuxsogood
 * Copyright (c) 2014-2024 linuxsogood.org,Inc.All Rights Reserved.
 */
package org.linuxsogood.idcardarea.service;

import org.linuxsogood.idcardarea.entity.IdcardArea;

/**
 * 
 * @author gm100861, </br> gm100861@gmail.com
 * @see org.linuxsogood.idcardarea.service.IDCardAreaService
 * @version	V0.0.1-SNAPSHOT, 2015年5月8日 下午2:33:41
 * @description
 * 
 */
public interface IDCardAreaService {

	IdcardArea validate(String substring);

	/**
	 * @description 根据身份证号码查询归属地
	 * @param substring
	 * @return
	 */
	IdcardArea queryArea(String cardNumber);

}
