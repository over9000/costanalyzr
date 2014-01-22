package info.pascalkrause.costanalyzr

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class ConcernController {

    static allowedMethods = [saveConcern: "POST", deleteConcern:"POST", updateConcern:"POST", addConcern:"POST"]

    def index(Integer max) {
		redirect(controller: "concern", action: "list")
    }
	
	def list() {
		render(view: "list", model: [concernInstanceList: Concern.list()])
	}
	
	@Transactional
	def deleteConcern() {	
		Concern concernToDelete = Concern.get(Integer.parseInt(params.get("concernId")))
		if (concernToDelete == null) {
			flash.typ = "error"
			flash.message = "Der zu löschende Konzern existiert nicht"
		} else {
			flash.typ = "success"
			flash.message = "Der Konzern ${concernToDelete} wurde gelöscht"
			try {
				// Das ist momentan die einzige Möglichkeit einen DataIntegrityViolationException abzufangen.
				Concern.withNewTransaction {
					Concern concern = Concern.get(concernToDelete.getId())
					concern.delete flush:true
				}
			} catch(Exception e) {
				if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
					flash.typ = "error"
					flash.message = """Der Konzern ${concernToDelete} konnte nicht gelöscht werden, da noch mindestens
									   eine Filiale dieses Konzerns in der Datenbank existiert."""
				} else {
				flash.typ = "error"
				flash.message = e.toString()
				}
			}
		}
		if(concernToDelete.errors.errorCount > 0) {
			flash.typ = "error"
			flash.message = concernToDelete.errors.toString()
		}
		render(view: "list", model: [concernInstanceList: Concern.list()])
	}
	
	def updateConcern() {
		Concern concernToEdit = Concern.get(Integer.parseInt(params.get("concernId")))
		if (concernToEdit == null) {
			flash.typ = "error"
			flash.message = "Der zu bearbeitende Konzern existiert nicht"
			render(view: "list", model: [concernInstanceList: Concern.list()])
		} else {
		render(view: "list", model: [concernInstanceList: Concern.list(), concernToEdit: concernToEdit.getId()])
		}
	}
	
	@Transactional
	def saveConcern() {
		Concern concernToUpdate = Concern.get(Integer.parseInt(params.get("concernId")))
		// Überprüfe übergebene Parameter für Konzern
		String newConcernName = params.get("newConcernName");
		if(concernToUpdate == null) {
			flash.typ = "error"
			flash.message = "Der zu aktualisierende Konzern existiert nicht"
			render(view: "list", model: [concernInstanceList: Concern.list()])
		} else if(newConcernName == null) {
			flash.typ = "error"
			flash.message = "Es wurde kein Konzernname übertragen"
			render(view: "list", model: [concernInstanceList: Concern.list(), concernToEdit: concernToUpdate.getId()])
		} else {
			flash.typ = "success"
			flash.message = "Der Name des Konzerns mit der ID #${concernToUpdate.getId()} wurde von ${concernToUpdate.getName()} auf ${newConcernName} geändert"
			concernToUpdate.setName(newConcernName)
			concernToUpdate.save flush: true
			if(concernToUpdate.errors.errorCount > 0) {
				if(concernToUpdate.errors.toString().contains("unique")) {
					flash.typ = "error"
					flash.message = "Ein Konzern mit dem Name ${concernToUpdate} existiert bereits"
				} else {
					flash.typ = "error"
					flash.message = concernToUpdate.errors.toString()
				}
			}
			render(view: "list", model: [concernInstanceList: Concern.list()])
		}
	}
	
    @Transactional
    def addConcern() {
        String concernName = params.get("concernName");
		// Überprüfe übergebene Parameter für Konzern
		if(!concernName || concernName.isEmpty()) {
			flash.typ = "error"
			flash.message = "Es wurde kein Konzernname übertragen"
		} else {
			Concern newConcern = new Concern()
			newConcern.setName(concernName)
			newConcern.save flush:true
			if(newConcern.errors.errorCount > 0) {
				if(newConcern.errors.toString().contains("unique")) {
					flash.typ = "error"
					flash.message = "Ein Konzern mit dem Name ${concernName} existiert bereits"
				} else {
					flash.typ = "error"
					flash.message = newConcern.errors.toString()
				}
			} else {
				flash.typ = "success"
				flash.message = "Der Konzern ${concernName} wurde unter der ID #${newConcern.id} angelegt"
			}
		}
		render(view: "list", model: [concernInstanceList: Concern.list()])
    }
}