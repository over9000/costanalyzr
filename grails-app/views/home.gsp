<%@page import="java.util.regex.Pattern.Category"%>
<%@ page import="info.pascalkrause.costanalyzr.Invoice" %>
<%@ page import="info.pascalkrause.costanalyzr.Category" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="${resource(dir: 'css/nvd3/v1.15', file: 'nv.d3.css')}" type="text/css">
<meta name="layout" content="costanalyzr" />
<title>Home</title>
</head>
<body>
<g:render template="/template/mainNavBar" model="[active: 'home']"/>
  <div class="container">
    <g:if test="${!flash?.isEmpty()}">
      <g:render template="/template/responseMessage" model="[responseMessage: flash]"/>
    </g:if>
    <div class="page-header">
      <h2>Aktueller Monat</h2>
    </div>
    <% GregorianCalendar currentDate = new Date().toCalendar() %>
    <% GregorianCalendar fromDate = new GregorianCalendar(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), 1) %>
    <% GregorianCalendar toDate = currentDate %>
    <div class="row">
      <div class="col-md-12">
          <g:stackedAreaChart fromDate="${fromDate.getTime()}" toDate="${toDate.getTime()}" timePeriodType="month" categories="${Category.getTopLevelCategories().toString()}"></g:stackedAreaChart>
      </div>
    </div>
    <g:each in="${Category.getTopLevelCategories()}" status="i" var="categoryInstance">
      <div class="row">
        <div class="col-md-12">
          <g:pieChart fromDate="${fromDate.getTime()}" toDate="${toDate.getTime()}" timePeriodType="month" timePeriodType="month" showAllChildren="" categories="${categoryInstance.toString()}" ></g:pieChart>
        </div>
      </div>
    </g:each>
  </div>
</body>
</html>