package com.hill.gwyb.service.impl;

import com.hill.gwyb.dao.ISConfigDao;
import com.hill.gwyb.po.TPCodeEntity;
import com.hill.gwyb.service.ISConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SConfigServiceImpl implements ISConfigService {
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ISConfigDao isConfigDao;

    @Override
    public void getVisibleConfig() throws Exception {
        request.setAttribute("sconfigs", isConfigDao.getVisibleConfig());
    }

    @Override
    public Map<String, Object> configFindAll(int currentTotal, int current, String typeId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;
        if (currentTotal >= 0 && current >= 0) {
            try {
                if (typeId == null) typeId = "";
                List<TPCodeEntity> sconfigs = isConfigDao.configFindAll(currentTotal, current, typeId);
                map.put("sconfig", sconfigs);
                //获取数据的条数
                map.put("total", isConfigDao.configFindAllTotal(typeId));
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
    public Map<String, Object> configAdd(TPCodeEntity tpCodeEntity) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;
        if (tpCodeEntity != null && tpCodeEntity.getPname() != null && tpCodeEntity.getPtype() != null
                && !"".equals(tpCodeEntity.getPname()) && !"".equals(tpCodeEntity.getPtype())) {
            TPCodeEntity result = isConfigDao.configFindOne(tpCodeEntity);
            if (result == null) {
                tpCodeEntity.setPid(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
                isConfigDao.configAdd(tpCodeEntity);
                map.put("message", "添加成功");
            } else {
                //该选项已添加
                error = 2;
                map.put("message", "该选项已添加");
            }
        } else {
            //参数异常
            error = 1;
            map.put("message", "参数异常");
        }
        map.put("error", error);
        return map;
    }

    @Override
    public Map<String, Object> configDelete(TPCodeEntity tpCodeEntity) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;
        if (tpCodeEntity != null && tpCodeEntity.getPid() != null && !"".equals(tpCodeEntity.getPid())) {
            TPCodeEntity result = isConfigDao.configFindOne(tpCodeEntity);
            if (result != null) {
                tpCodeEntity.setPid(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
                isConfigDao.configDelete(result);
                map.put("message", "删除成功");
            } else {
                //没有该选项
                error = 2;
                map.put("message", "没有该选项");
            }
        } else {
            //参数异常
            error = 1;
            map.put("message", "参数异常");
        }
        map.put("error", error);
        return map;
    }
}
