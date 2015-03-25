
<%
    //jsp directives
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" session="false"%>
<%
    //jsp imports
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mylib"%>
<%
    //java imports
%>
<%@ page import="com.excilys.cdb.model.Computer"%>

<%
    //get jsp variable
%>
<jsp:useBean id="computerBean" scope="request" class="com.excilys.cdb.model.Computer" />


<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<%@include file="/WEB-INF/includes/bootstrap.jsp"%>
</head>
<body>
    <%@include file="/WEB-INF/includes/header.jsp"%>
    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <div class="label label-default pull-right">id: 0</div>
                    <h1>Edit Computer</h1>
                    <form action="editComputer" method="POST">
                        <input type="hidden" value="0" />
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName">Computer name</label> <input type="text" class="form-control"
                                    id="computerName" placeholder="Computer name" value="${computerBean.name}">
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label> <input type="date" class="form-control"
                                    id="introduced" placeholder="Introduced date" value="${computerBean.releaseDate}">
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label> <input type="date"
                                    class="form-control" id="discontinued" placeholder="Discontinued date" value="${computerBean.discontDate}">
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label> <select class="form-control" id="companyId">
                                    <option value="0">--</option>
                                </select>
                            </div>
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="Edit" class="btn btn-primary"> or <a
                                href="dashboard.html" class="btn btn-default">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
