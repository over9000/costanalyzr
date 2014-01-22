package info.pascalkrause.costanalyzr

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class StoreSpec {

    def setup() {
    }

    def cleanup() {
    }
	
	void "test method toString()"() {
		
		Concern firstConcern = new Concern()
		firstConcern.setName("Konzern Foo bar")
		
		Store firstStore = new Store()
		String city = "Berlin"
		String zipCode = "11111"
		String streetName = "Foostraße"
		String streetNumber = "23"
		AvailableCountrys country = AvailableCountrys.DE
		firstStore.setConcern(firstConcern)
		firstStore.setCountry(country);
		firstStore.setCity(city)
		firstStore.setZipcode(zipCode)
		firstStore.setStreetName(streetName)
		firstStore.setStreetNumber(streetNumber)
		
		assertEquals("${firstConcern.getName()}, ${city}, ${zipCode}, ${streetName}, ${streetNumber}, ${country}", firstStore.toString())
	}
	
	void "test method equals()"() {
		
		Concern firstConcern = new Concern()
		firstConcern.setName("Konzern Foo bar")
		
		String city = "Berlin"
		String zipCode = "11111"
		String streetName = "Foostraße"
		String streetNumber = "23"
		AvailableCountrys country = AvailableCountrys.DE
		
		Store firstStore = new Store()
		firstStore.setConcern(firstConcern)
		firstStore.setCity(city)
		firstStore.setZipcode(zipCode)
		firstStore.setStreetName(streetName)
		firstStore.setStreetNumber(streetNumber)
		firstStore.setCountry(country);
		
		assertEquals(true, firstStore.equals(firstStore))
		
		Store secondStore = new Store()
		secondStore.setConcern(firstConcern)
		secondStore.setCity(city + "1")
		secondStore.setZipcode(zipCode)
		secondStore.setStreetName(streetName)
		secondStore.setStreetNumber(streetNumber)
		secondStore.setCountry(country);
		
		assertEquals(false, secondStore.equals(firstStore))
		
		Store thirdStore = new Store()
		thirdStore.setConcern(firstConcern)
		thirdStore.setCity(city + "1")
		thirdStore.setZipcode(zipCode + "1")
		thirdStore.setStreetName(streetName)
		thirdStore.setStreetNumber(streetNumber)
		thirdStore.setCountry(country);
		
		assertEquals(false, thirdStore.equals(firstStore))
		assertEquals(false, thirdStore.equals(secondStore))
		
		Store fourthStore = new Store()
		fourthStore.setConcern(firstConcern)
		fourthStore.setCity(city + "1")
		fourthStore.setZipcode(zipCode + "1")
		fourthStore.setStreetName(streetName + "1")
		fourthStore.setStreetNumber(streetNumber)
		fourthStore.setCountry(country);
		
		assertEquals(false, fourthStore.equals(firstStore))
		assertEquals(false, fourthStore.equals(secondStore))
		assertEquals(false, fourthStore.equals(thirdStore))
		
		Store fifthStore = new Store()
		fifthStore.setConcern(firstConcern)
		fifthStore.setCity(city + "1")
		fifthStore.setZipcode(zipCode + "1")
		fifthStore.setStreetName(streetName + "1")
		fifthStore.setStreetNumber(streetNumber + "1")
		fifthStore.setCountry(country);
		
		assertEquals(false, fifthStore.equals(firstStore))
		assertEquals(false, fifthStore.equals(secondStore))
		assertEquals(false, fifthStore.equals(thirdStore))
		assertEquals(false, fifthStore.equals(fourthStore))
		
		Store sixthStore = new Store()
		sixthStore.setConcern(firstConcern)
		sixthStore.setCity(city + "1")
		sixthStore.setZipcode(zipCode + "1")
		sixthStore.setStreetName(streetName + "1")
		sixthStore.setStreetNumber(streetNumber + "1")
		sixthStore.setCountry(AvailableCountrys.FR);
		
		assertEquals(false, sixthStore.equals(firstStore))
		assertEquals(false, sixthStore.equals(secondStore))
		assertEquals(false, sixthStore.equals(thirdStore))
		assertEquals(false, sixthStore.equals(fourthStore))
		assertEquals(false, sixthStore.equals(fifthStore))
	}
}