package info.pascalkrause.costanalyzr

import java.io.Serializable;

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class Category implements Serializable {

	static belongsTo = [parentCategory: Category]
	static hasMany = [childCategorys: Category]
	
	static mapping = {
		table 'public.category'
		version false
        id generator:'increment', column:'id'
		name column: 'name'
		parentCategory column:'parent_id'
   }
	
	Integer id
	String name
	Category parentCategory	
	
	/**
	 * Gleichen sich die Namen zweier Kategorien, so müssen die Vaterkategorien unterschiedlich sein. 
	 */
    static constraints = {
		name blank: false, unique: 'parentCategory'
		parentCategory nullable:true
    }
	
	@Override
	String toString() {
		String returnString
		if(parentCategory == null) {
			returnString = name
		} else {
			returnString = /${parentCategory.toString()} - ${name}/
		}
		
		return returnString
	}
	
	/**
	 * Gibt ein Liste mit allen momentan existierenden Hauptkategorien zurück.
	 * @return Eine Liste, welche alle momentan existierenden Hauptkategorien beinhaltet.
	 */
	static public List<Category> getTopLevelCategories() {
		List<Category> topLevelCategories = new ArrayList<Category>()
		Category.list().each {
			if(!it.getParentCategory()) {
				topLevelCategories.add(it)
			}
		}
		return topLevelCategories
	}
	
	/**
	 * Ein Kategorie ist genau dann gleich, wenn sie den selben Namen und die selben Väter in der selben Reihenfolge besitzt.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Category && obj.toString().equals(this.toString())) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isChildOf(Category parent) {
		if(this.getParentCategory() == null) {
			// Ist TopLevelCategory kann also keine Kinder haben
			return false;
		}
		return parent.isParentOf(this);
	}
	
	public boolean isParentOf(Category child) {
		if(child.getParentCategory() == null) {
			return false;
		}
		//Damit Toplevelkategorien nicht sich selber erkennen, wird immer die nächste geprüft
		if(this.equals(child.getParentCategory())) {
			return true;
		} else {
			isParentOf(child.getParentCategory())
		}
	}
	
	/**
	 * 
	 * @param categoryName The full name of the Category. The full name euqals the value of Category.toString()
	 * @return The Category with the passed name, or null if there is no Category with the passed name
	 */
	static public Category getCategoryByFullName(String categoryName) {
		List<String> tempList = new ArrayList<String>()
		tempList.add(categoryName);
		def categories = parseCategoriesByFullName(tempList)
		if(categories.size() == 1) {
			return categories.get(0)
		} else {
			return null
		}
	}
	
	/**
	 * 
	 * Diese Methode erzeugt aus einem einzelnen String, welcher den vollen Namen einer oder mehrerer Kategorien enthält.
	 * Enthält der String mehrere Kategorienamen, so müssen diese durch ein Komma getrennt sein.
	 * 
	 * Der String kann wie folgt aussehen.<br>
	 * Beispiel 1: Kategorie1<br>
	 * Beispiel 2: [Kategorie1]<br>
	 * Beispiel 3: Kategorie1, Kategorie2, ...<br>
	 * Beispiel 4: [Kategorie1, Kategorie2, ..]<br>
	 * Beispiel 5: Kategorie1  ,    Kategorie2    , ..<br>
	 * Beispiel 6: [   Kategorie1   ,    Kategorie2  , ..]<br>
	 * 
	 * Leerzeichen vor und nach einer Kategorie werden abschnitten.
	 * 
	 * Ein vollständiger Kategoriename entspricht dem Erzeugnis der Methode toString()
	 * 
	 * @param categoryNames Ein String mit Kategorienamen.
	 * @return Eine Liste mit den im String gefundenen Kategorien.
	 */
	static public List<Category> parseCategoriesByFullName(String categoryNames) {
		return parseCategoriesByFullName(categoryNames.replace("[", "").replace("]", "").split(",").toList())
	}
	
	/**
	 * 
	 * Diese Methode erzeugt aus einer Liste mit Strings, die jeweils einen vollständigen Kategorienamen repräsentieren müssen,
	 * einen Liste mit deren entsprechenden Kategorien.
	 * 
	 * Ein vollständiger Kategoriename entspricht dem Erzeugnis der Methode toString()
	 * 
	 * @param categoryNames Eine Liste mit Strings, welche vollständige Kategorienamen enthalten.
	 * @return Eine Liste mit Kategorien, die den übergebenen Kategorienamen entsprechen.
	 */
	static public List<Category> parseCategoriesByFullName(List<String> categoryNames) {
		List<Category> parsedCategories = new ArrayList<Category>()
		categoryNames.each {
			String currentName = it.toString().trim();
			Category.list().each {
				if(currentName.equals(it.toString())) {
					parsedCategories.add(it)
				}
			}
		}
		return parsedCategories
	}
}