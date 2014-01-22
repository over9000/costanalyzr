<div style="width: 100%" class="modal fade" id="invoiceItemModal${invoiceInstance.id}" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div style="width: 100%" class="modal-dialog">
    <div style="width: 80%;margin: 50px 0 0 10%" class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">X</button>
          <h2 class="modal-title" id="myModalLabel">Rechnungsposten zu Rechnung #${invoiceInstance.id}</h2>
        <div style="height:15px"></div>
				<div class="row">
  				<div class="col-md-2"><h4>Filiale</h4></div>
				  <div class="col-md-9"><h4><small>${invoiceInstance.storeId?.toString()}</small></h4></div>
			  </div>
        <div class="row">
          <div class="col-md-2"><h4>Datum</h4></div>
          <div class="col-md-9"><h4><small>${invoiceInstance.getCreationDateAsString()}</small></h4></div>
        </div>
        <div class="row">
          <div class="col-md-2"><h4>Zahlungsart</h4></div>
          <div class="col-md-9"><h4><small>${invoiceInstance.paymentMethod}</small></h4></div>
        </div>
      </div>
      <div id="invoiceItemModalBody${invoiceInstance.id}" class="modal-body">
        <g:render template="/invoiceItem/modalBody" model="[invoiceInstance: invoiceInstance]"/>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->