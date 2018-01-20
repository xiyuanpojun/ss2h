<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/jsp/common.jsp" %>
    <link rel="stylesheet" href="${ctx}/support/css/rolefunc.css">
    <script src="${ctx}/support/js/checkFrame.js"></script>
    <script src="${ctx}/support/js/rolefunc.js"></script>
</head>
<body>
<div class="base">
    <div class="nva">
        <div class="layui-tab">
            <ul class="layui-tab-title">
                <li class="layui-this" id="roel_func">角色-功能列表</li>
                    <li id="addrolfc">角色功能添加</li>
            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show">
                    <div class="showData">
                        <div class="serach">
                            <form class="layui-form" lay-filter="add">
                                <div class="layui-form-item">
                                    <label class="layui-form-label">角色</label>
                                    <div class="layui-input-block">
                                        <select name="role" id="role" lay-verify="required"
                                                lay-filter="searchChange">
                                            <c:if test="${urole=='1'}">
                                                <option value="">所有角色</option>
                                            </c:if>
                                                <option value="1">管理员</option>
                                                <option value="2">供应商</option>
                                        </select>
                       
                                    </div>
                                </div>
                            </form>
                        </div>
                        <table class="layui-table" lay-filter="table">
                            <thead>
                            <tr>
                                <th lay-data="{field:'role_id'}">角色账号</th>
                                <th lay-data="{field:'role_name'}">角色名称</th>
                                <th lay-data="{field:'order'}">功能序号</th>
                                <th lay-data="{field:'func_id'}">功能id</th>
                                <th lay-data="{field:'func_name'}">功能名称</th>
                                <th lay-data="{fixed: 'right', width:120, align:'center', toolbar: '#barTable'}">操作</th>
                            </tr>
                            </thead>
                        </table>
                        <script id="barTable" type="text/html">
                            <a  class="layui-btn layui-btn-danger layui-btn-xs delete" lay-event="del">删除</a>
                        </script>
                          <!--  <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>-->
                    </div>
                </div>
                <div class="layui-tab-item">
                    <form class="layui-form">
                        <div class="layui-form-item">
                             <div class="layui-form-item">
                            <label class="layui-form-label">角色</label>
                            <div class="layui-input-block">
                                <input type="radio" class="roleid" name="roleid" value="1" title="管理员" checked lay-filter="radio1">
                                <input type="radio"  class="roleid" name="roleid" value="2" title="供应商"  lay-filter="radio1">
                            </div>
                        </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">功能列表 </label>
                            <div id="funclist" class="layui-input-block" lay-filter="funcl"> 
                                <input type="checkbox" name="userEntity.urole" value="1" title="角色 管理">
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
<!-- 
<%--更新面板--%>
<div id="update" hidden="hidden">
    <div class="uform" id="uform">
        <form class="layui-form" lay-filter="uform">
            <div class="layui-form-item">
                <label class="layui-form-label">角色名称</label>
                <div class="layui-input-block">
                  <select name="tRoleFuncEntity.role_id" id="role_id2" lay-verify="required"
                                                lay-filter="searchChange">
                                                <option name="tRoleFuncEntity.role_id" value="">当前角色</option>
                                                <option name="tRoleFuncEntity.role_id" value="1">管理员</option>
                                                <option name="tRoleFuncEntity.role_id" value="2">供应商</option>
                    </select>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">序号</label>
                <div class="layui-input-block">
                     <select name="tRoleFuncEntity.order" id="order2" lay-verify="required"
                                                lay-filter="searchChange">
                                                <option  value="">当前序号</option>
                                                <option  value="">1</option>
                                                
                      </select>
                   
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">功能名称</label>
                <div class="layui-input-block">
                 <select name="tRoleFuncEntity.func_name" id="func_name2" lay-verify="required"
                                                lay-filter="searchChange">
                                                <option   value="">当前功能名称</option>
                                                
                      </select>
                    <input type="text" id="fUrl" name="tRoleFuncEntity.func_name" required lay-verify="required"
                           placeholder="请输入功能地址"
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
     -->
</div>
</body>
</html>
