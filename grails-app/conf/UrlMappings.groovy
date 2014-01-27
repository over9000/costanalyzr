class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.${format})?"{
            constraints {
                // apply constraints here
            }
        }
		"/enterInvoice" {
			controller = "enterInvoice"
		}

        "/"(view:"/index")
		"/about"(view:"/about")
		"/news"(view:"/news")
		"/bugs"(view:"/bugs")
		"/dataInputHome"(view:"/dataInputHome")
		"/dataOutput"(view:"/dataOutput")
		"/home"(view:"/home")
		"500"(view:'/error')
	}
}
