<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Log In</title>
    <link href="/static/style.css" rel="stylesheet" type="text/css">

</head>
<body>
<div id="wrapper">
    <div id="leftBar">
        <div id="menu"><a href="/"><img id="home" src="/static/home-finance.png"/></a></div>
    </div>
    <div id="cover">

        <div id="entrance">
            <c:url value="/newuser" var="regUrl"/>
            <form action="${regUrl}" method="POST">
                <div class="fieldLogin"><input type="text" name="login" id="login" required></div>
                <div class="fieldLogin"><input type="password" name="password" id="pass" required></div>
                <div class="checkIn"><input type="submit" value="Check In" /></div>
            </form>
            <c:if test="${exists ne null}">
                <p id="wrong">User already exists!</p>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>
