<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <title>Accounts</title>
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
  <c:set var="admin" value="ROLE_ADMIN"/>
  <c:set var="superR" value="ROLE_SUPER"/>

  <c:url value="/deleteAccount" var="deleteUrl"/>
  <c:url value="/addAccount" var="addUrl"/>
  <c:url value="/updateAccount" var="updateUrl"/>


  <c:forEach var="s" items="${roles}">
    <c:if test="${(s eq superR) || (s eq admin)}">
      <c:url value="/admin" var="adminUrl"/>
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
    <h1>Accounts</h1>
    <div id="labelLabel">
      <label class="link" for="checkCheck" id="clickAccount" title="addAccount"></label></br>
      <input type="checkbox" id="checkCheck">
      <div id="addAdd">
        <form action="${addUrl}" method="POST">
          <div class="field">
            Account name</br><input type="text" name="name">
          </div>
          <div class="fieldSum">
            Start sum</br><input type="text" name="sum">
          </div>
          <div class="field">
            <select name="currencyId">
              <c:forEach var="currency" items="${currencies}">
                <option value="${currency.id}">${currency.name}</option>
              </c:forEach>
            </select>
          </div>
          <div class="addButtonInline">
            <input type="submit" value="Add&#13;&#10;account"/>
          </div>
        </form>
      </div>
    </div>
    <table id="table">
      <tr>
        <th>Account name</th>
        <th>Balance</th>
        <th>Currency</th>
        <th></th>
      </tr>
      <c:forEach items="${accounts}" var="account">
        <tr class="list">
          <td> ${account.name}</td>
          <td>${account.sum}</td>
          <td> ${account.currency.name}</td>

          <td class="deleteButton">
            <form action="${deleteUrl}" method="POST">
              <input type="hidden" name="accountId" value="${account.id}"/>
              <button type="submit" title="Delete" name="delete"></button>
            </form>
          </td>
        </tr>
        <tr class="hidden">
          <td colspan="3">
            <div class="wrapperWrapper">
              <div class="wrapperUpdate">
                <div class="updateUpdate">
                  <form action="${updateUrl}" method="POST">
                    <div class="field">
                      Account name</br>
                      <input type="text" name="name" value="${account.name}"/>
                    </div>
                    <div class="fieldSum">
                      Balance</br>
                      <input type="text" name="restSum" value="${account.sum}"/>
                    </div>
                    <div class="field">
                      Currency</br>
                      <select name="currencyId">
                        <c:forEach var="currency" items="${currencies}">
                          <c:choose>
                            <c:when test="${account.currency.id eq currency.id}">
                              <c:set var="s" value="selected"/>
                            </c:when>
                            <c:otherwise>
                              <c:set var="s" value=""/>
                            </c:otherwise>
                          </c:choose>
                          <option  ${s} value="${currency.id}">${currency.name}</option>
                        </c:forEach>
                      </select>
                    </div>
                    <div class="addButtonInline">
                      <input type="hidden" name="accountId" value="${account.id}"/>
                      <input type="submit" value="Update" name="update">
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </td>
          <td>
          </td>
        </tr>
      </c:forEach>
    </table>
  </div>
</div>
</body>
</html>
