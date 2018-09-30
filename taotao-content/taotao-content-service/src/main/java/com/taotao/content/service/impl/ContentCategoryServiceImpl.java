package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容分类管理Service
 * @author Shanks
 * @data 2018年5月1日 下午4:06:02
 * @version V1.1
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		// 创建一个查询类
		TbContentCategoryExample contentCategoryExample = new TbContentCategoryExample();
		// 设置查询条件
		Criteria criteria = contentCategoryExample.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		criteria.andStatusEqualTo(1);
		// 查询
		List<TbContentCategory> categoryList = contentCategoryMapper.selectByExample(contentCategoryExample);
		// 将categoryList转换为List<EasyUITreeNode>
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbContentCategory contentCategory : categoryList) {
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			easyUITreeNode.setId(contentCategory.getId());
			easyUITreeNode.setText(contentCategory.getName());
			easyUITreeNode.setState(contentCategory.getIsParent() ? "closed" : "open");
			resultList.add(easyUITreeNode);
		}
		return resultList;
	}

	@Override
	public TaotaoResult addContentCategory(long parentId, String name) {
		// 创建一个pojo对象
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setName(name);
		contentCategory.setParentId(parentId);
		// 1(正常),2(删除)
		contentCategory.setStatus(1);
		contentCategory.setIsParent(false);
		// 排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
		contentCategory.setSortOrder(1);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		// 插入数据
		contentCategoryMapper.saveAndGetId(contentCategory);
		// 获取主键
		Long id = contentCategory.getId();
		// 判断父节点的isparent属性
		// 查询父节点
		TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parentNode.getIsParent()) {
			parentNode.setIsParent(true);
			// 更新父节点
			contentCategoryMapper.updateByPrimaryKey(parentNode);
		}
		// 返回主键
		return TaotaoResult.ok(id);
	}

	@Override
	public TaotaoResult updateContentCategory(long id, String name) {
		// 通过id查询节点对象
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		// 判断新的name值与原来的值是否相同，如果相同则不用更新
		if (name != null && name.equals(contentCategory.getName())) {
			return TaotaoResult.ok();
		}
		contentCategory.setName(name);
		// 设置更新时间
		contentCategory.setUpdated(new Date());
		// 更新数据库
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
		// 返回结果
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult deleteContentCategory(long id) {
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		// 判断删除的节点是否为父节点
		if (contentCategory.getIsParent()) {
			List<TbContentCategory> list = getContentCategoryListByParentId(id);
			// 递归删除
			for (TbContentCategory tbContentCategory : list) {
				deleteContentCategory(tbContentCategory.getId());
			}
		}
		// 判断父类中是否还有子类节点，没有的话，把父类改成子类
		if (getContentCategoryListByParentId(contentCategory.getParentId()).size() == 1) {
			TbContentCategory parentCategory = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
			parentCategory.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKey(parentCategory);
		}
		contentCategoryMapper.deleteByPrimaryKey(id);
		return TaotaoResult.ok(); 
    } 

	/**
	 * 获取该节点下的所有子节点
	 * @param id
	 * @return 父节点下的所有子节点
	 */
	// 通过父节点id来查询所有子节点,这是抽离抽离的公共方法
	private List<TbContentCategory> getContentCategoryListByParentId(Long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		return list;
	}

}
