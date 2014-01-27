<%@ page import="info.pascalkrause.costanalyzr.Invoice" %>
<%@ page import="info.pascalkrause.costanalyzr.Category" %>
<%@ page import="info.pascalkrause.costanalyzr.Article" %>
<%@ page import="info.pascalkrause.costanalyzr.InvoiceItem" %>
<%@ page import="info.pascalkrause.costanalyzr.Store" %>
<%@ page import="info.pascalkrause.costanalyzr.Concern" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="costanalyzr" />
<title>Daten eintragen</title>
</head>
<body>
<g:render template="/template/mainNavBar" model="[active: 'dataInput']"/>
<g:render template="/template/secondNavBarDataInput"/>
  <div class="container">
  <div style="height: 15px"></div>
    <g:if test="${!flash.isEmpty()}">
      <g:render template="/template/responseMessage" model="[responseMessage: flash]"/>
    </g:if>
    <div class="page-header">
      <h2>Gesamter Datenbestand</h2>
    </div>
    <div class="well">
      <div class="row">
        <div class="col-md-3"><h4>Älteste Rechnung</h4></div>
        <div class="col-md-6"><h4><small><g:formatDate format="yyyy-MM-dd" date="${Invoice.getOldestInvoiceCreationDate()?.getTime()}"/></small></h4></div>
      </div>
      <div class="row">
        <div class="col-md-3"><h4>Neuste Rechnung</h4></div>
        <div class="col-md-6"><h4><small><g:formatDate format="yyyy-MM-dd" date="${Invoice.getLatestInvoiceCreationDate()?.getTime()}"/></small></h4></div>
      </div>
      <div class="row">
        <div class="col-md-3"><h4>Gesamtanzahl Rechnungen:</h4></div>
        <div class="col-md-6"><h4><small>${Invoice.list().size()}</small></h4></div>
      </div>
      <div class="row">
        <div class="col-md-3"><h4>Gesamtanzahl Artikel:</h4></div>
        <div class="col-md-6"><h4><small>${Article.list().size()}</small></h4></div>
      </div>
      <div class="row">
        <div class="col-md-3"><h4>Gesamtanzahl Kategorien:</h4></div>
        <div class="col-md-6"><h4><small>${Category.list().size()}</small></h4></div>
      </div>
      <div class="row">
        <div class="col-md-3"><h4>Gesamtanzahl Filialen:</h4></div>
        <div class="col-md-6"><h4><small>${Store.list().size()}</small></h4></div>
      </div>
      <div class="row">
        <div class="col-md-3"><h4>Gesamtanzahl Konzerne:</h4></div>
        <div class="col-md-6"><h4><small>${Concern.list().size()}</small></h4></div>
      </div>
    </div>
  </div>
</body>
</html>