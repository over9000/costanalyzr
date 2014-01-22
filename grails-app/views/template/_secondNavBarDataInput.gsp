<div class="container" style="height: 40px">
	<hr style="background-color: grey; height: 2px;">
</div>
<div class="container">
	<div class="btn-toolbar" role="toolbar">
		<div class="btn-group btn-group-lg spacer">
			<a href="<g:createLink controller="enterInvoice" action="createInvoice"/>" class="btn btn-lg btn-primary">Rechnung eintragen</a>
		</div>
		<ul class="nav nav-tabs nav-justified">
		  <li <g:if test="${active == 'invoice'}">class="active"</g:if> ><a href="<g:createLink controller="invoice" action="index"/>" class="btn btn-lg">Rechnungen</a></li>
			<li <g:if test="${active == 'article'}">class="active"</g:if> ><a href="<g:createLink controller="article" action="index"/>" class="btn-lg">Artikel</a></li>
			<li <g:if test="${active == 'category'}">class="active"</g:if> ><a href="<g:createLink controller="category" action="index"/>" class="btn btn-lg">Kategorien</a></li>
			<li <g:if test="${active == 'store'}">class="active"</g:if> ><a href="<g:createLink controller="store" action="index"/>" class="btn-lg">Filialen</a></li>
			<li <g:if test="${active == 'concern'}">class="active"</g:if> ><a href="<g:createLink controller="concern" action="index"/>" class="btn-lg">Konzerne</a></li>
		</ul>
	</div>
</div>