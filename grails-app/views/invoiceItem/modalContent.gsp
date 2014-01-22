<div style="width: 80%;margin: 50px 0 0 10%" class="modal-content">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">X</button>
    <h2 class="modal-title" id="myModalLabel">Rechnungsposten zu Rechnung #${invoiceInstance?.id}</h2>
    <div style="height:15px"></div>
    <div class="row">
      <div class="col-md-2"><h4>Filiale</h4></div>
      <div class="col-md-6"><h4><small>${invoiceInstance?.getStore().toString()}</small></h4></div>
      <div class="col-md-1"><h4>Datum</h4></div>
      <div class="col-md-3"><h4><small>${invoiceInstance?.getCreationDateAsString()}</small></h4></div>
    </div>
    <div class="row">
      <div class="col-md-2"><h4>Betrag</h4></div>
      <div class="col-md-6"><h4><small><g:formatNumber type="currency" currencyCode="EUR" number="${invoiceInstance?.getTotalGrossPrice()}" format="#0.00" /></small></h4></div>
      <div class="col-md-1"><h4>Zahlungsart</h4></div>
      <div class="col-md-3"><h4><small>${invoiceInstance?.paymentMethod}</small></h4></div>
    </div>
    <div class="row">
    </div>
  </div>
  <div class="modal-body">
    <div id="invoiceItemModalBody">
      <g:render template="/invoiceItem/modalBody" model="[invoiceInstance: invoiceInstance]"/>
    </div>
  </div>
</div>