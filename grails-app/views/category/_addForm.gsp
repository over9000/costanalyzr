<%@ page import="info.pascalkrause.costanalyzr.Category" %>
<div class="well">
	<g:form class="form-inline" role="form" name="addCategory" action="addCategory" method="post">
		<div class="form-group">
			<label for="categoryName">Name der neuen Kategorie (erforderlich)</label>
			<g:textField id="categoryName" class="form-control" name="categoryName" value="" placeholder="Name der neuen Kategorie" required="" />
		</div>
		<div class="form-group">
      <label for="parentCategorySelect">Die übergeordnete Kategorie (optional)</label>
      <g:hiddenField id="parentCategoryId" name="parentCategoryId" value="null"/>
      <g:typeaheadCategorySelect id="parentCategorySelect" class="form-control" value="" noSelectionAvailable="" name="parentCategorySelect" type="text" placeholder="Kategorie auswählen (durch tippen)"/>
      <g:javascript>
        $(function() {
          $("#parentCategorySelect").on("typeahead:selected typeahead:autocompleted", function(e,datum) {
            $("#parentCategoryId").val(datum.id);
          })  
        })
      </g:javascript>
    </div>
		<div style="height: 15px"></div>
		<g:submitButton name="Hinzufügen" class="btn btn-info" value="Hinzufügen" />
	</g:form>
</div>