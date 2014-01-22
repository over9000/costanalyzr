package info.pascalkrause.costanalyzr

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class InvoiceItemService {

	def validateInvoiceItem(Invoice invoice, Article invoiceItemArticle, String invocieItemComment,
		String invoiceItemCount, String invoiceItemArticleGrossPrice, String invoiceItemArticleTaxRate, String invoiceItemReduced) {

		Expando validateResponse = new Expando()
		validateResponse.error = false;
		
		if(invoice == null) {
			validateResponse.typ = "error"
			validateResponse.message = "Die Rechnung der anzuzeigenden Rechnungsposten wurde nicht übermittelt oder existiert nicht"
			validateResponse.error = true;
		}
		if(invoiceItemArticle == null) {
			validateResponse.typ = "error"
			validateResponse.message = "Der Artikel des Rechnungsposten wurde nicht übermittelt oder existiert nicht"
			validateResponse.error = true;
		}
		if(!invoiceItemArticleGrossPrice || invoiceItemArticleGrossPrice.isEmpty()) {
			validateResponse.typ = "error"
			validateResponse.message = "Es wurde kein Bruttopreis übertragen"
			validateResponse.error = true;
		}
		if(!(invoiceItemArticleTaxRate ==~ /^[0-9]{1,2}([.][0-9]{1,2})?/)) {
			validateResponse.typ = "error"
			validateResponse.message = "Der MwST-Satz hat ein falsches Format." + / (^[0-9]{1,2}([.][0-9]{1,2})?)/
			validateResponse.error = true;
		}
		if(!(invoiceItemArticleGrossPrice ==~ /^[-]?[0-9]{1,16}([.][0-9]{1,2})?/)) {
			validateResponse.typ = "error"
			validateResponse.message = "Der Bruttopreis hat ein falsches Format." + / (^[-]?[0-9]{1,16}([.][0-9]{1,2})?)/
			validateResponse.error = true;
		}
		if(!(invoiceItemCount ==~ /^[-]?[0-9]{1,16}([.][0-9]{1,2})?/)) {
			validateResponse.typ = "error"
			validateResponse.message = "Die Anzahl hat ein falsches Format." + / (^[-]?[0-9]{1,16}([.][0-9]{1,2})?)/
			validateResponse.error = true;
		}
		return validateResponse
	}
}