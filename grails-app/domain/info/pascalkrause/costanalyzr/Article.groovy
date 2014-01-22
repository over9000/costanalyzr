package info.pascalkrause.costanalyzr

import java.io.Serializable;

/**
 * 
 * @author Pascal Krause
 * @version 1.0
 * 
 */
class Article implements Serializable {

	static mapping = {
				table 'public.article'
				version false
				id generator:'increment', column:'id'
				name column: 'name'
				category column:'category_id'
				// Die Spalte description erhält im Datenbankschema den Standardwert n/a
				// Wird hier im GORM der Wert "defaultValue : 'n/a' eingetragen gibt es einen Fehler
				// Wird ein anderes Datenbankschema verwendet kann dies zu unerwarteten Problemen führen
				description column:'description'
				standardTaxRate column:'standard_tax_rate', defaultValue: "0.00"
				standardGrossPrice column: 'standard_gross_price', defaultValue: "0.00"
		   }
	
	Integer id
	String name
	Category category
	String description
	double standardTaxRate
	double standardGrossPrice
	
	/**
	 * Gleicht sich der Name zweier Artikel, so müssen sich die Kategorien der Artikel von einander unterscheiden 
	 */
    static constraints = {
		name blank: false, unique: 'category'
		category nullable:false
		standardTaxRate blank: false
		standardGrossPrice blank: false
    }

	/**
	 * Ein Artikel gleicht einem anderen genau dann, sobald Artikelname und Artikelkategorie übereinstimmen. 
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Article &&
		   obj.name.equals(this.name) &&
		   obj.category.equals(this.category)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		String stringToReturn = /${this.name} (${this.category?.toString()})/
		return stringToReturn
	}
}