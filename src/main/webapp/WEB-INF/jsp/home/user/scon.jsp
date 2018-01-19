<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/jsp/common.jsp" %>
    <script src="${ctx}/support/js/checkFrame.js"></script>
    <link rel="stylesheet" href="${ctx}/support/css/scon.css">
    <script src="${ctx}/support/js/scon.js"></script>
</head>
<body>
<div class="base">
    <div class="nva">
        <div class="layui-tab">
            <ul class="layui-tab-title">
                <li class="layui-this">选项类型列表</li>
                <li>增加选项类型</li>
            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show">
                    <div class="showData">
                        <div class="serach">
                            <form class="layui-form" lay-filter="add">
                                <div class="layui-form-item">
                                    <label class="layui-form-label">选项类型</label>
                                    <div class="layui-input-block">
                                        <select name="province" id="province" lay-verify="required"
                                                lay-filter="searchChange">
                                            <option value="">所有类型</option>
                                            <c:forEach items="${sconfigs}" var="sconfig">
                                                <option value="${sconfig.ptype}">${sconfig.ptype}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <table class="layui-table" lay-filter="table">
                            <thead>
                            <tr>
                                <th lay-data="{field:'pname'}">选项名称</th>
                                <th lay-data="{field:'ptype'}">选项类型</th>
                                <th lay-data="{field:'pid'}">选项ID</th>
                                <th lay-data="{fixed: 'right', width:120, align:'center', toolbar: '#barTable'}"></th>
                            </tr>
                            </thead>
                        </table>
                        <script id="barTable" type="text/html">
                            <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
                        </script>
                    </div>
                </div>
                <div class="layui-tab-item">
                    <form class="layui-form">
                        <div class="layui-form-item">
                            <label class="layui-form-label">选项名称</label>
                            <div class="layui-input-block">
                                <input type="text" name="tpCodeEntity.pname" id="pname" required lay-verify="required"
                                       placeholder="请输入选项名称"
                                       autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">选项类型</label>
                            <div class="layui-input-block">
                                <select name="sptype" id="sptype">
                                    <option value="">选择或输入一个选项类型</option>
                                    <c:forEach items="${sconfigs}" var="sconfig">
                                        <option value="${sconfig.ptype}">${sconfig.ptype}</option>
                                    </c:forEach>
                                </select>
                                <br>
                                <input type="text" name="iptype" id="iptype" placeholder="选择或输入一个选项类型"
                                       autocomplete="off" class="layui-input">
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
</body>
</html>
