<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="page" required="true" type="com.excilys.cdb.model.Page" description="Current page"%>
<c:set var="pageNum" value="${page.num}"></c:set>
<c:set var="pageNumMax" value="${page.maxNum}"></c:set>
<c:set var="pageOffset" value="${page.offset}"></c:set>
<c:set var="pageSize" value="${page.size}"></c:set>
<div class="container text-center">
    <ul class="pagination">
        <c:if test="${pageNum > 1}">
            <jsp:setProperty property="offset" name="page" value="${pageOffset - pageSize}" />
            <li><a href="dashboard?${page.toUrlArgs()}" aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
            </a></li>
        </c:if>
        <c:forEach var="i" items="-2,-1,0,1,2">
            <c:set var="newNum" value="${pageNum + i}"></c:set>
            <c:set var="newOffset" value="${pageOffset + i * pageSize}"></c:set>
            <jsp:setProperty property="num" name="page" value="${newNum}" />
            <jsp:setProperty property="offset" name="page" value="${newOffset}" />
            <c:if test="${newNum > 0 && newNum < pageNumMax}">
                <li><a href="dashboard?${page.toUrlArgs()}">${newNum}</a></li>
            </c:if>
        </c:forEach>
        <c:if test="${pageNum < pageNumMax}">
            <jsp:setProperty property="offset" name="page" value="${pageOffset + pageSize}" />
            <li><a href="dashboard?${page.toUrlArgs()}" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
            </a></li>
        </c:if>
    </ul>
    <jsp:setProperty property="offset" name="page" value="${pageOffset}" />
    <!-- paginator -->
    <div class="btn-group btn-group-sm pull-right" role="group">
        <c:forEach var="newSize" items="10,50,100">
            <jsp:setProperty property="size" name="page" value="${newSize}" />
            <button type="button" class="btn btn-default"
                onclick="document.location.href='dashboard?${page.toUrlArgs()}'">${newSize}</button>
        </c:forEach>
    </div>
</div>
