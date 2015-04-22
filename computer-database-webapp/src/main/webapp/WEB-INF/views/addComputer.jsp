<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" session="false"%>


<%@ taglib tagdir="/WEB-INF/tags" prefix="mylib"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<jsp:useBean id="companyDtoList" scope="request" class="java.util.LinkedList" />
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="addComputer.page.title" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<%@include file="/WEB-INF/partials/bootstrap.jsp"%>
</head>
<body>
    <!-- header -->
    <%@include file="/WEB-INF/partials/header.jsp"%>
    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <h1>
                        <spring:message code="addComputer.form.title" />
                    </h1>
                    <spring:message code="editComputer.form.placeholder.name" var="namePlaceholder" />
                    <spring:message code="editComputer.form.placeholder.introduced" var="introducedPlaceholder" />
                    <spring:message code="editComputer.form.placeholder.discontinued" var="discontinuedPlaceholder" />
                    <form:form id="addComputer" method="POST" action="addComputer" modelAttribute="computerDto">
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName"><spring:message code="editComputer.form.name" /></label>
                                <form:input name="computerName" path="name" class="form-control" id="computerName"
                                    placeholder="${namePlaceholder}" />
                                <form:errors path="name" cssClass="error" />
                            </div>
                            <div class="form-group">
                                <label for="introduced"><spring:message code="editComputer.form.introduced" /></label>
                                <form:input type="date" name="introduced" path="introducedDate" class="form-control" id="introduced"
                                    placeholder="${introducedPlaceholder}" />
                                <form:errors path="introducedDate" cssClass="error" />
                            </div>
                            <div class="form-group">
                                <label for="discontinued"><spring:message code="editComputer.form.discontinued" /></label>
                                <form:input type="date" name="discontinued" path="discontinuedDate" class="form-control"
                                    id="discontinued" placeholder="${discontinuedPlaceholder}" />
                                <form:errors path="discontinuedDate" cssClass="error" />
                            </div>
                            <div class="form-group">
                                <label for="companyId"><spring:message code="editComputer.form.company" /></label>
                                <form:select name="companyId" path="companyId" cssClass="form-control">
                                    <form:option value="" >
                                        <spring:message code="editComputer.form.defaultCompanyLabel" />
                                    </form:option>
                                    <form:options items="${companyDtoList}" itemLabel="name" itemValue="id"/>
                                </form:select>
                            </div>
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="<spring:message code="add" />"
                                class="btn btn-primary"> or <a href="dashboard" class="btn btn-default"><spring:message
                                    code="cancel" /></a>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </section>
    <script src="${APP_ROOT}/js/form.computer.validate.js"></script>
</body>
</html>
