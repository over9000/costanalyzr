package info.pascalkrause.costanalyzr

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class InvoiceService {

	def validateInvoice(Store invoiceStore, String invoiceCreationDate, String invoicePaymentMethodAsString) {

		Expando validateResponse = new Expando()
		validateResponse.error = false;
		PaymentMethods invoicePaymentMethod
		try {
			invoicePaymentMethod = PaymentMethods.valueOf(invoicePaymentMethodAsString)
		} catch(Exception e) {
			validateResponse.typ = "error"
			validateResponse.message = "Es wurde keine gültige Zahlungsart übermittelt"
			validateResponse.error = true;
		}
		if(invoiceStore == null) {
			validateResponse.typ = "error"
			validateResponse.message = "Die zugehörige Filiale wurde nicht übermittelt oder existiert nicht"
			validateResponse.error = true;
		}
		if(!(invoiceCreationDate ==~ /^[0-9]{4}[-](0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])/)) {
			validateResponse.typ = "error"
			validateResponse.message = "Das Datum hat ein falsches Format." + /^[0-9]{4}[-](0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])/
			validateResponse.error = true;
		}
		return validateResponse
	}
}