<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/jsp/common.jsp" %>
    <script src="${ctx}/support/js/checkFrame.js"></script>
    <link rel="stylesheet" href="${ctx}/support/css/configuration.css">

    <script src="${ctx}/support/js/configuration.js"></script>
</head>
<body>
<div class="base">
    <div class="nva">
        <div class="layui-tab">
            <ul class="layui-tab-title">
                <li class="layui-this">配置列表</li>
                <c:if test="${urole=='1'}">
                    <li>增加配置</li>
                </c:if>
            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show">
                    <div class="showData">
                        <div class="serach">
                            <form class="layui-form" lay-filter="add">
                                <div class="layui-form-item">
                                    <label class="layui-form-label">专项</label>
                                    <div class="layui-input-block">
                                        <select name="config" id="config" lay-verify="required"
                                                lay-filter="searchChange">
                                            <option value="">所有专项</option>
                                            <c:forEach items="${configs}" var="config">
                                                <option value="${config.typeId}">${config.surveyType}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <table class="layui-table" lay-filter="table">
                            <thead>
                            <tr>
                                <th lay-data="{field:'surveyType'}">专项名称</th>
                                <th lay-data="{field:'showNum'}">展示条数</th>
                                <th lay-data="{field:'col'}">显示字段</th>
                                <th lay-data="{field:'colName'}">字段说明</th>
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
                <%--private String surveyType;--%>
                <%--private String showNum;--%>
                <%--private String col;--%>
                <%--private String colName;--%>
                <div class="layui-tab-item">
                    <form class="layui-form">
                        <div class="layui-form-item">
                            <label class="layui-form-label">专项</label>
                            <div class="layui-input-block">
                                <select name="typeId" id="atypeId" lay-verify="required"
                                        lay-filter="selectConfig">
                                    <option value="">选择专项</option>
                                    <c:forEach items="${configs}" var="config">
                                        <option value="${config.typeId}">${config.surveyType}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">显示条数</label>
                            <div class="layui-input-block">
                                <input type="text" id="ashowNum" name="showNum" required lay-verify="required|number"
                                       autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">显示字段</label>
                            <div class="layui-input-block" id="checkOption">
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
    <%--更新面板--%>
    <div id="update" hidden="hidden">
        <div class="uform" id="uform">
            <form class="layui-form" lay-filter="uform">
                <div class="layui-form-item">
                    <label class="layui-form-label">专项名称</label>
                    <div class="layui-input-block">
                        <input type="text" id="surveyType" name="configItemView.surveyType" required
                               lay-verify="required"
                               autocomplete="off" class="layui-input" disabled="disabled">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">显示条数</label>
                    <div class="layui-input-block">
                        <input type="text" id="showNum" name="configItemView.showNum" required
                               lay-verify="required|number"
                               placeholder="请输入显示条数"
                               autocomplete="off" class="layui-input">
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
</div>
</body>
</html>
