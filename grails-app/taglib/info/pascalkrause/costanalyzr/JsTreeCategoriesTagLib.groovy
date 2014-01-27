package info.pascalkrause.costanalyzr

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class JsTreeCategoriesTagLib {
	
	/**
	 * Erstellt ein Div-Block der mit einem jsTree versehen ist.<br>
	 * Wird keine Id übergeben wird eine eindeutige Id erzeugt.
	 */
    def jsTreeCategories = { attrs, body ->
		
		//Die Id wird benötigt damit das richtige Element mit dem jsTree versehen wird.
		def id = Utils.getRandomString()
		if(attrs.id != null) {
			id = attrs.id
		} else {
			attrs.putAt("id", id)
		}
		out << """
				<div """ << Utils.addPassedTagAttributes(attrs) << """></div>""" << r.require(module: "jstree_min") <<
				"""
				<script>
                 \$(function () {
    	           \$.ajax({
    		          url: "${Utils.getContextPath()}/analyze/getCategories",
    		          type: 'get',
	  	              dataType: 'json',
	   	              success: buildJsTree,
	   	              error: function (jqXHR, textStatus, errorThrown){console.error( "The following error occured: "+ textStatus, errorThrown);}
					});

	                function buildJsTree(res) {
                      \$('#${attrs.id}').jstree(
                        { 
                        'core' : {'data' : res},
	                    'checkbox' : {'keep_selected_style' : false},
	                    'plugins' : [ 'wholerow', 'checkbox' ]
	                    }
                      );
		              \$('#${attrs.id}').jstree().hide_icons();
	                }
                  });
				</script>
                """
	}
}