<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/jsp/common.jsp" %>
    <link rel="stylesheet" href="${ctx}/support/css/func.css">
    <script src="${ctx}/support/js/checkFrame.js"></script>
    <script src="${ctx}/support/js/func.js"></script>
    
</head>
<body>
<div class="base">
    <div class="nva">
        <div class="layui-tab">
            <ul class="layui-tab-title">
                <li class="layui-this" id="funclistmenu">功能列表</li>
                <c:if test="${urole=='1'}">
                    <li>添加功能</li>
                </c:if>
            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show">
                    <div class="showData">
                        <table class="layui-table" lay-filter="table">
                            <thead>
                            <tr>
                                <th lay-data="{field:'fId',fixed: true}">功能 id</th>
                                <th lay-data="{field:'fName'}">功能名称</th>
                                <th lay-data="{field:'fUrl'}">功能地址</th>
                                <th lay-data="{fixed: 'right', width:120, align:'center', toolbar: '#barTable'}">操作</th>
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
                    <form class="layui-form" id="funcform">
                        <div class="layui-form-item">
                            <label class="layui-form-label">功能id</label>
                            <div class="layui-input-block">
                                <input type="text" name="funcentity.fId" required lay-verify="required"
                                       placeholder="请输入id"
                                       autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">功能名称</label>
                            <div class="layui-input-block">
                                <input type="text" name="funcentity.fName" required lay-verify="required"
                                       placeholder="请输入功能名称"
                                       autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">功能地址</label>
                            <div class="layui-input-inline">
                                <input type="text" name="funcentity.fUrl" required lay-verify="required"
                                       placeholder="请输入功能地址" autocomplete="off" value="" class="layui-input">
                            </div>
                      
                        </div>
                      
                        <div class="layui-form-item">
                            <div class="layui-input-block">
                                <button class="layui-btn" id="btn" lay-submit lay-filter="btn">确定新增</button>
                                <button type="reset"  class="layui-btn layui-btn-primary">重置</button>
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
                <label class="layui-form-label">功能id</label>
                <div class="layui-input-block">
                    <input type="text" id="fId" name="funcentity.fId" required lay-verify="required"
                           placeholder="请输入账号"
                           autocomplete="off" class="layui-input" disabled="disabled">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">功能名称</label>
                <div class="layui-input-block">
                    <input type="text" id="fName" name="funcentity.fName" required lay-verify="required"
                           placeholder="请输入姓名"
                           autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">功能地址</label>
                <div class="layui-input-inline">
                    <input type="text" id="fUrl" name="funcentity.fUrl" required lay-verify="required"
                           placeholder="请输入密码" autocomplete="off" class="layui-input">
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
