package info.pascalkrause.costanalyzr

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class ArticleController {

    static allowedMethods = [saveArticle: "POST", deleteArticle:"POST", updateArticle:"POST", addArticle:"POST", getArticleListAsJSON:"GET"]
	
	def articleService

	def index() {
		redirect(action: "list")
	}
	
	def list() {
		render(view: "list", model: [articleInstanceList: Article.list()])
	}

	@Transactional
	def addArticle() {
		// Hole Parameter
		String articleName = params.get("articleName")
		Category articleCategory = null
		if(!params.get("articleCategoryId").equals("null")) {
			articleCategory = Category.get(Integer.parseInt(params.get("articleCategoryId")))
		}
		String articleDescription = params.get("articleDescription")
		String articleStandardTaxRate = params.get("articleStandardTaxRate")
		String articleStandardGrossPrice = params.get("articleStandardGrossPrice")
		// Setze Standardwerte
		if(!articleDescription ||articleDescription.isEmpty()) {
			articleDescription = 'n/a'
		}
		if(!articleStandardTaxRate || articleStandardTaxRate.isEmpty()) {
			articleStandardTaxRate = '19.00'
		}
		if(!articleStandardGrossPrice || articleStandardGrossPrice.isEmpty()) {
			articleStandardGrossPrice = '0.00'
		}
		// Überprüfe übergebene Parameter für Article 
		def validateResponse = articleService.validateArticle(articleCategory, articleName, articleStandardTaxRate, articleStandardGrossPrice);
		if(validateResponse.error) {
			flash.typ = validateResponse.typ
			flash.message = validateResponse.message
			render(view: "list", model: [articleInstanceList: Article.list()])
			return
		}
		Article newArticle = new Article();
		newArticle.setName(articleName);
		newArticle.setCategory(articleCategory);
		newArticle.setDescription(articleDescription)
		// Sollte keine Fehler werfen, wurde zuvor mit RegExp geprüft
		newArticle.setStandardTaxRate(Double.valueOf(articleStandardTaxRate))
		newArticle.setStandardGrossPrice(Double.valueOf(articleStandardGrossPrice))
		newArticle.save flush:true
		flash.typ = "success"
		flash.message = "Der Artikel ${newArticle.getName()} wurde unter der ID #${newArticle.id} angelegt"
		if(newArticle.errors.errorCount > 0) {
			if(newArticle.errors.toString().contains("unique")) {
				Article alreadyExist = null;
				Article.list().each {
					if(it.equals(newArticle) && it.getId() != newArticle.getId()) {
						alreadyExist = it;
					}
				}
				flash.typ = "error"
				flash.message = "Ein identischer Artikel (gleicher Name und Kategorie) existiert bereits unter der ID #${alreadyExist.id}"
			} else {
				flash.typ = "error"
				flash.message = newArticle.errors.toString()
			}
		}
		render(view: "list", model: [articleInstanceList: Article.list()])
	}
	
	@Transactional
	def deleteArticle() {
		Article articleToDelete = Article.get(Integer.parseInt(params.get("articleId")))
		if (articleToDelete == null) {
			flash.typ = "error"
			flash.message = "Der zu löschende Artikel existiert nicht"
		} else {
			flash.typ = "success"
			flash.message = "Der Artikel ${articleToDelete.getName()} mit der Kategorie (${articleToDelete.getCategory()}) wurde gelöscht"
			try {
				// Das ist momentan die einzige Möglichkeit einen DataIntegrityViolationException abzufangen.
				Article.withNewTransaction {
					Article article = Article.get(articleToDelete.getId())
					article.delete flush:true
				}
			} catch(Exception e) {
				if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
					flash.typ = "error"
					flash.message = """Der Artikel ${articleToDelete.getName()} mit der Kategorie (${articleToDelete.getCategory()})
									konnte nicht gelöscht werden, da er noch in mindestens einem Rechnungsposten verwendet wird"""
				} else {
				flash.typ = "error"
				flash.message = e.toString()
				}
			}
		}
		if(articleToDelete.errors.errorCount > 0) {
			flash.typ = "error"
			flash.message = articleToDelete.errors.toString()
		}
		render(view: "list", model: [articleInstanceList: Article.list()])
	}
	
	def updateArticle() {
		Article articleToEdit = Article.get(Integer.parseInt(params.get("articleId")))
		if (articleToEdit == null) {
			flash.typ = "error"
			flash.message = "Der zu bearbeitende Artikel existiert nicht"
			render(view: "list", model: [articleInstanceList: Article.list()])
		} else {
		render(view: "list", model: [articleInstanceList: Article.list(), articleToEdit: articleToEdit.getId()])
		}
	}
	
	@Transactional
	def saveArticle() {
		// Hole Parameter
		Article articleToUpdate = Article.get(Integer.parseInt(params.get("articleId")))
		String articleName = params.get("articleName");
		Category articleCategory = null
		if(!params.get("articleCategoryId").equals("null")) {
			articleCategory = Category.get(Integer.parseInt(params.get("articleCategoryId")))
		}
		String articleDescription = params.get("articleDescription");
		String articleStandardTaxRate = params.get("articleStandardTaxRate");
		String articleStandardGrossPrice = params.get("articleStandardGrossPrice");
		//Setze Standardwerte 
		if(!articleDescription ||articleDescription.isEmpty()) {
			articleDescription = 'n/a'
		}
		if(!articleStandardTaxRate || articleStandardTaxRate.isEmpty()) {
			articleStandardTaxRate = '19.00'
		}
		if(!articleStandardGrossPrice || articleStandardGrossPrice.isEmpty()) {
			articleStandardGrossPrice = '0.00'
		}
		if(articleToUpdate == null) {
			flash.typ = "error"
			flash.message = "Der zu aktualisierende Artikel existiert nicht"
			render(view: "list", model: [articleInstanceList: Article.list()])
			return
		}
		
		// Überprüfe übergebene Parameter für Article
		def validateResponse = articleService.validateArticle(articleCategory, articleName, articleStandardTaxRate, articleStandardGrossPrice);
		if(validateResponse.error) {
			flash.typ = validateResponse.typ
			flash.message = validateResponse.message
			render(view: "list", model: [articleInstanceList: Article.list(), articleToEdit: articleToUpdate.getId()])
			return
		}
		flash.typ = "success"
		flash.message = "Der Artikel mit der ID #${articleToUpdate.getId()} wurde erfolgreich geändert"
		articleToUpdate.setName(articleName)
		articleToUpdate.setCategory(articleCategory)
		articleToUpdate.setDescription(articleDescription)
		// Sollte keine Fehler werfen, wurde zuvor mit RegExp geprüft
		articleToUpdate.setStandardTaxRate(Double.valueOf(articleStandardTaxRate))
		articleToUpdate.setStandardGrossPrice(Double.valueOf(articleStandardGrossPrice))
		articleToUpdate.save flush: true
		if(articleToUpdate.errors.errorCount > 0) {
			if(articleToUpdate.errors.toString().contains("unique")) {
				Article alreadyExist = null;
				Article.list().each {
					if(it.equals(articleToUpdate) && it.getId() != articleToUpdate.getId()) {
						alreadyExist = it;
					}
				}
				flash.typ = "error"
				flash.message = "Ein identischer Artikel (gleicher Name und Kategorie) existiert bereits unter der ID #${alreadyExist.id}"
			} else {
				flash.typ = "error"
				flash.message = articleToUpdate.errors.toString()
			}
		}
		render(view: "list", model: [articleInstanceList: Article.list()])
	}
	
	/**
	 * Diese Methode erzeugt ein für Twitter-Typeahead passendes JSON-Objekt, welches alle existierende Artikel beinhaltet.
	 */
	def getArticleListAsJSON() {
		def keywords = ((String) params.get("keywords"))?.split(" ")
		String responseString = "[";
		Article.list().each {
			String[] tokens = (it.getName() + " " + it.getCategory().toString().replaceAll(" -", "") + " " + it.getDescription()).split(" ")
			for(int i=0; i < tokens.length; i++) {
				tokens[i] = /"${tokens[i]}"/
			}
			boolean matchWithKeyword = true;
			keywords.each {
				if(!tokens.toString().toLowerCase().contains(it.toLowerCase())) {
					matchWithKeyword = false
				}
			}
			if(matchWithKeyword) {
				responseString = responseString +
				/
				{
					"id": "${it.getId()}",
					"name": "${it.getName()}",
					"category": "${it.getCategory()}",
					"description": "${it.getDescription()}",
					"standardTaxRate": "${it.getStandardTaxRate()}",
					"standardGrossPrice": "${it.getStandardGrossPrice()}",
					"value": "${it.getName()}",
					"tokens": ${tokens}
					},
				/
			}
		}
		if(responseString.lastIndexOf(",") > 0) {
			responseString = responseString.substring(0, responseString.lastIndexOf(","))
		}
		responseString = responseString + "]"
		render(text: responseString, contentType: "text/json", encoding: "UTF-8")
	}
}