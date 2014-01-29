package info.pascalkrause.costanalyzr

import java.io.Serializable;
import java.text.SimpleDateFormat

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class Invoice implements Serializable {

	static hasMany = [invoiceItems: InvoiceItem]
	
	static mapping = {
		cache usage:'read-only'
		table 'public.invoice'
		version false
		id generator:'increment', column:'id'
		store column: 'store_id'
		creationDate column:'creation_date'
		paymentMethod column: 'payment_method', type: PGEnumUserType, params : [ enumClassName: "info.pascalkrause.costanalyzr.PaymentMethods"]
	}
	
	Integer id
	Store store
	GregorianCalendar creationDate
	PaymentMethods paymentMethod

	/**
	 * Es kann beliebig viele identische Rechnungen geben 
	 */
	static constraints = {
		store nullable: false
		creationDate blank: false
		paymentMethod blank:false
	}

	/**
	 * Gibt das CreationDate als String zurück im Format "yyyy-MM-dd"
	 * @return Das CreationDate als String zurück im Format "yyyy-MM-dd", ist das CreationDate null wird "kein Erstellungsdatum" zurückgegeben
	 */
	public String getCreationDateAsString() {
		if(creationDate) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(creationDate.getTime())
		} else {
			return "kein Erstellungsdatum"
		}
	}
	
	/**
	 * Gibt den gesamten Bruttopreis einer Rechnung zurück. (Bruttopreis der Rechnungsposten * Anzahl) 
	 * @return Den gesamten Bruttopreis einer Rechnung zurück. (Bruttopreis der Rechnungsposten * Anzahl) 
	 */
	public double getTotalGrossPrice() {
		double totalGrossPrice = 0;
		
		//Das createCriteria wird benötigt, da im Webflow nicht auf Invoice.invoiceItems zugegriffen werden kann
		def invoiceItemsCriteria = InvoiceItem.createCriteria()
		List<InvoiceItem> tempInvoiceItems = invoiceItemsCriteria.list {
			eq("invoice", this)
		}
		tempInvoiceItems.each {
			totalGrossPrice = totalGrossPrice + (it.count * it.grossPrice)
		}
		return totalGrossPrice
	}
	
	/**
	 * Hier wird nur das Datum und nicht eine spezielle Rechnung zurückgegeben, da es mehrere Rechnungen mit dem selben Datum geben kann.
	 * @return the creation date of the latest invoice. If there is no Invoice this method returns null
	 */
	static public GregorianCalendar getLatestInvoiceCreationDate() {
		GregorianCalendar returnDate = null;
		Invoice.list().each {
			if(!returnDate || returnDate.compareTo(it.getCreationDate()) < 0  ) {
				returnDate = it.getCreationDate()
			}
		}
		return returnDate
	}
	
	/**
	 * Hier wird nur das Datum und nicht eine spezielle Rechnung zurückgegeben, da es mehrere Rechnungen mit dem selben Datum geben kann.
	 * @return the creation date of the oldest invoice. If there is no Invoice this method returns null
	 */
	static public GregorianCalendar getOldestInvoiceCreationDate() {
		GregorianCalendar returnDate = null;
		Invoice.list().each {
			if(!returnDate || returnDate.compareTo(it.getCreationDate()) > 0  ) {
				returnDate = it.getCreationDate()
			}
		}
		return returnDate
	}
	
	/**
	 * @param from Dateformat is yyyy-MM-dd
	 * @param to Dateformat is yyyy-MM-dd
	 * @return A List with all Invoices between the passed dates. If there is no Invoice this method returns an empty List
	 */
	static public List<Invoice> getAllInvoicesBetween(String from, String to) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");		
		return getAllInvoicesBetween(sdf.parse(from).toCalendar(), sdf.parse(to).toCalendar())
	}
	
	/**
	 * @param from A Date
	 * @param to A Date
	 * @return A List with all Invoices between the passed dates. If there is no Invoice this method returns an empty List
	 */
	static public List<Invoice> getAllInvoicesBetween(Date from, Date to) {
		return getAllInvoicesBetween(from.toCalendar(), to.toCalendar())
	}
	
	/**
	 * @param from A GregorianCalendar
	 * @param to A GregorianCalendar
	 * @return A List with all Invoices between the passed dates. If there is no Invoice this method returns an empty List
	 */
	static public List<Invoice> getAllInvoicesBetween(GregorianCalendar from, GregorianCalendar to) {
		List<Invoice> invoicesToReturn = new ArrayList<Invoice>()
		Invoice.list(cache: true).each {
			//kleiner als 0 wenn die zeit von it kleiner als die von from is
			if(it.getCreationDate().compareTo(from) >= 0 && it.getCreationDate().compareTo(to) <= 0) {
				invoicesToReturn.add(it)
			}
		}
		return invoicesToReturn
	}
	
	/**
	 * Store + CreationDate + PaymentMethod
	 * @return
	 */
	@Override
	public String toString() {
		String stringToReturn = /${this.getStore()} - ${this.getCreationDateAsString()} - ${this.paymentMethod}/
		return stringToReturn
	}
}