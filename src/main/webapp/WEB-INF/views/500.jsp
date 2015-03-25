<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<%@include file="/WEB-INF/includes/bootstrap.jsp"%>
</head>
<c:set var="exception" value="${requestScope['javax.servlet.error.exception']}" />
<body>
    <%@include file="/WEB-INF/includes/header.jsp"%>
    <section id="main">
        <div class="container">
            <div class="alert alert-danger">
                Error 500: An error has occured! <br />
                <!-- stacktrace -->
                ${exception.getMessage()}
            </div>
        </div>
    </section>
    <script src="../js/jquery.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="../js/dashboard.js"></script>
</body>
</html>
