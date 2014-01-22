<div class="container">
	<div class="header">
		<ul class="nav nav-tabs pull-right">
			<li <g:if test="${active == 'home'}">class="active"</g:if> ><g:link url="${resource(dir:'/', file:'home')}">Home</g:link></li>
			<!-- Der Homebutten führt dort hin wohin auch der blick auf Einkaufszettel oben links führt. Zu einer Seite die alles erklärt Bei auswahl -->
			<li <g:if test="${active == 'dataInput'}">class="active"</g:if> ><g:link url="${resource(dir:'/', file:'dataInputHome')}">Daten eintragen</g:link></li>
			<li <g:if test="${active == 'dataOutput'}">class="active"</g:if> ><g:link controller="analyze" action="index">Daten auswerten</g:link></li>
			<li <g:if test="${active == 'news'}">class="active"</g:if> ><g:link url="${resource(dir:'/', file:'news')}">News</g:link></li>
			<li class="spacer <g:if test="${active == 'about'}"> active </g:if>"><g:link url="${resource(dir:'/', file:'about')}">Impressum</g:link></li>
		</ul>
		<h1>
			<g:link style="color:black; text-decoration: none;" url="${resource(dir:'/', file:'home')}">CostAnalyzR <small>alle Ausgaben im Blick</small></g:link>
		</h1>
	</div>
</div>