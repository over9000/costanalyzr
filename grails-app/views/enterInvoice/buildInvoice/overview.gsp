<%@ page import="info.pascalkrause.costanalyzr.Invoice" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="costanalyzr" />
<link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery-ui-1.10.3.css')}" type="text/css">
<title>Übersicht</title>
</head>
<body>
  <g:render template="/template/mainNavBar" model="[active: 'dataInput']" />
  <g:render template="/template/secondNavBarDataInput" model="[active: 'none']" />
  <div style="height: 15px"></div>
  <g:set var="invoice" value="${Invoice.get(flow?.invoiceId)}" />
  <div class="container">
    <g:if test="${!flash?.isEmpty()}">
      <g:render template="/template/responseMessage" model="[responseMessage: flash]"/>
    </g:if>
    <div style="height: 15px"></div>
      <g:render template="/enterInvoice/buildInvoice/webflowNavbar" model="[webflowNavbarCurrentState: 5]"/>
    <div style="height: 15px"></div>
    <div class="well">
      <div class="row">
        <div class="col-md-6"> <h3>Übersicht zu Rechnung #${invoice?.id}</h3> </div>
      </div>
      <!-- Inhalt Begin -->
      <div class="row">
        <div class="col-md-2"><h4>Filiale</h4></div>
        <div class="col-md-6"><h4><small>${invoice?.getStore()?.toString()}</small></h4></div>
        <div class="col-md-2"><h4>Datum</h4></div>
        <div class="col-md-2"><h4><small>${invoice?.getCreationDateAsString()}</small></h4></div>
      </div>
      <div class="row">
        <div class="col-md-2"><h4>Betrag</h4></div>
        <div class="col-md-6"><h4><small><g:formatNumber type="currency" currencyCode="EUR" number="${invoice?.getTotalGrossPrice()}" format="#0.00" /></small></h4></div>
        <div class="col-md-2"><h4>Zahlungsart</h4></div>
        <div class="col-md-2"><h4><small>${invoice?.getPaymentMethod()}</small></h4></div>
      </div>
      <table class="tablesorter table table-hover table-condensed">
        <thead>
          <tr>
            <th><a style="text-decoration: none; cursor: default; width: 5%">Artikel <span class="glyphicon"></span></a></th>
            <th><a style="text-decoration: none; cursor: default; width: 10%">Anzahl <span class="glyphicon"></span></a></th>
            <th><a style="text-decoration: none; cursor: default; width: 25%">Mwst <span class="glyphicon"></span></a></th>
            <th><a style="text-decoration: none; cursor: default; width: 25%">Kommentar <span class="glyphicon"></span></a></th>
            <th><a style="text-decoration: none; cursor: default; width: 25%">Reduziert <span class="glyphicon"></span></a></th>
            <th><a style="text-decoration: none; cursor: default; width: 25%">Preis <span class="glyphicon"></span></a></th>
          </tr>
        </thead>
        <tbody>
          <g:each in="${flow?.invoiceItems}" status="i" var="invoiceItemInstance">
            <tr>
              <td>${invoiceItemInstance?.getArticle()?.toString()}</td>
              <td>${invoiceItemInstance?.getCount()}</td>
              <td>${invoiceItemInstance?.getTaxRate()}</td>
              <td>${invoiceItemInstance?.getComment()}</td>
              <td style="width:100px"><g:checkBox  name="myCheckbox" disabled="disabled" value="${invoiceItemInstance?.getReduced()}" /></td>
              <td>${invoiceItemInstance?.getGrossPrice()}</td>
            </tr>
          </g:each>
        </tbody>
      </table>
      <!-- Inhalt Ende -->
      <g:form  role="form" name="chooseOrAddConcern" action="buildInvoice" method="post">
        <div style="height: 15px"></div>
        <div class="row">
          <div class="col-md-2">
            <div class="btn-group">
              <g:submitButton name="back" class="btn btn-info" value="Zurück" formnovalidate="" />
              <g:submitButton name="done" class="btn btn-info" value="Fertig" />
            </div>
          </div>
          <div class="col-md-3">
            <div class="btn-group">
              <g:submitButton name="addNewInvoice" class="btn btn-info" value="Eine weitere Rechnung eintragen" />
            </div>
          </div>
        </div>
      </g:form>
    </div>
  </div>
</body>
</html>