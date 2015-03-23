<%@page import="com.excilys.cdb.model.Computer"%>
<%
    //jsp directives
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" session="false"%>
<%
    //jsp imports
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    //java imports
%>
<%@ page import="com.excilys.cdb.model.Page"%>
<%
    //get arguments
%>
<jsp:useBean id="resultsPage" scope="request" class="com.excilys.cdb.model.Page" />
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="../css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="../css/font-awesome.css" rel="stylesheet" media="screen">
<link href="../css/main.css" rel="stylesheet" media="screen">
</head>
<body>
    <!-- header -->
    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="dashboard"> Application - Computer Database </a>
        </div>
    </header>
    <!-- main -->
    <section id="main">
        <div class="container">
            <!-- main title : count search results. -->
            <h1 id="homeTitle">${resultsPage.totalCount} Computers found</h1>
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="searchComputer" method="GET" class="form-inline">
                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name" />
                        <input type="submit" id="searchsubmit" value="Filter by name" class="btn btn-primary" />
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="addComputer.html">Add Computer</a> <a
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
                    <c:forEach var="computer" items="${resultsPage.content}">
                        <tr>
                            <td class="editMode"><input type="checkbox" name="cb" class="cb" value="0"></td>
                            <td><a href="editComputer.html" onclick="">${computer.name}</a></td>
                            <td>${computer.releaseDate}</td>
                            <td>${computer.discontDate}</td>
                            <td>${computer.manufacturer.name}</td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td class="editMode"><input type="checkbox" name="cb" class="cb" value="0"></td>
                        <td><a href="editComputer.html" onclick="">PowerBook</a></td>
                        <td>1991-01-01</td>
                        <td>2006-01-01</td>
                        <td>Apple Inc.</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </section>
    <footer class="navbar-fixed-bottom">
        <div class="container text-center">
            <ul class="pagination">
                <li><a href="#" aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
                </a></li>
                <li><a href="#">1</a></li>
                <li><a href="#">2</a></li>
                <li><a href="#">3</a></li>
                <li><a href="#">4</a></li>
                <li><a href="#">5</a></li>
                <li><a href="#" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
                </a></li>
            </ul>
        </div>
        <div class="btn-group btn-group-sm pull-right" role="group">
            <button type="button" class="btn btn-default">10</button>
            <button type="button" class="btn btn-default">50</button>
            <button type="button" class="btn btn-default">100</button>
        </div>
    </footer>
    <script src="../js/jquery.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="../js/dashboard.js"></script>
</body>
</html>