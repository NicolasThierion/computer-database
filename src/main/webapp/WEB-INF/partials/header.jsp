<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:message code="header.title" var="headerTitle" />
<spring:message code="flag" var="flag" />
<header class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <a class="navbar-brand" href="dashboard"> ${headerTitle} </a>
    <div id="navbar" class="navbar-collapse pull-right">
            <ul class="nav navbar-nav">
                <li class="dropdown">
                <a href="#" class="dropdown-toggle " data-toggle="dropdown" role="button" aria-expanded="false">
                    <spring:message code="flag" var="flag" />
                        <img src="resources/${flag}.png" alt="uk flag" style="width:25px;height:20px">
                    <spring:message code="language" />
                    <span class="caret"></span>
                </a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="?lang=en"><img src="resources/uk.png" alt="uk flag" style="width:25px;height:20px"> English</a></li>
                        <li><a href="?lang=fr"><img src="resources/fr.png" alt="fr flag" style="width:25px;height:20px"> Français</a></li>
                    </ul></li>
            </ul>
        </div>
    </div>
</header>
