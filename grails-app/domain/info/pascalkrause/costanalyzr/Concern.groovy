package info.pascalkrause.costanalyzr

import java.io.Serializable;

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class Concern implements Serializable {

	static hasMany = [concernStores: Store]
	
	static mapping = {
		cache usage:'read-write'
		table 'public.concern'
		version false
        id generator:'increment', column:'id'
		name column: 'name'
   }
	
	Integer id
	String name
	
	/**
	 * Der Name eines Konzerns muss einzigartig sein
	 */
    static constraints = {
		name blank: false, unique: true
    }
	
	String toString() {
		return name
	}
}