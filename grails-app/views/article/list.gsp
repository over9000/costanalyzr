<%@ page import="info.pascalkrause.costanalyzr.Article" %>
<%@ page import="info.pascalkrause.costanalyzr.Category" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="costanalyzr" />
<title>Übersicht: Artikel</title>
<link rel="stylesheet" href="${resource(dir: 'css/typeahead', file: 'typeahead.css')}" type="text/css">
<link rel="stylesheet" href="${resource(dir: 'css/typeahead', file: 'category.css')}" type="text/css">
</head>
<body>
  <g:render template="/template/mainNavBar" model="[active: 'dataInput']"/>
  <g:render template="/template/secondNavBarDataInput" model="[active: 'article']"/>
  <div style="height:15px"></div>
	<div class="container">
	  <g:if test="${!flash.isEmpty()}">
	    <g:render template="/template/responseMessage" model="[responseMessage: flash]"/>
	  </g:if>
    <h2>Übersicht: Artikel</h2>
    <div style="height:15px"></div>
      <g:render template="/article/addForm"/>
    <g:jqueryTableSorter class="table table-hover table-condensed">
			<thead>
				<tr class="row">
					<th class="col-md-1"><a style="text-decoration: none; cursor: default; width: 5%">#<span class="glyphicon"></span></a></th>
					<th class="col-md-2"><a style="text-decoration: none; cursor: default; width: 20%">Name<span class="glyphicon"></span></a></th>
					<th class="col-md-3"><a style="text-decoration: none; cursor: default; width: 20%">Kategorie<span class="glyphicon"></span></a></th>
					<th class="col-md-2"><a style="text-decoration: none; cursor: default; width: 20%">Beschreibung<span class="glyphicon"></span></a></th>
					<th class="col-md-1"><a style="text-decoration: none; cursor: default; width: 10%">MwST<span class="glyphicon"></span></a></th>
					<th class="col-md-1"><a style="text-decoration: none; cursor: default; width: 10%">Euro<span class="glyphicon"></span></a></th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${articleInstanceList}" status="i" var="articleInstance">
					<tr class="row">
					  <g:if test="${articleToEdit == articleInstance.getId()}">
					    <g:form name="updateOrDeleteListItem" method="post">
					      <g:hiddenField name="articleId" value="${articleInstance.id}" />
					      <td><a id="listId${articleInstance.id}" style="text-decoration: none; cursor: default"/> ${articleInstance.id}</td>
                <td><g:textField class="form-control" name="articleName" value="${articleInstance.getName()}" placeholder="${articleInstance.getName()}" required=""/></td>
                <td>
                  <g:hiddenField id="articleCategoryIdEdit" name="articleCategoryId" value="${articleInstance?.getCategory()?.getId()}"/>
                  <g:typeaheadCategorySelect id="articleCategorySelectEdit" class="form-control" value="${articleInstance?.getCategory()?.toString()}" name="articleCategorySelectEdit" required="" placeholder="${articleInstance?.getCategory()?.toString()}"/>
                  <g:javascript>
                    $(function() {
                      $("#articleCategorySelectEdit").on("typeahead:selected typeahead:autocompleted", function(e,datum) {
                        $("#articleCategoryIdEdit").val(datum.id).change();
                      })  
                    })
                  </g:javascript>
                </td>
                <td><g:textField id="articleDescription" class="form-control" name="articleDescription" value="${articleInstance?.getDescription()}" placeholder='Standardwert: "n/a"'/></td>
                <td>
                  <g:textField style="" id="articleStandardTaxRate" pattern="[0-9]{1,2}([.][0-9]{1,2})?"
                    oninvalid="setCustomValidity('${articleStandardTaxRatePattern}')" onchange="try{setCustomValidity('')}catch(e){}"
                    class="form-control" name="articleStandardTaxRate" value="${articleInstance?.getStandardTaxRate()}" placeholder='Standardwert: 19.00'/>
                </td>
                <td>
                  <g:textField style="" id="articleStandardGrossPrice" pattern="${articleStandardGrossPricePattern}"
                    oninvalid="setCustomValidity('${articleStandardGrossPricePattern}')" onchange="try{setCustomValidity('')}catch(e){}"
                    class="form-control" name="articleStandardGrossPrice" value="${articleInstance?.getStandardGrossPrice()}" placeholder='Standardwert: 0.00'/>
                </td>
                
                <td class="col-md-2">
                  <g:actionSubmit value="Speichern" class="btn btn-info" action="saveArticle" />
                  <g:actionSubmit value="Löschen" class="btn btn-warning" action="deleteArticle" />
                </td>
              </g:form>
              <g:javascript>window.location.href = "#listId${articleToEdit}";</g:javascript>
					  </g:if>
					  <g:else>
					    <td>${articleInstance.id}</td>
              <td>${articleInstance.name}</td>
              <td>${articleInstance.category}</td>
              <td>${articleInstance.description}</td>
              <td>${articleInstance.standardTaxRate}</td>
              <td>${articleInstance.standardGrossPrice}</td>
              <td class="col-md-2"><g:form name="updateOrDeleteListItem" method="post">
                <g:hiddenField name="articleId" value="${articleInstance.id}" />
                <g:actionSubmit value="Ändern" class="btn btn-info" action="updateArticle" />
                <g:actionSubmit value="Löschen" class="btn btn-warning" action="deleteArticle" />
              </g:form></td>
					  </g:else>
					</tr>
				</g:each>
			</tbody>
		</g:jqueryTableSorter>
	</div>
</body>
</html>