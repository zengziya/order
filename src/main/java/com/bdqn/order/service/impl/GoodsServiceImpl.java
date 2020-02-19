package com.bdqn.order.service.impl;

import com.bdqn.order.mapper.GoodsMapper;
import com.bdqn.order.mapper.GoodsinfoMapper;
import com.bdqn.order.pojo.Goods;
import com.bdqn.order.pojo.Goodsinfo;
import com.bdqn.order.service.GoodsService;
import com.github.pagehelper.PageHelper;
import com.sun.javafx.collections.MappingChange;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private GoodsinfoMapper goodsinfoMapper;

    @Override
    public List<Goods> getGoodsList(Goods goods) {
        //查询所有商品
        PageHelper.startPage(goods.getPageNum(),goods.getPageSize());
        return goodsMapper.selectGoodsList(goods);
    }

    @Override
    public Map getGoodsList(Goodsinfo goodsinfo) {
        Map map=new HashMap();
        //查询所有商品
        PageHelper.startPage(goodsinfo.getPageNum(),goodsinfo.getPageSize());
        //查询商品总数
        map.put("total",goodsinfoMapper.selectGoodsList(goodsinfo));

        map.put("goodsList",goodsinfoMapper.selectGoodsList(goodsinfo));
        return map;
    }
    @Override
    public Goodsinfo getGoodsById(Integer id) {
        return goodsinfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int getAllCount(Goods goods) {
        return goodsMapper.selectGoodsCount(goods);
    }

    @Override
    public int updateGoodsStock(Integer goodsId,Integer count){
        return goodsinfoMapper.updateGoodsStock(goodsId,count);
    }
}
