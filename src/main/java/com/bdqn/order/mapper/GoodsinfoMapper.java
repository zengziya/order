package com.bdqn.order.mapper;

import com.bdqn.order.pojo.Goods;
import com.bdqn.order.pojo.Goodsinfo;

import java.util.List;

public interface GoodsinfoMapper {
    int deleteByPrimaryKey(Integer goodsId);

    int insert(Goodsinfo record);

    int insertSelective(Goodsinfo record);

    Goodsinfo selectByPrimaryKey(Integer goodsId);
    List<Goodsinfo> selectGoodsList(Goodsinfo goodsinfo);

    int selectGoodsCount(Goodsinfo goodsinfo);

    int updateByPrimaryKeySelective(Goodsinfo record);

    int updateByPrimaryKeyWithBLOBs(Goodsinfo record);

    int updateByPrimaryKey(Goodsinfo record);
    int updateGoodsStock(Integer goodsId,Integer goodsCount);
}