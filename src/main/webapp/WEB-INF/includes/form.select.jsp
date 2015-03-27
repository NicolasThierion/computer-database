<select class="form-control" id="companyId" name="companyId">
    <option value="0">--</option>
    <c:set var="selected" value=""></c:set>
    <c:set var="found" value="false"></c:set>
    <c:forEach var="companyBean" items="${companiesPageBean.content}">
        <c:if test="${companyBean.id eq computerBean.company.id && found eq false }">
            <c:set var="selected" value="selected=\"selected\""></c:set>
            <c:set var="found" value="true"></c:set>
        </c:if>
        <option value="${companyBean.id}" ${selected}>${companyBean.name}</option>
        <c:set var="selected" value=""></c:set>
    </c:forEach>
</select>