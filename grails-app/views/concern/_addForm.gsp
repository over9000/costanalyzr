<div class="well">
	<g:form class="form-inline" role="form" name="addConcern" action="addConcern" method="post">
		<div class="form-group">
			<label for="concernName">Name des neuen Konzerns (erforderlich)</label>
			<g:textField id="concernName" class="form-control" name="concernName" value="" placeholder="Name des neuen Konzerns" required="" />
		</div>
		<div style="height: 15px"></div>
		<g:submitButton name="add" class="btn btn-info" value="Hinzufügen" />
	</g:form>
</div>