<%@ page import="info.pascalkrause.costanalyzr.Concern" %>
<%@ page import="info.pascalkrause.costanalyzr.AvailableCountrys" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="costanalyzr" />
<link rel="stylesheet" href="${resource(dir: 'css/typeahead', file: 'typeahead.css')}" type="text/css">
<link rel="stylesheet" href="${resource(dir: 'css/typeahead', file: 'store.css')}" type="text/css">
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
      <g:render template="/enterInvoice/buildInvoice/webflowNavbar" model="[webflowNavbarCurrentState: 2]"/>
    <div style="height: 15px"></div>
    <div class="well">
      <g:form  role="form" name="chooseOrAddStore" action="buildInvoice" method="post">
        <div class="row">
          <div class="col-md-6">
            <h3>Wähle eine Filiale aus,</h3>
          </div>
        </div>
        <div class="row">
          <div class="col-md-6">
            <g:if test="${flow?.choosenStore == null}">
              <g:set var="choosenStoreText" value="" />
              <g:set var="choosenStoreValue" value="noSelection" />
            </g:if>
            <g:else>
              <g:set var="choosenStoreValue" value="${flow?.choosenStore?.id}" />
              <g:set var="choosenStoreText" value="${flow?.choosenStore?.toString()}" />
            </g:else>
            <g:hiddenField id="buildInvoiceFlowStoreId" name="buildInvoiceFlowStoreId" value="${choosenStoreValue}"/>
            <g:typeaheadStoreSelect id="buildInvoiceFlowStoreIdSelect" concernId="${flow?.choosenConcern?.getId()}"  noSelectionAvailable="" name="buildInvoiceFlowStoreIdSelect" value="${choosenStoreText}" class="form-control buildInvoiceFlowAddFormFieldsetDisabler" placeholder="Bitte Filiale auswählen (durch tippen)" required="" />
            <g:javascript>
              $(function() {
                $("#buildInvoiceFlowStoreIdSelect").on("typeahead:selected typeahead:autocompleted", function(e,datum) {
                  $("#buildInvoiceFlowStoreId").val(datum.id);
                  if(datum.id === 'noSelection' ) {
                    $('.buildInvoiceFlowAddFormFieldset').prop('disabled', false);
                  } else {
                    $('.buildInvoiceFlowAddFormFieldset').prop('disabled', true);
                  }
                });
              })
            </g:javascript>
          </div>
          <div class="col-md-6">
            <small>(Neue Filiale? "Neue Filiale anlegen" eintippen)</small>
          </div>
        </div>
        <div class="row">
          <div class="col-md-6">
           <h3>...oder lege eine neue an</h3>
          </div>
        </div>
        <fieldset class="buildInvoiceFlowAddFormFieldset" <g:if test="${flow?.choosenStore != null}"> disabled </g:if>>
					<div class="row">
						<div class="col-md-3">
							<label for="buildInvoiceFlowNewStoreConcernId">Zugehöriger Konzern (erforderlich)</label>
							<g:select id="buildInvoiceFlowNewStoreConcernId" name="buildInvoiceFlowNewStoreConcernId" disabled="" from="${info.pascalkrause.costanalyzr.Concern.list()}" optionKey="id" value="${flow?.choosenConcern?.id}" class="form-control" />
						</div>
						<div class="col-md-4">
							<label for="buildInvoiceFlowNewStoreStreetName">Straßenname (erforderlich)</label>
							<g:textField id="buildInvoiceFlowNewStoreStreetName" name="buildInvoiceFlowNewStoreStreetName" value="" placeholder="z.B. Heidenweg" required="" class="form-control" />
						</div>
						<div class="col-md-3">
							<label for="buildInvoiceFlowNewStoreStreetNumber">Hausnummer (erforderlich)</label>
							<g:textField id="buildInvoiceFlowNewStoreStreetNumber" name="buildInvoiceFlowNewStoreStreetNumber" value="" placeholder="z.B. 23b" required="" class="form-control" />
						</div>
					</div>
					<div class="row">
						<div class="col-md-3">
							<label for="buildInvoiceFlowNewStoreCountry">Land (erforderlich)</label>
							<g:select id="buildInvoiceFlowNewStoreCountry" name="buildInvoiceFlowNewStoreCountry" from="${AvailableCountrys.values()}" required="" keys="${AvailableCountrys.values()*.name()}" value="" class="form-control" />
						</div>
						<div class="col-md-4">
							<label for="buildInvoiceFlowNewStoreCity">Stadt (erforderlich)</label>
							<g:textField id="buildInvoiceFlowNewStoreCity" required="" class="form-control" name="buildInvoiceFlowNewStoreCity" value="" placeholder='z.B. Berlin' />
						</div>
						<div class="col-md-3">
							<label for="buildInvoiceFlowNewStoreZipcode">Postleitzahl (erforderlich)</label>
							<g:textField id="buildInvoiceFlowNewStoreZipcode" class="form-control" name="buildInvoiceFlowNewStoreZipcode" value="" required="" placeholder='z.B. 98465' />
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