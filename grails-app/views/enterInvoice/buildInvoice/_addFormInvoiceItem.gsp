<%@ page import="info.pascalkrause.costanalyzr.Article" %>
<%@ page import="info.pascalkrause.costanalyzr.InvoiceItem" %>
<div class="well">
<g:set var="firstArticle" value="${Article.get(1)}" />
		<div class="row">
			<div class="col-md-6">
				<h3 class="webflow-heading">Rechnungsposten hinzufügen</h3>
			</div>
		</div>
    <div class="row">
      <div class="col-md-6">
        <label for="invoiceItemArticleId">Artikel (erforderlich) <a style="margin-left:5px" href="<g:createLink action="list" controller="article"/>" target="_blank">neuen Artikel anlegen</a></label>
        <g:hiddenField id="invoiceItemArticleId" name="invoiceItemArticleId" value="1"/>
        <g:typeaheadArticleSelect id="invoiceItemArticleSelect" class="form-control" required="" value="" name="invoiceItemArticleSelect" type="text" placeholder="Artikel auswählen (durch tippen)"/>
        <script>
        $(function() {  
            $("#invoiceItemArticleSelect").on("typeahead:selected typeahead:autocompleted", function(e,datum) {
              $("#invoiceItemArticleId").val(datum.id);
              $("#invoiceItemArticleGrossPrice").val(datum.standardGrossPrice);
              $("#invoiceItemArticleTaxRate").val(datum.standardTaxRate);
              $("#invoiceItemComment").val(datum.description);
              });
          })
        </script>
      </div>
      <div class="col-md-6">
        <label for="invoiceItemComment">Kommentar (optional)</label>
        <g:textField id="invoiceItemComment" class="form-control" name="invoiceItemComment" value="${firstArticle?.getDescription()}" placeholder='Standardwert: "n/a"' />
      </div>
    </div>
    <div class="row">
      <div class="col-md-3">
        <label for="invoiceItemCount">Anzahl (optional)</label>
        <g:set var="invoiceItemCountPatern" value="[-]?[0-9]{1,16}([.][0-9]{1,2})?" scope="page" />
        <g:textField id="invoiceItemCount" pattern="${invoiceItemCountPatern}" oninvalid="setCustomValidity('${invoiceItemCountPatern}')"
          onchange="try{setCustomValidity('')}catch(e){}" class="form-control" name="invoiceItemCount" value="" placeholder='Standardwert: 1.00' />
      </div>
      <div class="col-md-3">
        <label for="invoiceItemArticleTaxRate">MwST (optional)</label>
        <g:set var="articleTaxRatePattern" value="[0-9]{1,2}([.][0-9]{1,2})?" scope="page" />
        <g:textField id="invoiceItemArticleTaxRate" pattern="${articleTaxRatePattern}" oninvalid="setCustomValidity('${articleTaxRatePattern}')"
          onchange="try{setCustomValidity('')}catch(e){}" class="form-control" name="invoiceItemArticleTaxRate" value="${firstArticle?.getStandardTaxRate()}" placeholder='Standardwert: 19.00' />
      </div>
      <div class="col-md-3">
        <label for="invoiceItemArticleGrossPrice">Bruttopreis (erforderlich)</label>
        <g:set var="articleGrossPricePattern" value="[-]?[0-9]{1,16}([.][0-9]{1,2})?" scope="page" />
        <g:textField id="invoiceItemArticleGrossPrice" pattern="${articleGrossPricePattern}" oninvalid="setCustomValidity('${articleGrossPricePattern}')"
          onchange="try{setCustomValidity('')}catch(e){}" class="form-control" required="" name="invoiceItemArticleGrossPrice" value="${firstArticle?.getStandardGrossPrice()}" />
      </div>
      <div class="col-md-2">
        <div class="row">
          <label for="invoiceItemReduced">Reduziert (optional)</label>
        </div>
        <div class="row">
          <div id="invoiceItemReduced" class="btn-group" data-toggle="buttons">
            <label for="invoiceItemReduced" class="btn btn-default">
              <g:checkBox id="invoiceItemReduced" name="invoiceItemReduced"/> Reduziert
            </label>
          </div>
        </div>
      </div>
    </div>
    <div style="height: 15px"></div>
    <g:submitButton name="addInvoiceItem" class="btn btn-info" value="Hinzufügen" />
</div>