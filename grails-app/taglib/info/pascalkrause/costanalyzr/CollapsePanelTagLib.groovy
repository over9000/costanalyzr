package info.pascalkrause.costanalyzr

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class CollapsePanelTagLib {
    
	/**
	 * Erstellt einen Table der mit Jquery.tablesorter sortierbar ist.<br>
	 * Es können alle üblichen Attribute übergeben werden, wird keine Id übergeben wird eine eindeutige Id erzeugt.
	 * <b>panelTitle</b> (String) der Titel des ausklappbaren Panels
	 */
	def collapsePanel = { attrs, body ->
		
		def htmlWriter = getOut()
		
		//Die id wird benötigt damit das richtige Panel ausklappbar ist
		def id = Utils.getRandomString()
		
		if(attrs.id != null) {
			id = attrs.id
		} else {
			attrs.putAt("id", id)
		}
		
		htmlWriter << """
				<div class="panel panel-default">
        			<div class="panel-heading">
          				<h4 class="panel-title"> <a style="cursor:pointer;text-decoration: none;" data-toggle="collapse" data-target="#${attrs.id}"><span class="glyphicon glyphicon-resize-vertical"></span>${attrs.panelTitle}</a> </h4>
        			</div>
        			<div id="${attrs.id}" class="panel-collapse collapse">
          				<div class="panel-body">${body}</div>
        			</div>
				</div>
			"""
	}
	
	def bug = { attrs, body ->
		def panelTitle = "#${attrs.bugNumber} - ${attrs.bugName}"
		attrs.putAt("panelTitle", panelTitle)
		def htmlWriter = getOut()
		htmlWriter << collapsePanel(attrs, body)
	}
}