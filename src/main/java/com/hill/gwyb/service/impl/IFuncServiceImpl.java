package com.hill.gwyb.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hill.gwyb.dao.IFuncDao;
import com.hill.gwyb.po.TFuncEntity;
import com.hill.gwyb.service.IFuncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IFuncServiceImpl implements IFuncService {
@Autowired
private IFuncDao funcdao;
//查找功能列表
	@Override
	public Map<String, Object> findFuncAll(int currentTotal, int current) throws Exception  {
		  Map<String, Object> map = new HashMap<>();
	        int error = 0;
	        if (currentTotal >= 0 && current >= 0) {
	            try {
	                List<TFuncEntity> funclist = funcdao.findFuncAll(currentTotal, current);
	                map.put("funclist", funclist);
	                //获取数据的条数
	                map.put("total", funcdao.findFuncTotal());
	                map.put("message", "");
	            } catch (Exception e) {
	                //获取数据异常
	                error = 2;
	                map.put("message", "获取数据异常");
	                e.printStackTrace();
	            }
	        } else {
	            //请求参数异常
	            error = 1;
	            map.put("message", "请求参数异常");
	        }
	        map.put("error", error);
	        return map;
	}
	//查找单个功能的信息
	@Override
	public Map<String, Object> findOne(TFuncEntity funcentity) throws Exception {
		  Map<String, Object> map = new HashMap<>();
		int error=0;
		
		if(funcentity!=null&&funcentity.getfId()!=null&&!"".equals(funcentity.getfId())) {
			try {
			Map<String,Object> fm=new HashMap();
			TFuncEntity func=funcdao.findOne(funcentity.getfId());
			fm.put("fId",func.getfId());
			fm.put("fName",func.getfName());
			fm.put("fUrl",func.getfUrl());
			map.put("func",fm);
		}
		catch(Exception e) {
			//获取失败
				error=2;
				map.put("message","请求数据失败");
			}
		}
		else {
			 error=1;
			 map.put("message", "请求参数异常");
		}
		map.put("error",error);
		return map;
	}
	//更改单个功能的信息
	@Override
	public Map<String, Object> update(TFuncEntity funcentity) throws Exception{
		 Map<String, Object> map = new HashMap<>();
	        int error = 0;

	        if (funcentity != null&&funcentity.getfId()!=null&&funcentity.getfName()!=null&&funcentity.getfUrl()!=null&&!"".equals(funcentity.getfId())&&!"".equals(funcentity.getfName())&&!"".equals(funcentity.getfUrl())) {
	            try {
	                TFuncEntity result = funcdao.findOne(funcentity.getfId());
	                if (result != null) {
	                    result.setfName(funcentity.getfName());
	                    result.setfUrl(funcentity.getfUrl());
	                   
	                    funcdao.funcUpdate(funcentity);
	                    map.put("message", "修改成功");
	                } else {
	                    //修改失败
	                    error = 3;
	                    map.put("message", "修改失败");
	                }

	            } catch (Exception e) {
	                //修改失败
	                error = 2;
	                map.put("message", "修改失败");
	                e.printStackTrace();
	            }

	        } else {
	            //非法操作
	            error = 1;
	            map.put("message", "非法操作");
	        }
	        map.put("error", error);
	        return map;
	}
	//删除单个功能
	@Override
	public Map<String, Object> delete(TFuncEntity funcentity) throws Exception {
		 Map<String, Object> map = new HashMap<>();
	        int error = 0;

	        if (funcentity != null&&funcentity.getfId()!=null&&!"".equals(funcentity.getfId())) {
	            try {
	                TFuncEntity result = funcdao.findOne(funcentity.getfId());
	                if (result != null) {
	                    funcdao.delete(funcentity);
	                    map.put("message", "删除成功");
	                } else {
	                    //修改失败
	                    error = 3;
	                    map.put("message", "删除失败");
	                }

	            } catch (Exception e) {
	                //修改失败
	                error = 2;
	                map.put("message", "数据异常");
	                e.printStackTrace();
	            }

	        } else {
	            //非法操作
	            error = 1;
	            map.put("message", "非法操作");
	        }
	        map.put("error", error);
	        return map;
	}
	@Override
	public Map<String, Object> add(TFuncEntity funcentity) throws Exception {
		 Map<String, Object> map = new HashMap<>();
	        int error = 0;
	        if (funcentity != null&&funcentity.getfId()!=null&&funcentity.getfName()!=null&&funcentity.getfUrl()!=null) {
	            try {
	                TFuncEntity result = funcdao.findOne(funcentity.getfId());
	                if (result==null) {
	                    funcdao.add(funcentity);
	                    map.put("message", "添加成功");
	                } else {
	                    //添加失败
	                    error = 3;
	                    map.put("message", "该功能id已经存在请重新添加");
	                }

	            } catch (Exception e) {
	                //添加失败
	                error = 2;
	                map.put("message", "添加失败");
	                e.printStackTrace();
	            }

	        } else {
	            //非法操作
	            error = 1;
	            map.put("message", "非法操作");
	        }
	        map.put("error", error);
	        return map;
	}
	

}
