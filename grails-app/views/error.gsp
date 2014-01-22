<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.sql.SQLException"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="costanalyzr" />
<title>Error</title>
<link rel="stylesheet" href="${resource(dir: 'css', file: 'errors.css')}" type="text/css">
</head>
<body>
<g:render template="/template/mainNavBar" model="[active: 'blubb']"/>
  <div class="container">
  
    <g:if test="${exception.cause instanceof SQLException}">
      <g:if test="${exception.cause.getNextException() != null}">
        <g:renderException exception="${exception?.cause?.getNextException()}" />
      </g:if>
    </g:if>
    <g:else>
      <g:renderException exception="${exception?.cause}" />
    </g:else>
  </div>
</body>
</html>