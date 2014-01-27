package info.pascalkrause.costanalyzr

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class JQueryTableSorterTagLib {
	
	/**
	 * Erstellt einen Table der mit Jquery.tablesorter sortierbar ist.<br>
	 * Es können alle üblichen Attribute übergeben werden, wird keine Id übergeben wird eine eindeutige Id erzeugt.
	 */
	def jqueryTableSorter = { attrs, body ->
		
		def htmlWriter = getOut()
		
		// Fügt die CSS-Klasse jquerytablesorter hinzu. Diese wird für CSS benötigt (costAnalyzR.css)
		(attrs.class != null) ? attrs.putAt("class", "${attrs.class} jquerytablesorter") : attrs.putAt("class", "jquerytablesorter")

		//Die id wird benötigt damit auch der richtige table sortiert wird
		def id = Utils.getRandomString()
		if(attrs.id != null) {
			id = attrs.id
		} else {
			attrs.putAt("id", id)
		}
		htmlWriter << r.require(module: "jquery") << r.require(module: "jquery_table_sorter") <<
		""" <table  """ << Utils.addPassedTagAttributes(attrs) << ">" << body() << """</table>""" << 
		"""<script>\$(document).ready(function() {  \$( "#${id}" ).tablesorter(); });</script>"""
	}
}