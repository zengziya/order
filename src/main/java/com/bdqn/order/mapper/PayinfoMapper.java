package com.bdqn.order.mapper;

import com.bdqn.order.pojo.Payinfo;

public interface PayinfoMapper {
    int deleteByPrimaryKey(Integer payId);

    int insert(Payinfo record);

    int insertSelective(Payinfo record);

    Payinfo selectByPrimaryKey(Integer payId);

    int updateByPrimaryKeySelective(Payinfo record);

    int updateByPrimaryKey(Payinfo record);
}