package org.linuxsogood.idcardarea.mapper;

import org.linuxsogood.idcardarea.entity.IdcardArea;

public interface IdcardAreaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IdcardArea record);

    int insertSelective(IdcardArea record);

    IdcardArea selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IdcardArea record);

    int updateByPrimaryKey(IdcardArea record);
}