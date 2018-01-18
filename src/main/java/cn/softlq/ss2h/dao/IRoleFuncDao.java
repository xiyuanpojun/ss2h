package cn.softlq.ss2h.dao;

import java.util.List;

import cn.softlq.ss2h.po.TFuncEntity;
import cn.softlq.ss2h.po.TRoleFuncEntity;
import cn.softlq.ss2h.vo.RoleFuncItemView;

public interface IRoleFuncDao {
	//分页查找查找角色-功能列表
	List<RoleFuncItemView> findAll(int currentTotal, int current,String role) throws Exception;
	//查找所有的角色-功能记录总数
	Integer findTotal(String role) throws Exception;
	//删除单个角色-功能
	void delete(TRoleFuncEntity tRoleFuncEntity) throws Exception;
	//添加单个角色-功能
	void add(TRoleFuncEntity tRoleFuncEntity) throws Exception;
	List<TFuncEntity> findnofuncbyrole(String role);
	int findMaxOrderNUM(String string);
}
