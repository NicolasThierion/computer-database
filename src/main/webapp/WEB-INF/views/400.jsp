<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<%@include file="/WEB-INF/partials/bootstrap.jsp"%>
<%@ page isErrorPage="true"%>
</head>
<c:set var="message" value="${requestScope['javax.servlet.error.message']}" />
<body>
    <%@include file="/WEB-INF/partials/header.jsp"%>
    <section id="main">
        <div class="container">
            <div class="alert alert-danger">
                Error 400: An error has occured! <br />
                <!-- stacktrace -->
                ${message}
            </div>
        </div>
    </section>
    <script src="../js/jquery.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="../js/dashboard.js"></script>
</body>
</html>
