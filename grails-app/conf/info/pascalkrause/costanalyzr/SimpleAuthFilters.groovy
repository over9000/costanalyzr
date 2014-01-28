package info.pascalkrause.costanalyzr

class SimpleAuthFilters {
	
	def USERNAME = ""
	def PASSWORD = ""
	
static filters = {
        httpAuth(uri:"/**") {
            before = {
                def authHeader = request.getHeader('Authorization')
                if (authHeader) {
                    def usernamePassword = new String(authHeader.split(' ')[1].decodeBase64())
                    if (usernamePassword == "$USERNAME:$PASSWORD") {
                        return true
                    }
                }
                response.setHeader('WWW-Authenticate', 'basic realm="CostAnalyzR"')
                response.sendError(response.SC_UNAUTHORIZED)
                return false
            }
        }
    }
}