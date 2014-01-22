<div class="row">
  <div class="col-md-3">
    <div class="well media
      <g:if test="${webflowNavbarCurrentState < 1}"> webflow-undone-item </g:if> 
      <g:if test="${webflowNavbarCurrentState == 1}"> webflow-current-item </g:if>
    ">
      <a class="pull-right"><g:img dir="images/icons" file="done_128.png" width="40" height="40" alt="Ok" class="img-rounded"/></a>
      <div class="media-body"><h4 class="media-heading">Schritt 1: Konzern auswählen</h4></div>
    </div>
  </div>
  <div class="col-md-3">
    <div class="well media 
      <g:if test="${webflowNavbarCurrentState < 2}"> webflow-undone-item </g:if> 
      <g:if test="${webflowNavbarCurrentState == 2}"> webflow-current-item </g:if>
    ">
      <a class="pull-right"><g:img dir="images/icons" file="done_128.png" width="40" height="40" alt="Ok" class="img-rounded"/></a>
      <div class="media-body"><h4 class="media-heading">Schritt 2: Filiale auswählen</h4></div>
    </div>
  </div>
  <div class="col-md-3">
    <div class="well media
      <g:if test="${webflowNavbarCurrentState < 3}"> webflow-undone-item </g:if> 
      <g:if test="${webflowNavbarCurrentState == 3}"> webflow-current-item </g:if>
    ">
      <a class="pull-right"><g:img dir="images/icons" file="done_128.png" width="40" height="40" alt="" class="img-rounded"/></a>
      <div class="media-body"><h4 class="media-heading">Schritt 3: Rechnung erstellen</h4></div>
    </div>
  </div>
    <div class="col-md-3">
    <div class="well media
      <g:if test="${webflowNavbarCurrentState < 4}"> webflow-undone-item </g:if> 
      <g:if test="${webflowNavbarCurrentState == 4}"> webflow-current-item </g:if>
    ">
      <a class="pull-right"><g:img dir="images/icons" file="done_128.png" width="40" height="40" alt="" class="img-rounded"/></a>
      <div class="media-body"><h4 class="media-heading">Schritt 4: Posten hinzufügen</h4></div>
    </div>
  </div>
</div>