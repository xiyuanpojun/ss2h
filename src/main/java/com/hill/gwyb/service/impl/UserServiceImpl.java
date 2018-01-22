package com.hill.gwyb.service.impl;

import com.hill.gwyb.dao.IUserDao;
import com.hill.gwyb.po.TUserEntity;
import com.hill.gwyb.po.TUserLoginEntity;
import com.hill.gwyb.service.IUserService;
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
public class UserServiceImpl implements IUserService {
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private IUserDao userDao;

    @Override
    public Map<String, Object> userLogin(TUserEntity userEntity) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;
        if (userEntity != null
                && userEntity.getUserid() != null && userEntity.getUpwd() != null
                && !"".equals(userEntity.getUserid()) && !"".equals(userEntity.getUpwd())) {
            try {
                TUserEntity result = userDao.userFindOne(userEntity.getUserid());
                if (result != null) {
                    if (result.getUpwd().equals(userEntity.getUpwd())) {
                        TUserLoginEntity userLoginEntity = new TUserLoginEntity();
                        userLoginEntity.setUserid(userEntity.getUserid());
                        userLoginEntity.setSessionId(session.getId());
                        //获取上次登陆状态
                        Integer flag = userDao.userLoginInfoCheck(userLoginEntity);
                        if (flag == 0 || flag == 1) {
                            //登陆成功
                            map.put("url", "/user/userManage");
                            //跟踪用户信息
                            session.setAttribute("userId", result.getUserid());
                            session.setAttribute("uorg", result.getOrgid());
                            session.setAttribute("urole", result.getUrole());
                            //记录用户登陆信息
                            userLoginEntity.setUserid(result.getUserid());
                            userDao.userLoginInfoExec(userLoginEntity, false);
                        } else {
                            //已经登陆
                            error = 5;
                            map.put("message", "已经在其他地方登陆，请30分钟后再试。");
                        }

                    } else {
                        //密码错误
                        error = 4;
                        map.put("message", "密码错误");
                    }
                } else {
                    //用户名错误
                    error = 3;
                    map.put("message", "用户名错误");
                }
            } catch (Exception e) {
                //服务器异常
                error = 2;
                map.put("message", "服务器异常\n" + e.toString());
                e.printStackTrace();
            }
        } else {
            //非法登陆
            error = 1;
            map.put("message", "非法登陆");
        }
        map.put("error", error);
        return map;
    }

    @Override
    public Map<String, Object> userLogon(TUserEntity userEntity) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;
        if (userEntity != null && userEntity.getUserid() != null && !"".equals(userEntity.getUserid())) {
            TUserEntity result = userDao.userFindOne(userEntity.getUserid());
            if (checkLogin() && result != null) {
                //清除用户信息
                session.invalidate();
//                session.setAttribute("userId", null);
//                session.setAttribute("urole", null);
//                session.setAttribute("uorg", null);
                map.put("message", "成功退出");
                //记录用户登陆信息
                TUserLoginEntity userLoginEntity = new TUserLoginEntity();
                userLoginEntity.setUserid(result.getUserid());
                userDao.userLoginInfoExec(userLoginEntity, true);
            } else {
                //未登录
                error = 2;
                map.put("message", "未登录或未找到该用户");
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
    public synchronized boolean checkLogin() {
        return null != session.getAttribute("userId");
    }


    @Override
    public Map<String, Object> userAdd(TUserEntity userEntity) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;

        if (userEntity != null
                && userEntity.getUserid() != null && userEntity.getUpwd() != null && userEntity.getUrole() != null
                && userEntity.getOrgid() != null && userEntity.getUname() != null && !"".equals(userEntity.getUserid())
                && !"".equals(userEntity.getUpwd()) && !"".equals(userEntity.getUname())) {
            try {
                TUserEntity result = userDao.userFindOne(userEntity.getUserid());
                if (result == null) {
                    if ("1".equals(userEntity.getUrole())) {
                        userDao.userAdd(userEntity);
                        map.put("message", "添加成功");
                    } else if ("2".equals(userEntity.getUrole()) && !"".equals(userEntity.getOrgid())) {
                        userDao.userAdd(userEntity);
                        map.put("message", "添加成功");
                    } else {
                        //添加失败，省份未选择。
                        error = 3;
                        map.put("message", "添加失败，省份未选择。");
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
    public Map<String, Object> userDelete(TUserEntity userEntity) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;
        if (userEntity != null && userEntity.getUserid() != null && !"".equals(userEntity.getUserid())) {
            try {
                TUserEntity result = userDao.userFindOne(userEntity.getUserid());
                if ("2".equals(result.getUrole())) {
                    userDao.userDelete(result);
                    map.put("message", "删除成功");
                } else {
                    //删除失败
                    error = 3;
                    map.put("message", "删除失败,不能删除管理员。");
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
    public Map<String, Object> userUpdate(TUserEntity userEntity) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;

        if (userEntity != null
                && userEntity.getUserid() != null && userEntity.getUpwd() != null && userEntity.getOrgid() != null && userEntity.getUname() != null
                && !"".equals(userEntity.getUserid()) && !"".equals(userEntity.getUpwd()) && !"".equals(userEntity.getUname())) {
            try {
                TUserEntity result = userDao.userFindOne(userEntity.getUserid());
                if (result != null) {
                    result.setUname(userEntity.getUname());
                    result.setUpwd(userEntity.getUpwd());
                    if (result.getUrole().equals("1")) {
                        result.setOrgid(userEntity.getOrgid());
                        userDao.userUpdate(result);
                        map.put("message", "修改成功");
                    } else if (result.getUrole().equals("2") && !"".equals(userEntity.getOrgid())) {
                        result.setOrgid(userEntity.getOrgid());
                        userDao.userUpdate(result);
                        map.put("message", "修改成功");
                    } else {
                        //修改失败
                        error = 4;
                        map.put("message", "修改失败,请选择省份。");
                    }
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
    public Map<String, Object> userFindAll(int currentTotal, int current, String province) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;
        if (currentTotal >= 0 && current >= 0) {
            try {
                if (province == null || "".equals(province)) province = "0";
                List<TUserEntity> users = userDao.userFindAll(currentTotal, current, province);
                for (int i = 0; i < users.size(); i++) {
                    TUserEntity userEntity = users.get(i);
                    if ("1".equals(userEntity.getUrole())) {
                        userEntity.setUrole("管理员");
                    } else {
                        userEntity.setUrole("供应商");
                    }
                    if (null == userEntity.getOrgid()) {
                        userEntity.setOrgid("");
                    } else {
                        //查找省份
                        userEntity.setOrgid(userDao.userProvinceName(userEntity.getOrgid()));
                    }

                }

                map.put("users", users);
                //获取数据的条数
                map.put("total", userDao.userFindAllTotal(province));
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
    public Map<String, Object> userFindOne(TUserEntity userEntity) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int error = 0;

        if (userEntity != null
                && userEntity.getUserid() != null && !"".equals(userEntity.getUserid())) {
            try {
                TUserEntity result = userDao.userFindOne(userEntity.getUserid());
                Map<String, String> m = new HashMap<>();

                m.put("userid", result.getUserid());
                m.put("upwd", result.getUpwd());
                m.put("orgid", userDao.userProvinceName(result.getOrgid()));
                m.put("uname", result.getUname());
                m.put("urole", result.getUrole());

                map.put("user", m);
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
    public void userFunction() throws Exception {
        String userid = (String) session.getAttribute("userId");
        TUserEntity result = userDao.userFindOne(userid);
        if (userid != null && !"".equals(userid)) {
            request.setAttribute("func", userDao.userFunction(result.getUrole()));
        }
    }


    @Override
    public void getVisibleProvince() throws Exception {
        String userid = (String) session.getAttribute("userId");
        TUserEntity result = userDao.userFindOne(userid);
        if (userid != null && !"".equals(userid)) {
            request.setAttribute("orgs", userDao.getVisibleProvince(result.getUrole(), result.getOrgid()));
        }
    }

}
