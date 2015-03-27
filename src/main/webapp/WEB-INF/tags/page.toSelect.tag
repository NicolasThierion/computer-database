<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="entities" required="true" type="com.excilys.cdb.model.Page" description="Companies list to populate the select"%>
<%@ attribute name="selectTagId" required="true" type="java.lang.String" description="id of select field."%>
<%@ attribute name="selectedId" required="true" type="java.lang.Integer" description="id of selected option."%>
<select class="form-control" id="${selectTagId}" name="${selectTagId}">
    <option value="0">--</option>
    <c:set var="selected" value=""></c:set>
    <c:set var="found" value="false"></c:set>
    <c:forEach var="entityBean" items="${entities.content}">
        <c:if test="${entityBean.id eq selectedId && found eq false }">
            <c:set var="selected" value="selected=\"selected\""></c:set>
            <c:set var="found" value="true"></c:set>
        </c:if>
        <option value="${entityBean.id}" ${selected}>${entityBean.name}</option>
        <c:set var="selected" value=""></c:set>
    </c:forEach>
</select>
