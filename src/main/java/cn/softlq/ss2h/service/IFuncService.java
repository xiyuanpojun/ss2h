package cn.softlq.ss2h.service;

import java.util.Map;

import cn.softlq.ss2h.po.TFuncEntity;

public interface IFuncService {
public Map<String,Object> findFuncAll( int currentTotal,int current) throws Exception;
public Map<String,Object> findOne(TFuncEntity funcentity) throws Exception ;
public Map<String, Object> update(TFuncEntity funcentity) throws Exception;
public Map<String, Object> delete(TFuncEntity funcentity) throws Exception;
public Map<String, Object> add(TFuncEntity funcentity) throws Exception;
}
