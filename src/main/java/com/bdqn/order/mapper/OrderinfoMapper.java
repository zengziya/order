package com.bdqn.order.mapper;

import com.bdqn.order.pojo.Orderinfo;

public interface OrderinfoMapper {
    int deleteByPrimaryKey(Integer orderId);

    int insert(Orderinfo record);

    int insertSelective(Orderinfo record);

    Orderinfo selectByPrimaryKey(Integer orderId);

    int updateByPrimaryKeySelective(Orderinfo record);

    int updateByPrimaryKey(Orderinfo record);
}