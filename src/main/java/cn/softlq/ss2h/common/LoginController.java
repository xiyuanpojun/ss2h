package cn.softlq.ss2h.common;

import com.opensymphony.xwork2.ActionSupport;

import java.util.HashMap;
import java.util.Map;

public class LoginController extends ActionSupport {
    //json数据map
    private Map<String, Object> dataMap;

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    @Override
    public String execute() throws Exception {
        dataMap = new HashMap<>();
        dataMap.put("isLogon", true);
        return super.execute();
    }
}
