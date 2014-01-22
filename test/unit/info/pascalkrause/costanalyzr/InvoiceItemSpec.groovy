package info.pascalkrause.costanalyzr

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class InvoiceItemSpec {

    def setup() {
    }

    def cleanup() {
    }

    void "test method equals()"() {
		
		Concern firstConcern = new Concern()
		firstConcern.setName("Konzern Foo bar")
		
		Store firstStore = new Store()
		String city = "Berlin"
		String zipCode = "11111"
		String streetName = "Foostraße"
		String streetNumber = "23"
		
		AvailableCountrys country = AvailableCountrys.DE
		firstStore.setConcern(firstConcern)
		firstStore.setCountry(country);
		firstStore.setCity(city)
		firstStore.setZipcode(zipCode)
		firstStore.setStreetName(streetName)
		firstStore.setStreetNumber(streetNumber)
		
		Invoice firstInvoice = new Invoice();
		GregorianCalendar creationDateOfFirstInvoice = (new Date().parse("yyyy-MM-dd", "2013-01-01")).toCalendar()
		firstInvoice.setStore(firstStore)
		firstInvoice.setCreationDate(creationDateOfFirstInvoice)
		firstInvoice.setPaymentMethod(PaymentMethods.CASH)
		
		Category categoryFood = new Category()
		categoryFood.setName("Lebensmittel")
		
		Article pizza = new Article()
		pizza.setName("Pizza")
		pizza.setCategory(categoryFood)
		pizza.setStandardGrossPrice(1.10)
		pizza.setStandardTaxRate(7.0)
		
		double count = 1
		String comment = "foobar"
		boolean reduced = false;
		
		InvoiceItem firstInvoiceItem = new InvoiceItem()
		firstInvoiceItem.setInvoice(firstInvoice)
		firstInvoiceItem.setArticle(pizza);
		firstInvoiceItem.setCount(count)
		firstInvoiceItem.setComment(comment)
		firstInvoiceItem.setTaxRate(pizza.getStandardTaxRate())
		firstInvoiceItem.setGrossPrice(pizza.getStandardGrossPrice())
		firstInvoiceItem.setReduced(reduced)
		
		assertEquals(true, firstInvoiceItem.equals(firstInvoiceItem))
		
		Article chips = new Article()
		chips.setName("Chips")
		chips.setCategory(categoryFood)
		chips.setStandardGrossPrice(0.85)
		chips.setStandardTaxRate(7.0)
		
		InvoiceItem secondInvoiceItem = new InvoiceItem()
		secondInvoiceItem.setInvoice(firstInvoice)
		secondInvoiceItem.setArticle(chips);
		secondInvoiceItem.setCount(count)
		secondInvoiceItem.setComment(comment)
		secondInvoiceItem.setTaxRate(pizza.getStandardTaxRate())
		secondInvoiceItem.setGrossPrice(pizza.getStandardGrossPrice())
		secondInvoiceItem.setReduced(reduced)

		assertEquals(false, secondInvoiceItem.equals(firstInvoiceItem))
		
		InvoiceItem thirdInvoiceItem = new InvoiceItem()
		thirdInvoiceItem.setInvoice(firstInvoice)
		thirdInvoiceItem.setArticle(chips);
		thirdInvoiceItem.setCount(count)
		thirdInvoiceItem.setComment(comment + "1")
		thirdInvoiceItem.setTaxRate(pizza.getStandardTaxRate())
		thirdInvoiceItem.setGrossPrice(pizza.getStandardGrossPrice())
		thirdInvoiceItem.setReduced(reduced)

		assertEquals(false, thirdInvoiceItem.equals(firstInvoiceItem))
		assertEquals(false, thirdInvoiceItem.equals(secondInvoiceItem))
		
		InvoiceItem fourthInvoiceItem = new InvoiceItem()
		fourthInvoiceItem.setInvoice(firstInvoice)
		fourthInvoiceItem.setArticle(chips);
		fourthInvoiceItem.setCount(count)
		fourthInvoiceItem.setComment(comment + "1")
		fourthInvoiceItem.setTaxRate(pizza.getStandardTaxRate() + 1)
		fourthInvoiceItem.setGrossPrice(pizza.getStandardGrossPrice())
		fourthInvoiceItem.setReduced(reduced)
		

		assertEquals(false, fourthInvoiceItem.equals(firstInvoiceItem))
		assertEquals(false, fourthInvoiceItem.equals(secondInvoiceItem))
		assertEquals(false, fourthInvoiceItem.equals(thirdInvoiceItem))
		
		InvoiceItem fithInvoiceItem = new InvoiceItem()
		fithInvoiceItem.setInvoice(firstInvoice)
		fithInvoiceItem.setArticle(chips);
		fithInvoiceItem.setCount(count)
		fithInvoiceItem.setComment(comment + "1")
		fithInvoiceItem.setTaxRate(pizza.getStandardTaxRate() + 1)
		fithInvoiceItem.setTaxRate(pizza.getStandardGrossPrice() + 1)
		fithInvoiceItem.setReduced(reduced)
		

		assertEquals(false, fithInvoiceItem.equals(firstInvoiceItem))
		assertEquals(false, fithInvoiceItem.equals(secondInvoiceItem))
		assertEquals(false, fithInvoiceItem.equals(thirdInvoiceItem))
		assertEquals(false, fithInvoiceItem.equals(fourthInvoiceItem))
		
		InvoiceItem sixthInvoiceItem = new InvoiceItem()
		sixthInvoiceItem.setInvoice(firstInvoice)
		sixthInvoiceItem.setArticle(chips);
		sixthInvoiceItem.setCount(count)
		sixthInvoiceItem.setComment(comment + "1")
		sixthInvoiceItem.setTaxRate(pizza.getStandardTaxRate() + 1)
		sixthInvoiceItem.setTaxRate(pizza.getStandardGrossPrice() + 1)
		sixthInvoiceItem.setReduced(!reduced)

		assertEquals(false, sixthInvoiceItem.equals(firstInvoiceItem))
		assertEquals(false, sixthInvoiceItem.equals(secondInvoiceItem))
		assertEquals(false, sixthInvoiceItem.equals(fourthInvoiceItem))
		assertEquals(false, sixthInvoiceItem.equals(fithInvoiceItem))
    }
	
	void "test method toString()"() {
		
		Concern firstConcern = new Concern()
		firstConcern.setName("Konzern Foo bar")
		
		Store firstStore = new Store()
		String city = "Berlin"
		String zipCode = "11111"
		String streetName = "Foostraße"
		String streetNumber = "23"
		
		AvailableCountrys country = AvailableCountrys.DE
		firstStore.setConcern(firstConcern)
		firstStore.setCountry(country);
		firstStore.setCity(city)
		firstStore.setZipcode(zipCode)
		firstStore.setStreetName(streetName)
		firstStore.setStreetNumber(streetNumber)
		
		Invoice firstInvoice = new Invoice();
		GregorianCalendar creationDateOfFirstInvoice = (new Date().parse("yyyy-MM-dd", "2013-01-01")).toCalendar()
		firstInvoice.setStore(firstStore)
		firstInvoice.setCreationDate(creationDateOfFirstInvoice)
		firstInvoice.setPaymentMethod(PaymentMethods.CASH)
		
		Category categoryFood = new Category()
		categoryFood.setName("Lebensmittel")
		
		Article pizza = new Article()
		pizza.setName("Pizza")
		pizza.setCategory(categoryFood)
		pizza.setStandardGrossPrice(1.10)
		pizza.setStandardTaxRate(7.0)
		
		InvoiceItem firstInvoiceItem = new InvoiceItem()
		double count = 1
		String comment = "foobar"
		boolean reduced = false;
		firstInvoiceItem.setInvoice(firstInvoice)
		firstInvoiceItem.setArticle(pizza);
		firstInvoiceItem.setCount(count)
		firstInvoiceItem.setComment(comment)
		firstInvoiceItem.setTaxRate(pizza.getStandardTaxRate())
		firstInvoiceItem.setGrossPrice(pizza.getStandardGrossPrice())
		firstInvoiceItem.setReduced(reduced)
		
		String firstInvoiceItemString =  /Rechnung: #${firstInvoice?.getId()} - Artikelname: ${pizza?.getName()} - Anzahl: ${count} - MwSt: ${firstInvoiceItem.getTaxRate()} - Preis: ${firstInvoiceItem.getGrossPrice()} - Reduziert: ${firstInvoiceItem.getReduced()} - Kommentar: ${firstInvoiceItem.getComment()}/
		
		assertEquals(firstInvoiceItemString, firstInvoiceItem.toString())
	}
}