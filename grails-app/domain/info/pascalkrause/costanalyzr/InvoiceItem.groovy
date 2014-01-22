package info.pascalkrause.costanalyzr

import java.io.Serializable;

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class InvoiceItem implements Serializable {
	
	static belongsTo = [invoice: Invoice]

   	static mapping = {
		table 'public.invoice_item'
		version false
        id generator:'increment', column:'id'
		invoice column: 'invoice_id'
		article column:'article_id'
		count column: 'count'
		taxRate column: 'tax_rate'
		grossPrice column: 'gross_price'
		reduced column: 'reduced'
		// Die Spalte comment erhält im Datenbankschema den Standardwert n/a
		// Wird hier im GORM der Wert "defaultValue : 'n/a' eingetragen gibt es einen Fehler
		// Wird ein anderes Datenbankschema verwendet kann dies zu unerwarteten Problemen führen
		comment column: 'comment'
   }
	
	Integer id
	Invoice invoice
	Article article
	double count
	double taxRate
	double grossPrice
	boolean reduced
	String comment
	
	/**
	 * Gehört ein Rechnungsposten der selben Rechnung an, so muss dieser sich mindestens in einem der folgenden Attribute von anderen unterscheiden. <br>
	 * Artikel<br>
	 * Steuersatz<br>
	 * Bruttorpeis<br>
	 * reduziert<br>
	 * Kommentar
	 * 
	 */
    static constraints = {
		invoice nullable: false, unique: ['article', 'taxRate', 'grossPrice', 'reduced', 'comment']
		article nullable: false
		count blank: false
		taxRate blank:false
		grossPrice blank:false
		reduced blank:false
    }
	
	@Override
	public String toString() {
		String stringToReturn =  /Rechnung: #${invoice?.id} - Artikelname: ${article?.getName()} - Anzahl: ${count} - MwSt: ${taxRate} - Preis: ${grossPrice} - Reduziert: ${reduced} - Kommentar: ${comment}/
		return stringToReturn
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof InvoiceItem &&
			obj.invoice.equals(this.invoice) &&
			obj.article.equals(this.article) &&
			obj.taxRate.equals(this.taxRate) &&
			obj.grossPrice.equals(this.grossPrice) &&
			obj.reduced.equals(this.reduced) &&
			obj.comment.equals(this.comment)) {
			return true;
		} else {
			return false;
		}
	}
}