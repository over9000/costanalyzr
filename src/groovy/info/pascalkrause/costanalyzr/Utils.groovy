package info.pascalkrause.costanalyzr

import java.security.SecureRandom
import java.text.DecimalFormat

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
public class Utils {
	
	static private SecureRandom random = new SecureRandom();
	
	static String cutAtLastComma(String string) {
		if(string.lastIndexOf(",") > 0) {
			string = string.substring(0, string.lastIndexOf(","))
		}
	}
	
	/**
	 * Diese Methode rundet auf und schneidet alle, bis auf die ersten beiden, Nachkommastellen ab.  
	 */
	static Double cutDecimal(double price) {
		return (double) (Math.round(price*100.0f)/100.0f);
	}
	
	static public String getRandomString() {
		return new BigInteger(130, random).toString(32);
	}
	
	static String getContextPath() {
		return ContextUtil.getApplicationContext().getBean("grailsLinkGenerator").getContextPath()
	}
	
	/**
	 * Dump out attributes in HTML compliant fashion
	 */
	static String addPassedTagAttributes(attrs) {
		def outputString = ""
		attrs.each {k, v ->
				outputString = outputString + "$k=\"${v.encodeAsHTML()}\" "
		}
		return outputString
	}
}