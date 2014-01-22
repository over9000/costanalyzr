package info.pascalkrause.costanalyzr

import java.text.DateFormatSymbols
import java.text.SimpleDateFormat;
import org.codehaus.groovy.grails.web.mapping.LinkGenerator;

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class PieChartTagLib {
	
	LinkGenerator grailsLinkGenerator
    def costsAnalyzerService
	def SimpleDateFormat sdfyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * Diese TagLib erzeugt ein PieChart Diagramm (nvd3.js) in einem ausklappbaren Panel.<br>
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
	def pieChart = { attrs, body ->
		
		def htmlWriter = getOut()
		
		GregorianCalendar fromDate = attrs.fromDate.toCalendar()
		GregorianCalendar toDate = attrs.toDate.toCalendar()
		String timePeriodType = attrs.timePeriodType
		boolean pieChart = true
		boolean showAllChildren = false
		if(attrs.showAllChildren != null) {
			showAllChildren = true;
		}
		List<Category> categories = Category.parseCategoriesByFullName(attrs.categories)
		double totalCosts = costsAnalyzerService.totalCostsByTimePeriodAndCategories(fromDate, toDate, categories)
		def randomString = Utils.getRandomString()
		
		
		String timePeriodValue = ""
		if(timePeriodType.equals("month")) {
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
		
		String requestString = """/${Utils.getProjectName()}/analyze/getChartData?""" +
		"""fromDate=${sdfyyyyMMdd.format(fromDate.getTime())}""" +
		"""&toDate=${sdfyyyyMMdd.format(toDate.getTime())}""" +
		"""&timePeriodType=${timePeriodType}""" +
		"""&pieChart=${pieChart}""" +
		"""${showAllChildren ? "&showAllChildren=${showAllChildren}" : ""}""" +
		"""&categories=${categories}"""
		
		String requestStringShowChart = """${grailsLinkGenerator.getServerBaseURL()}/analyze/showChart?""" +
		"""fromDate=${sdfyyyyMMdd.format(fromDate.getTime())}""" +
		"""&toDate=${sdfyyyyMMdd.format(toDate.getTime())}""" +
		"""&timePeriodType=${timePeriodType}""" +
		"""&pieChart=${pieChart}""" +
		"""${showAllChildren ? "&showAllChildren=${showAllChildren}" : ""}""" +
		"""&categories=${categories}"""
		
		boolean collapse = false;

		htmlWriter << r.require(module: "nv_d3_v115") << """
				<div class="panel panel-default">
        			<div id="pieChartCategoryChildrenPanelHeading${randomString}" class="panel-heading">
          				<h4 class="panel-title"> <a style="cursor:pointer;text-decoration: none;" data-toggle="collapse" data-target="#pieChartCategoryChildrenPanel${randomString}">
							<span class="glyphicon glyphicon-resize-vertical"></span><span>PieChart</span>
						  	<span style="margin-left:10px" >Zeitraum: ${timePeriodValue}</span><br>"""
							if(showAllChildren) {
								htmlWriter	<< """<span style="margin-left:18px">Kategorie: ${categories.get(0)} und direkte Kategoriekinder</span>"""
							} else {
						  		htmlWriter	<< """<span style="margin-left:18px">Kategorien: ${categories.toString().replace("[", "").replace("]", "")}</span>"""
							}
							htmlWriter << """<span style="float: right">Gesamtausgaben: ${totalCosts} Euro</span></a> </h4>
        			</div>
        			<div id="pieChartCategoryChildrenPanel${randomString}" class="panel-collapse collapse ${collapse ? "": "in"}">
          				<div class="panel-body">
							<div id="pieChartCategoryChildren${randomString}" style='width:100%;height:100%'>
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
				</div>
				"""
		htmlWriter << """<script async>
					\$( function () {d3.json('${requestString}', function(data) {
						nv.addGraph(function() {

							var width = \$( "#pieChartCategoryChildrenPanelHeading${randomString}" ).width();
							var height = width/2;
							(height <= 400) ? height = 400 : height=height;
							\$( "#pieChartCategoryChildren${randomString}" ).height(height);

				 			var pieChartCategoryChildren${randomString} = nv.models.pieChart()
				 	    		.x(function(d) { return d.label })
				 	      		.y(function(d) { return d.value })
				 		  		.width(width)
							    .color(d3.scale.category10().range())
				 	      		.tooltips(true)
							 	.tooltipContent(function(key, y, e, graph) { return '<h3>' + key + '</h3>' +'<p>' + y + ' Euro </p>' ; })
				 	  	  		.height(height - 20)
				 	      		.showLabels(false)
//							 	.labelSunbeamLayout(true)
				 	  	  		;

								d3.select('#pieChartCategoryChildren${randomString} svg').append("text")
									.attr("x", width / 2 )
									.attr("y", height-10)
									.style("text-anchor", "middle")
									.text("Kategorien in denen während des angegeben Zeitraums nichts gekauft wurde, erscheinen nicht im Diagramm");

				 			var svg = d3.select('#pieChartCategoryChildren${randomString} svg')
				 	    		.datum(data)
				 	    		.transition()
				 	  			.duration(500)
				 				.attr('width', width)
				 				.attr('height', height)
				 	  			.call(pieChartCategoryChildren${randomString})
				 				;

				 				nv.utils.windowResize(pieChartCategoryChildren${randomString}.update);
				 				return pieChartCategoryChildren${randomString};
				 		});})
				 	})
				  </script>"""
	}
}