<%@ page import="info.pascalkrause.costanalyzr.Category" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="costanalyzr" />
<title>Bugs</title>
</head>
<body>
<g:render template="/template/mainNavBar" model="[active: 'news']"/>
  <div class="container">
    <div class="page-header">
      <h2>Bugs</h2>
    </div>
    <g:bug bugNumber="1" bugName="Invoice-Webflow Crasht beim Löschen von Rechnungsposten, sofern dieser bereits gelöscht wurde">
      Wird ein Rechnungsposten im Webflow gelöscht, obwohl dieser bereits an andere Stelle gelöscht wurde und somit nicht mehr vorhanden ist, crasht der Webflow.
    </g:bug>
    <g:bug bugNumber="2" bugName="Modal funktioniert auf Smartphone nicht">
      Das Modal wird geöffnet, jedoch kann man darin nicht scrollen. Die Seite im Hintergrund wird weiterhin gescrollt.
    </g:bug>
  </div>
</body>
</html>