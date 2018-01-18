package cn.softlq.ss2h.service;

import java.util.Map;

import cn.softlq.ss2h.po.TFuncEntity;
import cn.softlq.ss2h.po.TRoleFuncEntity;
import cn.softlq.ss2h.vo.RoleFuncItemView;

public interface IRoleFuncService {
	public Map<String,Object> findAll( int currentTotal,int current,String role) throws Exception;
	public Map<String, Object> delete(TRoleFuncEntity tRoleFuncEntity) throws Exception;
	public Map<String, Object> add(TRoleFuncEntity tRoleFuncEntity) throws Exception;
	public Map<String, Object> findnofuncbyrole(String role);
}
