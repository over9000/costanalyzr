package info.pascalkrause.costanalyzr

import java.text.SimpleDateFormat

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class JQueryUIDatePickerTagLib {
	
	/**
	 * Erstellt ein Textfeld welches mit einem JqueryUI.datepicker versehen ist.<br>
	 * Wird das Attribut "type" übergeben wird es entfernt, da es in dieser TagLib auf "text" gesetzt wird.<b>
	 * Wird keine Id übergeben wird eine eindeutige Id erzeugt. 
	 */
    def jqueryUIDatePicker = { attrs, body ->
		
		def htmlWriter = getOut()
		
		// Entferne das Attribut type, denn es wird sowieso auf text gesetzt.
		attrs.remove('type')
		//Die Id wird benötigt damit der datepicker die Daten in das richtige Element steckt.
		def id = Utils.getRandomString()
		if(attrs.id != null) {
			id = attrs.id
		} else {
			attrs.putAt("id", id)
		}
		
		htmlWriter << r.require(module: "jquery_ui") << r.require(module: "jquery_datepicker_german") <<
		""" <input type="text"  """ << Utils.addPassedTagAttributes(attrs) << "/>" << """ <script>\$(document).ready(function() {  \$( "#${id}" ).datepicker(); })</script>"""
	}
	

}