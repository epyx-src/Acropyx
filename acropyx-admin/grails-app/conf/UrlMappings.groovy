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
            //controller = "login"
            action = "home"
            //action = "auth"
        }
}
	
}
