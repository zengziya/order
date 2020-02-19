package com.bdqn.order.service;

import com.bdqn.order.pojo.Userinfo;

import java.math.BigDecimal;
import java.util.Map;

public interface UserService {

    public Map doLogin(Userinfo userinfo);

    public Userinfo getUserInfo(String userName);

    public Userinfo getUserInfoById(Integer userId);

    public int updateBalance(BigDecimal amt,Integer userId);
}
