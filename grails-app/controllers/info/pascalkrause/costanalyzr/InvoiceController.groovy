package info.pascalkrause.costanalyzr

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import java.text.SimpleDateFormat

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class InvoiceController {

	static allowedMethods = [saveInvoice: "POST", deleteInvoice:"POST", updateInvoice:"POST", addInvoice:"POST"]

	def index(Integer max) {
		redirect(controller: "invoice", action: "list")
	}

	def list() {
		render(view: "list", model: [invoiceInstanceList: Invoice.list()])
	}
	
	def invoiceService
	
	@Transactional
	def addInvoice() {
		// Hole Parameter
		Store invoiceStore = Store.get(Integer.parseInt(params.get("invoiceStoreId")))
		String invoiceCreationDate = params.get("invoiceCreationDate")
		String invoicePaymentMethodAsString = params.get("invoicePaymentMethod")
		// Überprüfe übergebene Parameter für Invoice
		def validateResponse = invoiceService.validateInvoice(invoiceStore, invoiceCreationDate, invoicePaymentMethodAsString);
		if(validateResponse.error) {
			flash.typ = validateResponse.typ
			flash.message = validateResponse.message
			render(view: "list", model: [invoiceInstanceList: Invoice.list()])
			return
		}
		PaymentMethods invoicePaymentMethod
		def creationDate
		try {
			invoicePaymentMethod = PaymentMethods.valueOf(invoicePaymentMethodAsString)
			creationDate = new GregorianCalendar();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			creationDate.setTime(sdf.parse(invoiceCreationDate));
		} catch(Exception e) {
			// Eigentlich müssten alle Fehleingaben in der invoiceService.validateInvoice() Methode abgefangen werden
			flash.typ = "error"
			flash.message = "${e.toString()}"
			render(view: "list", model: [invoiceInstanceList: Invoice.list()])
			return
		}
		Invoice newInvoice = new Invoice()
		newInvoice.setStore(invoiceStore)
		newInvoice.setPaymentMethod(invoicePaymentMethod)
		newInvoice.setCreationDate(creationDate)
		newInvoice.save flush:true
		flash.typ = "success"
		flash.message = "Die Rechnung wurde unter der ID #${newInvoice.id} angelegt"
		if(newInvoice.errors.errorCount > 0) {
			flash.typ = "error"
			flash.message = newInvoice.errors.toString()
			render(view: "list", model: [invoiceInstanceList: Invoice.list()])
		}
		render(view: "list", model: [invoiceInstanceList: Invoice.list()])
	}
	
	@Transactional
	def deleteInvoice() {
		Invoice invoiceToDelete = Invoice.get(Integer.parseInt(params.get("invoiceId")))
		if (invoiceToDelete == null) {
			flash.typ = "error"
			flash.message = "Die zu löschende Rechnung existiert nicht"
		} else {
			flash.typ = "success"
			flash.message = "Die Rechnung mit der ID #${invoiceToDelete.id} wurde gelöscht"
			invoiceToDelete.delete flush:true
		}
		if(invoiceToDelete.errors.errorCount > 0) {
			flash.typ = "error"
			flash.message = invoiceToDelete.errors.toString()
		}
		render(view: "list", model: [invoiceInstanceList: Invoice.list()])
	}
	
	def updateInvoice() {
		Invoice invoiceToEdit = Invoice.get(Integer.parseInt(params.get("invoiceId")))
		if (invoiceToEdit == null) {
			flash.typ = "error"
			flash.message = "Die zu bearbeitende Rechnung existiert nicht"
			render(view: "list", model: [invoiceInstanceList: Invoice.list()])
		} else {
			render(view: "list", model: [invoiceInstanceList: Invoice.list(), invoiceToEdit: invoiceToEdit.getId()])
		}
	}
	
	@Transactional
	def saveInvoice() {
		// Hole Parameter
		Store invoiceStore = Store.get(Integer.parseInt(params.get("invoiceStoreId")))
		String invoiceCreationDate = params.get("invoiceCreationDate")
		String invoicePaymentMethodAsString = params.get("invoicePaymentMethod")
		Invoice invoiceToUpdate = Invoice.get(Integer.parseInt(params.get("invoiceId")))
		if (invoiceToUpdate == null) {
			flash.typ = "error"
			flash.message = "Die zu aktualisierende Rechnung existiert nicht"
			render(view: "list", model: [invoiceInstanceList: Invoice.list()])
		}
		// Überprüfe übergebene Parameter für Invoice
		def validateResponse = invoiceService.validateInvoice(invoiceStore, invoiceCreationDate, invoicePaymentMethodAsString);
		if(validateResponse.error) {
			flash.typ = validateResponse.typ
			flash.message = validateResponse.message
			render(view: "list", model: [invoiceInstanceList: Invoice.list()])
			return
		}
		PaymentMethods invoicePaymentMethod
		def creationDate
		try {
			invoicePaymentMethod = PaymentMethods.valueOf(invoicePaymentMethodAsString) 
			creationDate = new GregorianCalendar();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			creationDate.setTime(sdf.parse(invoiceCreationDate));
		} catch(Exception e) {
			// Eigentlich müssten alle Fehleingaben in der invoiceService.validateInvoice() Methode abgefangen werden
			flash.typ = "error"
			flash.message = "${e.toString()}"
			render(view: "list", model: [invoiceInstanceList: Invoice.list()])
			return
		}
		invoiceToUpdate.setStore(invoiceStore)
		invoiceToUpdate.setPaymentMethod(invoicePaymentMethod)
		invoiceToUpdate.setCreationDate(creationDate)
		invoiceToUpdate.save flush:true
		flash.typ = "success"
		flash.message = "Die Rechnung mit der ID #${invoiceToUpdate.id} wurde erfolgreich aktualisiert"
		if(invoiceToUpdate.errors.errorCount > 0) {
			flash.typ = "error"
			flash.message = invoiceToUpdate.errors.toString()
			render(view: "list", model: [invoiceInstanceList: Invoice.list()])
		}
		render(view: "list", model: [invoiceInstanceList: Invoice.list()])
	}	
}