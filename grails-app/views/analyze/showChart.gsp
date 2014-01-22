<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="costanalyzr" />
<link rel="stylesheet" href="${resource(dir: 'css/nvd3/v1.15', file: 'nv.d3.css')}" type="text/css">
<title>Auswertung vorbereiten</title>
</head>
<body>
  <g:render template="/template/mainNavBar" model="[active: 'dataOutput']" />
  <div style="height: 15px"></div>
  <div class="container">
    <g:if test="${!flash?.isEmpty()}">
      <g:render template="/template/responseMessage" model="[responseMessage: flash]"/>
    </g:if>
    <div class="well">
      <div class="row">
        <div class="col-md-8">
          <h3>Auswertung (URL)</h3>
        </div>
      </div>
      <div style="height: 15px"></div>
      <g:if test="${stackedAreaChart == true}">
        <div class="row">
          <div class="col-md-12">
            <g:if test="${showAllChildren == false}">                    
              <g:stackedAreaChart fromDate="${fromDate?.getTime()}" toDate="${toDate?.getTime()}" timePeriodType="${timePeriodType}"
                categories="${categories?.toString()}"></g:stackedAreaChart>
            </g:if>
            <g:else>
              <g:stackedAreaChart fromDate="${fromDate?.getTime()}" toDate="${toDate?.getTime()}" timePeriodType="${timePeriodType}"
                categories="${categories?.toString()}" showAllChildren=""></g:stackedAreaChart>
            </g:else>
          </div>
        </div>
      </g:if>
      <g:if test="${pieChart == true}">
        <div class="row">
          <div class="col-md-12">
            <g:if test="${showAllChildren == false}">                    
              <g:pieChart fromDate="${fromDate?.getTime()}" toDate="${toDate?.getTime()}" timePeriodType="${timePeriodType}"
                categories="${categories?.toString()}"></g:pieChart>
            </g:if>
            <g:else>
              <g:pieChart fromDate="${fromDate?.getTime()}" toDate="${toDate?.getTime()}" timePeriodType="${timePeriodType}"
                categories="${categories?.toString()}" showAllChildren=""></g:pieChart>
            </g:else>
          </div>
        </div>
      </g:if>
      <div style="height: 15px"></div>
      <div class="row">
        <g:form  role="form" action="prepareEvaluation" method="post">
          <div class="col-md-2">
            <div class="btn-group">
              <g:submitButton name="back" class="btn btn-info" value="Zurück" />
              <g:submitButton name="done" class="btn btn-info" value="Fertig" />
            </div>
          </div>
          <div class="col-md-3">
            <div class="btn-group">
              <g:submitButton name="newEvaluation" class="btn btn-info" value="Eine weitere Auswertung zusammenstellen" />
            </div>
          </div>
        </g:form>
      </div>
    </div>
  </div>
</body>
</html>