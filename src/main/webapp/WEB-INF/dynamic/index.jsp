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
    <h1>Transactions</h1>
    <c:url value="/" var="filterUrl"/>
    <div id="filter">
      <form action="${filterUrl}" method="GET">
        <div class="select">
          <select class="accountFilter" name="accountId">
            <option value="">-</option>
            <c:forEach var="account" items="${accounts}">
              <c:choose>
                <c:when test="${account.id eq accountId}">
                  <c:set var="s" value="selected"/>
                </c:when>
                <c:otherwise>
                  <c:set var="s" value=""/>
                </c:otherwise>
              </c:choose>
              <option ${s} value="${account.id}">${account.name}</option>
            </c:forEach>
          </select>
          <select class="currencyFilter" name="currencyId">
            <c:forEach var="currency" items="${currencies}">
              <c:choose>
                <c:when test="${currency.id eq currencyId}">
                  <c:set var="s" value="selected"/>
                </c:when>
                <c:otherwise>
                  <c:set var="s" value=""/>
                </c:otherwise>
              </c:choose>
              <option ${s} value="${currency.id}">${currency.name}</option>
            </c:forEach>
          </select>
          <div id="score">${accountsSum}</div>
        </div>
        <div class="addFilterButton">
          <input type="submit" value="Filter"/>
        </div>
        <div class="select">
          <select class="customerFilter" name="customerId">
            <option value="">-</option>
            <c:forEach var="customer" items="${customers}">
              <c:choose>
                <c:when test="${customer.id eq customerId}">
                  <c:set var="s" value="selected"/>
                </c:when>
                <c:otherwise>
                  <c:set var="s" value=""/>
                </c:otherwise>
              </c:choose>
              <option ${s} value="${customer.id}">${customer.name}</option>
            </c:forEach>
          </select>
          <select class="entryFilter" name="entryId">
            <option value="">-</option>
            <c:forEach var="entry" items="${entries}">
              <c:choose>
                <c:when test="${entry.id eq entryId}">
                  <c:set var="s" value="selected"/>
                </c:when>
                <c:otherwise>
                  <c:set var="s" value=""/>
                </c:otherwise>
              </c:choose>
              <option ${s} value="${entry.id}">${entry.name}</option>
            </c:forEach>
          </select>
        </div>
      </form>
    </div>
    <c:if test="${accountId eq 0}">
      <c:set var="accountId" value="${defaultAccount.id}"/>
    </c:if>
    <div id="transactionLabel">
      <label class="link" for="checkTransaction" id="clickTransaction" title="addTransaction"></label></br>
      <input type="checkbox" id="checkTransaction">
      <div id="addTransaction">
        <c:url value="/addTransaction" var="addUrl"/>
        <form action="${addUrl}" method="POST">
          <div class="addComment">
            Comment</br>
            <input type="text" name="comment">
          </div>
          <div class="add">
            <div class="selectPattern">
              Account</br>
              <select name="accountId">
                <c:forEach var="account" items="${accounts}">
                  <c:choose>
                    <c:when test="${account.id eq defaultAccount.id}">
                      <c:set var="s" value="selected"/>
                      <c:set var="currencyId" value="${account.currency.id}"/>
                    </c:when>
                    <c:otherwise>
                      <c:set var="s" value=""/>
                    </c:otherwise>
                  </c:choose>
                  <option ${s} value="${account.id}">${account.name}</option>
                </c:forEach>
              </select>
            </div>
            <div class="selectPattern">
              Customer</br>
              <select name="customerId">
                <c:forEach var="customer" items="${customers}">
                  <c:choose>
                    <c:when test="${customer.id eq defaultCustomer.id}">
                      <c:set var="s" value="selected"/>
                    </c:when>
                    <c:otherwise>
                      <c:set var="s" value=""/>
                    </c:otherwise>
                  </c:choose>
                  <option ${s} value="${customer.id}">${customer.name}</option>
                </c:forEach>
              </select>
            </div>
            <div class="selectPattern">
              Entry</br>
              <select name="entryId">
                <c:forEach var="entry" items="${entries}">
                  <c:choose>
                    <c:when test="${entry.id eq entryId}">
                      <c:set var="s" value="selected"/>
                    </c:when>
                    <c:otherwise>
                      <c:set var="s" value=""/>
                    </c:otherwise>
                  </c:choose>
                  <c:if test="${entry.type ne transactionTransfer}">
                    <option ${s} value="${entry.id}">${entry.name}</option>
                  </c:if>
                </c:forEach>
              </select>
            </div>
          </div>
          <div class="addButton">
            <input type="submit" value="Send"/>
          </div>
          <div class="add">
            <div class="addSum">
              <div class="sumIn">
                <input type="text" name="sumIn" placeholder="0.0">
              </div>
              <div class="sumOut">
                <input type="text" name="sumOut" placeholder="0.0">
              </div>
            </div>
            <div class="addCurrency">
              <select name="currencyId">
                <c:forEach var="currency" items="${currencies}">
                  <c:choose>
                    <c:when test="${currency.id eq defaultAccount.currency.id}">
                      <c:set var="s" value="selected"/>
                    </c:when>
                    <c:otherwise>
                      <c:set var="s" value=""/>
                    </c:otherwise>
                  </c:choose>
                  <option ${s} value="${currency.id}">${currency.name}</option>
                </c:forEach>
              </select>
            </div>
          </div>
        </form>
      </div>
    </div>
    <div id="transferLabel">
      <label class="link" for="checkTransfer" id="clickTransfer" title="addTransfer"></label></br>
      <input type="checkbox" id="checkTransfer">
      <div id="addTransfer">
        <c:url value="/addTransfer" var="transferUrl"/>
        <form action="${transferUrl}" method="POST">
          <div class="accounts">
            <div class="accountOut">
              <span class="sendText">Send from:</span><select name="accountOutId">
              <c:forEach var="account" items="${accounts}">
                <c:choose>
                  <c:when test="${account.id eq defaultAccount.id}">
                    <c:set var="s" value="selected"/>
                  </c:when>
                  <c:otherwise>
                    <c:set var="s" value=""/>
                  </c:otherwise>
                </c:choose>
                <option ${s} value="${account.id}">${account.name}</option>
              </c:forEach>
            </select>
            </div>
            <div class="accountIn">
              <span class="sendText">Send to:</span><select name="accountInId">
              <c:forEach var="account" items="${accounts}">
                <c:choose>
                  <c:when test="${account.id eq accountId}">
                    <c:set var="s" value="selected"/>
                  </c:when>
                  <c:otherwise>
                    <c:set var="s" value=""/>
                  </c:otherwise>
                </c:choose>
                <option ${s} value="${account.id}">${account.name}</option>
              </c:forEach>
            </select>
            </div>
          </div>
          <div class="selectors">
            <div class="selectPattern">
              <input type="text" name="sum" placeholder="0.0">
            </div>
            <div class="selectCurrency">
              <select name="currencyId">
                <c:forEach var="currency" items="${currencies}">
                  <c:choose>
                    <c:when test="${currency.id eq defaultAccount.currency.id}">
                      <c:set var="s" value="selected"/>
                    </c:when>
                    <c:otherwise>
                      <c:set var="s" value=""/>
                    </c:otherwise>
                  </c:choose>
                  <option ${s} value="${currency.id}">${currency.name}</option>
                </c:forEach>
              </select>
            </div>
            <div class="selectPattern">
              <select name="customerId">
                <c:forEach var="customer" items="${customers}">
                  <c:choose>
                    <c:when test="${customer.id eq defaultCustomer.id}">
                      <c:set var="s" value="selected"/>
                    </c:when>
                    <c:otherwise>
                      <c:set var="s" value=""/>
                    </c:otherwise>
                  </c:choose>
                  <option ${s} value="${customer.id}">${customer.name}</option>
                </c:forEach>
              </select>
            </div>
          </div>
          <div class="addButton">
            <input type="submit" value="Send"/>
          </div>
        </form>
      </div>
    </div>
    <table id="transactions">
      <tr>
        <th></th>
        <th>Entry</th>
        <th>Sum in</th>
        <th>Sum out</th>
        <th>Currency</th>
        <th>Rate</th>
        <th>Customer</th>
        <th>Account</th>
        <th>Comment</th>
        <th></th>
      </tr>
      <c:forEach var="transaction" items="${transactions}">
        <tr class="transaction">
          <c:choose>
            <c:when test="${transaction.type eq transactionTransfer}">
              <c:set var="styleIcon" value="transferStyle"/>
            </c:when>
            <c:when test="${transaction.type eq transactionProfit}">
              <c:set var="styleIcon" value="profitStyle"/>
            </c:when>
            <c:when test="${transaction.type eq transactionLoss}">
              <c:set var="styleIcon" value="lossStyle"/>
            </c:when>
            <c:when test="${transaction.type eq transactionZero}">
              <c:set var="styleIcon" value="zeroStyle"/>
            </c:when>
            <c:otherwise>
              <c:set var="styleIcon" value=""/>
            </c:otherwise>
          </c:choose>
          <td class="${styleIcon}" title="${transaction.type}"></td>
          <td>${transaction.entry.name}</td>
          <td class="sumInTd">${transaction.sumIn}</td>
          <td class="sumOutTd">${transaction.sumOut}</td>
          <td>${transaction.currency.name}</td>
          <td>${transaction.exchange.rate}</td>
          <td>${transaction.customer.name}</td>
          <td>${transaction.account.name}</td>
          <td>${transaction.comment}</td>
          <td class="deleteButton">
            <form action="${deleteUrl}" method="POST">
              <input type="hidden" name="transactionId" value="${transaction.id}"/>
              <button type="submit" title="Delete" name="delete"></button>
            </form>
          </td>
        </tr>
        <tr class="hidden">
          <td></td>
          <td colspan="8">
            <div class="wrapperWrapper">
              <div class="wrapperUpdate">
                <c:choose>
                  <c:when test="${transaction.type eq transactionTransfer}">
                    <div class="updateTransfer">
                      <form action="${updateTransactionUrl}" method="POST">
                        <div class="accounts">
                          <div class="accountOut">
                            <span class="sendText">Send from:</span>
                            <select name="accountId">
                              <c:forEach var="account" items="${accounts}">
                                <c:choose>
                                  <c:when test="${((transaction.account.id eq account.id) && (transaction.sumOut gt 0))
                                                        || ((transaction.transaction.account.id eq account.id) && (transaction.transaction.sumOut gt 0))}">
                                    <c:set var="s" value="selected"/>
                                  </c:when>
                                  <c:otherwise>
                                    <c:set var="s" value=""/>
                                  </c:otherwise>
                                </c:choose>
                                <option ${s}
                                  value="${account.id}">${account.name}</option>
                              </c:forEach>
                            </select>
                          </div>
                          <div class="accountIn">
                            <span class="sendText">Send to:</span>
                            <select name="accountLinkId">
                              <c:forEach var="account" items="${accounts}">
                                <c:choose>
                                  <c:when test="${((transaction.account.id eq account.id) && (transaction.sumIn gt 0))
                                                    || ((transaction.transaction.account.id eq account.id) && (transaction.transaction.sumIn ge 0))}">
                                    <c:set var="s" value="selected"/>
                                  </c:when>
                                  <c:otherwise>
                                    <c:set var="s" value=""/>
                                  </c:otherwise>
                                </c:choose>
                                <option ${s}
                                  value="${account.id}">${account.name}</option>
                              </c:forEach>
                            </select>
                          </div>
                        </div>
                        <div class="selectors">
                          <div class="selectPattern">
                            <c:choose>
                              <c:when test="${transaction.sumIn gt 0}">
                                <c:set var="sum" value="${transaction.sumIn}"/>
                              </c:when>
                              <c:when test="${transaction.sumOut ge 0}">
                                <c:set var="sum" value="${transaction.sumOut}"/>
                              </c:when>
                              <c:otherwise>
                                <c:set var="sum" value="0.0"/>
                              </c:otherwise>
                            </c:choose>
                            <input type="text" name="sumOut" value="${sum}">
                          </div>
                          <div class="selectCurrency">
                            <select name="currencyId">
                              <c:forEach var="currency" items="${currencies}">
                                <c:choose>
                                  <c:when test="${transaction.currency.id eq currency.id}">
                                    <c:set var="s" value="selected"/>
                                  </c:when>
                                  <c:otherwise>
                                    <c:set var="s" value=""/>
                                  </c:otherwise>
                                </c:choose>
                                <option ${s}
                                  value="${currency.id}">${currency.name}</option>
                              </c:forEach>
                            </select>
                          </div>
                          <div class="selectPattern">
                            <select name="customerId">
                              <c:forEach var="customer" items="${customers}">
                                <c:choose>
                                  <c:when test="${transaction.customer.id eq customer.id}">
                                    <c:set var="s" value="selected"/>
                                  </c:when>
                                  <c:otherwise>
                                    <c:set var="s" value=""/>
                                  </c:otherwise>
                                </c:choose>
                                <option ${s}
                                  value="${customer.id}">${customer.name}</option>
                              </c:forEach>
                            </select>
                          </div>
                        </div>
                        <div class="addButton">
                          <input type="hidden" name="entryId"
                                 value="${transaction.entry.id}"/>
                          <input type="hidden" name="transactionId"
                                 value="${transaction.id}"/>
                          <input type="submit" value="Update"/>
                        </div>
                      </form>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <div class="updateTransaction">
                      <form action="${updateTransactionUrl}" method="POST">
                        <div class="addComment">
                          Comment</br>
                          <input type="text" name="comment"
                                 placeholder="${transaction.comment}">
                        </div>
                        <div class="add">
                          <div class="selectPattern">
                            Account</br>
                            <select name="accountId">
                              <c:forEach var="account" items="${accounts}">
                                <c:choose>
                                  <c:when test="${transaction.account.id eq account.id}">
                                    <c:set var="s" value="selected"/>
                                  </c:when>
                                  <c:otherwise>
                                    <c:set var="s" value=""/>
                                  </c:otherwise>
                                </c:choose>
                                <option ${s}
                                  value="${account.id}">${account.name}</option>
                              </c:forEach>
                            </select>
                          </div>
                          <div class="selectPattern">
                            Customer</br>
                            <select name="customerId">
                              <c:forEach var="customer" items="${customers}">
                                <c:choose>
                                  <c:when test="${transaction.customer.id eq customer.id}">
                                    <c:set var="s" value="selected"/>
                                  </c:when>
                                  <c:otherwise>
                                    <c:set var="s" value=""/>
                                  </c:otherwise>
                                </c:choose>
                                <option ${s}
                                  value="${customer.id}">${customer.name}</option>
                              </c:forEach>
                            </select>
                          </div>
                          <div class="selectPattern">
                            Entry</br>
                            <select name="entryId">
                              <c:forEach var="entry" items="${entries}">
                                <c:choose>
                                  <c:when test="${transaction.entry.id eq entry.id}">
                                    <c:set var="s" value="selected"/>
                                  </c:when>
                                  <c:otherwise>
                                    <c:set var="s" value=""/>
                                  </c:otherwise>
                                </c:choose>
                                <option ${s} value="${entry.id}">${entry.name}</option>
                              </c:forEach>
                            </select>
                          </div>
                        </div>
                        <div class="addButton">
                          <input type="hidden" name="transactionId"
                                 value="${transaction.id}"/>
                          <input type="submit" value="Update"/>
                        </div>
                        <div class="add">
                          <div class="addSum">
                            <div class="sumIn">
                              <input type="text" name="sumIn"
                                     value="${transaction.sumIn}">
                            </div>
                            <div class="sumOut">
                              <input type="text" name="sumOut"
                                     value="${transaction.sumOut}">
                            </div>
                          </div>
                          <div class="addCurrency">
                            <select name="currencyId">
                              <c:forEach var="currency" items="${currencies}">
                                <c:choose>
                                  <c:when test="${transaction.currency.id eq currency.id}">
                                    <c:set var="s" value="selected"/>
                                  </c:when>
                                  <c:otherwise>
                                    <c:set var="s" value=""/>
                                  </c:otherwise>
                                </c:choose>
                                <option ${s}
                                  value="${currency.id}">${currency.name}</option>
                              </c:forEach>
                            </select>
                          </div>
                        </div>
                      </form>
                    </div>
                  </c:otherwise>
                </c:choose>
              </div>
            </div>
          </td>
          <td></td>
        </tr>
      </c:forEach>
    </table>
  </div>
</div>
</body>
</html>
