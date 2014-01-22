package info.pascalkrause.costanalyzr

import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat;

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class StackedAreaChartTagLib {
	
	LinkGenerator grailsLinkGenerator
    def costsAnalyzerService
	def SimpleDateFormat sdfyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * Diese TagLib erzeugt ein StackedAreaChart Diagramm (nvd3.js) in einem ausklappbaren Panel.<br>
	 * Es werden folgende Attribute benötigt:<br>
	 * <br>
	 * <b>fromDate</b> (Date) Das Datum ab dem Daten für das Diagramm ausgewertet werden sollen z.B. fromDate="${new Date().toCalendar()}"<br>
	 * <br>
	 * <b>toDate</b> (Date) Das Datum bis zu dem Daten für das Diagramm ausgewertet werden sollen z.B. fromDate="${new Date().toCalendar()}"<br>
	 * <br>
	 * <b>timePeriodType</b> (String: "month", "year", "custom") Der Typ des Zeitraums wird benötigt um die X-Achsen Schritte optimal darstellen zu können.<br>
	 * <br>
	 * <b>showAllChildren</b> Mit dieser Option werden zusätzlich zu der Übergeben Kategorie alle direkten Kategorie Kinder im Diagramm angezeigt.
	 * Werden mehrere Kategorien übergeben, wird nur eine daraus genommen (Mit dieser Option sollte nur eine Kategorie übergeben werden).
	 * Der Wert dieses Attributs ist egal, wichtig ist nur das es übergeben wird, sofern diese Option gewünscht ist.<br>
	 * <br>
	 * <b>categories</b> (String) oder (List<String>) Die Kategorien die im Diagramm ausgewertet werden sollen. Entweder es wird eine List<String> mit den
	 * vollen Kategorienamen (toString()) enthält, oder ein String. Der String darf wie folgt aussehen:<br>
	 * Beispiel 1: Kategorie1<br>
	 * Beispiel 2: [Kategorie1]<br>
	 * Beispiel 3: Kategorie1, Kategorie2, ...<br>
	 * Beispiel 4: [Kategorie1, Kategorie2, ..]<br>
	 * Beispiel 5: Kategorie1  ,    Kategorie2    , ..<br>
	 * Beispiel 6: [   Kategorie1   ,    Kategorie2  , ..]<br>
	 * <br>
	 * <b>siehe auch:</b> Category.parseCategoriesByFullName
	 */
	def stackedAreaChart = { attrs, body ->
		
		def htmlWriter = getOut()
		
		GregorianCalendar fromDate = attrs.fromDate.toCalendar()
		GregorianCalendar toDate = attrs.toDate.toCalendar()
		String timePeriodType = attrs.timePeriodType
		boolean stackedAreaChart = true
		boolean showAllChildren = false
		if(attrs.showAllChildren != null) {
			showAllChildren = true;
		}

		List<Category> categories = Category.parseCategoriesByFullName(attrs.categories.toString())
		double totalCosts = costsAnalyzerService.totalCostsByTimePeriodAndCategories(fromDate, toDate, categories)
		def randomString = Utils.getRandomString()
		
		int maxDaysOfMonth = fromDate.getActualMaximum(Calendar.DAY_OF_MONTH)
		String timePeriodValue = ""
		boolean isTimePediodTypeMonth = false;
		if(timePeriodType.equals("month")) {
			isTimePediodTypeMonth = true;
			DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
			String[] monthNames = symbols.getMonths();
			timePeriodValue = "${monthNames[fromDate.get(Calendar.MONTH)]} ${fromDate.get(Calendar.YEAR)}"
		}
		if(timePeriodType.equals("year")) {
			timePeriodValue = "Das Jahr ${fromDate.get(Calendar.YEAR)}"
		}
		if(timePeriodType.equals("custom")) {
			timePeriodValue = "${sdfyyyyMMdd.format(fromDate.getTime())} - ${sdfyyyyMMdd.format(toDate.getTime())}"
		}
		
		String requestStringChartData = """/${Utils.getProjectName()}/analyze/getChartData?""" +
		"""fromDate=${sdfyyyyMMdd.format(fromDate.getTime())}""" +
		"""&toDate=${sdfyyyyMMdd.format(toDate.getTime())}""" +
		"""&timePeriodType=${timePeriodType}""" +
		"""&stackedAreaChart=${stackedAreaChart}""" +
		"""${showAllChildren ? "&showAllChildren=${showAllChildren}" : ""}""" +
		"""&categories=${categories}"""
		
		String requestStringShowChart = """${grailsLinkGenerator.getServerBaseURL()}/analyze/showChart?""" +
		"""fromDate=${sdfyyyyMMdd.format(fromDate.getTime())}""" +
		"""&toDate=${sdfyyyyMMdd.format(toDate.getTime())}""" +
		"""&timePeriodType=${timePeriodType}""" +
		"""&stackedAreaChart=${stackedAreaChart}""" +
		"""${showAllChildren ? "&showAllChildren=${showAllChildren}" : ""}""" +
		"""&categories=${categories}"""
		
		boolean collapse = false;

		htmlWriter << r.require(module: "nv_d3_v115") << """
				<div class="panel panel-default">
        			<div id="stackedAreaChartPanelHeading${randomString}" class="panel-heading">
          				<h4 class="panel-title"> <a style="cursor:pointer;text-decoration: none;" data-toggle="collapse" data-target="#stackedAreaChartPanel${randomString}">
						  <span class="glyphicon glyphicon-resize-vertical"></span><span>StackedAreaChart</span>
						  <span style="margin-left:10px" >Zeitraum: ${timePeriodValue}</span><br>"""
						  if(showAllChildren) {
							htmlWriter	<< """<span style="margin-left:18px">Kategorie: ${categories.get(0)} und direkte Kategoriekinder</span>"""
						  } else {
						  	htmlWriter	<< """<span style="margin-left:18px">Kategorien: ${categories.toString().replace("[", "").replace("]", "")}</span>"""
						  }
						  htmlWriter << """<span style="float: right">Gesamtausgaben: ${totalCosts} Euro</span></a> </h4>
        			</div>
        			<div id="stackedAreaChartPanel${randomString}" class="panel-collapse collapse ${collapse ? "": "in"}">
          				<div class="panel-body">
							<div id="stackedAreaChart${randomString}" style='width:100%;height:100%'>
								<svg></svg>
							</div>
							<br>
						  	<div class="panel panel-default">
						  		<div class="panel-heading">URL zu diesem Diagramm</div>
						  		<div class="panel-body">
						  			<a href="${requestStringShowChart}" target="_blank" >${requestStringShowChart}</a>
						  		</div>
						  	</div>
						</div>
        			</div>
				</div>"""
	htmlWriter  << """<script async>

					\$( function () {
						d3.json('${requestStringChartData}', function(data) {
							nv.addGraph(function() {
								var width = \$( "#stackedAreaChartPanelHeading${randomString}" ).width();
								var height = width/2;
								(height <= 400) ? height = 400 : height=height;
								\$( "#stackedAreaChart${randomString}" ).height(height);

								var stackedAreaChart${randomString} = nv.models.stackedAreaChart()
									.x(function(d) { return d[0] })
									.y(function(d) { return d[1] })
									.clipEdge(true)
									.width(width)
									.color(d3.scale.category10().range())
									.height(height - 20)
									.margin({left: 80})
									.margin({right: 10})
									.useInteractiveGuideline(true)
									;

								var valueFormatter = (stackedAreaChart${randomString}.style() == 'expand') ?
									function(d,i) {return d3.format(".1%")(d);} :
									function(d,i) {return yAxis.tickFormat()(d) + " Euro"; };
								// Dieser Befehl funktioniert noch nicht. Siehe https://github.com/novus/nvd3/pull/313
								// Siehe js/nvd3/v1.15/nv.d3.js Zeile 14142 (Direkte Änderung in der Bibliothek)
								stackedAreaChart${randomString}.interactiveLayer.tooltip.valueFormatter(valueFormatter);
								
								var headerFormatter = function(d) { return "Datum: " + d};
								var headerFormatterMonth = function(d) { return "Datum: " + d  + " ${timePeriodValue}"};
								stackedAreaChart${randomString}.interactiveLayer.tooltip.headerFormatter(${isTimePediodTypeMonth ? "headerFormatterMonth" : "headerFormatter"});

								d3.select('#stackedAreaChart${randomString} svg').append("text")
									.attr("x", width / 2 )
									.attr("y", height-10)
									.style("text-anchor", "middle")
									.text("Kategorien in denen während des angegeben Zeitraums nichts gekauft wurde, erscheinen nicht im Diagramm");

								stackedAreaChart${randomString}.xAxis
									.showMaxMin(false)
									.ticks(15)
									.${isTimePediodTypeMonth ? "tickFormat(function(d){ return d })" : "tickFormat(function(d) { return d3.time.format('%Y-%m-%d')(new Date(d))})"}
									;
								
								${isTimePediodTypeMonth ? "stackedAreaChart${randomString}.forceX([1, ${maxDaysOfMonth}]);" : ""}
								${isTimePediodTypeMonth ? "stackedAreaChart${randomString}.xAxis.axisLabel('Tag im Monat');" : "stackedAreaChart${randomString}.xAxis.axisLabel('Zeitachse');"}
								${isTimePediodTypeMonth ? "stackedAreaChart${randomString}.xAxis.ticks(${maxDaysOfMonth});" : ""}

								stackedAreaChart${randomString}.yAxis
									.axisLabel("Ausgaben in Euro")
									.tickFormat(function(d){ return d3.format(',.2f')(d)});
								    
								d3.select('#stackedAreaChart${randomString} svg').
									datum(data).
									transition().
									duration(500).
									call(stackedAreaChart${randomString}).
									attr('width', width).
									attr('height', (height-20))
									;

								nv.utils.windowResize(stackedAreaChart${randomString}.update);
								return stackedAreaChart${randomString};
							});
					 });
				})
			   </script>"""
	}
}