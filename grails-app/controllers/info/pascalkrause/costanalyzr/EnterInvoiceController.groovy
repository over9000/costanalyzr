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
class EnterInvoiceController {
	
	def storeService
	def invoiceService
	def invoiceItemService
	
	def createInvoice() {
		redirect(action: "buildInvoice")
	}
	
	//Transactional?
	def buildInvoiceFlow =  {
		chooseConcern {
			on("next").to("validateConcern")
			on("cancel").to("cancel")
		}
		
		validateConcern {
			action {
				if(!params.buildInvoiceFlowConcernId.equals("noSelection")) {
					def choosenConcern = Concern.get(Integer.parseInt(params.buildInvoiceFlowConcernId))
					if(!choosenConcern) {
						flash.typ = "error"
						flash.message = "Der übermittelte Konzern existiert nicht"
						invalid()
					} else {
						flow.choosenConcern = choosenConcern;
						valid()
					}
				} else {
					Concern newConcern = new Concern()
					newConcern.setName(params.buildInvoiceFlowNewConcernName)
					newConcern.save flush:true
					if(newConcern.errors.errorCount > 0) {
						if(newConcern.errors.toString().contains("unique")) {
							flash.typ = "error"
							flash.message = "Ein Konzern mit dem Name ${newConcern.getName()} existiert bereits"
						} else {
							flash.typ = "error"
							flash.message = newConcern.errors.toString()
						}
						invalid()
					} else {
						flash.typ = "success"
						flash.message = "Ein neuer Konzern mit dem Namen ${newConcern.getName()} wurde angelegt"
						flow.choosenConcern = newConcern;
						valid()
					}
				}
			}
			on("valid"){
				if(flash.isEmpty()) {
					[flow: flow]
				} else {
					[flow: flow, flash: flash]
				}
			}.to("chooseStore")
			
			on("invalid"){ [flow: flow, flash: flash] }.to("chooseConcern")
		}
		
		chooseStore {			
			on("next").to("validateStore")
			on("back").to("chooseConcern")
			on("cancel").to("cancel")
		}
		
		validateStore {
			action {
				//Der Wert noSelection kommt aus StoreController Methode getStoreListAsJSON
				if(!params.buildInvoiceFlowStoreId.equals("noSelection")) {
					def choosenStore = Store.get(Integer.parseInt(params.buildInvoiceFlowStoreId))
					if(!choosenStore) {
						flash.typ = "error"
						flash.message = "Die übermittelte Filiale existiert nicht"
						invalid()
					}
					flow.choosenStore = choosenStore;
					valid()
				} else {
					Concern storeConcern = flow.choosenConcern
					String storeStreetName = params.buildInvoiceFlowNewStoreStreetName
					String storeStreetNumber = params.buildInvoiceFlowNewStoreStreetNumber
					String storeCountryAsString = params.buildInvoiceFlowNewStoreCountry
					String storeCity = params.buildInvoiceFlowNewStoreCity
					String storeZipcode = params.buildInvoiceFlowNewStoreZipcode
					// Überprüfe übergebene Parameter für Store
					def validateStoreResponse = storeService.validateStore(storeConcern, storeStreetName, storeStreetNumber, storeCountryAsString, storeCity, storeZipcode)
					if(validateStoreResponse.error) {
						flash.typ = validateStoreResponse.typ
						flash.message = validateStoreResponse.message
						invalid()
					} else {
						AvailableCountrys storeCountry
						try { storeCountry = AvailableCountrys.valueOf(storeCountryAsString) } catch(Exception e) { /*Würde in storeService.validateStore() abgefangen werden*/ }
						Store newStore = new Store()
						newStore.setCity(storeCity)
						newStore.setZipcode(storeZipcode)
						newStore.setConcern(storeConcern)
						newStore.setStreetName(storeStreetName)
						newStore.setStreetNumber(storeStreetNumber)
						newStore.setCountry(storeCountry)
						newStore.save flush:true
						flash.typ = "success"
						flash.message = "Die Filiale ${newStore} wurde angelegt"
						if(newStore.errors.errorCount > 0) {
							if(newStore.errors.toString().contains("unique")) {
								flash.typ = "error"
								flash.message = "Eine identische Filiale existiert bereits}"
								invalid()
							} else {
								flash.typ = "error"
								flash.message = newStore.errors.toString()
								invalid()
							}
						}
						flow.choosenStore = newStore;
						valid()
					}
				}
			}
			on("valid"){
				if(flash.isEmpty()) {
					[flow: flow]
				} else {
					[flow: flow, flash: flash]
				}
			}.to("createInvoice")
			on("invalid"){ [flow: flow, flash: flash] }.to("chooseStore")
		}
		
		createInvoice{
			on("next").to("validateInvoice")
			on("back").to("chooseStore")
			on("cancel").to("cancel")
		}

		validateInvoice{
			action {
				Store invoiceStore = Store.get(flow.choosenStore.getId())
				String invoiceCreationDate = params.invoiceCreationDate
				String invoicePaymentMethodAsString = params.invoicePaymentMethod
				// Überprüfe übergebene Parameter für Invoice
				def validateResponse = invoiceService.validateInvoice(invoiceStore, invoiceCreationDate, invoicePaymentMethodAsString);
				if(validateResponse.error) {
					flash.typ = validateResponse.typ
					flash.message = validateResponse.message
					invalid()
				} else {
					try {
						PaymentMethods invoicePaymentMethod
						invoicePaymentMethod = PaymentMethods.valueOf(invoicePaymentMethodAsString)
						def creationDate = new GregorianCalendar();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						creationDate.setTime(sdf.parse(invoiceCreationDate));
						Invoice newInvoice = new Invoice()
						flash.typ = "success"
						flash.message = "Die Rechnung ${newInvoice} wurde angelegt"
						// Damit nicht immer eine neue Rechnung angelegt wird
						if(flow?.invoiceId) {
							newInvoice = Invoice.get(flow?.invoiceId)
							flash.typ = "success"
							flash.message = "Die Rechnung wurde geändrt zu ${newInvoice}"
						}
						newInvoice.setStore(invoiceStore)
						newInvoice.setPaymentMethod(invoicePaymentMethod)
						newInvoice.setCreationDate(creationDate)
						newInvoice.save flush:true
						if(newInvoice.errors.errorCount > 0) {
							flash.typ = "error"
							flash.message = newInvoice.errors.toString()
						}
						flow.invoiceId = newInvoice.getId();
						valid()
					} catch(Exception e) {
					 	// Eigentlich müssten alle Fehleingaben in der invoiceService.validateInvoice() Methode abgefangen werden
						flash.typ = "error"
						flash.message = "${e.toString()}"
						invalid()
					}
				}				
			}
			on("valid"){
				if(flash.isEmpty()) {
					[flow: flow]
				} else {
					[flow: flow, flash: flash]
				}
			}.to("addInvoiceItems")
			on("invalid"){ [flow: flow, flash: flash] }.to("createInvoice")
		}
		
		addInvoiceItems{
			on("addInvoiceItem").to("validateAddInvoiceItem")
			on("next"){ [flow: flow] }.to("overview")
			on("back").to("createInvoice")
			on("deleteItem").to("deleteInvoiceItems")
			on("cancel").to("cancel")
			
		}
		
		deleteInvoiceItems {
			action {
				InvoiceItem invoiceItemToDelete = InvoiceItem.get(Integer.parseInt(params.invoiceItemToDeleteId))
				if(invoiceItemToDelete) {
					invoiceItemToDelete.delete flush:true
					if(invoiceItemToDelete.errors.errorCount > 0) {
						flash.typ = "error"
						flash.message = invoiceItemToDelete.errors.toString()
						invalid()
					} else {
						flash.typ = "success"
						flash.message = "Der Rechnungsposten wurde erfolgreich gelöscht}"
						def invoiceItemsCriteria = InvoiceItem.createCriteria()
						flow.invoiceItems = invoiceItemsCriteria.list {
								 eq("invoice", invoiceItemToDelete.getInvoice())
						}
						valid()
					}
				} else {
					flash.typ = "error"
					flash.message = "Der zu löschende Rechnungsposten wurde nicht übermittelt, oder existiert nicht"
					invalid()
				}
			}
			on("valid"){ [flow: flow, flash: flash] }.to("addInvoiceItems")
			on("invalid"){ [flow: flow, flash: flash] }.to("addInvoiceItems")
		}
		
		validateAddInvoiceItem {
			action {
				Invoice invoice = Invoice.get(flow.invoiceId)
				Article invoiceItemArticle = Article.get(Integer.parseInt(params.invoiceItemArticleId))
				String invoiceItemComment = params.invoiceItemComment
				String invoiceItemCount = params.invoiceItemCount
				String invoiceItemArticleGrossPrice = params.invoiceItemArticleGrossPrice
				String invoiceItemArticleTaxRate = params.invoiceItemArticleTaxRate
				String invoiceItemReduced = params.invoiceItemReduced
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
					invalid()
				} else {
					//Umwandlung müsste gefahrenlos sein, weil alle Werte in der Methode invoiceItemService.validateInvoiceItem überprüft werden
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
					if(newInvoiceItem.errors.errorCount > 0) {
						if(newInvoiceItem.errors.toString().contains("unique")) {
							flash.typ = "error"
							flash.message = "Ein identischer Rechnungsposten existiert bereits in dieser Rechnung (bitte löschen und erneut mit korrekter Anzahl eingeben)"
							invalid()
						} else {
							flash.typ = "error"
							flash.message = newInvoiceItem.errors.toString()
							invalid()
						}
					} else {
						flash.typ = "success"
						flash.message = "Der Rechnungsposten ${newInvoiceItem.toString()} wurde erfolgreich angelegt"
						def invoiceItemsCriteria = InvoiceItem.createCriteria()
						flow.invoiceItems = invoiceItemsCriteria.list { 
								 eq("invoice", invoice)
						}
						valid()
					}
				}
			}
			on("valid"){ [flow: flow, flash: flash] }.to("addInvoiceItems")
			on("invalid"){ [flow: flow, flash: flash] }.to("addInvoiceItems")
		}
		
		overview {
			on("back").to("addInvoiceItems")
			on("done").to("cancel")
			on("addNewInvoice").to("addNewInvoice")
		}
		
		cancel {
			redirect(action: 'showDataInputHome')
		}
		
		addNewInvoice {
			redirect(action: 'createInvoice')
		}
	}
	
	def showDataInputHome() {
		flash.typ = params.typ;
		flash.message = params.message;
		render(view: "/dataInputHome")
	}
}