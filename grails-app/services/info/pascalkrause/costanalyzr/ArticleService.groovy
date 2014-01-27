package info.pascalkrause.costanalyzr

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class ArticleService {

    def validateArticle(Category articleCategory, String articleName, String articleStandardTaxRate, String articleStandardGrossPrice) {

		Expando validateResponse = new Expando()
		validateResponse.error = false;
		
		if(articleCategory == null) {
			validateResponse.typ = "error"
			validateResponse.message = "Die Kategorie des Artikels wurde nicht übermittelt, oder existiert nicht"
			validateResponse.error = true;
		}
		if(!articleName || articleName.isEmpty()) {
			validateResponse.typ = "error"
			validateResponse.message = "Es wurde kein Artikelname übertragen"
			validateResponse.error = true;
		}
		if(!(articleStandardTaxRate ==~ /^[0-9]{1,2}([.][0-9]{1,2})?/)) {
			validateResponse.typ = "error"
			validateResponse.message = "Der Standard MwST-Satz hat ein falsches Format." + / (^[0-9]{1,2}([.][0-9]{1,2})?)/
			validateResponse.error = true;
		}
		if(!(articleStandardGrossPrice ==~ /^[-]?[0-9]{1,16}([.][0-9]{1,2})?/)) {
			validateResponse.typ = "error"
			validateResponse.message = "Der Standard Bruttopreis hat ein falsches Format." + / (^[-]?[0-9]{1,16}([.][0-9]{1,2})?)/
			validateResponse.error = true;
		}
		return validateResponse
	}
}