package info.pascalkrause.costanalyzr

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class CostsAnalyzerService {
	
	public double totalCostsByTimePeriodAndCategories(GregorianCalendar fromDate, GregorianCalendar toDate, List<Category> categories) {
		def invoices = Invoice.getAllInvoicesBetween(fromDate, toDate)
		double totalCosts = 0
		invoices.each {
			it.invoiceItems.each {
				InvoiceItem currentInvoiceItem = it;
				Category currentInvoiceItemCategory = currentInvoiceItem.getArticle().getCategory();
				categories.each {
					if(currentInvoiceItemCategory.isChildOf(it) || currentInvoiceItemCategory.equals(it)) {
						totalCosts = totalCosts + (currentInvoiceItem.getCount() * currentInvoiceItem.getGrossPrice())
					}
				}
			}
		}
		return Utils.cutDecimal(totalCosts)
	}
}