<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="page" required="true" type="com.excilys.cdb.model.Page" description="Current page"%>

<c:set var="pageNum" value="${page.num}"></c:set>
<c:set var="pageNumMax" value="${page.maxNum}"></c:set>
<c:set var="pageOffset" value="${page.offset}"></c:set>
<c:set var="pageSize" value="${page.size}"></c:set>

<div class="container text-center">
    <ul class="pagination">
        <c:if test="${pageNum > 1}">
            <li><a href="dashboard?offset=${pageOffset - pageSize}" aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
            </a></li>
        </c:if>

        <c:if test="${pageNum-2 > 0}">
            <li><a id="switchPageMalus2" href="dashboard?offset=${pageOffset - 2 * pageSize}&size=${size}">${pageNum-2}</a></li>
        </c:if>
        <c:if test="${pageNum-1 > 0}">
            <li><a id="switchPageMalus1" href="dashboard?offset=${pageOffset - pageSize}&size=${size}">${pageNum-1}</a></li>
        </c:if>
        <li><a id="switchCurrentPage" href="#">${pageNum}</a></li>
        <c:if test="${pageNum+1 <= pageNumMax}">
            <li><a id="switchPageAdd1" href="dashboard?offset=${pageOffset + pageSize}&size=${size}">${pageNum+1}</a></li>
        </c:if>
        <c:if test="${pageNum+2 <= pageNumMax}">
            <li><a id="switchPageAdd2" href="dashboard?offset=${pageOffset + 2 * pageSize}&size=${size}">${pageNum+2}</a></li>
        </c:if>
        <c:if test="${pageNum < pageNumMax}">
            <li><a href="dashboard?offset=${pageOffset + pageSize}" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
            </a></li>
        </c:if>
    </ul>
    <!-- paginator -->
    <div class="btn-group btn-group-sm pull-right" role="group">
        <c:forEach var="pageSize" items="10,50,100">
            <button type="button" class="btn btn-default" onclick="document.location.href='dashboard?pageSize=${pageSize}'">${pageSize}</button>
        </c:forEach>

    </div>
</div>
