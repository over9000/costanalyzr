package info.pascalkrause.costanalyzr

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class InvoiceSpec {

    def setup() {
    }

    def cleanup() {
    }

	void "test method getCreationDateAsString()"() {
		
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
		
		assertEquals("2013-01-01", firstInvoice.getCreationDateAsString())
	}
	
	void "test method getTotalGrossPrice()"() {
		
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
		
		def testInvoiceInstances=[]
		mockDomain(Invoice, testInvoiceInstances)
		
		Invoice firstInvoice = new Invoice();
		GregorianCalendar creationDateOfFirstInvoice = (new Date().parse("yyyy-MM-dd", "2013-01-01")).toCalendar()
		firstInvoice.setStore(firstStore)
		firstInvoice.setCreationDate(creationDateOfFirstInvoice)
		firstInvoice.setPaymentMethod(PaymentMethods.CASH)
		firstInvoice.save();
		
		Category categoryFood = new Category()
		categoryFood.setName("Lebensmittel")
		
		Article pizza = new Article()
		pizza.setName("Pizza")
		pizza.setCategory(categoryFood)
		pizza.setStandardGrossPrice(1.10)
		pizza.setStandardTaxRate(7.0)
		
		Article chips = new Article()
		chips.setName("Chips")
		chips.setCategory(categoryFood)
		chips.setStandardGrossPrice(0.85)
		chips.setStandardTaxRate(7.0)
		
		def testInvoiceItemInstances=[]
		mockDomain(InvoiceItem, testInvoiceItemInstances)
		
		double pizzaCount = 5.5
		InvoiceItem firstInvoiceItem = new InvoiceItem()
		firstInvoiceItem.setInvoice(firstInvoice)
		firstInvoiceItem.setArticle(pizza);
		firstInvoiceItem.setCount(pizzaCount)
		firstInvoiceItem.setComment("foobar")
		firstInvoiceItem.setTaxRate(pizza.getStandardTaxRate())
		firstInvoiceItem.setGrossPrice(pizza.getStandardGrossPrice())
		firstInvoiceItem.setReduced(false)
		firstInvoiceItem.save()
		
		double chipsCount = 2
		InvoiceItem secondInvoiceItem = new InvoiceItem()
		secondInvoiceItem.setInvoice(firstInvoice)
		secondInvoiceItem.setArticle(chips);
		secondInvoiceItem.setCount(chipsCount)
		secondInvoiceItem.setComment("foobar")
		secondInvoiceItem.setTaxRate(chips.getStandardTaxRate())
		secondInvoiceItem.setGrossPrice(chips.getStandardGrossPrice())
		secondInvoiceItem.setReduced(false)
		secondInvoiceItem.save()
		
		assertEquals((pizzaCount * pizza.getStandardGrossPrice()) + (chipsCount * chips.getStandardGrossPrice()), firstInvoice.getTotalGrossPrice())
	}
	
	void "test method getLatestInvoiceCreationDate()"() {
		
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
		firstInvoice.save();
		
		Invoice secondInvoice = new Invoice();
		GregorianCalendar creationDateOfSecondInvoice = (new Date().parse("yyyy-MM-dd", "2013-01-02")).toCalendar()
		secondInvoice.setStore(firstStore)
		secondInvoice.setCreationDate(creationDateOfSecondInvoice)
		secondInvoice.setPaymentMethod(PaymentMethods.CASH)
		secondInvoice.save();
		
		Invoice thirdInvoice = new Invoice();
		GregorianCalendar creationDateOfThirdInvoice = (new Date().parse("yyyy-MM-dd", "2014-11-29")).toCalendar()
		thirdInvoice.setStore(firstStore)
		thirdInvoice.setCreationDate(creationDateOfThirdInvoice)
		thirdInvoice.setPaymentMethod(PaymentMethods.CASH)
		thirdInvoice.save();
		
		Invoice fourthInvoice = new Invoice();
		GregorianCalendar creationDateOfFourthInvoice = (new Date().parse("yyyy-MM-dd", "2014-11-30")).toCalendar()
		fourthInvoice.setStore(firstStore)
		fourthInvoice.setCreationDate(creationDateOfFourthInvoice)
		fourthInvoice.setPaymentMethod(PaymentMethods.CASH)
		fourthInvoice.save();
		
		assertEquals(fourthInvoice.getCreationDate(), Invoice.getLatestInvoiceCreationDate())
	}
	
	void "test method getOldestInvoiceCreationDate()"() {
		
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
		firstInvoice.save();
		
		Invoice secondInvoice = new Invoice();
		GregorianCalendar creationDateOfSecondInvoice = (new Date().parse("yyyy-MM-dd", "2013-01-02")).toCalendar()
		secondInvoice.setStore(firstStore)
		secondInvoice.setCreationDate(creationDateOfSecondInvoice)
		secondInvoice.setPaymentMethod(PaymentMethods.CASH)
		secondInvoice.save();
		
		Invoice thirdInvoice = new Invoice();
		GregorianCalendar creationDateOfThirdInvoice = (new Date().parse("yyyy-MM-dd", "2014-11-29")).toCalendar()
		thirdInvoice.setStore(firstStore)
		thirdInvoice.setCreationDate(creationDateOfThirdInvoice)
		thirdInvoice.setPaymentMethod(PaymentMethods.CASH)
		thirdInvoice.save();
		
		Invoice fourthInvoice = new Invoice();
		GregorianCalendar creationDateOfFourthInvoice = (new Date().parse("yyyy-MM-dd", "2014-11-30")).toCalendar()
		fourthInvoice.setStore(firstStore)
		fourthInvoice.setCreationDate(creationDateOfFourthInvoice)
		fourthInvoice.setPaymentMethod(PaymentMethods.CASH)
		fourthInvoice.save();
		
		assertEquals(firstInvoice.getCreationDate(), Invoice.getOldestInvoiceCreationDate())
	}
	
	void "test method getAllInvoicesBetween()"() {
		
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
		firstInvoice.save();
		
		Invoice secondInvoice = new Invoice();
		GregorianCalendar creationDateOfSecondInvoice = (new Date().parse("yyyy-MM-dd", "2013-01-02")).toCalendar()
		secondInvoice.setStore(firstStore)
		secondInvoice.setCreationDate(creationDateOfSecondInvoice)
		secondInvoice.setPaymentMethod(PaymentMethods.CASH)
		secondInvoice.save();
		
		Invoice thirdInvoice = new Invoice();
		GregorianCalendar creationDateOfThirdInvoice = (new Date().parse("yyyy-MM-dd", "2014-11-29")).toCalendar()
		thirdInvoice.setStore(firstStore)
		thirdInvoice.setCreationDate(creationDateOfThirdInvoice)
		thirdInvoice.setPaymentMethod(PaymentMethods.CASH)
		thirdInvoice.save();
		
		Invoice fourthInvoice = new Invoice();
		GregorianCalendar creationDateOfFourthInvoice = (new Date().parse("yyyy-MM-dd", "2014-11-30")).toCalendar()
		fourthInvoice.setStore(firstStore)
		fourthInvoice.setCreationDate(creationDateOfFourthInvoice)
		fourthInvoice.setPaymentMethod(PaymentMethods.CASH)
		fourthInvoice.save();
		
		List<Invoice> listWithallInvoices = new ArrayList<Invoice>()
		listWithallInvoices.add(firstInvoice)
		listWithallInvoices.add(secondInvoice)
		listWithallInvoices.add(thirdInvoice)
		listWithallInvoices.add(fourthInvoice)
		
		String fromStringAllInvoices = "2013-01-01"
		String toStringAllInvoices = "2014-11-30"
		assertEquals(listWithallInvoices, Invoice.getAllInvoicesBetween(fromStringAllInvoices, toStringAllInvoices))
		Date fromDateAllInvoices = (new Date().parse("yyyy-MM-dd", fromStringAllInvoices))
		Date toDateAllInvoices = (new Date().parse("yyyy-MM-dd", toStringAllInvoices))
		assertEquals(true, listWithallInvoices.equals(Invoice.getAllInvoicesBetween(fromDateAllInvoices, toDateAllInvoices)))
		assertEquals(true, listWithallInvoices.equals(Invoice.getAllInvoicesBetween(fromDateAllInvoices.toCalendar(), toDateAllInvoices.toCalendar())))
		
		fromStringAllInvoices = "2012-01-01"
		toStringAllInvoices = "2015-11-30"
		assertEquals(listWithallInvoices, Invoice.getAllInvoicesBetween(fromStringAllInvoices, toStringAllInvoices))
		fromDateAllInvoices = (new Date().parse("yyyy-MM-dd", fromStringAllInvoices))
		toDateAllInvoices = (new Date().parse("yyyy-MM-dd", toStringAllInvoices))
		assertEquals(true, listWithallInvoices.equals(Invoice.getAllInvoicesBetween(fromDateAllInvoices, toDateAllInvoices)))
		assertEquals(true, listWithallInvoices.equals(Invoice.getAllInvoicesBetween(fromDateAllInvoices.toCalendar(), toDateAllInvoices.toCalendar())))
		
		fromStringAllInvoices = "2014-01-01"
		toStringAllInvoices = "2015-11-30"
		assertEquals(false, listWithallInvoices.equals(Invoice.getAllInvoicesBetween(fromStringAllInvoices, toStringAllInvoices)))
		fromDateAllInvoices = (new Date().parse("yyyy-MM-dd", fromStringAllInvoices))
		toDateAllInvoices = (new Date().parse("yyyy-MM-dd", toStringAllInvoices))
		assertEquals(false, listWithallInvoices.equals(Invoice.getAllInvoicesBetween(fromDateAllInvoices, toDateAllInvoices)))
		assertEquals(false, listWithallInvoices.equals(Invoice.getAllInvoicesBetween(fromDateAllInvoices.toCalendar(), toDateAllInvoices.toCalendar())))
		
		List<Invoice> listWithSecondAndThirdInvoice = new ArrayList<Invoice>()
		listWithSecondAndThirdInvoice.add(secondInvoice)
		listWithSecondAndThirdInvoice.add(thirdInvoice)
		
		String fromStringSecondAndThirdInvoice = "2013-01-02"
		String toStringSecondAndThirdInvoice = "2014-11-29"
		assertEquals(listWithSecondAndThirdInvoice, Invoice.getAllInvoicesBetween(fromStringSecondAndThirdInvoice, toStringSecondAndThirdInvoice))
		Date fromDateSecondAndThirdInvoice = (new Date().parse("yyyy-MM-dd", fromStringSecondAndThirdInvoice))
		Date toDateSecondAndThirdInvoice = (new Date().parse("yyyy-MM-dd", toStringSecondAndThirdInvoice))
		assertEquals(true, listWithSecondAndThirdInvoice.equals(Invoice.getAllInvoicesBetween(fromDateSecondAndThirdInvoice, toDateSecondAndThirdInvoice)))
		assertEquals(true, listWithSecondAndThirdInvoice.equals(Invoice.getAllInvoicesBetween(fromDateSecondAndThirdInvoice.toCalendar(), toDateSecondAndThirdInvoice.toCalendar())))
		
		fromStringSecondAndThirdInvoice = "2012-01-01"
		toStringSecondAndThirdInvoice = "2015-11-30"
		assertEquals(false, listWithSecondAndThirdInvoice.equals(Invoice.getAllInvoicesBetween(fromStringSecondAndThirdInvoice, toStringSecondAndThirdInvoice)))
		fromDateSecondAndThirdInvoice = (new Date().parse("yyyy-MM-dd", fromStringSecondAndThirdInvoice))
		toDateSecondAndThirdInvoice = (new Date().parse("yyyy-MM-dd", toStringSecondAndThirdInvoice))
		assertEquals(false, listWithSecondAndThirdInvoice.equals(Invoice.getAllInvoicesBetween(fromDateSecondAndThirdInvoice, toDateSecondAndThirdInvoice)))
		assertEquals(false, listWithSecondAndThirdInvoice.equals(Invoice.getAllInvoicesBetween(fromDateSecondAndThirdInvoice.toCalendar(), toDateSecondAndThirdInvoice.toCalendar())))
		
		List<Invoice> listWithSecondInvoice = new ArrayList<Invoice>()
		listWithSecondInvoice.add(secondInvoice)
		
		String fromStringSecondInvoice = "2013-01-02"
		String toStringSecondInvoice = "2013-01-02"
		assertEquals(listWithSecondInvoice, Invoice.getAllInvoicesBetween(fromStringSecondInvoice, toStringSecondInvoice))
		Date fromDateSecondInvoice = (new Date().parse("yyyy-MM-dd", fromStringSecondInvoice))
		Date toDateSecondInvoice = (new Date().parse("yyyy-MM-dd", toStringSecondInvoice))
		assertEquals(true, listWithSecondInvoice.equals(Invoice.getAllInvoicesBetween(fromDateSecondInvoice, toDateSecondInvoice)))
		assertEquals(true, listWithSecondInvoice.equals(Invoice.getAllInvoicesBetween(fromDateSecondInvoice.toCalendar(), toDateSecondInvoice.toCalendar())))
		
		fromStringSecondInvoice = "2013-01-02"
		toStringSecondInvoice = "2013-11-30"
		assertEquals(listWithSecondInvoice, Invoice.getAllInvoicesBetween(fromStringSecondInvoice, toStringSecondInvoice))
		fromDateSecondInvoice = (new Date().parse("yyyy-MM-dd", fromStringSecondInvoice))
		toDateSecondInvoice = (new Date().parse("yyyy-MM-dd", toStringSecondInvoice))
		assertEquals(true, listWithSecondInvoice.equals(Invoice.getAllInvoicesBetween(fromDateSecondInvoice, toDateSecondInvoice)))
		assertEquals(true, listWithSecondInvoice.equals(Invoice.getAllInvoicesBetween(fromDateSecondInvoice.toCalendar(), toDateSecondInvoice.toCalendar())))
		
		fromStringSecondInvoice = "2013-01-01"
		toStringSecondInvoice = "2015-11-30"
		assertEquals(false, listWithSecondInvoice.equals(Invoice.getAllInvoicesBetween(fromStringSecondInvoice, toStringSecondInvoice)))
		fromDateSecondInvoice = (new Date().parse("yyyy-MM-dd", fromStringSecondInvoice))
		toDateSecondInvoice = (new Date().parse("yyyy-MM-dd", toStringSecondInvoice))
		assertEquals(false, listWithSecondInvoice.equals(Invoice.getAllInvoicesBetween(fromDateSecondInvoice, toDateSecondInvoice)))
		assertEquals(false, listWithSecondInvoice.equals(Invoice.getAllInvoicesBetween(fromDateSecondInvoice.toCalendar(), toDateSecondInvoice.toCalendar())))
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
		
		String firstInvoiceString = /${firstStore.toString()} - ${firstInvoice.getCreationDateAsString()} - ${firstInvoice.getPaymentMethod()}/
		
		assertEquals(firstInvoiceString, firstInvoice.toString())
	}
}