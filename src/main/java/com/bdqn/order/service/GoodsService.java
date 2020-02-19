package com.bdqn.order.service;

import com.bdqn.order.pojo.Goods;
import com.bdqn.order.pojo.Goodsinfo;
import com.github.pagehelper.PageHelper;

import java.util.List;
import java.util.Map;

public interface GoodsService {
     List<Goods> getGoodsList(Goods goods);

     Goodsinfo getGoodsById(Integer id);

     int getAllCount(Goods goods);

     Map getGoodsList(Goodsinfo goodsinfo);

     /***
      * 修改商品库存
      * @param goodsId
      * @return
      */
     int updateGoodsStock(Integer goodsId,Integer count);
}
