class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

        "/admin" {
            controller = "competition"
            action = "index"
        }

        "/" {
            controller = "event"
            action = "home"
        }

		"500"(view:'/error')
	}
}
