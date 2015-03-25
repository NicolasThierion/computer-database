
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
<%@ page import="com.excilys.cdb.model.Page"%>


<% //set jsp variables %>
<c:set var="queryName" value="${resultsPageBean.queryString}" />
<c:set var="resultsCount" value="${resultsPageBean.totalCount}" />
<c:set var="pageNum" value="${resultsPageBean.num}" />
<c:set var="pageSize" value="${resultsPageBean.length}" />
<c:set var="pageNumMax" value="${resultsPageBean.maxNum}" />


<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<%@include file="/WEB-INF/includes/bootstrap.jsp" %>
</head>

<body>
    <!-- header -->
    <%@include file="/WEB-INF/includes/header.jsp" %>
    <!-- main -->
    <section id="main">
        <div class="container">
            <!-- main title : count search results. -->
            <h1 id="homeTitle">${resultsCount} Computers found</h1>
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="searchComputer" method="GET" class="form-inline">
                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name" value="${queryName}" />
                        <input type="submit" id="searchsubmit" value="Filter by name" class="btn btn-primary" />
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="addComputer">Add Computer</a> <a
                        class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();">Edit</a>
                </div>
            </div>
        </div>
        <form id="deleteForm" action="#" method="POST">
            <input type="hidden" name="selection" value="">
        </form>
        <!-- search results page list -->
        <div class="container" style="margin-top: 10px;">
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <!-- Variable declarations for passing labels as parameters -->
                        <!-- Table header for Computer Name -->
                        <th class="editMode" style="width: 60px; height: 22px;"><input type="checkbox"
                            id="selectall" /> <span style="vertical-align: top;"> - <a href="#"
                                id="deleteSelected" onclick="$.fn.deleteSelected();"> <i class="fa fa-trash-o fa-lg"></i>
                            </a>
                        </span></th>
                        <th>Computer name</th>
                        <th>Introduced date</th>
                        <!-- Table header for Discontinued Date -->
                        <th>Discontinued date</th>
                        <!-- Table header for Company -->
                        <th>Company</th>
                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
                    <c:forEach var="computerBean" items="${resultsPageBean.content}">
                        <tr>
                            <td class="editMode"><input type="checkbox" name="cb" class="cb" value="0"></td>
                            <td><a href="editComputer?computerId=${computerBean.id}" onclick="">${computerBean.name}</a></td>
                            <td>${computerBean.releaseDate}</td>
                            <td>${computerBean.discontDate}</td>
                            <td>${computerBean.manufacturer.name}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </section>

    <footer class="navbar-fixed-bottom">
        <mylib:pagination page="${resultsPageBean}"/>
    </footer>
    <script src="js/dashboard.js"></script>

</body>
</html>
