<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/jsp/common.jsp" %>
    <script src="${ctx}/support/js/checkFrame.js"></script>
    <link rel="stylesheet" href="${ctx}/support/css/surveyor.css">
    <script src="${ctx}/support/js/surveyor.js"></script>
</head>
<body>
<div class="base">
    <div class="nva">
        <div class="layui-tab">
            <ul class="layui-tab-title">
                <li class="layui-this">用户列表</li>
                <li>增加调查员</li>
            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show">
                    <div class="showData">
                        <div class="serach">
                            <form class="layui-form" lay-filter="add">
                                <div class="layui-form-item">
                                    <label class="layui-form-label">所属城市</label>
                                    <div class="layui-input-block">
                                        <select name="province" id="province" lay-verify="required"
                                                lay-filter="searchChange">
                                            <option value="">所有城市</option>
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
                                <th lay-data="{field:'sUserId'}">调查员账号</th>
                                <th lay-data="{field:'sUserName'}">调查员姓名</th>
                                <th lay-data="{field:'orgid'}">所属城市</th>
                                <th lay-data="{field:'disrm'}">已分配样本数量</th>
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
                            <label class="layui-form-label">调查员账号</label>
                            <div class="layui-input-block">
                                <input type="text" name="surveyUserEntity.sUserId" required lay-verify="required"
                                       placeholder="请输入调查员账号"
                                       autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">调查员姓名</label>
                            <div class="layui-input-block">
                                <input type="text" name="surveyUserEntity.sUserName" required lay-verify="required"
                                       placeholder="请输入调查员姓名"
                                       autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">所属城市</label>
                            <div class="layui-input-block">
                                <select name="surveyUserEntity.orgid" id="said" lay-verify="required">
                                    <option value="">选择所属城市</option>
                                    <c:forEach items="${orgs}" var="org">
                                        <option value="${org.orgid}">${org.orgname}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <div class="layui-input-block">
                                <button class="layui-btn" id="btn" alt="${userId}" lay-submit lay-filter="btn">确定新增
                                </button>
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
                <label class="layui-form-label">调查员账号</label>
                <div class="layui-input-block">
                    <input type="text" id="sUserId" name="surveyUserEntity.sUserId" required lay-verify="required"
                           placeholder="请输入账号"
                           autocomplete="off" class="layui-input" disabled="disabled">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">调查员姓名</label>
                <div class="layui-input-block">
                    <input type="text" id="sUserName" name="surveyUserEntity.sUserName" required lay-verify="required"
                           placeholder="请输入调查员姓名"
                           autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">所属城市</label>
                <div class="layui-input-block">
                    <select id="orgid" name="surveyUserEntity.orgid" lay-filter="updateChange" lay-verify="required">
                        <option value="">选择所属城市</option>
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
