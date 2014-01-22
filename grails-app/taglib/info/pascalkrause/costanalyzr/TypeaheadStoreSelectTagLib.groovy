package info.pascalkrause.costanalyzr

class TypeaheadStoreSelectTagLib {
	
	
	/**
	 * Erstellt ein Textfeld welches so mit Twitter-Typeahead versehen ist, dass man alle momentan existierenden Kategorien suchen kann.<br>
	 * Wird das Attribut "type" übergeben wird es entfernt, da es in dieser TagLib auf "text" gesetzt wird.<b>
	 * Wird keine Id übergeben wird eine eindeutige Id erzeugt.<br>
	 * <b>noSelectionAvailable</b> wird dieses Attribut übergeben, wird es zusätzlich auch ermöglicht keine Filiale auszuwählen. Der Wert des Attributs ist egal.<br>
	 * <br>
	 * <b>concernId</b> (Integer) wird dieses Attribut übergeben, werden nur Filialen des übergeben Konzerns angezeigt.
	 */
	def typeaheadStoreSelect = { attrs, body ->
		
		def htmlWriter = getOut()
		
		// Entferne das Attribut type, denn es wird sowieso auf text gesetzt.
		attrs.remove('type')
		//Die Id wird benötigt damit Twitter-Typeahead an das richtige Element bindet
		def id = Utils.getRandomString()
		if(attrs.id != null) {
			id = attrs.id
		} else {
			attrs.putAt("id", id)
		}
		
		def noSelectionAvailable = false;
		if(attrs.noSelectionAvailable != null) {
			noSelectionAvailable = true;
		}
		
		def concernId = false;
		if(attrs.concernId != null) {
			concernId = true;
		}
		
		htmlWriter << r.require(module: "typeahead") << r.require(module: "typeahead_hogan") <<
		""" <input type="text"  """ << Utils.addPassedTagAttributes(attrs) << "/>" <<
		""" <script>\$(document).ready(function() { 
				\$( "#${id}" ).typeahead([ {
					remote : '/CostAnalyzR/store/getStoreListAsJSON?keywords=%QUERY${noSelectionAvailable ? "&noSelectionAvailable=true" : ""}${concernId ? "&concernId=${attrs.concernId}" : ""}',
					template: [
						'<p class="store-concern">{{concern}}</p>',
						'<p class="store-streetNameAndNumber">{{streetNameAndNumber}}</p>',
		  				'<p class="store-cityZipCodeCountry">{{cityZipCodeCountry}}</p>'
					].join(''),
					limit: 5,
					engine : Hogan
				} ]);
				//Needed for css
				\$( "#${id}" ).parent().addClass("typeaheadStoreSelect");
			})</script>"""
	}
}
