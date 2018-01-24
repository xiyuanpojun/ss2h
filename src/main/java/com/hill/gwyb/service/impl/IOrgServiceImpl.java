package com.hill.gwyb.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hill.gwyb.dao.IFuncDao;
import com.hill.gwyb.dao.IOrgDao;
import com.hill.gwyb.dao.ISConfigDao;
import com.hill.gwyb.po.TFuncEntity;
import com.hill.gwyb.po.TOrgEntity;
import com.hill.gwyb.service.IFuncService;
import com.hill.gwyb.service.IOrgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class IOrgServiceImpl implements IOrgService {
@Autowired
private HttpServletRequest request;
@Autowired
private IOrgDao orgdao;
//查找机构列表
	@Override
	public Map<String, Object> findAll(int currentTotal, int current,String porgid) throws Exception  {
		  Map<String, Object> map = new HashMap<>();
	        int error = 0;
	        if (currentTotal >= 0 && current >= 0) {
	            try {
	            	 List<TOrgEntity> orglist =orgdao.findAll(currentTotal, current,porgid);
	                  map.put("orglist", orglist);
	                //获取数据的条数
	                map.put("total", orgdao.findTotal(porgid));
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
//查找
@Override
public Map<String, Object> findOne(TOrgEntity orgentity) throws Exception {
	// TODO Auto-generated method stub
	return null;
}
//修改
@Override
public Map<String, Object> update(TOrgEntity orgentity) throws Exception {
	// TODO Auto-generated method stub
	return null;
}
//删除机构
@Override
public Map<String, Object> delete(TOrgEntity orgentity) throws Exception {
	 Map<String, Object> map = new HashMap<>();
     int error = 0;
     if (orgentity != null&&orgentity.getOrgid()!=null&&orgentity.getOrgname()!=null&&!"".equals(orgentity.getOrgid())&&!"".equals(orgentity.getOrgname())) {
         try {
            
            	 orgdao.delete(orgentity);
                 map.put("message", "删除成功");

         } catch (Exception e) {
             //修改失败
             error = 2;
             map.put("message", "删除失败");
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
//添加机构 
@Override
public Map<String, Object> add(TOrgEntity orgentity) throws Exception {
	 Map<String, Object> map = new HashMap<>();
     if (orgentity != null&&orgentity.getOrgid()!=null&&orgentity.getOrgname()!=null&&!"".equals(orgentity.getOrgid())&&!"".equals(orgentity.getOrgname())) {
         try {
        	 boolean flag =orgdao.findOne(orgentity);
             if (flag==false) {
                 orgdao.add(orgentity);
                 map.put("message", "0");
             } else {
                 //该机构已经存在 
                 map.put("message", "1");
             }

         } catch (Exception e) {
             //添加失败
             map.put("message", "2");
             e.printStackTrace();
         }

     } else {
         //非法操作
         map.put("message", "3");
     }
     return map;
}
//新增机构时检查 机构是否存在
@Override
public Map<String, Object> checkId(String oid) {
	Map<String, Object> map = new HashMap<>();
    if (oid!=null&&!"".equals(oid)) {
        try {
        	TOrgEntity result = orgdao.findOneById(oid);
            if (result==null) {
                map.put("message", "1");
            } else {
                map.put("message", "0");
            }

        } catch (Exception e) {
           
            map.put("message", "查询异常");
            e.printStackTrace();
        }

    } 
    else if(oid==null&&"".equals(oid)){
    	 map.put("message", "3");
    }
    else {
        //非法操作
        map.put("message", "非法操作");
    } 
    return map;
}
//查找上级机构列表
@Override
public void showporglist() throws Exception {
	// TODO Auto-generated method stub
	 if(request.getAttribute("porglist")!=null) {
		 request.setAttribute("porglist",null);
	 }
	 else {
	 request.setAttribute("porglist", orgdao.showporglist());
	 List<TOrgEntity> alist=orgdao.showporglist();
	 System.out.println("孙悟"+alist.size());
	 }
}


}
