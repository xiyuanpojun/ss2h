<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/jsp/common.jsp" %>
    <script src="${ctx}/support/js/checkFrame.js"></script>
    <link rel="stylesheet" href="${ctx}/support/css/userList.css">
    <script src="${ctx}/support/js/userList.js"></script>
</head>
<body>
<div class="base">
    <div class="nva">
        <div class="layui-tab">
            <ul class="layui-tab-title">
                <li class="layui-this">用户列表</li>
                <c:if test="${urole=='1'}">
                    <li>增加用户</li>
                </c:if>
            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show">
                    <div class="showData">
                        <div class="serach">
                            <form class="layui-form" lay-filter="add">
                                <div class="layui-form-item">
                                    <label class="layui-form-label">所属省份</label>
                                    <div class="layui-input-block">
                                        <select name="province" id="province" lay-verify="required"
                                                lay-filter="searchChange">
                                            <c:if test="${urole=='1'}">
                                                <option value="">所有省份</option>
                                            </c:if>
                                            <c:forEach items="${orgs}" var="org">
                                                <option value="${org.orgid}">${org.orgname}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <table class="layui-table" lay-filter="table">
                            <thead>
                            <tr>
                                <th lay-data="{field:'userid'}">用户账号</th>
                                <th lay-data="{field:'uname'}">用户姓名</th>
                                <th lay-data="{field:'orgid'}">所属省份</th>
                                <th lay-data="{field:'urole'}">角色</th>
                                <th lay-data="{fixed: 'right', width:120, align:'center', toolbar: '#barTable'}"></th>
                            </tr>
                            </thead>
                        </table>
                        <script id="barTable" type="text/html">
                            <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
                            <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
                        </script>
                    </div>
                </div>
                <div class="layui-tab-item">
                    <form class="layui-form">
                        <div class="layui-form-item">
                            <label class="layui-form-label">用户账号</label>
                            <div class="layui-input-block">
                                <input type="text" name="userEntity.userid" required lay-verify="required"
                                       placeholder="请输入账号"
                                       autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">用户姓名</label>
                            <div class="layui-input-block">
                                <input type="text" name="userEntity.uname" required lay-verify="required"
                                       placeholder="请输入姓名"
                                       autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">用户密码</label>
                            <div class="layui-input-inline">
                                <input type="text" name="userEntity.upwd" required lay-verify="required"
                                       placeholder="请输入密码" autocomplete="off" value="111111" class="layui-input">
                            </div>
                            <div class="layui-form-mid layui-word-aux">默认{111111}</div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">角色</label>
                            <div class="layui-input-block">
                                <input type="radio" name="userEntity.urole" lay-filter="said" value="1" title="管理员">
                                <input type="radio" name="userEntity.urole" lay-filter="said" value="2" title="供应商"
                                       checked>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">选择所属省份</label>
                            <div class="layui-input-block">
                                <select name="userEntity.orgid" id="said" lay-verify="required">
                                    <option value="">选择所属省份</option>
                                    <c:forEach items="${orgs}" var="org">
                                        <option value="${org.orgid}">${org.orgname}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <div class="layui-input-block">
                                <button class="layui-btn" id="btn" lay-submit lay-filter="btn">确定新增</button>
                                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<%--更新面板--%>
<div id="update" hidden="hidden">
    <div class="uform" id="uform">
        <form class="layui-form" lay-filter="uform">
            <div class="layui-form-item">
                <label class="layui-form-label">用户账号</label>
                <div class="layui-input-block">
                    <input type="text" id="userid" name="userEntity.userid" required lay-verify="required"
                           placeholder="请输入账号"
                           autocomplete="off" class="layui-input" disabled="disabled">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">用户姓名</label>
                <div class="layui-input-block">
                    <input type="text" id="uname" name="userEntity.uname" required lay-verify="required"
                           placeholder="请输入姓名"
                           autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">用户密码</label>
                <div class="layui-input-inline">
                    <input type="password" id="upwd" name="userEntity.upwd" required lay-verify="required"
                           placeholder="请输入密码" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">所属省份</label>
                <div class="layui-input-block">
                    <select id="orgid" name="userEntity.orgid" lay-filter="updateChange">
                        <option value="">选择所属省份</option>
                        <c:forEach items="${orgs}" var="org">
                            <option value="${org.orgid}">${org.orgname}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button class="layui-btn" id="uBtn" lay-submit lay-filter="uBtn">确定修改</button>
                    <button type="button" id="uCan" class="layui-btn layui-btn-primary">取消</button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
