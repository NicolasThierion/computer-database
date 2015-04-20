
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
<c:set var="search" value="${pageBean.search}" />
<c:set var="resultsCount" value="${pageBean.totalCount}" />
<c:set var="pageNum" value="${pageBean.num}" />
<c:set var="pageSize" value="${pageBean.size}" />
<c:set var="pageNumMax" value="${pageBean.maxNum}" />

<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<%@include file="/WEB-INF/partials/bootstrap.jsp" %>
</head>

<body>
    <!-- header -->
    <%@include file="/WEB-INF/partials/header.jsp" %>
    <!-- main -->
    <section id="main">
        <div class="container">
            <!-- main title : count search results. -->
            <h1 id="homeTitle">${resultsCount} Computers found</h1>
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="searchComputer" method="GET" class="form-inline">
                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name" value="${search}" />
                        <input type="submit" id="searchsubmit" value="Filter by name" class="btn btn-primary" />
                        <input type="hidden" name="offset" value="${pageBean.offset}"/>
                        <input type="hidden" name="sortBy" value="${pageBean.sortBy}"/>
                        <input type="hidden" name="order" value="${pageBean.sortOrder}"/>
                        <input type="hidden" name="size" value="${pageBean.size}"/>
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="addComputer">Add Computer</a> <a
                        class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();">Edit</a>
                </div>
            </div>
        </div>
        <form id="deleteForm" action="deleteComputer" method="POST">
            <input type="hidden" name="selection" value="" />
            <input type="hidden" name="offset" value="${pageBean.offset}"/>
            <input type="hidden" name="search" value="${pageBean.search}"/>
            <input type="hidden" name="sortBy" value="${pageBean.sortBy}"/>
            <input type="hidden" name="order" value="${pageBean.sortOrder}"/>
            <input type="hidden" name="size" value="${pageBean.size}"/>
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
                        <th><a onclick="$.fn.toggleSortBy('computer')">Computer name</a></th>
                        <th>Introduced date</th>
                        <!-- Table header for Discontinued Date -->
                        <th>Discontinued date</th>
                        <!-- Table header for Company -->
                        <th>Company</th>
                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
                    <c:forEach var="computerBean" items="${pageBean.content}">
                        <tr>
                            <td class="editMode"><input type="checkbox" name="cb" class="cb" value="${computerBean.id}"></td>
                            <td><a href="editComputer?computerId=${computerBean.id}" onclick="">${computerBean.name}</a></td>
                            <td>${computerBean.releaseDate}</td>
                            <td>${computerBean.discontinuedDate}</td>
                            <td>${computerBean.manufacturer.name}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </section>

    <footer class="navbar-fixed-bottom">
        <mylib:page.paginator page="${pageBean}"/>
    </footer>
    <script src="js/dashboard.js"></script>

</body>
</html>
