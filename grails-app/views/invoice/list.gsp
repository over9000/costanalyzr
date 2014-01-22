<%@ page import="info.pascalkrause.costanalyzr.Invoice" %>
<%@ page import="info.pascalkrause.costanalyzr.Store" %>
<%@ page import="info.pascalkrause.costanalyzr.PaymentMethods" %>
<%@ page import="info.pascalkrause.costanalyzr.Utils" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="costanalyzr" />
<r:require modules="typeahead, typeahead_hogan"/>
<title>Übersicht: Rechnungen</title>
<link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery-ui-1.10.3.css')}" type="text/css">
<link rel="stylesheet" href="${resource(dir: 'css/typeahead', file: 'typeahead.css')}" type="text/css">
<link rel="stylesheet" href="${resource(dir: 'css/typeahead', file: 'article.css')}" type="text/css">
<link rel="stylesheet" href="${resource(dir: 'css/typeahead', file: 'store.css')}" type="text/css">
</head>
<body>
  <g:render template="/template/mainNavBar" model="[active: 'dataInput']"/>
  <g:render template="/template/secondNavBarDataInput" model="[active: 'invoice']"/>
  <div style="height:15px"></div>
  <div class="container">
    <g:if test="${!flash.isEmpty()}">
      <g:render template="/template/responseMessage" model="[responseMessage: flash]"/>
    </g:if>
    <h2>Übersicht: Rechnungen</h2>
    <div style="height:15px"></div>
    <g:render template="/invoice/addForm"/>
    <g:jqueryTableSorter class="table table-hover table-condensed">
      <thead>
        <tr class="row">
          <th class="col-md-1"><a style="text-decoration: none; cursor: default; width: 5%"># <span class="glyphicon"></span></a></th>
          <th class="col-md-1"><a style="text-decoration: none; cursor: default; width: 10%">Posten <span class="glyphicon"></span></a></th>
          <th class="col-md-4"><a style="text-decoration: none; cursor: default; width: 30%">Filiale <span class="glyphicon"></span></a></th>
          <th class="col-md-1"><a style="text-decoration: none; cursor: default; width: 10%">Datum <span class="glyphicon"></span></a></th>
          <th class="col-md-2"><a style="text-decoration: none; cursor: default; width: 10%">Zahlungsart <span class="glyphicon"></span></a></th>
          <th class="col-md-1"><a style="text-decoration: none; cursor: default; width: 10%">Euro <span class="glyphicon"></span></a></th>
        </tr>
      </thead>
      <tbody>
        <g:each in="${invoiceInstanceList}" status="i" var="invoiceInstance">
          <tr class="row">
         <g:if test="${invoiceToEdit == invoiceInstance.getId()}">
              <g:form name="updateOrDeleteListItem" method="post">
                <g:hiddenField name="invoiceId" value="${invoiceInstance.id}" />
                <td ><a id="listId${invoiceInstance.id}" style="text-decoration: none; cursor: default"/> ${invoiceInstance.id}</td>
                <td >
                  <g:if test="${invoiceInstance?.invoiceItems != null}">
                    <button type="button" disabled="disabled" class="btn btn-sm btn-primary">Erst speichern</button>
                  </g:if>
                  <g:else>
                    <button type="button" disabled="disabled" class="btn btn-sm btn-primary">wird ermittelt</button>
                  </g:else>
                </td>
                <td >
                 <g:hiddenField id="invoiceStoreIdEdit" name="invoiceStoreId" value="${invoiceInstance?.getStore()?.getId()}"/>
                 <g:typeaheadStoreSelect id="invoiceStoreIdSelectEdit" required="" name="invoiceStoreIdSelect" value="${invoiceInstance?.getStore()?.toString()}" class="form-control" placeholder="${invoiceInstance?.getStore()?.toString()}" />
                 <g:javascript>
                  $(function() {
                    $("#invoiceStoreIdSelectEdit").on("typeahead:selected typeahead:autocompleted", function(e,datum) {
                      $("#invoiceStoreIdEdit").val(datum.id);
                    });
                  })
                </g:javascript>
                </td>
                
                <td ><g:jqueryUIDatePicker id="invoiceCreationDateToEdit" name="invoiceCreationDate" class="form-control"
                  required="" value="${invoiceInstance.getCreationDateAsString()}" placeholder="z.B. 2013-06-08" />
                </td>
                <td ><g:select id="invoicePaymentMethod" name="invoicePaymentMethod" from="${PaymentMethods.values()}" keys="${PaymentMethods.values()*.name()}" value="${invoiceInstance?.paymentMethod}" class="form-control" /></td>
                <td ><input id="invoiceTotalGrossPrice" name="invoiceTotalGrossPrice" type="text" class="form-control"
                  disabled="" value="<g:formatNumber number="${invoiceInstance.getTotalGrossPrice()}" format="#0.00" />"/>
                </td>
                <td class="col-md-2">
                  <g:actionSubmit value="Speichern" class="btn btn-info" action="saveInvoice" />
                  <g:actionSubmit value="Löschen" class="btn btn-warning" action="deleteInvoice" />
                </td>
              </g:form>
              <g:javascript>window.location.href = "#listId${invoiceToEdit}";</g:javascript>
            </g:if>
            <g:else>
              <td >${invoiceInstance.id}</td>
              <g:if test="${invoiceInstance?.invoiceItems != null}">
                <td ><a data-toggle="modal" data-keyboard="true" data-show="true" class="btn btn-sm btn-primary" href="<g:createLink controller="invoiceItem"
                  action="getInvoiceItemList" params='[invoiceId: "${invoiceInstance.id}"]'/>" data-target="#invoiceItemModal">(${invoiceInstance?.invoiceItems?.size()}) Anzeigen</a>
                </td>
              </g:if>
              <g:else>
                <td><button type="button" disabled="disabled" class="btn btn-sm btn-primary">wird ermittelt</button></td>
              </g:else>
              <td >${invoiceInstance.getStore()?.toString()}</td>
              <td >${invoiceInstance.getCreationDateAsString()}</td>
              <td >${invoiceInstance.paymentMethod}</td>
              <td ><g:formatNumber number="${invoiceInstance.getTotalGrossPrice()}" format="#0.00" /></td>
              <td class="col-md-2"><g:form name="updateOrDeleteListItem" method="post">
                <g:hiddenField name="invoiceId" value="${invoiceInstance.id}" />
                <g:actionSubmit value="Ändern" class="btn btn-info" action="updateInvoice" />
                <g:actionSubmit value="Löschen" class="btn btn-warning" action="deleteInvoice" />
                </g:form>
             </td>
           </g:else>
           </tr>
        </g:each>
      </tbody>
    </g:jqueryTableSorter>
    <g:javascript src="/bootstrap/invoiceItemModalContent.js"/>
    <div style="width: 100%" class="modal fade" id="invoiceItemModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div style="width: 100%" class="modal-dialog"></div>
    </div>
  </div>
</body>
</html>