<%@ page import="info.pascalkrause.costanalyzr.AvailableCountrys" %>
<%@ page import="info.pascalkrause.costanalyzr.Category" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="costanalyzr" />
<title>Übersicht: Filialen</title>
</head>
<body>
  <g:render template="/template/mainNavBar" model="[active: 'dataInput']"/>
  <g:render template="/template/secondNavBarDataInput" model="[active: 'store']"/>
  <div style="height:15px"></div>
	<div class="container">
	  <g:if test="${!flash.isEmpty()}">
	    <g:render template="/template/responseMessage" model="[responseMessage: flash]"/>
	  </g:if>
    <h2>Übersicht: Filialen</h2>
    <div style="height:15px"></div>
      <g:render template="/store/addForm"/>
    <g:jqueryTableSorter class="table table-hover table-condensed">
			<thead>
				<tr>
					<th><a style="text-decoration: none; cursor: default; width: 5%"># <span class="glyphicon"></span></a></th>
					<th><a style="text-decoration: none; cursor: default; width: 20%">Konzern <span class="glyphicon"></span></a></th>
					<th><a style="text-decoration: none; cursor: default; width: 50%">Straße / Hausnummer <span class="glyphicon"></span></a></th>
					<th><a style="text-decoration: none; cursor: default; width: 50%">Land / Stadt / Postleitzahl <span class="glyphicon"></span></a></th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${storeInstanceList}" status="i" var="storeInstance">
					<tr>
					  <g:if test="${storeToEdit == storeInstance.getId()}">
					    <g:form name="updateOrDeleteListItem" method="post">
					      <g:hiddenField name="storeId" value="${storeInstance.id}" />
					      <td><a id="listId${storeInstance.id}" style="text-decoration: none; cursor: default"/> ${storeInstance.id}</td>
					      <td>
					       <label for="storeConcernId">Zugehöriger Konzern (erforderlich)</label>
					       <g:select id="storeConcernId" name="storeConcernId" from="${info.pascalkrause.costanalyzr.Concern.list()}" optionKey="id" value="${storeInstance.concernId}" class="form-control" />
					      </td>
                <td>
                  <label for="storeStreetName">Straßenname (erforderlich)</label>
                  <g:textField id="storeStreetName" name="storeStreetName" value="${storeInstance.streetName}" placeholder="${storeInstance.streetName}" required="" class="form-control" />
                  <label for="storeStreetNumber">Hausnummer (erforderlich)</label>
                  <g:textField id="storeStreetNumber" name="storeStreetNumber" value="${storeInstance.streetNumber}" placeholder="${storeInstance.streetNumber}" required="" class="form-control" />
                </td>
                <td>
                  <label for="storeCountry">Land (erforderlich)</label>
                  <g:select id="storeCountry" name="storeCountry" from="${AvailableCountrys.values()}" keys="${AvailableCountrys.values()*.name()}" value="${storeInstance.country}" class="form-control" />
                  <label for="storeCity">Stadt (erforderlich)</label>
                  <g:textField id="storeCity" name="storeCity" value="${storeInstance.city}" placeholder="${storeInstance.city}" required="" class="form-control" />
                  <label for="storeZipcode">Postleitzahl (erforderlich)</label>
                  <g:textField id="storeZipcode" name="storeZipcode" value="${storeInstance.zipcode}" placeholder="${storeInstance.zipcode}" required="" class="form-control" />
                </td> 
                <td>
                  <g:actionSubmit value="Speichern" class="btn btn-info" action="saveStore" />
                  <g:actionSubmit value="Löschen" class="btn btn-warning" action="deleteStore" />
                </td>
              </g:form>
              <g:javascript>window.location.href = "#listId${storeToEdit}";</g:javascript>
					  </g:if>
					  <g:else>
					    <td>${storeInstance.id}</td>
              <td>${storeInstance.concern.getName()}</td>
              <td>${storeInstance.streetName}  ${storeInstance.streetNumber}</td>
              <td>${storeInstance.country}  ${storeInstance.city}  ${storeInstance.zipcode}</td>
              <td><g:form name="updateOrDeleteListItem" method="post">
                <g:hiddenField name="storeId" value="${storeInstance.id}" />
                <g:actionSubmit value="Ändern" class="btn btn-info" action="updateStore" />
                <g:actionSubmit value="Löschen" class="btn btn-warning" action="deleteStore" />
              </g:form></td>
					  </g:else>
					</tr>
				</g:each>
			</tbody>
		</g:jqueryTableSorter>
	</div>
</body>
</html>