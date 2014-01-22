package info.pascalkrause.costanalyzr

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional;

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class CategoryController {
	
	static allowedMethods = [saveCategory: "POST", deleteCategory:"POST", updateCategory:"POST", addCategory:"POST", getCategoryListAsJSON:"GET"]
	
	def index(Integer max) {
		redirect(action: "list")
	}
	
	def list() {
		render(view: "list", model: [categoryInstanceList: Category.list()])
	}
	
	@Transactional
	def addCategory() {
		// Hole Parameter
		String categoryName = params.get("categoryName");
		String parentCategoryId = params.get("parentCategoryId");
		Category parentCategory = null
		// Die parentCategory darf NULL sein, und da nur Strings übertragen werden können wird auf den String "null" geprüft
		if(!parentCategoryId.equals("null")) {
			parentCategory = Category.get(Integer.parseInt(parentCategoryId)) 
		}
		// Hier wird kein categoryService benutzt, da es momentan nur zwei Überprüfungen sind.
		// Lieber 3 Zeilen die redundant sind als eine komplett neuen Service
		if(!parentCategoryId.equals("null") && parentCategory == null) {
			flash.typ = "error"
			flash.message = "Die übergeordnete Kategorie der neuen Kategorie existiert nicht"
			render(view: "list", model: [categoryInstanceList: Category.list()])
			return
		}
		if(!categoryName || categoryName.isEmpty()) {
			flash.typ = "error"
			flash.message = "Es wurde kein Kategoriename übertragen"
			render(view: "list", model: [categoryInstanceList: Category.list()])
			return
		}
		Category newCategory = new Category()
		newCategory.setName(categoryName);
		newCategory.setParentCategory(parentCategory)
		newCategory.save flush:true
		flash.typ = "success"
		flash.message = "Die Kategorie ${newCategory.toString()} wurde unter der ID #${newCategory.id} angelegt"		
		if(newCategory.errors.errorCount > 0) {
			if(newCategory.errors.toString().contains("unique")) {
				Category alreadyExist = null;
				Category.list().each {
					if(it.equals(newCategory) && it.getId() != newCategory.getId()) {
						alreadyExist = it;
					}
				}
				flash.typ = "error"
				flash.message = "Eine identische Kategorie existiert bereits unter der ID #${alreadyExist.id}"
			} else {
				flash.typ = "error"
				flash.message = newCategory.errors.toString()
			}
		}
		render(view: "list", model: [categoryInstanceList: Category.list()])
	}

	@Transactional
	def deleteCategory() {
		Category categoryToDelete = Category.get(Integer.parseInt(params.get("categoryId")))
		if (categoryToDelete == null) {
			flash.typ = "error"
			flash.message = "Die zu löschende Kategorie existiert nicht"
		} else {
			flash.typ = "success"
			flash.message = "Die Kategorie ${categoryToDelete} wurde gelöscht"
			try {
				// Das ist momentan die einzige Möglichkeit einen DataIntegrityViolationException abzufangen.
				Category.withNewTransaction {
					Category category = Category.get(categoryToDelete.getId())
					category.delete flush:true
				}
			} catch(Exception e) {
				if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
					flash.typ = "error"
					flash.message = """Die Kategorie ${categoryToDelete} konnte nicht gelöscht werden, da sie selbst
									   oder mindestens eines ihrer Kategoriekinder in einem Artikel verwendet wird."""
				} else {
				flash.typ = "error"
				flash.message = e.toString()
				}
			}
		}
		if(categoryToDelete.errors.errorCount > 0) {
			flash.typ = "error"
			flash.message = categoryToDelete.errors.toString()
		}
		render(view: "list", model: [categoryInstanceList: Category.list()])
	}
	
	def updateCategory() {
		/**
		 * Hier muss darauf geachtet werden, dass die zu bearbeitende Kategorie nicht
		 * als Auswahlmöglichkeit für die parentCategory zur Verfügung gestellt wird.
		 */
		def parentCategorysToChoose = Category.list();
		Category categoryToEdit = Category.get(Integer.parseInt(params.get("categoryId")))
		parentCategorysToChoose.remove(categoryToEdit);
		if (categoryToEdit == null) {
			flash.typ = "error"
			flash.message = "Die zu bearbeitende Kategorie existiert nicht"
			render(view: "list", model: [categoryInstanceList: Category.list()])
		} else {
		render(view: "list", model: [categoryInstanceList: Category.list(), categoryToEdit: categoryToEdit.getId(), parentCategorysToChoose: parentCategorysToChoose])
		}
	}
	
	@Transactional
	def saveCategory() {
		Category categoryToUpdate = Category.get(Integer.parseInt(params.get("categoryId")))
		String newCategoryName = params.get("newCategoryName");
		String parentCategoryId = params.get("parentCategoryId");
		Category parentCategory = null
		if(!parentCategoryId.equals("null")) {
			parentCategory = Category.get(Integer.parseInt(parentCategoryId))
		}
		if(!parentCategoryId.equals("null") && parentCategory == null) {
			flash.typ = "error"
			flash.message = "Die übergeordnete Kategorie der zu aktualisierende Kategorie existiert nicht"
			render(view: "list", model: [categoryInstanceList: Category.list()])
			return
		}
		if(categoryToUpdate == null) {
			flash.typ = "error"
			flash.message = "Die zu aktualisierende Kategorie existiert nicht"
			render(view: "list", model: [categoryInstanceList: Category.list()])
			return
		}
		/**
		 * Hier muss darauf geachtet werden, dass die parentCategory nicht die zu aktualisierende Kategorie ist.
		 */
		if(!parentCategoryId && !parentCategoryId?.isEmpty() && categoryToUpdate.getId() == parentCategory.getId()) {
			flash.typ = "error"
			flash.message = "Die zu aktualisierende Kategorie darf nicht auf sich selbst verweisen"
			render(view: "list", model: [categoryInstanceList: Category.list(), categoryToEdit: categoryToUpdate.getId()])
			return
		}
		// Die neue Vaterkategorie darf nicht Kind der zu aktualisierenden Kategorie sein. (Endlosschleife)
		if(parentCategory.isChildOf(categoryToUpdate)) {
			flash.typ = "error"
			flash.message = "Die übergeordnete Kategorie darf kein Kind der zu aktualisierende Kategorie sein"
			render(view: "list", model: [categoryInstanceList: Category.list(), categoryToEdit: categoryToUpdate.getId()])
			return
		}
		if(newCategoryName == null) {
			flash.typ = "error"
			flash.message = "Es wurde kein Kategoriename übertragen"
			render(view: "list", model: [categoryInstanceList: Category.list(), categoryToEdit: categoryToUpdate.getId()])
		} else {
			String oldCategoryName = categoryToUpdate;
			categoryToUpdate.setName(newCategoryName)
			categoryToUpdate.setParentCategory(parentCategory)
			categoryToUpdate.save flush: true
			flash.typ = "success"
			flash.message = "Die Kategorie mit der ID #${categoryToUpdate.getId()} wurde von ${oldCategoryName} auf ${categoryToUpdate} geändert"
			if(categoryToUpdate.errors.errorCount > 0) {
			if(categoryToUpdate.errors.toString().contains("unique")) {
				Category alreadyExist = null;
				Category.list().each {
					if(it.equals(categoryToUpdate) && it.getId() != categoryToUpdate.getId()) {
						alreadyExist = it;
					}
				}
				flash.typ = "error"
				flash.message = "Eine identische Kategorie existiert bereits unter der ID #${alreadyExist.id}"
			} else {
				flash.typ = "error"
				flash.message = categoryToUpdate.errors.toString()
			}
		}
			render(view: "list", model: [categoryInstanceList: Category.list()])
		}
	}
	
	/**
	 * Diese Methode erzeugt ein für Twitter-Typeahead passendes JSON-Objekt, welches alle existierende Kategorien beinhaltet.
	 */
	def getCategoryListAsJSON() {
		
		// Hole Parameter
		def keywords = ((String) params.get("keywords"))?.split(" ")
		// Werden die Vaterkategorien ausgewählt, muss es auch die Möglichkeit geben keine auszuwählen, um eine Kategorie zu einer TopLevelKategorie zu machen.
		def noSelectionAvailable = params.get("noSelectionAvailable")
		String responseString = "[";
		if(noSelectionAvailable != null) {
			responseString = responseString +
			/
			{
				"id": "null",
				"name": "Keine Kategorie",
				"value": "Keine Kategorie",
				"tokens": [""]
				},
			/
		}
		Category.list().each {
			String[] tokens = it.toString().replaceAll(" -", "").split(" ")
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
					"name": "${it.toString()}",
					"value": "${it.toString()}",
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