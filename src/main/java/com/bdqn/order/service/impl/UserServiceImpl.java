package com.bdqn.order.service.impl;

import com.bdqn.order.mapper.UserMapper;
import com.bdqn.order.mapper.UserinfoMapper;
import com.bdqn.order.pojo.OperLog;
import com.bdqn.order.pojo.Userinfo;
import com.bdqn.order.service.UserService;
import com.bdqn.order.util.Md5Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
@RabbitListener(queues = "operLogQueue")
public class UserServiceImpl implements UserService {

    @Resource
    private UserinfoMapper userinfoMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public Map doLogin(Userinfo userinfo) {
        Map map=new HashMap();
        map.put("retCode","1000");
        map.put("retMsg","登陆成功");
        Subject subject= SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken(userinfo.getUserName(), Md5Utils.hash(userinfo.getUserPwd())));
            map.put("user",subject.getPrincipal());
            OperLog operLog=new OperLog();
            operLog.setCreateTime(new Date());
            operLog.setOprName(userinfo.getUserName());
            operLog.setOprType("登陆");
            rabbitTemplate.convertAndSend("OperLogExchange","OperLogRouting", operLog);
        }catch (UnknownAccountException un){
            map.put("retCode","901");
            map.put("retMsg","用户不存在");
        }
        return map;

    }
    public Userinfo getUserInfo(String userName){
        return userinfoMapper.selectByName(userName);
    }

    public Userinfo getUserInfoById(Integer userId){
        return userinfoMapper.selectByPrimaryKey(userId);
    }

    public int updateBalance(BigDecimal amt, Integer userId){
        return userinfoMapper.updateBalance(amt,userId);
    }

}
