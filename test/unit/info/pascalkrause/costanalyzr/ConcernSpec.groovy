package info.pascalkrause.costanalyzr

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class ConcernSpec {

    def setup() {
    }

    def cleanup() {
    }
	
	void "test method toString()"() {
		
		Concern firstConcern = new Concern()
		firstConcern.setName("Konzern Foo bar")
		
		assertEquals("Konzern Foo bar", firstConcern.toString())
	}
}