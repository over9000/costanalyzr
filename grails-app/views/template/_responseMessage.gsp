<div class="panel
  <g:if test="${responseMessage?.typ == 'error'}"> panel-danger </g:if>
  <g:if test="${responseMessage?.typ == 'success'}"> panel-success </g:if>
  <g:if test="${responseMessage?.typ == 'warning'}"> panel-warning </g:if>
">
  <div class="panel-heading">
    <g:if test="${responseMessage?.typ == 'error'}"> Error </g:if>
    <g:if test="${responseMessage?.typ == 'success'}"> Erfolg </g:if>
    <g:if test="${responseMessage?.typ == 'warning'}"> Warnung </g:if>
  </div>
  <div class="panel-body">
    ${responseMessage?.message} <g:if test="${responseMessage?.clear()}"></g:if>
  </div>
</div>