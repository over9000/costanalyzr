package info.pascalkrause.costanalyzr

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class StoreService {
	
    def validateStore(Concern storeConcern, String storeStreetName, String storeStreetNumber, String storeCountryAsString, String storeCity, String storeZipcode) {

		Expando validateResponse = new Expando()
		validateResponse.error = false;
		AvailableCountrys storeCountry
		try {
			storeCountry = AvailableCountrys.valueOf(storeCountryAsString)
		} catch(Exception e) {
			validateResponse.typ = "error"
			validateResponse.message = "Es wurde keine gültiges Land übermittelt"
			validateResponse.error = true;
		}
		if(storeConcern == null) {
			validateResponse.typ = "error"
			validateResponse.message = "Der zugehörige Konzern wurde nicht übermittelt oder existiert nicht"
			validateResponse.error = true;
		}
		if(!storeStreetName || storeStreetName.isEmpty()) {
			validateResponse.typ = "error"
			validateResponse.message = "Es wurde kein Straßenname übermittelt"
			validateResponse.error = true;
		}
		if(!storeStreetNumber || storeStreetNumber.isEmpty()) {
			validateResponse.typ = "error"
			validateResponse.message = "Es wurde keine Hausnummer übermittelt"
			validateResponse.error = true;
		}
		if(!storeCity || storeCity.isEmpty()) {
			validateResponse.typ = "error"
			validateResponse.message = "Es wurde keine Stadt übermittelt"
			validateResponse.error = true;
		}
		if(!storeZipcode || storeZipcode.isEmpty()) {
			validateResponse.typ = "error"
			validateResponse.message = "Es wurde keine Postleitzahl übertragen"
			validateResponse.error = true;
		}
		return validateResponse
    }
}