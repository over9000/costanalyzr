<%@ page import="info.pascalkrause.costanalyzr.Concern" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="costanalyzr" />
<title>Neue Rechnung eintragen</title>
</head>
<body>
	<g:render template="/template/mainNavBar" model="[active: 'dataInput']" />
	<g:render template="/template/secondNavBarDataInput" model="[active: 'none']" />
	<div style="height: 15px"></div>
	<div class="container">
    <g:if test="${!flash?.isEmpty()}">
      <g:render template="/template/responseMessage" model="[responseMessage: flash]"/>
    </g:if>
    <div style="height: 15px"></div>
      <g:render template="/enterInvoice/buildInvoice/webflowNavbar" model="[webflowNavbarCurrentState: 1]"/>
    <div style="height: 15px"></div>
		<div class="well">
			<g:form  role="form" name="chooseOrAddConcern" action="buildInvoice" method="post">
			  <div class="row">
			    <div class="col-md-6">
			      <h3>Wähle einen Konzern aus,</h3>
			    </div>
			  </div>
			  <div class="row">
			    <div class="col-md-6">
		        <g:select id="buildInvoiceFlowConcernId" name="buildInvoiceFlowConcernId" from="${info.pascalkrause.costanalyzr.Concern.list()}" optionKey="id" value="${flow?.choosenConcern?.id}" noSelection="['noSelection':'-Neuen Konzern anlegen-']" class="form-control buildInvoiceFlowAddFormFieldsetDisabler" />
		        <g:javascript src="buildInvoiceFlow/costanalyzrBuildInvoiceFlowToggleAddFormFieldset.js"/>
	        </div>
	      </div>
        <div class="row">
          <div class="col-md-6">
           <h3>...oder lege einen neuen an</h3>
          </div>
        </div>
        <fieldset class="buildInvoiceFlowAddFormFieldset" <g:if test="${flow?.choosenConcern != null}"> disabled </g:if>>
        <div class="row">
          <div class="col-md-6">
            <!-- <label for="articleCategoryId">Konzernname (erforerlich)</label> -->
            <input type="text"  class="form-control" id="buildInvoiceFlowNewConcernName" name="buildInvoiceFlowNewConcernName" required placeholder="Neuer Konzernname">
          </div>
        </div>
        </fieldset>
        <div style="height: 15px"></div>
        <div class="row">
          <div class="col-md-3">
            <div class="btn-group">
              <g:submitButton name="cancel" class="btn btn-info" value="Abbrechen" formnovalidate="" />
              <g:submitButton formnovalidate disabled name="back" class="btn btn-info" value="Zurück" />
              <g:submitButton name="next" class="btn btn-info" value="Weiter" />
            </div>
          </div>
        </div>
		  </g:form>
    </div>
	</div>
</body>
</html>