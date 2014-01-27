package info.pascalkrause.costanalyzr

class TypeaheadArticleSelectTagLib {
	
	/**
	 * Erstellt ein Textfeld welches so mit Twitter-Typeahead versehen ist, dass man alle momentan existierenden Artikel suchen kann.<br>
	 * Wird das Attribut "type" übergeben wird es entfernt, da es in dieser TagLib auf "text" gesetzt wird.<b>
	 * Wird keine Id übergeben wird eine eindeutige Id erzeugt. 
	 */
	def typeaheadArticleSelect = { attrs, body ->
		
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
		htmlWriter << r.require(module: "typeahead") << r.require(module: "typeahead_hogan") << 
		""" <input type="text"  """ << Utils.addPassedTagAttributes(attrs) << "/>" <<
		""" <script>\$(document).ready(function() { 
				\$( "#${id}" ).typeahead([ {
					remote : '${Utils.getContextPath()}/article/getArticleListAsJSON?keywords=%QUERY',
					template: [
		  				'<p class="article-name">{{name}}</p>',
		  				'<p class="article-category">Kategorie: {{category}}</p>',
		  				'<p class="article-description">Beschreibung: {{description}}</p>'
					].join(''),
					limit: 5,
					engine : Hogan
				} ]);
				//Needed for css
				\$( "#${id}" ).parent().addClass("typeaheadArticleSelect");
			})</script>"""
	}
}