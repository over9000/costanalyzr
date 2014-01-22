<%@ page import="info.pascalkrause.costanalyzr.AvailableCountrys" %>
<%@ page import="info.pascalkrause.costanalyzr.Category" %>
<div class="well">
	<g:form class="form-inline" name="addStore" action="addStore" method="post">
		<div class="row">
			<div class="col-md-3">
				<label for="storeConcernId">Zugehöriger Konzern (erforderlich)</label>
				<g:select id="storeConcernId" name="storeConcernId" from="${info.pascalkrause.costanalyzr.Concern.list()}" optionKey="id" value="" required="" class="form-control" />
			</div>
			<div class="col-md-4">
				<label for="storeStreetName">Straßenname (erforderlich)</label>
				<g:textField id="storeStreetName" name="storeStreetName" value="" placeholder="z.B. Heidenweg" required="" class="form-control" />
			</div>
			<div class="col-md-3">
				<label for="storeStreetNumber">Hausnummer (erforderlich)</label>
				<g:textField id="storeStreetNumber" name="storeStreetNumber" value="" placeholder="z.B. 23b" required="" class="form-control" />
			</div>
		</div>
		<div class="row">
			<div class="col-md-3">
				<label for="storeCountry">Land (erforderlich)</label>
				<g:select id="storeCountry" name="storeCountry" required="" from="${AvailableCountrys.values()}" keys="${AvailableCountrys.values()*.name()}" value="" class="form-control" />
			</div>
			<div class="col-md-4">
				<label for="storeCity">Stadt (erforderlich)</label>
				<g:textField id="storeCity" class="form-control" name="storeCity" required="" value="" placeholder='z.B. Berlin' />
			</div>
			<div class="col-md-3">
				<label for="storeZipcode">Postleitzahl (erforderlich)</label>
				<g:textField id="storeZipcode" class="form-control" name="storeZipcode" value="" required="" placeholder='z.B. 98465' />
			</div>
		</div>
		<div style="height: 15px"></div>
		<g:submitButton name="Hinzufügen" class="btn btn-info" value="Hinzufügen" />
	</g:form>
</div>