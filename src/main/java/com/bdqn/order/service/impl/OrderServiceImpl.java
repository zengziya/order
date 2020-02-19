package com.bdqn.order.service.impl;

import com.bdqn.order.mapper.OrderMapper;
import com.bdqn.order.pojo.*;
import com.bdqn.order.service.GoodsService;
import com.bdqn.order.service.OrderService;
import com.bdqn.order.service.PayService;
import com.bdqn.order.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RabbitListener(queues = "operLogQueue")
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Resource
    private UserService userService;

    @Resource
    private PayService payService;

    @Resource
    private GoodsService goodsService;
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Map addOrder(Integer goodsId) {
        Map<String,Object> map=new HashMap<>();
        /**
        Map<String,Object> map=new HashMap<>();
        Goodsinfo goods =goodsService.getGoodsById(id);
        Order order=new Order();
        order.setGoodsId(goods.getGoodsId());
        order.setOrderPrice(goods.getGoodsPrice());
        order.setUserId((Integer) session.getAttribute("loginUser"));
        order.setOrderStatus("未支付");
        if(orderService.addOrder(order)>0){
            map.put("result","success");
        }*/

        map.put("retCode","500");
        map.put("retMsg","系统错误，请稍后重试");

        //查询商品，库存校验，
        //数据的脏写脏读在商品实时哭惨体现的尤为突出
        //使用锁来解决并发请求时的数据安全问题
        //使用redis来做锁
        int result = goodsService.updateGoodsStock(goodsId,(-1));
        if(result <= 0){
            map.put("retCode","801");
            map.put("retMsg","商品已售罄，请下次购买");
            return map;
        }
        Goodsinfo goods =goodsService.getGoodsById(goodsId);
        // 从shiro中取出用户数据
        Userinfo userinfo = (Userinfo) SecurityUtils.getSubject().getPrincipal();
        Order order1=new Order();
        order1.setGoodsId(goodsId);
        order1.setOrderPrice(goods.getGoodsPrice());
        order1.setUserId(userinfo.getUserId());
        order1.setOrderStatus("未支付");
        //添加订单
        if(orderMapper.insertSelective(order1)>0){
            map.put("retCode","1000");
            map.put("retMsg","下单成功");

            OperLog operLog=new OperLog();
            operLog.setCreateTime(new Date());
            operLog.setOprName(userinfo.getUserName());
            operLog.setOprType("下单");
            rabbitTemplate.convertAndSend("OperLogExchange","OperLogRouting", operLog);
            return map;
        }
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Map pay(Integer orderId){
        Map map=new HashMap();
        Userinfo userinfo=(Userinfo)SecurityUtils.getSubject().getPrincipal();
        //校验订单状态
        Order order=orderMapper.selectByPrimaryKey(orderId);
        if (!order.getOrderStatus().equals("未支付")){
            map.put("retCode","802");
            map.put("retMsg","订单状态异常");
            return map;
        }
        //校验用户余额
        Userinfo userinfo1 = userService.getUserInfoById(userinfo.getUserId());
        if ( userService.updateBalance(order.getOrderPrice(),userinfo1.getUserId())<0){
            map.put("retCode","803");
            map.put("retMsg","您的余额不足，请充值");
            return map;
        }
        //修改订单状态
        Order orderBeupdate=new Order();
        orderBeupdate.setOrderId(orderId);
        orderBeupdate.setOrderStatus("已支付");
        orderMapper.updateByPrimaryKeySelective(orderBeupdate);
        //添加支付记录
        Pay pay=new Pay();
        pay.setOrderId(orderId);
        pay.setPayBefore(userinfo1.getUserBalance());
        pay.setPayAfter(userinfo1.getUserBalance().subtract(order.getOrderPrice()));
        pay.setUserId(userinfo.getUserId());
        pay.setPayTime(new Date());
        payService.addPayInfo(pay);
        map.put("retCode","1000");
        map.put("retMsg","支付成功");

        OperLog operLog=new OperLog();
        operLog.setCreateTime(new Date());
        operLog.setOprName(userinfo.getUserName());
        operLog.setOprType("购买支付成功");
        rabbitTemplate.convertAndSend("OperLogExchange","OperLogRouting", operLog);
        return map;
    }

    @Override
    public List<Order> selectOrderByUserId(Integer id) {
        return orderMapper.selectAllOrder(id);
    }

    @Override
    public Order selectOrderById(Integer id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateOrder(Order order) {
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public int cancelOrder() {
        return orderMapper.cancelOrder();
    }
}
