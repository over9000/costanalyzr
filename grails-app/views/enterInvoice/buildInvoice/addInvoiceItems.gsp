<%@ page import="info.pascalkrause.costanalyzr.Article" %>
<%@ page import="info.pascalkrause.costanalyzr.InvoiceItem" %>
<%@ page import="info.pascalkrause.costanalyzr.Invoice" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<link rel="stylesheet" href="${resource(dir: 'css/typeahead', file: 'typeahead.css')}" type="text/css">
<link rel="stylesheet" href="${resource(dir: 'css/typeahead', file: 'article.css')}" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="costanalyzr" />
<title>Rechnungsposten hinzufügen</title>
</head>
<body>
	<g:render template="/template/mainNavBar" model="[active: 'dataInput']" />
	<g:render template="/template/secondNavBarDataInput" model="[active: 'none']" />
  <div style="height: 15px"></div>
  <div class="container">
    <g:if test="${!flash?.isEmpty()}">
      <g:render template="/template/responseMessage" model="[responseMessage: flash]"/>
    </g:if>
    <div style="height: 15px"></div>
    <g:render template="/enterInvoice/buildInvoice/webflowNavbar" model="[webflowNavbarCurrentState: 4]"/>
    <div style="height: 15px"></div>
    <g:form action="buildInvoice" method="post">
      <g:render template="/enterInvoice/buildInvoice/addFormInvoiceItem"/>
		  <div class="well">
			  <g:jqueryTableSorter class="table table-hover table-condensed">
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
	        <g:hiddenField id="invoiceItemToDeleteId" name="invoiceItemToDeleteId" value=""/>
	          <g:each in="${flow?.invoiceItems}" status="i" var="invoiceItemInstance">
	            <tr>
	              <td>${invoiceItemInstance?.getArticle()?.toString()}</td>
	              <td>${invoiceItemInstance?.getCount()}</td>
	              <td>${invoiceItemInstance?.getTaxRate()}</td>
	              <td>${invoiceItemInstance?.getComment()}</td>
	              <td style="width:100px"><g:checkBox  name="myCheckbox" disabled="disabled" value="${invoiceItemInstance?.getReduced()}" /></td>
	              <td>${invoiceItemInstance?.getGrossPrice()}</td>
	              <td>
	                <g:submitButton name="deleteItem" class="btn btn-info" value="Löschen" onclick="jQuery('#invoiceItemToDeleteId').val(${invoiceItemInstance?.getId()})" />
	              </td>
	            </tr>
	          </g:each>
	        </tbody>
	      </g:jqueryTableSorter>
        <div class="row">
          <div class="col-md-3">
            <div class="btn-group">
              <g:submitButton name="cancel" class="btn btn-info" value="Abbrechen" formnovalidate="" />
              <g:submitButton name="back" class="btn btn-info" value="Zurück" formnovalidate="" />
              <g:submitButton name="next" class="btn btn-info" value="Weiter" />
            </div>
          </div>
        </div>
      </div>
    </g:form>
	</div>
</body>
</html>