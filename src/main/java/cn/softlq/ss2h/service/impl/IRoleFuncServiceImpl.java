package cn.softlq.ss2h.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.softlq.ss2h.dao.IRoleFuncDao;
import cn.softlq.ss2h.po.TFuncEntity;
import cn.softlq.ss2h.po.TRoleFuncEntity;
import cn.softlq.ss2h.service.IRoleFuncService;
import cn.softlq.ss2h.vo.RoleFuncItemView;
@Service
public class IRoleFuncServiceImpl implements IRoleFuncService {
	@Autowired
  private IRoleFuncDao roleFuncDao;
	@Override
	public Map<String, Object> findAll(int currentTotal, int current, String role) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> map = new HashMap<>();
	        int error = 0;
	        if (currentTotal >= 0 && current >= 0) {
	            try {
	                if (role == null || "".equals(role)) role = "0";
	                List<RoleFuncItemView> rolefunclist =roleFuncDao.findAll(currentTotal, current,role);
	                map.put("rolefunclist", rolefunclist);
	                //获取数据的条数
	                map.put("total", roleFuncDao.findTotal(role));
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
	
	@Override
	public Map<String, Object> delete(TRoleFuncEntity tRoleFuncEntity) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> map = new HashMap<>();
	        int error = 0;
	        if (tRoleFuncEntity != null&&tRoleFuncEntity.getFuncId()!=null&&tRoleFuncEntity.getRoleid()!=null&&!"".equals(tRoleFuncEntity.getFuncId())&&!"".equals(tRoleFuncEntity.getRoleid())) {
	            try {
	            	
	                roleFuncDao.delete(tRoleFuncEntity);
	                map.put("message", "删除成功");
	            } catch (Exception e) {
	                //修改失败
	                error = 2;
	                map.put("message", "数据失败");
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
	public Map<String, Object> add(TRoleFuncEntity tRoleFuncEntity) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> map = new HashMap<>();
	        int error = 0;

	        if (tRoleFuncEntity != null&&tRoleFuncEntity.getFuncId()!=null&&tRoleFuncEntity.getRoleid()!=null&&!"".equals(tRoleFuncEntity.getFuncId())&&!"".equals(tRoleFuncEntity.getRoleid())) {
	            try {
	            	String [] funidlist=tRoleFuncEntity.getFuncId().split(",");
	            	
	        		for(int i=1;i<funidlist.length;i++) {
	        			Long size=roleFuncDao.findMaxOrderNUM(tRoleFuncEntity.getRoleid());
	        			tRoleFuncEntity.setOrderNum(size==0?1:size++);
	        			tRoleFuncEntity.setFuncId(funidlist[i]);
	        			roleFuncDao.add(tRoleFuncEntity);
	        		}
	                map.put("message", "添加成功");
	            } catch (Exception e) {
	                //添加失败
	                error = 2;
	                map.put("message", "");
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
	public Map<String, Object> findnofuncbyrole(String role) {
		// TODO Auto-generated method stub
		 Map<String, Object> map = new HashMap<>();
	        int error = 0;
	        if (role!=null) {
	            try {
	               List<TFuncEntity> tfunclist= roleFuncDao.findnofuncbyrole(role);
	               map.put("tfunclist",tfunclist);
	               
	            } catch (Exception e) {
	                //修改失败
	                error = 2;
	                map.put("message", "数据失败");
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
