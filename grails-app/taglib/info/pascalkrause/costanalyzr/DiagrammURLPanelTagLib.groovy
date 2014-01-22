package info.pascalkrause.costanalyzr

class DiagrammURLPanelTagLib {
    	def diagrammURLPanel = { attrs, body ->
			
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
}
