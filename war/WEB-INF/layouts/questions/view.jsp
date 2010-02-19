<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="art-Block">
    <div class="art-Block-tl"></div>
    <div class="art-Block-tr"></div>
    <div class="art-Block-bl"></div>
    <div class="art-Block-br"></div>
    <div class="art-Block-tc"></div>
    <div class="art-Block-bc"></div>
    <div class="art-Block-cl"></div>
    <div class="art-Block-cr"></div>
    <div class="art-Block-cc"></div>
    <div class="art-Block-body">
        <div class="art-BlockHeader">
            <div class="l"></div>
            <div class="r"></div>
            <div class="art-header-tag-icon">

                <div class="t">Home</div>
            </div>
        </div>
        <div class="art-BlockContent">
            <div class="art-BlockContent-body">
                <ul>
                    <li><a href="<%=request.getContextPath()%>/home/index">Index</a><br/></li>
                    <li><a href="<%=request.getContextPath()%>/home/about">About</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>

<c:if test="${remoteCustomer == null}">
    <div class="art-Block">
        <div class="art-Block-tl"></div>
        <div class="art-Block-tr"></div>
        <div class="art-Block-bl"></div>
        <div class="art-Block-br"></div>
        <div class="art-Block-tc"></div>
        <div class="art-Block-bc"></div>
        <div class="art-Block-cl"></div>
        <div class="art-Block-cr"></div>
        <div class="art-Block-cc"></div>
        <div class="art-Block-body">
            <div class="art-BlockHeader">
                <div class="l"></div>
                <div class="r"></div>
                <div class="art-header-tag-icon">

                    <div class="t">Security</div>
                </div>
            </div>
            <div class="art-BlockContent">
                <div class="art-BlockContent-body">
                    <c:choose>
                        <c:when test="${pageContext.request.remoteUser!=null}">
                            <div>Welcome <strong>${pageContext.request.remoteUser}</strong>!</div>
                            <ul>
                                <li><a href="<%=request.getContextPath()%>/security/logout">Logout</a><br/></li>
                                <li><a href="<%=request.getContextPath()%>/security/changePassword">Change password</a><br/></li>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            <ul>
                                <li><a href="<%=request.getContextPath()%>/security/login">Login</a><br/></li>
                            </ul>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

</c:if>

<c:if test="${pageContext.request.remoteUser == null}">
    <div class="art-Block">
        <div class="art-Block-tl"></div>
        <div class="art-Block-tr"></div>
        <div class="art-Block-bl"></div>
        <div class="art-Block-br"></div>
        <div class="art-Block-tc"></div>
        <div class="art-Block-bc"></div>
        <div class="art-Block-cl"></div>
        <div class="art-Block-cr"></div>
        <div class="art-Block-cc"></div>
        <div class="art-Block-body">
            <div class="art-BlockHeader">
                <div class="l"></div>
                <div class="r"></div>
                <div class="art-header-tag-icon">

                    <div class="t">For Customer</div>
                </div>
            </div>
            <div class="art-BlockContent">
                <div class="art-BlockContent-body">
                    <c:choose>
                        <c:when test="${remoteCustomer ==null}">
                            <ul>
                                <li><a href="<%=request.getContextPath()%>/for-customer/login">Customer Login</a></li>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            <div>Welcome <strong>${remoteCustomer.name}!</strong></div>
                            <ul>
                                <li><a href="<%=request.getContextPath()%>/for-customer/viewPurchase">Purchase history</a></li>
                                <li><a href="<%=request.getContextPath()%>/for-customer/logout">Logout</a></li>
                                <c:if test="${remoteCustomer.securityEnabled eq true}">
                                    <li><a href="<%=request.getContextPath()%>/for-customer/changePassword">Change password</a></li>
                                </c:if>
                            </ul>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

</c:if>
