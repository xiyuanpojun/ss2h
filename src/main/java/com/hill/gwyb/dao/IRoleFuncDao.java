package com.hill.gwyb.dao;

import java.util.List;

import com.hill.gwyb.po.TFuncEntity;
import com.hill.gwyb.po.TRoleFuncEntity;
import com.hill.gwyb.vo.RoleFuncItemView;

public interface IRoleFuncDao {
	//分页查找查找角色-功能列表
	List<RoleFuncItemView> findAll(int currentTotal, int current, String role) throws Exception;
	//查找所有的角色-功能记录总数
	Integer findTotal(String role) throws Exception;
	//删除单个角色-功能
	void delete(TRoleFuncEntity tRoleFuncEntity) throws Exception;
	//添加单个角色-功能
	void add(TRoleFuncEntity tRoleFuncEntity) throws Exception;
	List<TFuncEntity> findnofuncbyrole(String role);
	int findMaxOrderNUM(String string);
}
