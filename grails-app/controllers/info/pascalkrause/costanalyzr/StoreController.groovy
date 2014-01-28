package info.pascalkrause.costanalyzr

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class StoreController {

    static allowedMethods = [saveStore: "POST", deleteStore:"POST", updateStore:"POST", addStore:"POST", getStoreListAsJSON:"GET"]

	def index(Integer max) {
		redirect(controller: "store", action: "list")
	}
	
	def list() {
		render(view: "list", model: [storeInstanceList: Store.list()])
	}
	
	def storeService
	
	@Transactional
	def addStore() {
		// Hole Parameter
		Concern storeConcern = Concern.get(Integer.parseInt(params.get("storeConcernId")))
		String storeStreetName = params.get("storeStreetName")
		String storeStreetNumber = params.get("storeStreetNumber")
		String storeCountryAsString = params.get("storeCountry")
		String storeCity = params.get("storeCity")
		String storeZipcode = params.get("storeZipcode")
		// Überprüfe übergebene Parameter für Store
		def validateResponse = storeService.validateStore(storeConcern, storeStreetName, storeStreetNumber, storeCountryAsString, storeCity, storeZipcode)
		if(validateResponse.error) {
			flash.typ = validateResponse.typ
			flash.message = validateResponse.message
			render(view: "list", model: [storeInstanceList: Store.list()])
			return
		}
		AvailableCountrys storeCountry
		try { storeCountry = AvailableCountrys.valueOf(storeCountryAsString) } catch(Exception e) { println e /*Würde in storeService.validateStore() abgefangen werden*/ }
		Store newStore = new Store()
		newStore.setCity(storeCity)
		newStore.setZipcode(storeZipcode)
		newStore.setConcern(storeConcern)
		newStore.setStreetName(storeStreetName)
		newStore.setStreetNumber(storeStreetNumber)
		newStore.setCountry(storeCountry)
		newStore.save flush:true
		flash.typ = "success"
		flash.message = "Die Filiale ${newStore} wurde unter der ID #${newStore.id} angelegt"
		if(newStore.errors.errorCount > 0) {
			if(newStore.errors.toString().contains("unique")) {
				Store alreadyExist = null;
				Store.list().each {
					if(it.equals(newStore) && it.getId() != newStore.getId()) {
						alreadyExist = it;
					}
				}
				flash.typ = "error"
				flash.message = "Eine identische Filiale existiert bereits unter der ID #${alreadyExist.id}"
			} else {
				flash.typ = "error"
				flash.message = newStore.errors.toString()
			}
		}
		render(view: "list", model: [storeInstanceList: Store.list()])
	}
	
	@Transactional
	def deleteStore() {
		Store storeToDelete = Store.get(Integer.parseInt(params.get("storeId")))
		if (storeToDelete == null) {
			flash.typ = "error"
			flash.message = "Die zu löschende Filiale existiert nicht"
		} else {
			flash.typ = "success"
			flash.message = "Die Filiale ${storeToDelete} mit der ID #${storeToDelete.id} wurde gelöscht"
			try {
				// Das ist momentan die einzige Möglichkeit einen DataIntegrityViolationException abzufangen.
				Store.withNewTransaction {
					Store store = Store.get(storeToDelete.getId())
					store.delete flush:true
				}
			} catch(Exception e) {
				if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
					flash.typ = "error"
					flash.message = """Die Filiale ${storeToDelete} konnte nicht gelöscht werden, da es noch mindestens eine Rechnung gibt,
									in der diese Filiale verwendet wird."""
				} else {
				flash.typ = "error"
				flash.message = e.toString()
				}
			}
		}
		if(storeToDelete.errors.errorCount > 0) {
			flash.typ = "error"
			flash.message = storeToDelete.errors.toString()
		}
		render(view: "list", model: [storeInstanceList: Store.list()])
	}
	
	def updateStore() {
		Store storeToEdit = Store.get(Integer.parseInt(params.get("storeId")))
		if (storeToEdit == null) {
			flash.typ = "error"
			flash.message = "Der zu bearbeitende Artikel existiert nicht"
			render(view: "list", model: [storeInstanceList: Store.list()])
		} else {
		render(view: "list", model: [storeInstanceList: Store.list(), storeToEdit: storeToEdit.getId()])
		}
	}
	
	@Transactional
	def saveStore() {
		// Hole Parameter
		Store storeToUpdate = Store.get(Integer.parseInt(params.get("storeId")))
		Concern storeConcern = Concern.get(Integer.parseInt(params.get("storeConcernId")))
		String storeStreetName = params.get("storeStreetName")
		String storeStreetNumber = params.get("storeStreetNumber")
		String storeCountryAsString = params.get("storeCountry")
		String storeCity = params.get("storeCity")
		String storeZipcode = params.get("storeZipcode")
		if(storeToUpdate == null) {
			flash.typ = "error"
			flash.message = "Die zu aktualisierende Filiale existiert nicht"
			render(view: "list", model: [storeInstanceList: Store.list()])
			return
		}
		// Überprüfe übergebene Parameter für Store
		def validateStoreResponse = storeService.validateStore(storeConcern, storeStreetName, storeStreetNumber, storeCountryAsString, storeCity, storeZipcode)
		if(validateStoreResponse.error) {
			flash.typ = validateStoreResponse.typ
			flash.message = validateStoreResponse.message
			render(view: "list", model: [storeInstanceList: Store.list()])
			return
		}
		AvailableCountrys storeCountry
		try { storeCountry = AvailableCountrys.valueOf(storeCountryAsString) } catch(Exception e) { /*Würde in storeService.validateStore() abgefangen werden*/ }
		flash.typ = "success"
		flash.message = "Die Filiale mit der ID #${storeToUpdate.getId()} wurde erfolgreich geändert"
		storeToUpdate.setStreetName(storeStreetName)
		storeToUpdate.setStreetNumber(storeStreetNumber)
		storeToUpdate.setCity(storeCity)
		storeToUpdate.setZipcode(storeZipcode)
		storeToUpdate.setCountry(storeCountry)
		storeToUpdate.setConcern(storeConcern)
		storeToUpdate.save flush: true
		if(storeToUpdate.errors.errorCount > 0) {
			if(storeToUpdate.errors.toString().contains("unique")) {
				Store alreadyExist = null;
				Store.list().each {
					if(it.equals(storeToUpdate) && it.getId() != storeToUpdate.getId()) {
						alreadyExist = it;
					}
				}
				flash.typ = "error"
				flash.message = "Eine identische Filiale existiert bereits unter der ID #${alreadyExist.id}"
			} else {
				flash.typ = "error"
				flash.message = storeToUpdate.errors.toString()
			}
		}
		render(view: "list", model: [storeInstanceList: Store.list()])
	}
	
	def getStoreListAsJSON() {
		def keywords = ((String) params.get("keywords"))?.split(" ")
		def noSelectionAvailable = params.get("noSelectionAvailable")
		def concernId = params.get("concernId")
		String responseString = "[";
		if(noSelectionAvailable != null) {
			responseString = responseString +
			/
			{
				"id": "noSelection",
				"concern": " ",
				"streetNameAndNumber": "Neue Filiale anlegen",
				"cityZipCodeCountry": " ",
				"value": "Neue Filiale anlegen",
				"tokens": [""]
			},
			/
		}
		def stores = Store.list();
		if(concernId ==~ /\d+/) {
			def passedConcern = Concern.get(Integer.parseInt(concernId))
			def storeCriteria = Store.createCriteria();
			stores = storeCriteria.list {
								 eq("concern", passedConcern)
							}
		}
		stores.each {
			String[] tokens = it.toString().replace(" ", "").split(",")
			for(int i=0; i < tokens.length; i++) {
				tokens[i] = /"${tokens[i]}"/
			}
			boolean matchWithKeyword = true;
			keywords.each {
				if(!tokens.toString().toLowerCase().contains(it.toLowerCase())) {
					matchWithKeyword = false
				}
			}
			if(matchWithKeyword) {
				responseString = responseString +
				/
				{
					"id": "${it.getId()}",
					"concern": "${it.getConcern().getName()}",
					"streetNameAndNumber": "${it.getStreetName()} ${it.getStreetNumber()}",
					"cityZipCodeCountry": "${it.getCountry().toString()} - ${it.getZipcode()} - ${it.getCity()}",
					"value": "${it.toString()}",
					"tokens": ${tokens}
					},
				/
			}
		}
		
		if(responseString.lastIndexOf(",") > 0) {
			responseString = responseString.substring(0, responseString.lastIndexOf(","))
		}
		responseString = responseString + "]"
		render(text: responseString, contentType: "text/json", encoding: "UTF-8")
	}
}
