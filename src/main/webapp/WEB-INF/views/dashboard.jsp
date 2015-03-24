<%@include file="../includes/header.jsp" %>

<% //set jsp variables %>
<c:set var="queryName" value="${resultsPageBean.queryString}" />
<c:set var="resultsCount" value="${resultsPageBean.totalCount}" />

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
                            <td><a href="editComputer" onclick="">${computerBean.name}</a></td>
                            <td>${computerBean.releaseDate}</td>
                            <td>${computerBean.discontDate}</td>
                            <td>${computerBean.manufacturer.name}</td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td class="editMode"><input type="checkbox" name="cb" class="cb" value="0"></td>
                        <td style="background: silver"><a href="editComputer" onclick="">PowerBook (static
                                example. TO REMOVE)</a></td>
                        <td style="background: silver">1991-01-01</td>
                        <td style="background: silver">2006-01-01</td>
                        <td style="background: silver">Apple Inc.</td>
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
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/dashboard.js"></script>
</body>
</html>
