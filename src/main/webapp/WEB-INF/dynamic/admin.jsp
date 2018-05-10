<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <title>Transactions</title>
  <link href="/static/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="wrapper">
  <c:url value="/accounts" var="accountsUrl"/>
  <c:url value="/entries" var="entriesUrl"/>
  <c:url value="/customers" var="customersUrl"/>
  <c:url value="/currencies" var="currenciesUrl"/>
  <c:url value="/settings" var="settingsUrl"/>
  <c:url value="/logout" var="logoutUrl"/>
  <c:url value="/updateTransaction" var="updateTransactionUrl"/>
  <c:url value="/deleteTransaction" var="deleteUrl"/>
  <c:set var="admin" value="ROLE_ADMIN"/>
  <c:set var="superR" value="ROLE_SUPER"/>

  <c:set var="transactionTransfer" value="TRANSFER"/>
  <c:set var="transactionProfit" value="PROFIT"/>
  <c:set var="transactionLoss" value="LOSS"/>
  <c:set var="transactionZero" value="ZERO"/>
  <c:forEach var="s" items="${roles}">
    <c:if test="${(s eq superR) || (s eq admin)}">
      <c:url value="/admin" var="adminUrl"/>
    </c:if>
    <c:if test="${s eq superR}">
      <c:set var="superAdmin" value=""/>
    </c:if>
  </c:forEach>
  <div id="leftBar">
    <div id="menu">
      <div id="wrapperMenu">
        <ul>
          <li><a href="/"><img src="/static/transaction.png" alt="Transactions" title="Transactions"></a></li>
          <li><a href="${accountsUrl}"><img src="/static/account.png" alt="Accounts" title="Accounts"></a>
          </li>
          <li><a href="${currenciesUrl}"><img src="/static/currency.png" alt="Currencies" title="Currencies"></a>
          </li>
          <li><a href="${customersUrl}"><img src="/static/customer.png" alt="Customers" title="Customers"></a>
          </li>
          <li><a href="${entriesUrl}"><img src="/static/entry.png" alt="Entries" title="Entries"></a></li>
          <li><a href="${settingsUrl}"><img src="/static/setting.png" alt="Settings" title="Settings"></a>
          </li>
        </ul>
      </div>
    </div>
  </div>

  <div id="main">
    <p id="user"><span id="userName">Hello, ${login}</span>
      <c:if test="${adminUrl ne null}">
        <span id="adminRole"><a href="${adminUrl}">ADMIN</a></span>
      </c:if>
      <span id="console"><a href="${logoutUrl}">LOGOUT</a></span>
    </p>
    <ul>
      <c:forEach var="currencyCash" items="${cash}">
        <li>${currencyCash.value} ${currencyCash.key}</li>
      </c:forEach>
    </ul>
    <h1>Secret page for admins only!</h1>

    <form action="/admin" method="POST">
      <table id="table">
        <tr>
          <th>Del</th>
          <th>User</th>
        </tr>
        <c:forEach items="${users}" var="user">
          <c:if test="${user.role ne \"SUPER\"}">
            <tr class="list">
              <td><input type="checkbox" name="check" value="${user.login}" id="${user.login}"/></td>
              <td><c:out value="${user.login}"/></td>
            </tr>
          </c:if>
        </c:forEach>
        <tr>
          <td colspan="2">
            <div class="addButtonInline">
              <input type="submit" value="Delete&#13;&#10;users" name="del"/>
            </div>
          </td>
        </tr>
      </table>
    </form>

    <c:if test="${superAdmin ne null}">
      ${superAdmin}
      <table class="table">
        <tr>
          <th>User</th>
          <th>Role</th>
          <th>Update</th>
        </tr>
        <c:forEach items="${users}" var="user">
          <c:if test="${user.role ne \"SUPER\"}">
            <form action="/admin" method="POST">
              <tr class="list">
                <td><c:out value="${user.login}"/></td>
                <td>
                  <select class="userRole" name="checkRole">
                    <c:forEach var="role" items="${allRoles}">
                      <c:choose>
                        <c:when test="${user.role eq role}">
                          <c:set var="s" value="selected"/>
                        </c:when>
                        <c:otherwise>
                          <c:set var="s" value=""/>
                        </c:otherwise>
                      </c:choose>
                      <c:if test="${role ne \"SUPER\"}">
                        <option ${s} value="${role}">${role}</option>
                      </c:if>
                    </c:forEach>
                  </select>
                </td>
                <td>
                  <input type="hidden" name="userLogin" value="${user.login}"/>
                  <button type="submit" title="Update role" name="update" class="up">Update</button>
                </td>
              </tr>
            </form>
          </c:if>
        </c:forEach>
      </table>
    </c:if>

  </div>
</div>
</body>
</html>
