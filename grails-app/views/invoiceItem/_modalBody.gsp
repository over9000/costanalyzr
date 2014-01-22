<%@ page import="info.pascalkrause.costanalyzr.InvoiceItem" %>
<%@ page import="info.pascalkrause.costanalyzr.Article" %>
<%@ page import="info.pascalkrause.costanalyzr.Utils" %>
<g:if test="${!flash.isEmpty()}">
	<g:render template="/template/responseMessage" model="[responseMessage: flash]" />
</g:if>
<g:render template="/invoiceItem/addForm" model="[invoiceInstance: invoiceInstance]"/>
<g:jqueryTableSorter class="table table-hover table-condensed">
  <thead>
    <tr>
      <th><a style="text-decoration: none; cursor: default; width: 5%">Artikel <span class="glyphicon"></span></a></th>
      <th><a style="text-decoration: none; cursor: default; width: 10%">Anzahl <span class="glyphicon"></span></a></th>
      <th><a style="text-decoration: none; cursor: default; width: 25%">Mwst <span class="glyphicon"></span></a></th>
      <th><a style="text-decoration: none; cursor: default; width: 25%">Kommentar <span class="glyphicon"></span></a></th>
      <th><a style="text-decoration: none; cursor: default; width: 25%">Reduziert <span class="glyphicon"></span></a></th>
      <th><a style="text-decoration: none; cursor: default; width: 25%">Bruttopreis <span class="glyphicon"></span></a></th>
    </tr>
  </thead>
  <tbody>
		<g:each in="${invoiceInstance?.invoiceItems?}" status="i" var="invoiceItemInstance">
			<tr>
			  <g:if test="${invoiceItemToEdit == invoiceItemInstance?.getId()}">
			    <g:set var="invoiceItemCountPatern" value="[-]?[0-9]{1,16}([.][0-9]{1,2})?" scope="page" />
          <g:set var="articleTaxRatePattern" value="[0-9]{1,2}([.][0-9]{1,2})?" scope="page" />
          <g:set var="articleGrossPricePattern" value="[-]?[0-9]{1,16}([.][0-9]{1,2})?" scope="page" />
			    <g:hiddenField id="invoiceItemArticleIdEdit" name="invoiceItemArticleId" value="${invoiceItemInstance?.getArticle()?.getId()}"/>
			    <td><g:typeaheadArticleSelect id="invoiceItemArticleSelectEdit" class="form-control" required="" value="${invoiceItemInstance?.getArticle()?.toString()}" name="invoiceItemArticleSelectEdit" type="text" placeholder="${invoiceItemInstance?.getArticle()?.getName()}"/></td>
			    <script>
			    $(function() { 
			    	  $("#invoiceItemArticleSelectEdit").on("typeahead:selected typeahead:autocompleted", function(e,datum) {
			    	    $("#invoiceItemArticleIdEdit").val(datum.id);
			    	    });
			    	})
			    </script>
			    <td><g:textField style="width:100px" id="invoiceItemCountEdit" pattern="${invoiceItemCountPatern}" oninvalid="setCustomValidity('${invoiceItemCountPatern}')" onchange="try{setCustomValidity('')}catch(e){}" class="form-control" name="invoiceItemCount" value="${invoiceItemInstance?.getCount()}" placeholder='Standardwert: 1.00' /></td>
			    <td><g:textField style="width:100px" id="invoiceItemArticleTaxRateEdit" pattern="${articleTaxRatePattern}" oninvalid="setCustomValidity('${articleTaxRatePattern}')" onchange="try{setCustomValidity('')}catch(e){}" class="form-control" name="invoiceItemArticleTaxRate" value="${invoiceItemInstance?.getTaxRate()}" placeholder='Standardwert: 19.00' /></td>
			    <td><g:textField id="invoiceItemCommentEdit" class="form-control" name="invoiceItemComment" value="${invoiceItemInstance?.getComment()}" placeholder='Standardwert: "n/a"' /></td>
			    <td><g:checkBox style="width:100px" id="invoiceItemReducedEdit" name="invoiceItemReduced" checked="${invoiceItemInstance?.getReduced()}"/></td>
	        <td><g:textField style="width:150px" id="invoiceItemArticleGrossPriceEdit" pattern="${articleGrossPricePattern}" oninvalid="setCustomValidity('${articleGrossPricePattern}')" onchange="try{setCustomValidity('')}catch(e){}" class="form-control" required="" name="invoiceItemArticleGrossPrice" value="${invoiceItemInstance?.getGrossPrice()}" /></td>
			    <td>
			      <input class="btn btn-info" type="button" value="Speichern" onclick="jQuery.ajax({type:'POST',data:
			        { invoiceItemId: ${invoiceItemInstance?.getId()},
			          invoiceItemCount : $('#invoiceItemCountEdit').val(),
			          invoiceItemArticleId : $('#invoiceItemArticleIdEdit').val(),
			          invoiceItemArticleTaxRate : $('#invoiceItemArticleTaxRateEdit').val(),
			          invoiceItemComment : $('#invoiceItemCommentEdit').val(),
			          invoiceItemArticleGrossPrice : $('#invoiceItemArticleGrossPriceEdit').val(),
			          invoiceItemReduced : $('#invoiceItemReducedEdit').is(':checked')
			        }, url:'/\${Utils.getProjectName()}/invoiceItem/saveInvoiceItem',success:function(data,textStatus){jQuery('#invoiceItemModalBody').html(data);},error:function(XMLHttpRequest,textStatus,errorThrown){}});return false">
            <input class="btn btn-warning" type="button" value="Löschen" onclick="jQuery.ajax({type:'POST',data:
              { invoiceItemId: ${invoiceItemInstance?.getId()}
              }, url:'/${Utils.getProjectName()}/invoiceItem/deleteInvoiceItem',success:function(data,textStatus){jQuery('#invoiceItemModalBody').html(data);},error:function(XMLHttpRequest,textStatus,errorThrown){}});return false">
          </td>
          <g:javascript>window.location.href = "#listId${invoiceItemToEdit}";</g:javascript>
			  </g:if>
			  <g:else>
			    <td>${invoiceItemInstance?.getArticle()?.toString()}</td>
					<td>${invoiceItemInstance?.getCount()}</td>
					<td>${invoiceItemInstance?.getTaxRate()}</td>
					<td>${invoiceItemInstance?.getComment()}</td>
					<td style="width:100px"><g:checkBox  name="myCheckbox" disabled="disabled" value="${invoiceItemInstance?.getReduced()}" /></td>
					<td>${invoiceItemInstance?.getGrossPrice()}</td>
					<td>
					  <g:form name="updateOrDeleteListItem" method="post">
						  <g:hiddenField name="invoiceItemId" value="${invoiceItemInstance.id}" />
							<g:submitToRemote value="Ändern" update="invoiceItemModalBody" url="[action: 'updateInvoiceItem', controller: 'invoiceItem']" class="btn btn-info" />
							<g:submitToRemote value="Löschen" update="invoiceItemModalBody" url="[action: 'deleteInvoiceItem', controller: 'invoiceItem']" class="btn btn-warning" />
						</g:form>
				  </td>
			  </g:else>
			</tr>
		</g:each>
	</tbody>
</g:jqueryTableSorter>