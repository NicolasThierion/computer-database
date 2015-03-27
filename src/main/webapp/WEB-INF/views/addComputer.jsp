<jsp:useBean id="companiesPageBean" scope="request" class="com.excilys.cdb.model.Page" />
<%@ taglib tagdir="/WEB-INF/tags" prefix="mylib"%>

<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<%@include file="/WEB-INF/includes/bootstrap.jsp"%>
</head>
<body>
    <!-- header -->
    <%@include file="/WEB-INF/includes/header.jsp"%>
    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <h1>Add Computer</h1>
                    <form action="addComputer" method="POST" onsubmit="return validate()">
                        <input type="hidden" name="update" value="true" />
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName">Computer name</label> <input type="text" class="form-control"
                                    id="computerName" name="computerName" placeholder="Computer name"> <span
                                    id="computerNameError" class="collapse error">Valid name is mandatory</span>
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label> <input type="date" class="form-control"
                                    id="introduced" name="introduced" placeholder="Introduced date"> <span
                                    id="introducedError" class="collapse error">invalid date.</span>
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label> <input type="date"
                                    class="form-control" id="discontinued" name="discontinued"
                                    placeholder="Discontinued date"> <span id="discontinuedError"
                                    class="collapse error">invalid date.</span>
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <mylib:page.toSelect entities="${companiesPageBean}"
                                    selectedId="${computerBean.company.id}" selectTagId="companyId" />
                            </div>
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="Add" class="btn btn-primary"> or <a href="dashboards"
                                class="btn btn-default">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
    <script src="${APP_ROOT}/js/form.computer.validate.js"></script>
</body>
</html>
