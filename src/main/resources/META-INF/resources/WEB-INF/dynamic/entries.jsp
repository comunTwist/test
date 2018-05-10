<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <title>Entries</title>
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

  <c:url value="/addEntry" var="addUrl"/>
  <c:url value="/updateEntry" var="updateUrl"/>
  <c:url value="/deleteEntry" var="deleteUrl"/>

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
    <h1>Entries</h1>
    <div id="labelLabel">
      <label class="link" for="checkCheck" id="clickEntry" title="addEntry"></label></br>
      <input type="checkbox" id="checkCheck">
      <div id="addAdd">
        <form action="${addUrl}" method="POST">
          <div class="field">
            Entry name<br/><input type="text" name="name">
          </div>
          <div class="addButtonInline">
            <input type="submit" value="Add&#13;&#10;entry"/>
          </div>
        </form>
      </div>
    </div>
    <table id="table">
      <tr>
        <th>Entry name</th>
        <th></th>
      </tr>
      <c:forEach items="${entries}" var="entry">
        <tr class="list">
          <td>${entry.name}</td>
          <td class="deleteButton">
            <form action="${deleteUrl}" method="POST">
              <input type="hidden" name="entryId" value="${entry.id}"/>
              <button type="submit" title="Delete" name="delete"></button>
            </form>
          </td>
        </tr>
        <tr class="hidden">
          <td>
            <div class="wrapperWrapper">
              <div class="wrapperUpdate">
                <div class="updateUpdate">
                  <form action="${updateUrl}" method="POST">
                    <div class="field">
                      Entry name</br>
                      <input type="text" name="name" value="${entry.name}"/>
                    </div>
                    <div class="addButtonInline">
                      <input type="hidden" name="entryId" value="${entry.id}"/>
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
