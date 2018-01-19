package cn.softlq.ss2h.service.impl;

import cn.softlq.ss2h.dao.ISurveyorDao;
import cn.softlq.ss2h.dao.IUserDao;
import cn.softlq.ss2h.po.TSurveyUserEntity;
import cn.softlq.ss2h.po.TUserEntity;
import cn.softlq.ss2h.service.ISurveyorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SurveyorServiceImpl implements ISurveyorService {
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private ISurveyorDao surveyorDao;


    @Override
    public void getVisibleProvince() throws Exception {
        String userid = (String) session.getAttribute("userId");
        TUserEntity result = userDao.userFindOne(userid);
        if (userid != null && !"".equals(userid)) {
            request.setAttribute("orgs", surveyorDao.getVisibleProvince(result.getOrgid()));
        }
    }

    @Override
    public Map<String, Object> userFindAll(int currentTotal, int current, String province) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;
        if (currentTotal >= 0 && current >= 0) {
            try {
                if (province == null || "".equals(province)) province = "0";
                String userid = (String) session.getAttribute("userId");
                List<TSurveyUserEntity> users = surveyorDao.userFindAll(currentTotal, current, province, userid);
                for (int i = 0; i < users.size(); i++) {
                    TSurveyUserEntity surveyUserEntity = users.get(i);
                    //查找城市名字
                    surveyUserEntity.setOrgid(surveyorDao.userProvinceName(surveyUserEntity.getOrgid()));
                }
                map.put("users", users);
                //获取数据的条数
                map.put("total", surveyorDao.userFindAllTotal(province, userid));
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
    public Map<String, Object> userAdd(TSurveyUserEntity entity)throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;
        if (entity != null && entity.getsUserId() != null && entity.getsUserName() != null && entity.getOrgid() != null && entity.getUserid() != null
                && !"".equals(entity.getsUserId()) && !"".equals(entity.getsUserName()) && !"".equals(entity.getOrgid()) && !"".equals(entity.getUserid())) {
            try {
                TSurveyUserEntity result = surveyorDao.userFindOne(entity.getsUserId());
                String userid = (String) session.getAttribute("userId");
                if (result == null) {
                    if (entity.getUserid().equals(userid)) {
                        surveyorDao.userAdd(entity);
                        map.put("message", "添加成功");
                    } else {
                        //非法操作
                        error = 1;
                        map.put("message", "非法操作");
                    }
                } else {
                    //该账号已注册
                    error = 3;
                    map.put("message", "该账号已注册");
                }

            } catch (Exception e) {
                //保存失败
                error = 2;
                map.put("message", "保存失败");
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
    public Map<String, Object> userDelete(TSurveyUserEntity entity) throws Exception{
        Map<String, Object> map = new HashMap<>();
        int error = 0;
        if (entity != null && entity.getsUserId() != null && entity.getUserid() != null
                && !"".equals(entity.getsUserId()) && !"".equals(entity.getUserid())) {
            try {
                TSurveyUserEntity result = surveyorDao.userFindOne(entity.getsUserId());
                String userid = (String) session.getAttribute("userId");
                if (result != null) {
                    if (userid.equals(entity.getUserid())) {
                        surveyorDao.userDelete(result);
                        map.put("message", "删除成功");
                    } else {
                        //删除失败
                        error = 4;
                        map.put("message", "非法操作");
                    }
                } else {
                    //删除失败
                    error = 3;
                    map.put("message", "非法操作");
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
    public Map<String, Object> userFindOne(TSurveyUserEntity entity)throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;
        if (entity != null
                && entity.getUserid() != null && !"".equals(entity.getUserid())) {
            try {
                TSurveyUserEntity result = surveyorDao.userFindOne(entity.getsUserId());
                String userid = (String) session.getAttribute("userId");
                if (result != null && userid.equals(result.getUserid())) {
                    Map<String, String> m = new HashMap<>();
                    m.put("sUserId", result.getsUserId());
                    m.put("sUserName", result.getsUserName());
                    m.put("orgid", result.getOrgid());
                    map.put("user", m);
                } else {
                    //获取失败
                    error = 3;
                    map.put("message", "获取失败");
                }
            } catch (Exception e) {
                //获取失败
                error = 2;
                map.put("message", "获取失败");
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
    public Map<String, Object> userUpdate(TSurveyUserEntity entity) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;
        if (entity != null && entity.getsUserId() != null && entity.getsUserName() != null && entity.getOrgid() != null && entity.getUserid() != null
                && !"".equals(entity.getsUserId()) && !"".equals(entity.getsUserName()) && !"".equals(entity.getOrgid()) && !"".equals(entity.getUserid())) {
            try {
                TSurveyUserEntity result = surveyorDao.userFindOne(entity.getsUserId());
                String userid = (String) session.getAttribute("userId");
                if (result != null) {
                    if (entity.getUserid().equals(userid)) {
                        result.setOrgid(entity.getOrgid());
                        result.setsUserName(entity.getsUserName());
                        surveyorDao.userUpdate(result);
                        map.put("message", "修改成功");
                    } else {
                        //非法操作
                        error = 1;
                        map.put("message", "非法操作");
                    }
                } else {
                    //没有该账号
                    error = 3;
                    map.put("message", "没有该账号");
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
}
