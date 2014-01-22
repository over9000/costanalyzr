<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="costanalyzr" />
<title>Übersicht: Konzerne</title>
</head>
<body>
  <g:render template="/template/mainNavBar" model="[active: 'dataInput']"/>
  <g:render template="/template/secondNavBarDataInput" model="[active: 'concern']"/>
  <div style="height:15px"></div>
	<div class="container">
	  <g:if test="${!flash.isEmpty()}">
	    <g:render template="/template/responseMessage" model="[responseMessage: flash]"/>
	  </g:if>
    <h2>Übersicht: Konzerne</h2>
    <div style="height:15px"></div>
      <g:render template="/concern/addForm"/>
    <g:jqueryTableSorter class="table table-hover table-condensed">
			<thead>
				<tr>
					<th><a style="text-decoration: none; cursor: default"># <span class="glyphicon"></span></a></th>
					<th><a style="text-decoration: none; cursor: default">Name <span class="glyphicon"></span>
					</a></th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${concernInstanceList}" status="i" var="concernInstance">
					<tr>
					  <g:if test="${concernToEdit == concernInstance.getId()}">
					    <g:form name="updateOrDeleteListItem" method="post">
					      <g:hiddenField name="concernId" value="${concernInstance.id}" />
					      <td><a id="listId${concernInstance.id}" style="text-decoration: none; cursor: default"/> ${concernInstance.id}</td>
                <td><g:textField class="form-control" name="newConcernName" value="${concernInstance.getName()}" placeholder="${concernInstance.getName()}" required=""/></td>
                <td>
                  <g:actionSubmit value="Speichern" class="btn btn-info" action="saveConcern" />
                  <g:actionSubmit value="Löschen" class="btn btn-warning" action="deleteConcern" />
                </td>
              </g:form>
              <g:javascript>window.location.href = "#listId${concernToEdit}";</g:javascript>
					  </g:if>
					  <g:else>
					    <td>${concernInstance.id}</td>
              <td>${concernInstance.name}</td>
              <td><g:form name="updateOrDeleteListItem" method="post">
                <g:hiddenField name="concernId" value="${concernInstance.id}" />
                <g:actionSubmit value="Ändern" class="btn btn-info" action="updateConcern" />
                <g:actionSubmit value="Löschen" class="btn btn-warning" action="deleteConcern" />
              </g:form></td>
					  </g:else>
					</tr>
				</g:each>
			</tbody>
		</g:jqueryTableSorter>
	</div>
</body>
</html>