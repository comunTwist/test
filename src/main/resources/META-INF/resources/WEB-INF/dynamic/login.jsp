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
      <c:url value="/authorize" var="loginUrl"/>
      <form action="${loginUrl}" method="POST">
        <div class="fieldLogin"><input type="text" name="login" id="login" required></div>
        <div class="fieldLogin"><input type="password" name="password" id="pass" required></div>
        <div class="logIn"><a href="/register">Sing Up</a> <input type="submit" value="Log In"/></div>
      </form>
      <c:if test="${param.error ne null}">
        <p id="wrong">Wrong login or password!</p>
      </c:if>
      <c:if test="${param.logout ne null}">
        <p id="bye">Goodbye!</p>
      </c:if>
      <c:if test="${param.registered ne null}">
        <p id="reg">You are registered!</p>
      </c:if>
    </div>
  </div>
</div>
</body>
</html>
