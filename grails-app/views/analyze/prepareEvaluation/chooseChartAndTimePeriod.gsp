<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="costanalyzr" />
<link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery-ui-1.10.3.css')}" type="text/css">
<title>Auswertung vorbereiten</title>
</head>
<body>
	<g:render template="/template/mainNavBar" model="[active: 'dataOutput']" />
	<div style="height: 15px"></div>
	<div class="container">
    <g:if test="${!flash?.isEmpty()}">
      <g:render template="/template/responseMessage" model="[responseMessage: flash]"/>
    </g:if>
    <div style="height: 15px"></div>
      <g:render template="/analyze/prepareEvaluation/webflowNavbar" model="[webflowNavbarCurrentState: 1]"/>
    <div style="height: 15px"></div>
		<div class="well">
			<g:form  role="form" action="prepareEvaluation" method="post">
			  <div class="row">
			    <div class="col-md-6">
			      <h3>Wähle einen Zeitraum aus <small>(Eine Wahlmöglichkeit)</small></h3>
					</div>
			  </div>
			  <g:javascript src="prepareEvaluationFlow/disableRadioButtons.js" />
			  <div class="row">
            <div class="col-md-3">
              <label style="vertical-align: middle;font-size:16px">Ein bestimmtes Jahr</label>
              <div class="input-group"><span class="input-group-addon"><input id="timeRadiosYear" name="timeRadios" required="" type="radio" value="year"></span>
                <fieldset id="fieldsetTimeYear">
                  <g:set var="regExpYear" value="^\\d{4}\$" scope="page" />
                  <g:jqueryUIDatePicker id="timeYear" name="timeYear" class="form-control" required="" placeholder="yyyy" pattern="${regExpYear}"
                    oninvalid="setCustomValidity('${regExpYear}')" onchange="try{setCustomValidity('')}catch(e){}"/>
                  <script>
                    $(document).ready(function() {
                    	$( "#timeYear" ).datepicker( "option", "dateFormat", "yy" );
                    	$('#timeYear').on('click', function(e) {
                    		  var yearRadioButton = $('#timeRadiosYear');
                          yearRadioButton.prop("checked", true).change();
                      });
                    });
                  </script>
                </fieldset>
              </div>
            </div>
            <div class="col-md-3">
              <label style="vertical-align: middle;font-size:16px">Ein bestimmter Monat</label>
              <div class="input-group"><span class="input-group-addon"><input id="timeRadiosMonth" name="timeRadios" type="radio" value="month"></span>
                <fieldset id="fieldsetTimeMonth">
                  <g:set var="regExpMonth" value="^\\d{4}-(0[1-9]|1[0-2])\$" scope="page" />
                  <g:jqueryUIDatePicker id="timeMonth" name="timeMonth" class="form-control" required="" placeholder="yyyy-MM" pattern="${regExpMonth}"
                    oninvalid="setCustomValidity('${regExpMonth}')" onchange="try{setCustomValidity('')}catch(e){}"/>
                  <script>
                    $(document).ready(function() {
                  	  $( "#timeMonth" ).datepicker( "option", "dateFormat", "yy-mm" );
                  	  $('#timeMonth').on('click', function(e) {
                  		  var yearRadioButton = $('#timeRadiosMonth');
                        yearRadioButton.prop("checked", true).change();
                      });
                  	});
                  </script>
                </fieldset>
              </div>
            </div>
            <div class="col-md-6">
              <label style="vertical-align: middle;font-size:16px">Ein bestimmter Zeitraum</label>
              <div class="input-group"><span class="input-group-addon"><input id="timeRadiosCustom" name="timeRadios" type="radio" value="custom"></span>
                <fieldset id="fieldsetTimeCustom">
                  <g:set var="regExpCustom" value="^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\$" scope="page" />
                  <div style="width:100%" class="input-group"><span style="width:55px" class="input-group-addon">von:</span>
                    <g:jqueryUIDatePicker id="timeCustomFrom" name="timeCustomFrom" class="form-control" required="" placeholder="yyyy-MM-dd" pattern="${regExpCustom}"
                      oninvalid="setCustomValidity('${regExpCustom}')" onchange="try{setCustomValidity('')}catch(e){}"/>
                  </div>
                  <div style="width:100%" class="input-group"><span style="width:55px" class="input-group-addon">bis:</span>
                    <g:jqueryUIDatePicker id="timeCustomTo" name="timeCustomTo" class="form-control" required="" placeholder="yyyy-MM-dd" pattern="${regExpCustom}"
                      oninvalid="setCustomValidity('${regExpCustom}')" onchange="try{setCustomValidity('')}catch(e){}"
                      value="${new SimpleDateFormat('yyyy-MM-dd').format(new Date())}"/>
                  </div>
                </fieldset>
              </div>
            </div>
            <script>
              $(document).ready(function() {
                $('#timeCustomFrom, #timeCustomTo').on('click', function(e) {
                	  var yearRadioButton = $('#timeRadiosCustom');
                    yearRadioButton.prop("checked", true).change();
                });
              });
            </script>
        </div>
        <div class="row">
          <div class="col-md-6">
            <h3>Wähle Diagrammarten aus <small>(Mehrere Wahlmöglichkeiten)</small></h3>
          </div>
        </div>
        <div class="row">
          <div style="height:100%" class="col-md-6">
            <label style="vertical-align: middle;font-size:16px">StackedAreaChart</label>
              <div class="input-group">
                <span class="input-group-addon">
                  <input id="stackedAreaChartCheckbox" name="stackedAreaChartCheckbox" type="checkbox">
                </span>
                <div id="stackedAreaChartInputGroup" style="width:100%" class="input-group">
                  <span class="input-group-addon">
                    <g:img dir="images" file="stackedAreaChartExampleSmall.png" style="float:left" width="200px" height="100px" alt="Ok" class="img-rounded"/>
                  </span>
                  <span style="white-space: normal;width:100%" class="input-group-addon">
                    Mit dieser Diagrammart können besonders gut zeitliche Zusammenhänge erkannt werden.<br>
                    <strong>Mögliche Fragestellung:</strong> <em>Wann</em> wurde <em>wie viel</em> Geld für <em>was</em> ausgegeben.
                  </span>
                </div>
              </div>
          </div>
          <script>
            $(document).ready(function() {
            	$('#stackedAreaChartInputGroup').on('click', function(e) {
                var stackedAreaChartCheckbox = $('#stackedAreaChartCheckbox');
                stackedAreaChartCheckbox.prop("checked", !stackedAreaChartCheckbox.prop("checked"));
                });
        	  });
          </script>
          <div style="height:100%" class="col-md-6">
            <label style="vertical-align: middle;font-size:16px">PieChart</label>
              <div class="input-group">
                <span class="input-group-addon">
                  <input id="pieChartCheckbox" name="pieChartCheckbox" type="checkbox">
                </span>
                <div id="pieChartInputGroup" style="width:100%" class="input-group">
                  <span class="input-group-addon">
                    <g:img dir="images" file="peChartExampleSmall.png" style="float:left" width="200px" height="100px" alt="ok" class="img-rounded"/>
                  </span>
                  <span style="white-space: normal;width:100%" class="input-group-addon">
                    <p>In diesem Diagramm werden die einzelnen Beträge aufsummiert und als Gesamtbetrag angezeigt.<br>
                    <strong>Mögliche Fragestellung:</strong> <em>Wie viel</em> Geld wurde für <em>was</em> ausgegeben</p>
                  </span>
                </div>
              </div>
          </div>
          <script>
            $(document).ready(function() {
              $('#pieChartInputGroup').on('click', function(e) {
                var pieChartCheckbox = $('#pieChartCheckbox');
                pieChartCheckbox.prop("checked", !pieChartCheckbox.prop("checked"));
                });
            });
          </script>
        </div>
        <div style="height: 15px"></div>
        <div class="row">
          <div class="col-md-3">
            <div class="btn-group">
              <g:submitButton name="cancel" class="btn btn-info" value="Abbrechen" formnovalidate="" />
              <g:submitButton formnovalidate disabled name="add" class="btn btn-info" value="Zurück" />
              <g:submitButton name="next" class="btn btn-info" value="Weiter" />
            </div>
          </div>
        </div>
		  </g:form>
    </div>
	</div>
</body>
</html>