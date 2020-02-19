package com.bdqn.order.service.impl;

import com.bdqn.order.mapper.OperLogMapper;
import com.bdqn.order.pojo.OperLog;
import com.bdqn.order.service.OperLogService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@RabbitListener(queues = "operLogQueue")
public class OperLogServiceImpl implements OperLogService {

    @Resource
    OperLogMapper operLogMapper;

    @Override
    public int addOperLog(OperLog log) {
        return operLogMapper.insert(log);
    }

    @RabbitHandler
    public void addMqOperLog(OperLog operLog) {
        operLogMapper.insertSelective(operLog);
    }
}
