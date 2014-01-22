<%@ page import="info.pascalkrause.costanalyzr.Category" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="${resource(dir: 'css/nvd3', file: 'nv.d3.min.css')}" type="text/css">
<link rel="stylesheet" href="${resource(dir: 'css/treeMap', file: 'bootstrap-listTree.css')}" type="text/css">
<meta name="layout" content="costanalyzr" />
<title>Home</title>
</head>
<body>
<g:render template="/template/mainNavBar" model="[active: 'dataOutput']"/>
  <div class="container">
    <g:if test="${!flash?.isEmpty()}">
      <g:render template="/template/responseMessage" model="[responseMessage: flash]"/>
    </g:if>
    <div class="page-header">
      <h2>Auswertungen</h2>
    </div>
    <div class="row">
      <div class="col-md-12">
          <g:monthChart date="${new Date().parse('yyyy-MM','2013-12')}" categories="${Category.getTopLevelCategories()}"></g:monthChart>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
          <g:monthChart date="${new Date().parse('yyyy-MM','2013-11')}" categories="${Category.getTopLevelCategories()}"></g:monthChart>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
          <g:monthChart date="${new Date().parse('yyyy-MM','2013-10')}" categories="${Category.getTopLevelCategories()}"></g:monthChart>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
          <g:monthChart date="${new Date().parse('yyyy-MM','2013-09')}" categories="${Category.getTopLevelCategories()}"></g:monthChart>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
          <g:monthChart date="${new Date().parse('yyyy-MM','2013-08')}" categories="${Category.getTopLevelCategories()}"></g:monthChart>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
          <g:monthChart date="${new Date().parse('yyyy-MM','2012-08')}" categories="${Category.getTopLevelCategories()}"></g:monthChart>
      </div>
    </div>
  </div>
</body>
</html>