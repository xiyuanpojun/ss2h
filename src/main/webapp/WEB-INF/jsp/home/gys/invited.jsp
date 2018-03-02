<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/jsp/common.jsp" %>
    <script type="text/javascript"
            src="http://api.map.baidu.com/api?v=3.0&ak=cXvqMh90NDoHaYCCl4p3T5jIq7yv46cl"></script>
    <script src="${ctx}/support/js/gys/invited.js"></script>
</head>
<body>

<form class="layui-form" action="">
    <div class="demoTable" style="margin-top: 2px;margin-left: 2px;margin-bottom: 2px;">
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
        <button class="layui-btn" lay-submit lay-filter="searchForm">搜索</button>
    </div>
</form>
<div class="box" style="margin-top: 0px;">
<table id="tab1" class="layui-table" lay-filter="surveyList" style="margin-top: 0px;"></table>
</div>

<fieldset class="layui-elem-field layui-field-title" style="margin-bottom: 0px;">
    <legend>提交样本分配结果</legend>
</fieldset>
<div class="layui-row layui-col-space10" style="margin-bottom: 0px;margin-right: 0px;margin-top: 0px;">
    <div class="layui-col-md3">
        <form class="layui-form" id="subDistForm" action="">
            <div class="layui-form-item">
                <label class="layui-form-label">是否已分配</label>
                <div class="layui-input-block">
                    <input type="radio" name="distRes" value="1" title="已分配" checked="">
                    <input type="radio" name="distRes" value="2" title="取消分配">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">调查员信息</label>
                <div class="layui-input-block">
                    <select name="diaocy" id="diaocy">
                        <option value=""></option>
                    </select>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button class="layui-btn" id="userSub" lay-submit lay-filter="distSub">提交</button>
                </div>
            </div>
        </form>
    </div>
    <div class="layui-col-md9">
        <div id="map" style="width:100%;height:250px;"></div>
    </div>

</div>


</body>
</html>
