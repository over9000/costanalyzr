package info.pascalkrause.costanalyzr

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class InvoiceItemController {

    static allowedMethods = [addInvoiceItem: "POST", deleteInvoiceItem: "POST", updateInvoiceItem: "POST", saveInvoiceItem: "POST"]
	
	def invoiceItemService
	
	@Transactional
	def addInvoiceItem() {
		// Hole Parameter
		/**
		 * ist invoiceId oder invoiceItemArticleId NULL wird es zu einem fehler kommen. Sollte man noch abfangen
		 */
		Invoice invoice = Invoice.get(Integer.parseInt(params.get("invoiceId")))
		Article invoiceItemArticle = Article.get(Integer.parseInt(params.get("invoiceItemArticleId")))
		String invoiceItemComment = params.get("invoiceItemComment")
		String invoiceItemCount = params.get("invoiceItemCount")
		String invoiceItemArticleGrossPrice = params.get("invoiceItemArticleGrossPrice")
		String invoiceItemArticleTaxRate = params.get("invoiceItemArticleTaxRate")
		String invoiceItemReduced = params.get("invoiceItemReduced")
		// Setze Standardwerte
		if(!invoiceItemComment ||invoiceItemComment.isEmpty()) {
			invoiceItemComment = 'n/a'
		}
		if(!invoiceItemArticleTaxRate || invoiceItemArticleTaxRate.isEmpty()) {
			invoiceItemArticleTaxRate = '19.00'
		}
		if(!invoiceItemCount || invoiceItemCount.isEmpty()) {
			invoiceItemCount = '1.00'
		}
		if(!invoiceItemReduced || invoiceItemReduced.isEmpty()) {
			invoiceItemReduced = 'false'
		} else {
			if(invoiceItemReduced.equals("on")) {
				invoiceItemReduced = "true"
			}
		}
		// Überprüfe übergebene Parameter für InvoiceItem 
		def validateResponse = invoiceItemService.validateInvoiceItem(invoice, invoiceItemArticle, invoiceItemComment, invoiceItemCount,
			invoiceItemArticleGrossPrice, invoiceItemArticleTaxRate, invoiceItemReduced)
		if(validateResponse.error) {
			flash.typ = validateResponse.typ
			flash.message = validateResponse.message
			render(template: "/invoiceItem/modalBody", model: [invoiceInstance: invoice])
			return
		}
		// Sollte keine Fehler werfen, wurde zuvor mit RegExp geprüft
		boolean reduced = new Boolean(invoiceItemReduced);
		double count = Double.valueOf(invoiceItemCount)
		double taxRate = Double.valueOf(invoiceItemArticleTaxRate)
		double grossPrice = Double.valueOf(invoiceItemArticleGrossPrice)
		InvoiceItem newInvoiceItem = new InvoiceItem()
		newInvoiceItem.setInvoice(invoice);
		newInvoiceItem.setArticle(invoiceItemArticle)
		newInvoiceItem.setComment(invoiceItemComment)
		newInvoiceItem.setCount(count)
		newInvoiceItem.setGrossPrice(grossPrice)
		newInvoiceItem.setTaxRate(taxRate)
		newInvoiceItem.setReduced(reduced)
		newInvoiceItem.save flush:true
		flash.typ = "success"
		flash.message = "Der Rechnungsposten wurde erfolgreich angelegt"
		if(newInvoiceItem.errors.errorCount > 0) {
			if(newInvoiceItem.errors.toString().contains("unique")) {
				flash.typ = "error"
				flash.message = "Ein identischer Rechnungsposten existiert bereits in dieser Rechnung (bitte Anzahl beim vorhandenen erhöhen)"
			} else {
				flash.typ = "error"
				flash.message = newInvoiceItem.errors.toString()
			}
		}
		render(template: "/invoiceItem/modalBody", model: [invoiceInstance: invoice])	
	}
	
	def getInvoiceItemList() {
		Invoice invoiceId = Invoice.get(Integer.parseInt(params.get("invoiceId")))
		if(!invoiceId) {
			flash.typ = "error"
			flash.message = "Es wurde keine Rechnung übermittel, oder diese existiert nicht."
		}
		render (view: "modalContent", model: [invoiceInstance: invoiceId])
	}
	
	@Transactional
	def deleteInvoiceItem() {
		InvoiceItem invoiceItemToDelete = InvoiceItem.get(Integer.parseInt(params.get("invoiceItemId")))
		if (invoiceItemToDelete == null) {
			flash.typ = "error"
			flash.message = "Der zu löschende Rechnungsposten existiert nicht"
		} else {
			flash.typ = "success"
			flash.message = "Der Rechnungsposten  wurde erfolgreich gelöscht"
			invoiceItemToDelete.delete flush:true
		}
		if(invoiceItemToDelete.errors.errorCount > 0) {
			flash.typ = "error"
			flash.message = invoiceItemToDelete.errors.toString()
		}
		render(template: "/invoiceItem/modalBody", model: [invoiceInstance: invoiceItemToDelete.getInvoice()])
	}
	
	def updateInvoiceItem() {
		InvoiceItem invoiceItemToUpdate = InvoiceItem.get(params.get("invoiceItemId"))
		if (invoiceItemToUpdate == null) {
			flash.typ = "error"
			flash.message = "Der zu bearbeitende Rechnungsposten existiert nicht"
			render(template: "/invoiceItem/modalBody", model: [invoiceInstance: invoiceItemToUpdate.getInvoice()])
		} else {
		render(template: "/invoiceItem/modalBody", model: [invoiceInstance: invoiceItemToUpdate.getInvoice(), invoiceItemToEdit: invoiceItemToUpdate.getId()])
		}
	}
	
	@Transactional
	def saveInvoiceItem() {
		// Hole Parameter
		InvoiceItem invoiceItemToUpdate = InvoiceItem.get(Integer.parseInt(params.get("invoiceItemId")))
		Article invoiceItemArticle = Article.get(Integer.parseInt(params.get("invoiceItemArticleId")))
		String invoiceItemComment = params.get("invoiceItemComment")
		String invoiceItemCount = params.get("invoiceItemCount")
		String invoiceItemArticleGrossPrice = params.get("invoiceItemArticleGrossPrice")
		String invoiceItemArticleTaxRate = params.get("invoiceItemArticleTaxRate")
		String invoiceItemReduced = params.get("invoiceItemReduced")
		// Setze Standardwerte
		if(!invoiceItemComment ||invoiceItemComment.isEmpty()) {
			invoiceItemComment = 'n/a'
		}
		if(!invoiceItemArticleTaxRate || invoiceItemArticleTaxRate.isEmpty()) {
			invoiceItemArticleTaxRate = '19.00'
		}
		if(!invoiceItemCount || invoiceItemCount.isEmpty()) {
			invoiceItemCount = '1.00'
		}
		if(!invoiceItemReduced || invoiceItemReduced.isEmpty()) {
			invoiceItemReduced = 'false'
		} else {
			if(invoiceItemReduced.equals("on")) {
				invoiceItemReduced = "true"
			}
		}
		if(invoiceItemToUpdate == null) {
			flash.typ = "error"
			flash.message = "Der zu aktualisierende Rechnungsposten wurde nicht übermittelt oder existiert nicht"
			render(template: "/invoiceItem/modalBody")
			return
		}
		// Überprüfe übergebene Parameter für InvoiceItem
		def validateResponse = invoiceItemService.validateInvoiceItem(invoiceItemToUpdate?.getInvoice(), invoiceItemArticle, invoiceItemComment, invoiceItemCount,
			invoiceItemArticleGrossPrice, invoiceItemArticleTaxRate, invoiceItemReduced)
		if(validateResponse.error) {
			flash.typ = validateResponse.typ
			flash.message = validateResponse.message
			render(template: "/invoiceItem/modalBody", model: [invoiceInstance: invoiceItemToUpdate?.getInvoice()])
			return
		}
		// Sollte keine Fehler werfen, wurde zuvor mit RegExp geprüft
		boolean reduced = new Boolean(invoiceItemReduced);
		double count = Double.valueOf(invoiceItemCount)
		double taxRate = Double.valueOf(invoiceItemArticleTaxRate)
		double grossPrice = Double.valueOf(invoiceItemArticleGrossPrice)
		invoiceItemToUpdate.setArticle(invoiceItemArticle)
		invoiceItemToUpdate.setComment(invoiceItemComment)
		invoiceItemToUpdate.setCount(count)
		invoiceItemToUpdate.setGrossPrice(grossPrice)
		invoiceItemToUpdate.setTaxRate(taxRate)
		invoiceItemToUpdate.setReduced(reduced)
		invoiceItemToUpdate.save flush:true
		flash.typ = "success"
		flash.message = "Der Rechnungsposten wurde erfolgreich geändert"
		if(invoiceItemToUpdate.errors.errorCount > 0) {
			if(invoiceItemToUpdate.errors.toString().contains("unique")) {
				flash.typ = "error"
				flash.message = "Ein identischer Rechnungsposten existiert bereits in dieser Rechnung (bitte Anzahl beim vorhandenen erhöhen)"
			} else {
				flash.typ = "error"
				flash.message = invoiceItemToUpdate.errors.toString()
			}
		}
		render(template: "/invoiceItem/modalBody", layout: "ajaxInvoiceItem", model: [invoiceInstance: invoiceItemToUpdate.getInvoice()])
	}
}