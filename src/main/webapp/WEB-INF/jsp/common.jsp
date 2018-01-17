<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--jstl--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--全局jsp根路径--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%--全局JavaScript根路径--%>
<script>
    var ctx = "${ctx}";
</script>

<%--layui-v2.2.5--%>
<link rel="stylesheet" href="${ctx}/support/layui-v2.2.5/css/layui.css" media="all">
<script src="${ctx}/support/layui-v2.2.5/layui.js"></script>

<%--jquery-3.1.1--%>
<script src="${ctx}/support/js/jquery-3.1.1.js"></script>
<script>
    function checkLogon(data) {
        if (typeof (data.isLogon) !== "undefined") {
            if (self !== top) {
                window.parent.location.href = ctx + '/index.jsp';
            } else {
                window.location.href = ctx + '/index.jsp';
            }
        }
    }

    function checkLogin() {
        $.ajax({
            url: ctx + "/user/user_checkLogin",
            type: "POST",
            data: {},
            dataType: "json",
            success: function (data) {
                checkLogon(data);
            }
        });
    }
</script>