<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="costanalyzr" />
<link rel="stylesheet" href="${resource(dir: 'css/jstree', file: 'style.min.css')}" />
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
      <g:render template="/analyze/prepareEvaluation/webflowNavbar" model="[webflowNavbarCurrentState: 2]"/>
    <div style="height: 15px"></div>
    <div class="well">
      <g:form  role="form" action="prepareEvaluation" method="post">
        <div class="row">
          <div class="col-md-8">
            <h3>Wähle Kategorien aus <small>(die dann im Diagramm dargestellt werden)</small></h3>
          </div>
        </div>
        <div class="row">
          <div class="col-md-6">
            <div class="row">
              <div class="col-md-12">
                <div class="btn-group">
                  <button type="button" id="test" class="btn btn-info" onclick="$(function () {$('#categoriesTree').jstree('select_all');})" >Alle auswählen</button>
                  <button type="button" id="test" class="btn btn-info" onclick="$(function () {$('#categoriesTree').jstree('deselect_all');})" >Alle abwählen</button>
                </div>
                <div class="btn-group" data-toggle="buttons">
                <fieldset id="showAllChildrenFieldset">
                  <label class="btn btn-info">
                     <input id="showAllChildren" name="showAllChildren" type="checkbox"> Zeige alle direkten Kategoriekinder an
                  </label>
                </fieldset>
                </div>
              </div>
            </div>
          <g:jsTreeCategories id="categoriesTree"></g:jsTreeCategories>
          <g:hiddenField id="selectedCategories" name="selectedCategories" value=""/>
          <script>
            $(function() {
            	  $("#categoriesTree").bind("changed.jstree", function(evt, data) {
            		  getSelectedNodes();
            		  
            		});
            })
  
            function getSelectedNodes() {
            	  var allSelectedNodes = $("#categoriesTree").jstree('get_selected', false).sort();
            	  var onlyTopSelectedNodes = new Array();
            	  var currentCategory = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
            		for(var i = 0; i < allSelectedNodes.length; i++) {
            			  if(allSelectedNodes[i].indexOf(currentCategory) == -1) {
            				  //Neue Kategorie kein Kind der aktuellen Kategorie
              			  onlyTopSelectedNodes.push(allSelectedNodes[i]);
              			  currentCategory = allSelectedNodes[i];
              			}
            		}
            	  if(onlyTopSelectedNodes.length <= 1) {
            		  $('#showAllChildrenFieldset').prop('disabled', false);
            	  } else {
            	    $('#showAllChildrenFieldset').prop('disabled', true);
            	  }
            	  console.log(String(onlyTopSelectedNodes));
            	  $("#selectedCategories").val(onlyTopSelectedNodes.join(","));
            }
          </script>
          </div>
          <div class="col-md-6">
            <div class="panel panel-info">
              <div class="panel-heading">
                <h3 class="panel-title">Hinweis</h3>
              </div>
              <div class="panel-body">
                <ul> 
                  <li>Es sollten maximal 10 Kateogiren ausgewählt werden</li>
                  <li>Wird eine Kategorie ausgewählt, beinhaltet diese auch alle Kategoriekinder</li>
                  <li>Wird eine Kategorie ausgewählt, kann nicht zusätzlich eine der Vaterkategorien ausgewählt werden</li>
                  <li>Wird nur eine Kategorie gewählt, können automatisch alle direkten Kategoriekinder im Diagramm mit angezeigt werden</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
        <div style="height: 15px"></div>
        <div class="row">
          <div class="col-md-3">
            <div class="btn-group">
              <g:submitButton name="cancel" class="btn btn-info" value="Abbrechen" formnovalidate="" />
              <g:submitButton name="back" class="btn btn-info" value="Zurück" formnovalidate=""/>
              <g:submitButton name="next" class="btn btn-info" value="Weiter" />
            </div>
          </div>
        </div>
      </g:form>
    </div>
  </div>
</body>
</html>