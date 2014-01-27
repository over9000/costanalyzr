package info.pascalkrause.costanalyzr

import java.text.SimpleDateFormat;

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class AnalyzeController {
	
	static allowedMethods = [getCategories:"GET", getChartData:"GET"]

    def index() {
		redirect(action: "prepareEvaluation")
	}
	
	def prepareEvaluationFlow =  {
		
		chooseChartAndTimePeriod {
			on("next").to("validateChartAndTimePeriod")
			on("cancel").to("cancel")
		}
		validateChartAndTimePeriod {
			action {
				// Überprüfe welche Charts ausgewählt wurden
				boolean chartSelectionValid = false;
				boolean stackedAreaChart = false
				if(params.get("stackedAreaChartCheckbox") != null) {
					stackedAreaChart = true
					chartSelectionValid = true;
				}
				boolean pieChart = false
				if(params.get("pieChartCheckbox") != null) {
					pieChart = true
					chartSelectionValid = true;
				}
				// Überprüfe Zeitraum
				boolean timePeriodSelectionValid = false
				// Ein Enum wäre für timePeriodType schöner
				// Momentan gibt es die Werte custom, year und month
				String timePeriodType = params.timeRadios
				GregorianCalendar fromDate = null
				GregorianCalendar toDate = null
				if(timePeriodType.equals("custom")) {
					SimpleDateFormat sdfCustom = new SimpleDateFormat("yyyy-MM-dd");
					fromDate = sdfCustom.parse(params.timeCustomFrom).toCalendar()
					toDate = sdfCustom.parse(params.timeCustomTo).toCalendar()
					timePeriodSelectionValid = true
				} else if(timePeriodType.equals("year")) {
					SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
					GregorianCalendar tempCalYear = sdfYear.parse(params.timeYear).toCalendar()
					fromDate = new GregorianCalendar(tempCalYear.get(Calendar.YEAR), Calendar.JANUARY, 1)
					toDate = new GregorianCalendar(tempCalYear.get(Calendar.YEAR), Calendar.DECEMBER, 31)
					timePeriodSelectionValid = true
				} else if(timePeriodType.equals("month")) {
					SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
					GregorianCalendar tempCalMonth = sdfMonth.parse(params.timeMonth).toCalendar()
					fromDate = new GregorianCalendar(tempCalMonth.get(Calendar.YEAR), tempCalMonth.get(Calendar.MONTH), 1)
					toDate = new GregorianCalendar(tempCalMonth.get(Calendar.YEAR), tempCalMonth.get(Calendar.MONTH), tempCalMonth.getActualMaximum(Calendar.DAY_OF_MONTH))
					timePeriodSelectionValid = true
				}
				boolean doesInvoicesExist = false;
				if(Invoice.getAllInvoicesBetween(fromDate, toDate).size() > 0) {
					doesInvoicesExist = true;
				}
				GregorianCalendar today = new Date().toCalendar();
				if(fromDate.compareTo(today) > 0 ) {
					// fromDate liegt in der Zukunft. Setzte toDate auf heute
					toDate = today;
					flash.typ = "warning"
					flash.message = "Das Startdatum liegt in der Zukunft, es wurde automatisch auf den heutigen Tag geändert"
				}
				if(toDate.compareTo(today) > 0 ) {
					// toDate liegt in der Zukunft. Setzte toDate auf heute
					toDate = today;
					flash.typ = "warning"
					flash.message = "Das Enddatum liegt in der Zukunft, es wurde automatisch auf den heutigen Tag geändert"
				}
				// Wenn mindestens ein Chart und ein Zeitraum ausgewählt wurde und es im angegeben Zeitraum Rechnungen gibt gehe zum nächsten Schritt
				if(timePeriodSelectionValid && chartSelectionValid && doesInvoicesExist) {
					flow.toDate = toDate
					flow.fromDate = fromDate
					flow.pieChart = pieChart
					flow.stackedAreaChart = stackedAreaChart
					flow.timePeriodType = timePeriodType
					valid()
				} else {
					if(!timePeriodSelectionValid) {
						flash.typ = "error"
						flash.message = "Es wurde kein Zeitraum ausgewählt"
					} else if(!chartSelectionValid) {
						flash.typ = "error"
						flash.message = "Es wurde keine Diagrammart ausgewählt"
					} else if(!doesInvoicesExist) {
						flash.typ = "error"
						flash.message = "Es existieren keine Rechnungen im ausgewählten Zeitraum"
					}
					invalid()
				}
			}
			on("valid"){
				if(flash.isEmpty()) {
					[flow: flow]
				} else {
					[flow: flow, flash: flash]
				}
			}.to("chooseCategories")
			on("invalid"){ [flow: flow, flash: flash] }.to("chooseChartAndTimePeriod")
		}
		
		chooseCategories {
			on("next").to("validateChooseCategories")
			on("back").to("chooseChartAndTimePeriod")
			on("cancel").to("cancel")
		}
		
		validateChooseCategories {
			action {
				def selectedCategoriesValid = false;
				def showAllChildren = false;
				def categories = new ArrayList<Category>()
				if(params.get("selectedCategories") != null) {
					// Prüfe ob eine oder mehrere Kategorien ausgewählt wurden
					if(params.selectedCategories.indexOf(",") <= 0) {
						// Wahrscheinlich wurde eine übertragen (Wurden mehrere ausgewählt, werden diese auf der view mit einem Komma gejoint, daher wohl nur eine)
						Category category = Category.getCategoryByFullName(params.selectedCategories)
						if(category != null) {
							categories.add(category)
							selectedCategoriesValid = true
							// Wenn nur eine Kategorie übermittelt überprüfe ob auch die Kinder angezeigt werden sollen
							if(params.get("showAllChildren") != null) {
								showAllChildren = true;
							}
						} else {
							// Die ausgewählte Kategorie existiert nicht oder es wurde keine übermittelt
							flash.typ = "error"
							flash.message = "Die ausgewählte Kategorie existiert nicht, oder es wurde keine übermittelt"
						}
					} else {
						def categoriesAsString = params.selectedCategories.split(",").toList();
						categories = Category.parseCategoriesByFullName(categoriesAsString);
						if(categoriesAsString.size() != categories.size()) {
							// Es wurden fehlerhafte Kategorien übertragen
							flash.typ = "error"
							flash.message = "Es wurden nicht existente Kategorien übermittelt"
						} else {
						selectedCategoriesValid = true;
						}
					}
				} else {
					flash.typ = "error"
					flash.message = "Es wurden keine Kategorien übermittelt"
				}
				if(selectedCategoriesValid) {
					flow.categories = categories
					flow.showAllChildren = showAllChildren
					valid()
				} else {
					invalid()
				}
			}
			on("valid"){
				if(flash.isEmpty()) {
					[flow: flow]
				} else {
					[flow: flow, flash: flash]
				}
			}.to("showResult")
			on("invalid"){ [flow: flow, flash: flash] }.to("chooseCategories")
		}
		
		showResult {
			on("done").to("cancel")
			on("back").to("chooseCategories")
			on("newEvaluation").to("newEvaluation")
		}
		
		newEvaluation {
			redirect(action: 'index')
		}
		
		
		cancel {
			redirect(action: 'showHome')
		}
	}
	
	def showHome() {
		flash.typ = params.typ;
		flash.message = params.message;
		redirect(uri: "/home")
	}
	
	/**
	 * Example of JSON data for jstree <br>
	 * [<br>
     *  { "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },<br>
     *  { "id" : "ajson2", "parent" : "#", "text" : "Root node 2" },<br>
     *  { "id" : "ajson3", "parent" : "ajson2", "text" : "Child 1" },<br>
     *  { "id" : "ajson4", "parent" : "ajson2", "text" : "Child 2" },<br>
     * ]<br>
     * <br>
     * Erzeugt ein JSON Objekt um alle existierenden Kategorien in einem jstree anzeigen zu können.
	 */
	def getCategories() {
		String responseString = "["
		Category.getAll().sort{it.toString()}.each {
			def topLevelCategory = false;
			if(it.getParentCategory() == null) {
				topLevelCategory = true;
			}
			responseString = responseString + /{"id" : "${it.toString()}", "parent" : "${(topLevelCategory) ? '#' : it.getParentCategory().toString()}", "text" : "${it.getName()}"},/
		}
		responseString = Utils.cutAtLastComma(responseString)
		responseString = responseString + "]"
		render responseString
	}

	/**
	 * <b>fromDate</b> Ein String welcher den Beginn des Auswertungszeitraum festlegt. Der String muss im folgenden Format (yyyy-MM-dd) übermittelt werden<br>
	 * <b>toDate</b> Ein String welcher das Ende des Auswertungszeitraum festlegt. Der String muss im folgenden Format (yyyy-MM-dd) übermittelt werden<br>
	 * <b>timePeriodType</b> Wird benötigt um die Einheiten an der X-Achse optimal darstellen zu können. Gültige Werte sind month, year, custom (wird nur bei Diagrammen mit X-Achse benötigt momentan also nur StackedAreaChart)<br> 
	 * <b>pieChart</b> (Der Wert ist egal, wichtig ist nur das es ein Attribut mit diesem Namen in params gibt, falls dieser ChartTyp gewünscht ist)<br>
	 * <b>stackedAreaChart</b> (Der Wert ist egal, wichtig ist nur das es ein Attribut mit diesem Namen in params gibt, falls dieser ChartTyp gewünscht ist)<br>
	 * <b>showAllChildren</b> (Der Wert ist egal Hauptsache es gibt ein Attribut mit diesem Namen, falls diese Option gewünscht ist)<br>
	 * <b>categories</b> Die Kategorien die ausgewertet werden sollen. Der String darf unterschiedlich aussehen. Es folgen gültige Beispiele:<br>
	 * [category1, category2, ...] (Wenn mehrere Kategorien übertragen werden. Whitespaces zwischen einem Komma und dem Namen einer Kategorie sind erlaubt)<br> 
	 * category1, category2, ... (Wenn mehrere Kategorien übertragen werden. Whitespaces zwischen einem Komma und dem Namen einer Kategorie sind erlaubt)<br> 
	 * category1 (Wenn NUR eine Kategorie übertragen wird)<br>
	 * 
	 */
	def showChart() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar fromDate = sdf.parse(params.get("fromDate")).toCalendar()
		GregorianCalendar toDate = sdf.parse(params.get("toDate")).toCalendar()

		String timePeriodType = params.get("timePeriodType")
		boolean pieChart = false;
		(params.get("pieChart") != null) ? (pieChart = true) : (pieChart = false);
		boolean stackedAreaChart = false;
		(params.get("stackedAreaChart") != null) ? (stackedAreaChart = true) : (stackedAreaChart = false);
		boolean showAllChildren = false;
		(params.get("showAllChildren") != null) ? (showAllChildren = true) : (showAllChildren = false);
		
		println params.get("categories")
		List<Category> categories = Category.parseCategoriesByFullName(params.get("categories"))
		println categories
		
		render(view: "showChart", model: [fromDate: fromDate, toDate: toDate, timePeriodType: timePeriodType,
			pieChart: pieChart, stackedAreaChart: stackedAreaChart, showAllChildren: showAllChildren, categories: categories])
	}
	
	/**
	 * <b>fromDate</b> Ein String welcher den Beginn des Auswertungszeitraum festlegt. Der String muss im folgenden Format (yyyy-MM-dd) übermittelt werden<br>
	 * <b>toDate</b> Ein String welcher das Ende des Auswertungszeitraum festlegt. Der String muss im folgenden Format (yyyy-MM-dd) übermittelt werden<br>
	 * <b>timePeriodType</b> Wird benötigt um die Einheiten an der X-Achse optimal darstellen zu können. Gültige Werte sind month, year, custom (wird nur bei Diagrammen mit X-Achse benötigt momentan also nur StackedAreaChart)<br> 
	 * <b>pieChart</b> (Der Wert ist egal, wichtig ist nur das es ein Attribut mit diesem Namen in params gibt, falls dieser ChartTyp gewünscht ist)<br>
	 * <b>stackedAreaChart</b> (Der Wert ist egal, wichtig ist nur das es ein Attribut mit diesem Namen in params gibt, falls dieser ChartTyp gewünscht ist)<br>
	 * <b>showAllChildren</b> (Der Wert ist egal Hauptsache es gibt ein Attribut mit diesem Namen, falls diese Option gewünscht ist)<br>
	 * <b>categories</b> Die Kategorien die ausgewertet werden sollen. Der String darf unterschiedlich aussehen. Es folgen gültige Beispiele:<br>
	 * [category1, category2, ...] (Wenn mehrere Kategorien übertragen werden. Whitespaces zwischen einem Komma und dem Namen einer Kategorie sind erlaubt)<br> 
	 * category1, category2, ... (Wenn mehrere Kategorien übertragen werden. Whitespaces zwischen einem Komma und dem Namen einer Kategorie sind erlaubt)<br> 
	 * category1 (Wenn NUR eine Kategorie übertragen wird)<br>
	 * <br>
	 * Wichtig! Es kann immer nur ein Charttyp ausgewählt werden. Werden mehrere übermittelt wird der erste gültige Typ genommen
	 * 
	 * Erzeugt ein JSON Objekt, welches die Ergebnisse der Auswertungen beinhaltet um den ausgewählte Charttyp darstellen zu können.
	 * Werden keine Rechnungen im angegeben Zeitraum gefunden wird [] zurück gegeben
	 */
	def getChartData() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar fromDate = sdf.parse(params.get("fromDate")).toCalendar()
		GregorianCalendar toDate = sdf.parse(params.get("toDate")).toCalendar()

		String timePeriodType = params.get("timePeriodType")
		boolean pieChart = false;
		(params.get("pieChart") != null) ? (pieChart = true) : (pieChart = false);
		boolean stackedAreaChart = false;
		(params.get("stackedAreaChart") != null) ? (stackedAreaChart = true) : (stackedAreaChart = false);
		boolean showAllChildren = false;
		(params.get("showAllChildren") != null) ? (showAllChildren = true) : (showAllChildren = false);
		List<Category> categories = Category.parseCategoriesByFullName(params.get("categories"))
		
		List<Invoice> invoices = Invoice.getAllInvoicesBetween(fromDate, toDate);
		
		if(invoices.isEmpty()) {
			// Shows "No Data availalble" in the chart
			render(text: "[]", contentType: "text/json", encoding: "UTF-8")
			return
		}
		String chartData = ""
		// Handelt es sich um ein PieChart oder StackedAreaChart?
		if(pieChart) {
			chartData = getPieChartDataAsJSON(categories, invoices, showAllChildren);
		} else if(stackedAreaChart) {
			chartData = getStackedAreaChartDataAsJSON(categories, invoices, showAllChildren, timePeriodType, fromDate, toDate);
		}
		render(text: chartData, contentType: "text/json", encoding: "UTF-8")
	}
	
	private String getStackedAreaChartDataAsJSON(List<Category> categories, List<Invoice> invoices,
		boolean showAllChildren, String timePeriodType, GregorianCalendar fromDate, GregorianCalendar toDate) {
		Map<Category, Double> tempCosts = new HashMap<Category, Double>()
		Map<Long, Map<Category,Double>> calculatedCosts = new HashMap<Long, Map<Category,Double>>()
		GregorianCalendar tempDate = fromDate.clone();
		if(showAllChildren) {
			if(!(categories.size() > 0)) {
				// Shows "No Data availalble" in the chart
				return "[]"
			}
			Category passedParentCategory = categories.get(0)
			List<Category> categoryChildren = passedParentCategory.childCategorys.toList()
			while(tempDate.compareTo(toDate) <= 0) {
				invoices.each {
					// Prüfe ob es Rechnungen an diesem Tag gibt
					if(it.getCreationDate().equals(tempDate)) {
						it.invoiceItems.each {
							def currentInvoiceItem = it;
							def currentInvoiceItemCategory = it.getArticle().getCategory()
							if(currentInvoiceItemCategory.equals(passedParentCategory)) {
								// Hole den alten Betrag und addiere den neuen Hinzu
								Double oldValue = (tempCosts.get(passedParentCategory) != null) ? tempCosts.get(passedParentCategory) : 0;
								Double newValue = oldValue + (currentInvoiceItem.getCount() * currentInvoiceItem.getGrossPrice())
								tempCosts.put(passedParentCategory, newValue)
							} else {
								// Überprüfe ob die Kategorie des Rechnungsposten gleich oder ein Kind von einer der gewünschten Kategorien ist
								categoryChildren.each {
									if(currentInvoiceItemCategory.isChildOf(it) || it.equals(currentInvoiceItemCategory)) {
										// Hole den alten Betrag und addiere den neuen Hinzu
										Double oldValue = (tempCosts.get(it) != null) ? tempCosts.get(it) : 0;
										Double newValue = oldValue + (currentInvoiceItem.getCount() * currentInvoiceItem.getGrossPrice())
										tempCosts.put(it, newValue)
									}
								}
							}
						}
					}
				}
				// Alle Rechnungen dieses Tags sind ausgewertet trage sie ein
				def mapToAdd = new HashMap<Category, Double>()
				tempCosts.keySet().each {
					mapToAdd.put(it, (tempCosts.get(it)) ? tempCosts.get(it) : 0)
				}
				calculatedCosts.put(tempDate.getTimeInMillis(), mapToAdd);
				// Bei der add Methode macht es kein Unterschied ob DAY_OF_MONTh oder DAY_OF_YEAR etc
				tempDate.add(Calendar.DAY_OF_MONTH, 1);
			}
		} else {
			while(tempDate.compareTo(toDate) <= 0) {
				invoices.each {
					// Prüfe ob es Rechnungen an diesem Tag gibt
					if(it.getCreationDate().equals(tempDate)) {
						it.invoiceItems.each {
							def currentInvoiceItem = it;
							def currentInvoiceItemCategory = it.getArticle().getCategory()
							// Überprüfe ob die Kategorie des Rechnungsposten gleich oder ein Kind von einer der gewünschten Kategorien ist
							categories.each {
								if(it.isParentOf(currentInvoiceItemCategory) || it.equals(currentInvoiceItemCategory)) {
									// Hole den alten Betrag und addiere den neuen Hinzu
									Double oldValue = (tempCosts.get(it) != null) ? tempCosts.get(it) : 0;
									Double newValue = oldValue + (currentInvoiceItem.getCount() * currentInvoiceItem.getGrossPrice())
									tempCosts.put(it, newValue)
								}
							}
						}
					}
				}
				// Alle Rechnungen dieses Tags sind ausgewertet trage sie ein
				def mapToAdd = new HashMap<Category, Double>()
				tempCosts.keySet().each {
					mapToAdd.put(it, (tempCosts.get(it)) ? tempCosts.get(it) : 0)
				}
				calculatedCosts.put(tempDate.getTimeInMillis(), mapToAdd);
				// Bei der add Methode macht es kein Unterschied ob DAY_OF_MONTh oder DAY_OF_YEAR etc
				tempDate.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		/**
		 * So müssen die Daten für NVD3 aufbereitet werden
		 * [
		 * 	{
		 *  "key" : "Millionen pro Jahr",
		 *  "values" : [ [2001, 1], [2004, 5], [2005, 8], [2006, 10] ]
		 *  }
		 * ]
		 */

		String chartDataToReturn = "[";
		tempCosts.keySet().each {
			tempDate = fromDate.clone();
			String values = ""
			while(tempDate.compareTo(toDate) <= 0) {
				if(timePeriodType.equals("month")) {
					int dayOfMonth = tempDate.get(Calendar.DAY_OF_MONTH)
					double currentValue = (calculatedCosts.get(tempDate.getTimeInMillis()).get(it)) ? calculatedCosts.get(tempDate.getTimeInMillis()).get(it) : 0
					values = values + "[${dayOfMonth}, ${Utils.cutDecimal(currentValue)}], "
				} else {
					long timeOfCurrentDayInMillis = tempDate.getTimeInMillis();
					double currentValue = (calculatedCosts.get(tempDate.getTimeInMillis()).get(it)) ? calculatedCosts.get(tempDate.getTimeInMillis()).get(it) : 0
					values = values + "[${timeOfCurrentDayInMillis}, ${Utils.cutDecimal(currentValue)}], "
				}
				tempDate.add(Calendar.DAY_OF_MONTH, 1);
			}
			values = Utils.cutAtLastComma(values)
			chartDataToReturn = chartDataToReturn +
			/
			{
				"key" : "${it}",
				"values" : [ ${values} ]
				},
			/
		}
		chartDataToReturn= Utils.cutAtLastComma(chartDataToReturn)
		chartDataToReturn = chartDataToReturn + "]"
		return chartDataToReturn
	}
	
	private String getPieChartDataAsJSON(List<Category> categories, List<Invoice> invoices, boolean showAllChildren) {
		Map<Category, Double> tempCosts = new HashMap<Category, Double>()
		if(showAllChildren) {
			if(!(categories.size() > 0)) {
				// Shows "No Data availalble" in the chart
				return "[]"
			}
			Category passedParentCategory = categories.get(0)
			def categoryChildren = passedParentCategory.childCategorys.toList()
			invoices.each {
				it.invoiceItems.toList().each {
					Category currentInvoiceItemCategory = it.getArticle().getCategory();
					InvoiceItem currentInvoiceItem = it;
					if(currentInvoiceItemCategory.equals(passedParentCategory)) {
						Double oldValue = (tempCosts.get(passedParentCategory) != null) ? tempCosts.get(passedParentCategory) : 0;
						Double newValue = oldValue + (currentInvoiceItem.getCount() * currentInvoiceItem.getGrossPrice())
						tempCosts.put(passedParentCategory, newValue)
					} else {
						categoryChildren.each {
							if(currentInvoiceItemCategory.isChildOf(it) || it.equals(currentInvoiceItemCategory)) {
								Double oldValue = (tempCosts.get(it) != null) ? tempCosts.get(it) : 0;
								Double newValue = oldValue + (currentInvoiceItem.getCount() * currentInvoiceItem.getGrossPrice())
								tempCosts.put(it, newValue)
							}
						}
					}
				}
			}
		} else {
			// Es handelt sich um reine TopLevelKategorien. Es muss nur geprüft werden, ob der Artikel ein Kind davon ist.
			invoices.each {
				it.invoiceItems.each {
					def invoiceItemCategory = it.getArticle().getCategory();
					def currentInvoiceItem = it;
					categories.each {
						if(invoiceItemCategory.isChildOf(it) || it.equals(invoiceItemCategory)) {
							Double oldValue = (tempCosts.get(it) != null) ? tempCosts.get(it) : 0;
							Double newValue = oldValue + (currentInvoiceItem.getCount() * currentInvoiceItem.getGrossPrice())
							tempCosts.put(it, newValue)
						}
					}
				}
			}
		}
		// Die Ausgaben pro Kategorie wurden berechnet. Jetzt wird ein JSON-String aus den Werten gebaut
		String chartDataToReturn = """
		[
		""";
		if(tempCosts.keySet().size() > 0) {
			tempCosts.keySet().each {
				chartDataToReturn = chartDataToReturn + """
			{
				"label" : "${it}",
				"value" : "${Utils.cutDecimal(tempCosts.get(it))}"
			},
			"""
			}
			chartDataToReturn= Utils.cutAtLastComma(chartDataToReturn)
		} else {
			chartDataToReturn = chartDataToReturn + """
			{
				"label" : "Im angegeben Zeitraum wurde in keiner der hier zu zeigenden Kategorien ein Artikel gekauft",
				"value" : "${0.00}"
			}
			"""
		}
		chartDataToReturn = chartDataToReturn + """
		]
		"""
		return chartDataToReturn
	}	
}