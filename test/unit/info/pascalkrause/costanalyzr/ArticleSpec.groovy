package info.pascalkrause.costanalyzr

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class ArticleSpec {

    def setup() {
    }

    def cleanup() {
    }

    void "test method equals()"() {
		
		Category categoryFood = new Category()
		categoryFood.setName("Lebensmittel")
		
		Category categoryHousehold = new Category()
		categoryHousehold.setName("Haushalt")
		
		Article pizza = new Article()
		pizza.setName("Pizza")
		pizza.setCategory(categoryFood)
		pizza.setStandardGrossPrice(0)
		pizza.setStandardTaxRate(0)
		
		Article sausage = new Article()
		pizza.setName("Wurst")
		pizza.setCategory(categoryFood)
		pizza.setStandardGrossPrice(0)
		pizza.setStandardTaxRate(0)
		
		Article kitchenRoll = new Article()
		kitchenRoll.setName("Küchenrolle")
		kitchenRoll.setCategory(categoryHousehold)
		kitchenRoll.setStandardGrossPrice(0)
		kitchenRoll.setStandardTaxRate(0)
		
		assertEquals(true, sausage.equals(sausage))
		
		assertEquals(false, pizza.equals(sausage))
		assertEquals(false, sausage.equals(kitchenRoll))
		assertEquals(false, pizza.equals(kitchenRoll))
    }
	
	void "test method toString()"() {
		
		Category categoryFood = new Category()
		categoryFood.setName("Lebensmittel")
		
		Article pizza = new Article()
		pizza.setName("Pizza")
		pizza.setCategory(categoryFood)
		pizza.setStandardGrossPrice(0)
		pizza.setStandardTaxRate(0)
		
		assertEquals("Pizza (${pizza.getCategory().toString()})", pizza.toString())
	}
}