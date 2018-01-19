package com.hill.gwyb.service;

import java.util.Map;

import com.hill.gwyb.po.TRoleFuncEntity;

public interface IRoleFuncService {
	public Map<String,Object> findAll( int currentTotal,int current,String role) throws Exception;
	public Map<String, Object> delete(TRoleFuncEntity tRoleFuncEntity) throws Exception;
	public Map<String, Object> add(TRoleFuncEntity tRoleFuncEntity) throws Exception;
	public Map<String, Object> findnofuncbyrole(String role);
}
