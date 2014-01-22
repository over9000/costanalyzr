<%@ page import="info.pascalkrause.costanalyzr.Invoice" %>
<%@ page import="info.pascalkrause.costanalyzr.PaymentMethods" %>
<%@ page import="java.text.SimpleDateFormat" %>
<div class="well">
	<g:form class="form-inline" name="addInvoice" action="addInvoice" method="post">
		<div class="row">
			<div class="col-md-5">
				<label for="invoiceStoreId">Die ausstellende Filiale (erforderlich)</label>
				<g:hiddenField id="invoiceStoreId" name="invoiceStoreId" value=""/>
				<g:typeaheadStoreSelect id="invoiceStoreIdSelect" name="invoiceStoreIdSelect" value="" required="" class="form-control" placeholder="Bitte Filiale auswählen (durch tippen)" />
				<g:javascript>
				  $(function() {
            $("#invoiceStoreIdSelect").on("typeahead:selected typeahead:autocompleted", function(e,datum) {
             $("#invoiceStoreId").val(datum.id);
            });
          })
        </g:javascript>
			</div>
			<div class="col-md-2">
				<label for="invoiceCreationDate">Datum (erforderlich)</label>
				<g:jqueryUIDatePicker id="invoiceCreationDate" name="invoiceCreationDate" class="form-control"
				  required="" value="${new SimpleDateFormat('yyyy-MM-dd').format(new Date())}" placeholder="z.B. 2013-06-08" />
			</div>
			<div class="col-md-3">
				<label for="invoicePaymentMethod">Zahlungsart (erforderlich)</label>
				<g:select id="invoicePaymentMethod" name="invoicePaymentMethod" from="${PaymentMethods.values()}" optionKey="key" value="" class="form-control" />
			</div>
		</div>
		<div style="height: 15px"></div>
		<g:submitButton name="Hinzufügen" class="btn btn-info"
			value="Hinzufügen" />
	</g:form>
</div>