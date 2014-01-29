package info.pascalkrause.costanalyzr

import java.io.Serializable;

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class Store implements Serializable {

	static belongsTo = [concern: Concern]
	
   	static mapping = {
		cache usage:'read-only'
		table 'public.store'
		version false
        id generator:'increment', column:'id'
		concern column: 'concern_id'
		zipcode column:'zipcode'
		streetName column: 'street_name'
		streetNumber column: 'street_number'
		city column: 'city'
		country column: 'country', type: PGEnumUserType, params : [ enumClassName: "info.pascalkrause.costanalyzr.AvailableCountrys"]
   }
	
	Integer id
	String city
	String zipcode
	Concern concern
	String streetName
	String streetNumber
	AvailableCountrys country
	
	/**
	 * Gehört eine Filiale dem selben Konzern an, so muss diese sich mindestens in einem der folgenden Attribute von anderen unterscheiden.<br>
	 * Land<br>
	 * Stadt<br>
	 * PLZ<br>
	 * Straßenname<br>
	 * Hausnummer<br>
	 */
    static constraints = {
		city blank:false
		concern nullable: false, unique: ['country', 'city', 'zipcode', 'streetName', 'streetNumber']
		zipcode blank: false
		streetName blank:false
		streetNumber blank:false
		country blank:false
    }
	
	@Override
	String toString() {
		String returnString = /${concern.getName()}, ${city}, ${zipcode}, ${streetName}, ${streetNumber}, ${country}/
		return returnString
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Store &&
			obj.city.equals(this.city) &&
			obj.zipcode.equals(this.zipcode) &&
			obj.concern.equals(this.concern) &&
			obj.streetName.equals(this.streetName) &&
			obj.streetNumber.equals(this.streetNumber) &&
			obj.country.equals(this.country)) {
			return true;
		} else {
			return false;
		}
	}
}