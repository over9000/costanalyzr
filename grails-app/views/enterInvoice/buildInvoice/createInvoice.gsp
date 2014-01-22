<%@ page import="info.pascalkrause.costanalyzr.Concern" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="info.pascalkrause.costanalyzr.PaymentMethods" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="costanalyzr" />
<link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery-ui-1.10.3.css')}" type="text/css">
<title>Neue Rechnung eintragen</title>
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
      <g:render template="/enterInvoice/buildInvoice/webflowNavbar" model="[webflowNavbarCurrentState: 3]"/>
    <div style="height: 15px"></div>
		<div class="well">
			<g:form  role="form" name="chooseOrAddConcern" action="buildInvoice" method="post">
			  <div class="row">
			    <div class="col-md-6">
			      <h3>Datum und Zahlungsart eintragen</h3>
			    </div>
			  </div>
        <fieldset>
          <div class="row">
            <div class="col-md-3">
              <label for="invoiceCreationDate">Datum (erforderlich)</label>
              <g:jqueryUIDatePicker id="invoiceCreationDate" name="invoiceCreationDate" type="text" class="form-control" required=""
                value="${new SimpleDateFormat('yyyy-MM-dd').format(new Date())}" placeholder="z.B. 2013-06-08" />
            </div>
            <div class="col-md-3">
              <label for="invoicePaymentMethod">Zahlungsart (erforderlich)</label>
              <g:select id="invoicePaymentMethod" name="invoicePaymentMethod" from="${PaymentMethods.values()}" keys="${PaymentMethods.values()*.name()}" value="" class="form-control" />
            </div>
          </div>
        </fieldset>
        <div style="height: 15px"></div>
        <div class="row">
          <div class="col-md-3">
            <div class="btn-group">
              <g:submitButton name="cancel" class="btn btn-info" value="Abbrechen" formnovalidate="" />
              <g:submitButton name="back" class="btn btn-info" value="Zurück" formnovalidate="" />
              <g:submitButton name="next" class="btn btn-info" value="Weiter" />
            </div>
          </div>
        </div>
		  </g:form>
    </div>
	</div>
</body>
</html>