<%@ page import="info.pascalkrause.costanalyzr.Article" %>
<%@ page import="info.pascalkrause.costanalyzr.Category" %>
<div class="well">
	<g:form class="form-inline" name="addArticle" action="addArticle" method="post">
		<div class="row">
			<div class="col-md-4">
				<label for="articleName">Name des neuen Artikels (erforderlich)</label>
				<g:textField id="articleName" class="form-control" name="articleName" value="" placeholder="Name der neuen Kategorie" required="" />
			</div>
			<div class="col-md-6">
			  <label for="articleCategorySelect">Die Kategorie des Artikels (erforderlich) <a style="margin-left:5px" href="<g:createLink action="list" controller="category"/>" target="_blank">neue Kategorie anlegen</a> </label>
        <g:hiddenField id="articleCategoryId" name="articleCategoryId" value="null"/>
        <g:typeaheadCategorySelect id="articleCategorySelect" class="form-control" value="" name="articleCategorySelect" required="" placeholder="Kategorie auswählen (durch tippen)"/>
        <g:javascript>
          $(function() {
            $("#articleCategorySelect").on("typeahead:selected typeahead:autocompleted", function(e,datum) {
              $("#articleCategoryId").val(datum.id).change();
            })  
          })
        </g:javascript>
			</div>
		</div>
		<div class="row">
			<div class="col-md-4">
				<label for="articleDescription">Beschreibung (optional)</label>
				<g:textField id="articleDescription" class="form-control" name="articleDescription" value="" placeholder='Standardwert: "n/a"' />
			</div>
			<div class="col-md-3">
				<label for="articleStandardTaxRate">Standard MwST (optional)</label>
				<g:set var="articleStandardTaxRatePattern" value="[0-9]{1,2}([.][0-9]{1,2})?" scope="page" />
				<g:textField id="articleStandardTaxRate" pattern="[0-9]{1,2}([.][0-9]{1,2})?" oninvalid="setCustomValidity('${articleStandardTaxRatePattern}')"
				  onchange="try{setCustomValidity('')}catch(e){}" class="form-control" name="articleStandardTaxRate" value="" placeholder='Standardwert: 19.00' />
			</div>
			<div class="col-md-3">
				<label for="articleStandardGrossPrice">Standard Bruttopreis (optional)</label>
				<g:set var="articleStandardGrossPricePattern" value="[-]?[0-9]{1,16}([.][0-9]{1,2})?" scope="page" />
				<g:textField id="articleStandardGrossPrice" pattern="${articleStandardGrossPricePattern}" oninvalid="setCustomValidity('${articleStandardGrossPricePattern}')"
					onchange="try{setCustomValidity('')}catch(e){}" class="form-control" name="articleStandardGrossPrice" value="" placeholder='Standardwert: 0.00' />
			</div>
		</div>
		<div style="height: 15px"></div>
		<g:submitButton name="Hinzufügen" class="btn btn-info"
			value="Hinzufügen" />
	</g:form>
</div>