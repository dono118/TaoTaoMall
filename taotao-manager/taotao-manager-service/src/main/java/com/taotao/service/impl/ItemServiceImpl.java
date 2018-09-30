package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.jedis.service.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

/**
 * 商品查询Service
 * 
 * @author Shanks
 * @data 2018年4月30日 上午11:28:03
 * @version V1.1
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired  
	private JmsTemplate jmsTemplate;  
	@Autowired  
    private JedisClient jedisClient;  
	
    @Value("${ITEM_INFO}")  
    private String ITEM_INFO;  
    @Value("${ITEM_EXPIRE}")  
    private Integer ITEM_EXPIRE;

    @Resource(name="itemAddTopic")  
    private Destination destination;
    
	@Override
	public TbItem getItemById(long itemId) {
		//查询数据库之前先查询缓存  
        try {  
            String json = jedisClient.get(ITEM_INFO+":"+itemId+":BASE");  
            if(StringUtils.isNotBlank(json)){  
                //把json转换成对象  
                return JSON.parseObject(json, TbItem.class);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
		// 根据主键查询
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//把查询结果添加到缓存  
        try {  
            //把查询结果添加到缓存  
            jedisClient.set(ITEM_INFO+":"+itemId+":BASE", JSON.toJSONString(item));  
            //设置过期时间，提高缓存的利用率  
            jedisClient.expire(ITEM_INFO+":"+itemId+":BASE", ITEM_EXPIRE);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
		return item;
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//查询数据库之前先查询缓存  
        try {  
            String json = jedisClient.get(ITEM_INFO+":"+itemId+":DESC");  
            if(StringUtils.isNotBlank(json)){  
                //把json转换成对象  
                return JSON.parseObject(json, TbItemDesc.class);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);  
        //把查询结果添加到缓存  
        try {  
            //把查询结果添加到缓存  
            jedisClient.set(ITEM_INFO+":"+itemId+":DESC", JSON.toJSONString(tbItemDesc));  
            //设置过期时间，提高缓存的利用率  
            jedisClient.expire(ITEM_INFO+":"+itemId+":DESC", ITEM_EXPIRE);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return tbItemDesc;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// 分页处理
		PageHelper.startPage(page, rows);
		// 执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		// 取分页信息
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		// 返回处理结果
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);

		return result;
	}

	@Override
	public TaotaoResult createItem(TbItem tbItem, String desc) throws Exception {
		//生成商品ID  
        long itemId = IDUtils.genItemId();  
        //补全item的属性  
        tbItem.setId(itemId);  
        //商品状态，1-正常，2-下架，3-删除  
        tbItem.setStatus(((byte) 1));  
        tbItem.setCreated(new Date());  
        tbItem.setUpdated(new Date());  
        itemMapper.insert(tbItem);  
        //添加商品描述  
        insertItemDesc(itemId, desc);  
      //发送activemq消息  
        jmsTemplate.send(destination,new MessageCreator() {  
              
            @Override  
            public Message createMessage(Session session) throws JMSException {  
                TextMessage textMessage = session.createTextMessage(itemId+"");  
                return textMessage;  
            }  
        });
        return TaotaoResult.ok();
	}

	// 添加商品描述
	private void insertItemDesc(long itemId, String desc) {
		//创建一个商品描述表对应的pojo  
        TbItemDesc itemDesc = new TbItemDesc();  
        //补全pojo的属性  
        itemDesc.setItemId(itemId);  
        itemDesc.setItemDesc(desc);  
        itemDesc.setCreated(new Date());  
        itemDesc.setUpdated(new Date());  
        //向商品描述表插入数据  
        itemDescMapper.insert(itemDesc);
	}


}
