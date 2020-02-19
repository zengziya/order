package com.bdqn.order.mapper;

import com.bdqn.order.pojo.Userinfo;

import java.math.BigDecimal;

public interface UserinfoMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(Userinfo record);

    int insertSelective(Userinfo record);

    Userinfo selectByPrimaryKey(Integer userId);
    Userinfo selectByName(String name);
    int updateByPrimaryKeySelective(Userinfo record);

    int updateByPrimaryKey(Userinfo record);

    public int updateBalance(BigDecimal amt, Integer userId);
}