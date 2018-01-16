package cn.softlq.ss2h.service.impl;

import cn.softlq.ss2h.dao.IConfigDao;
import cn.softlq.ss2h.dao.IUserDao;
import cn.softlq.ss2h.po.TSurveyColEntity;
import cn.softlq.ss2h.po.TSurveyTypeEntity;
import cn.softlq.ss2h.po.TUserEntity;
import cn.softlq.ss2h.service.IConfigService;
import cn.softlq.ss2h.vo.ConfigItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class IConfigServiceImpl implements IConfigService {
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IConfigDao configDao;

    //获取专项，存放到request中。
    @Override
    public void getVisibleConfig() throws Exception {
        String userid = (String) session.getAttribute("userId");
        TUserEntity result = userDao.userFindOne(userid);
        if (userid != null && !"".equals(userid)) {
            List<TSurveyTypeEntity> list = configDao.getVisibleConfig();
            request.setAttribute("configs", configDao.getVisibleConfig());
        }
    }

    @Override
    public Map<String, Object> configFindAll(int currentTotal, int current, String typeId) {
        Map<String, Object> map = new HashMap<>();
        int error = 0;
        if (currentTotal >= 0 && current >= 0) {
            try {
                if (typeId == null || "".equals(typeId)) typeId = "0";
                List<TSurveyColEntity> list = configDao.configFindAll(currentTotal, current, typeId);
                List<ConfigItemView> items = new ArrayList<>();
                for (int i = currentTotal * (current - 1); i < list.size(); i++) {
                    TSurveyColEntity surveyTypeEntity = list.get(i);

                    ConfigItemView configItemView = new ConfigItemView();
                    configItemView.setSurveyType(surveyTypeEntity.getSurveyType());//专项名称
                    configItemView.setShowNum(String.valueOf(surveyTypeEntity.getShowNum()));//展示数量
                    configItemView.setCol(surveyTypeEntity.getCol());//显示字段
                    configItemView.setColName(surveyTypeEntity.getColName());//字段说明
                    items.add(configItemView);

                    if (i == (currentTotal * (current - 1) + currentTotal)) break;
                }

                map.put("config", items);
                //获取数据的条数
                map.put("total", list.size());
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
    public Map<String, Object> configDelete(ConfigItemView configItemView) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;
        if (configItemView != null && configItemView.getSurveyType() != null && configItemView.getShowNum() != null && configItemView.getCol() != null && configItemView.getColName() != null
                && !"".equals(configItemView.getSurveyType()) && !"".equals(configItemView.getShowNum()) && !"".equals(configItemView.getCol()) && !"".equals(configItemView.getColName())) {
            try {

                Integer result = configDao.configDelete(configItemView);
                if (result != null && result == 1) {
                    map.put("message", "删除成功");
                } else {
                    //删除失败
                    error = 3;
                    map.put("message", "删除失败");
                }
            } catch (Exception e) {
                //删除失败
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

    @Override
    public Map<String, Object> configUpdate(ConfigItemView configItemView) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;

        if (configItemView != null
                && configItemView.getSurveyType() != null && configItemView.getShowNum() != null
                && !"".equals(configItemView.getSurveyType()) && !"".equals(configItemView.getShowNum())) {
            try {
                TSurveyTypeEntity result = configDao.configFindOne(configItemView.getSurveyType());
                if (result != null) {
                    result.setShowNum(Long.valueOf(configItemView.getShowNum()));
                    configDao.configUpdate(result);
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

    @Override
    public Map<String, Object> configFindTabCol(String typeId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;
        if (typeId != null && !"".equals(typeId)) {
            map.put("cols", configDao.configFindTabCol(typeId));
        } else {
            //参数异常
            error = 1;
            map.put("message", "参数异常");
        }
        map.put("error", error);
        return map;
    }

    @Override
    public Map<String, Object> configAdd(ConfigItemView configItemView, String fruits) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;
        if (configItemView != null && configItemView.getShowNum() != null && configItemView.getSurveyType() != null && fruits != null
                && !"".equals(configItemView.getShowNum()) && !"".equals(configItemView.getSurveyType()) && !"".equals(fruits)) {
            TSurveyTypeEntity result = configDao.configFindOne(configItemView.getSurveyType());
            if (result != null) {
                result.setShowNum(Long.valueOf(configItemView.getShowNum()));
                configDao.configUpdate(result);//第一步更新
                configDao.configDeleteAll(result);//第二步删除
                String[] fruit = fruits.split(",");
                configDao.configAdd(result, fruit);//第三步更新
                map.put("message", "添加成功");
            } else {
                //参数异常
                error = 2;
                map.put("message", "参数异常");
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
