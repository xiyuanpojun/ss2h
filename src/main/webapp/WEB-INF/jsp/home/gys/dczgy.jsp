<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/jsp/common.jsp" %>
    <script type="text/javascript"
            src="http://api.map.baidu.com/api?v=3.0&ak=cXvqMh90NDoHaYCCl4p3T5jIq7yv46cl"></script>
    <script src="${ctx}/support/js/gys/invitation.js"></script>

</head>
<body>
<form class="layui-form" action="">
    <div class="demoTable" style="margin-top: 2px;margin-left: 2px;">
        调查类型:
        <div class="layui-inline">
            <select name="stype" id="stype">
            </select>
        </div>
        地市:
        <div class="layui-inline">
            <select name="city" id="city">
                <option value=""></option>
            </select>
        </div>
        客户类型:
        <div class="layui-inline">
            <select name="custType" id="custType">
                <option value=""></option>
            </select>
        </div>

        <br/>
        地址搜索:
        <div class="layui-inline">
            <input value="" name="address" class="layui-input" style="width: 472px;" type="text" placeholder="请输入地址搜索"
                   autocomplete="off"
                   lay-verify="title">
        </div>
        距离范围:
        <div class="layui-inline">
            <select name="dist">
                <option value="3000">3公里</option>
                <option value="5000">5公里</option>
                <option value="7000">7公里</option>
                <option value="10000">10公里</option>
                <option value="12000">12公里</option>
                <option value="15000">15公里</option>
            </select>
        </div>
        <button class="layui-btn" lay-submit lay-filter="searchForm">搜索</button>
        共查询出样本信息<label id="tab-total">0</label>条
    </div>
</form>
<div class="box" style="margin-top: 0px;">
    <table id="tab1" class="layui-table" lay-filter="surveyList" style="margin-top: 0px;"></table>
</div>
<div class="layui-row layui-col-space10" style="margin-bottom: 0px;margin-right: 0px;margin-top: 0px;">
    <div class="layui-col-md12">
        <div id="map" style="width:100%;height:260px"></div>
    </div>

</div>

</body>
</html>
