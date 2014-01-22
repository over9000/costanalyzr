<%@ page import="info.pascalkrause.costanalyzr.Category" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="costanalyzr" />
<title>Übersicht: Kategorien</title>
<link rel="stylesheet" href="${resource(dir: 'css/typeahead', file: 'typeahead.css')}" type="text/css">
<link rel="stylesheet" href="${resource(dir: 'css/typeahead', file: 'category.css')}" type="text/css">
</head>
<body>
  <g:render template="/template/mainNavBar" model="[active: 'dataInput']"/>
  <g:render template="/template/secondNavBarDataInput" model="[active: 'category']"/>
  <div style="height:15px"></div>
	<div class="container">
	  <g:if test="${!flash.isEmpty()}">
	    <g:render template="/template/responseMessage" model="[responseMessage: flash]"/>
	  </g:if>
    <h2>Übersicht: Kategorien</h2>
    <div style="height:15px"></div>
      <g:render template="/category/addForm"/>
    <g:jqueryTableSorter class="table table-hover table-condensed">
			<thead>
				<tr>
					<th><a style="text-decoration: none; cursor: default"># <span class="glyphicon"></span></a></th>
					<th><a style="text-decoration: none; cursor: default">Name <span class="glyphicon"></span></a></th>
					<th><a style="text-decoration: none; cursor: default">Übergeordnete Kategorie <span class="glyphicon"></span></a></th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${categoryInstanceList}" status="i" var="categoryInstance">
					<tr>
					  <g:if test="${categoryToEdit == categoryInstance.getId()}">
					    <g:form name="updateOrDeleteListItem" method="post">
					      <g:hiddenField name="categoryId" value="${categoryInstance.id}" />
					      <td><a id="listId${categoryInstance.id}" style="text-decoration: none; cursor: default"/> ${categoryInstance.id}</td>
                <td><g:textField class="form-control" name="newCategoryName" value="${categoryInstance.getName()}" placeholder="${categoryInstance.getName()}" required=""/></td>
                <td>
                  <g:hiddenField id="parentCategoryIdEdit" name="parentCategoryId" value="${categoryInstance?.getParentCategory()?.getId()}"/>
                  <g:typeaheadCategorySelect id="parentCategorySelectEdit" class="form-control" value="${categoryInstance?.getParentCategory()?.toString()}"
                    noSelectionAvailable="" name="parentCategorySelect" type="text" placeholder="${categoryInstance?.getParentCategory()?.toString()}"/>
                  <g:javascript>
                    $(function() {
                      $("#parentCategorySelectEdit").on("typeahead:selected typeahead:autocompleted", function(e,datum) {
                        $("#parentCategoryIdEdit").val(datum.id);
                      })  
                    })
                  </g:javascript>
                </td>
                <td>
                  <g:actionSubmit value="Speichern" class="btn btn-info" action="saveCategory" />
                  <g:actionSubmit value="Löschen" class="btn btn-warning" action="deleteCategory" />
                </td>
              </g:form>
              <g:javascript>window.location.href = "#listId${categoryToEdit}";</g:javascript>
					  </g:if>
					  <g:else>
					    <td>${categoryInstance.id}</td>
              <td>${categoryInstance.name}</td>
              <td>${categoryInstance?.parentCategory?.toString()}</td>
              <td><g:form name="updateOrDeleteListItem" method="post">
                <g:hiddenField name="categoryId" value="${categoryInstance.id}" />
                <g:actionSubmit value="Ändern" class="btn btn-info" action="updateCategory" />
                <g:actionSubmit value="Löschen" class="btn btn-warning" action="deleteCategory" />
              </g:form></td>
					  </g:else>
					</tr>
				</g:each>
			</tbody>
		</g:jqueryTableSorter>
	</div>
</body>
</html>