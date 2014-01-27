import info.pascalkrause.costanalyzr.Utils

modules = {
    jquery_datepicker_german {
        resource url: "/js/jquery-ui/datePicker.js"
	}
	
	jquery_ui {
		resource url: "/js/jquery-ui/jquery-ui-1.10.3.min.js"
	}
	
	jquery_table_sorter {
		resource url: "/js/jquery/jquery.tablesorter.js"
	}
	
	nv_d3_v115 {
		resource url: "/js/nvd3/v1.15/d3.v3.min.js"
		//Hier nicht die min Version, da in Zeile 14134 Euro eingefügt wurde. (nv.d3.js Zeile: 14134 für mehr Infos)
		resource url: "/js/nvd3/v1.15/nv.d3.js"
	}
	
	typeahead {
		resource url: "/js/typeahead/typeahead.min.js"
	}
	
	typeahead_hogan {
		resource url: "/js/typeahead/hogan-2.0.0.min.js"
	}
	
	jstree_min {
		resource url: "/js/jstree/jstree.min.js"
	}
}