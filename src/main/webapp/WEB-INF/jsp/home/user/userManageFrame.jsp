<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <%@include file="/WEB-INF/jsp/common.jsp" %>
    <link rel="stylesheet" href="${ctx}/support/css/userManageFrame.css">
    <script src="${ctx}/support/js/userManageFrame.js"></script>
    <title>供应商样本预约管理系统</title>
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo"><img alt="hill" src="${ctx}/support/img/hill2.png" width="300" height="30"
                                     style="margin-top: 6px"></div>
        <ul class="layui-nav layui-layout-left"></ul>
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:;">
                    <img src="${ctx}/support/img/user.png" class="layui-nav-img">${userId}
                </a>
            </li>
            <li class="layui-nav-item"><a alt="${userId}" id="logon" href="#">退出系统</a>
            </li>
        </ul>
    </div>
    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <ul class="layui-nav layui-nav-tree" id="urls">
                <c:forEach items="#{func}" var="item">
                    <li class="layui-nav-item"><a href="#" alt="${ctx}${item.fUrl}">${item.fName}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
    <div class="layui-body">
        <iframe class="show_page" id="show_page" name="show_page" frameborder="0"></iframe>
    </div>
    <div class="layui-footer">
        ® www.hill.org.cn
    </div>
</div>
</body>
</html>