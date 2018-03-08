<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/jsp/common.jsp" %>
    <link rel="stylesheet" href="${ctx}/support/css/org.css">
    <script src="${ctx}/support/js/checkFrame.js"></script>
    <script src="${ctx}/support/js/org.js"></script>
    <style type="text/css">
        .check {
            left: -12px;
            width: 100px;
        }
    </style>
</head>
<body>
<div class="base">
    <div class="nva">
        <div class="layui-tab">
            <ul class="layui-tab-title">
                <li class="layui-this" id="orglistmenu">机构列表</li>
                <li id="addorg">添加机构</li>
            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show">
                    <div class="showData">
                        <div class="serach">
                            <form class="layui-form" lay-filter="add">
                                <div class="layui-form-item">
                                    <label class="layui-form-label">上级机构</label>
                                    <div class="layui-input-block" id="sporg">
                                        <select name="porgid" id="porgid" lay-verify="required"
                                                lay-filter="searchChange" class="pog">
                                            <option value="all">所有机构</option>
                                            <c:forEach items="${sessionScope.porglist}" var="org">
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
                                <th lay-data="{field:'orgid',fixed: true}">机构 id</th>
                                <th lay-data="{field:'orgname'}">机构名称</th>
                                <th lay-data="{field:'pOrgid'}">上级机构id</th>
                                <th lay-data="{field:'pOrg'}">上级机构名称</th>
                                <th lay-data="{fixed: 'right', width:120, align:'center', toolbar: '#barTable'}">操作</th>
                            </tr>
                            </thead>
                        </table>
                        <script id="barTable" type="text/html">
                            <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
                        </script>
                    </div>
                </div>
                <div class="layui-tab-item">
                    <form class="layui-form" id="orgform">
                        <div class="layui-form-item">
                            <label class="layui-form-label">机构等级</label>
                            <div class="layui-input-block">
                                <input type="radio" class="grade" name="grade" value="1" title="市" checked
                                       lay-filter="radio1">
                                <input type="radio" class="grade" name="grade" value="2" title="省" lay-filter="radio1">
                            </div>

                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">机构id</label>
                            <div class="layui-input-block">
                                <input type="text" id="orgid" name="orgentity.orgid" required lay-verify="required"
                                       placeholder="请输入id"
                                       autocomplete="off" class="layui-input">
                                <label id="checkId" class="layui-form-label check"></label>
                            </div>

                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">机构名称</label>
                            <div class="layui-input-block">
                                <input type="text" id="orgname" name="orgentity.orgname" required lay-verify="required"
                                       placeholder="请输入机构名称"
                                       autocomplete="off" class="layui-input">
                                <label id="checkfname" class="layui-form-label check"></label>
                            </div>

                        </div>
                        <div class="layui-form-item" id="porglist">
                            <label class="layui-form-label">上级机构</label>
                            <div class="layui-input-block" id="aporg">
                                <select name="orgentity.pOrgid" id="porgid2" class="pog" lay-verify="required"
                                        lay-filter="searchChange1">
                                    <c:forEach items="${porglist}" var="org">
                                        <option value="${org.orgid}">${org.orgname}</option>
                                    </c:forEach>
                                </select>
                                <input type="hidden" id="gporg" name="orgentity.pOrg" value=""/>
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <div class="layui-input-block">
                                <button class="layui-btn" id="btn" lay-submit lay-filter="btn">确定新增</button>
                                <button type="reset" id="reset1" class="layui-btn layui-btn-primary">重置</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
