package com.bdqn.order.service;

import com.bdqn.order.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Map addOrder(Integer goodsId);

    List<Order> selectOrderByUserId(Integer id);

    Order selectOrderById(Integer id);

    int updateOrder(Order order);

    int cancelOrder();

    Map pay(Integer orderId);
}
