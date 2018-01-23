<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/1/18
  Time: 14:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:choose>
    <c:when test="${userList.size()==0 }">
        <div align="center" style="padding-top: 20px"><font color="red">${q}</font>未查询到结果，请换个关键字试试！</div>
    </c:when>
    <c:otherwise>
        <div align="center" style="padding-top: 20px">
            查询<font color="red">${q}</font>关键字，约${resultTotal}条记录！
        </div>
        <c:forEach var="u" items="${userList }" varStatus="status">
            <div class="panel-heading ">

                <div class="row">
                    <div class="col-md-6">
                        <div class="row">
                            <div class="col-md-12">
                                <b>
                                    <%--<a href="<%=path %>/user/showUser/${u.id}">${u.userName}</a>--%>
                                </b>
                                <br/>
                                    ${u.password}
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 col-md-offset-2">
                        <p class="text-muted text-right">
                                ${u.password}
                        </p>
                    </div>
                </div>
            </div>
            <div class="panel-footer">
                <p class="text-right">
							<span class="label label-default">
							<span class="glyphicon glyphicon-comment" aria-hidden="true"></span>
							 ${u.password}
							</span>
                </p>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>
</body>
</html>
