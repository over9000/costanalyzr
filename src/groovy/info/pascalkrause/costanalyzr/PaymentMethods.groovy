package info.pascalkrause.costanalyzr

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
public enum PaymentMethods {
	CASH("Bar"), CASH_CARD("EC-Karte"), DEBIT("Lastschrift")
	
	 final String val
     PaymentMethods(String name) { this.val = name }
	 
	 String toString() { val }
	 String getKey() { name() }
}