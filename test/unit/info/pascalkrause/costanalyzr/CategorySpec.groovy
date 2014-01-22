package info.pascalkrause.costanalyzr

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
class CategorySpec  {

    def setup() {

    }

    def cleanup() {
    }

	void "test method toString()"() {
		Category mainCategory = new Category();
		mainCategory.setName("Lebensmittel");

		Category childCategory = new Category();
		childCategory.setName("Fertigprodukte");
		childCategory.setParentCategory(mainCategory);

		Category secondChildCategory = new Category();
		secondChildCategory.setName("Pizza");
		secondChildCategory.setParentCategory(childCategory);
		
		assertEquals("Lebensmittel", mainCategory.toString());
		assertEquals("Lebensmittel - Fertigprodukte", childCategory.toString());
		assertEquals("Lebensmittel - Fertigprodukte - Pizza", secondChildCategory.toString());
		
	}
	
	
	void "test method getTopLevelCategories()"() {
		
		Category mainCategory = new Category();
		mainCategory.setName("Lebensmittel");
		mainCategory.save();

		Category childCategory = new Category();
		childCategory.setName("Fertigprodukte");
		childCategory.setParentCategory(mainCategory);
		childCategory.save();

		Category secondChildCategory = new Category();
		secondChildCategory.setName("Pizza");
		secondChildCategory.setParentCategory(childCategory);
		secondChildCategory.save();
		
		Category secondMainCategory = new Category();
		secondMainCategory.setName("Wohnung");
		secondMainCategory.save();
		
		Category thirdChildCategory = new Category();
		thirdChildCategory.setName("Küche");
		thirdChildCategory.setParentCategory(secondMainCategory);
		thirdChildCategory.save();
		
		List<Category> allMainCategories = new ArrayList<Category>();
		allMainCategories.add(mainCategory);
		allMainCategories.add(secondMainCategory);
		
		assertEquals(allMainCategories, Category.getTopLevelCategories());
		
		Category thirdMainCategory = new Category();
		thirdMainCategory.setName("Troll");
		thirdMainCategory.save();
		
		assertEquals(false, Category.getTopLevelCategories().equals(allMainCategories));
	}
		
	void "test method equals()"() {
		Category mainCategory = new Category();
		mainCategory.setName("Lebensmittel");

		Category childCategory = new Category();
		childCategory.setName("Fertigprodukte");
		childCategory.setParentCategory(mainCategory);

		Category secondChildCategory = new Category();
		secondChildCategory.setName("Pizza");
		secondChildCategory.setParentCategory(childCategory);

		assertEquals(true, mainCategory.equals(mainCategory));
		assertEquals(true, childCategory.equals(childCategory));
		assertEquals(true, secondChildCategory.equals(secondChildCategory));

		assertEquals(false, secondChildCategory.equals(childCategory));
		assertEquals(false, secondChildCategory.equals(mainCategory));
		
		assertEquals(false, mainCategory.equals(childCategory));
		assertEquals(false, mainCategory.equals(secondChildCategory));
		
		assertEquals(false, childCategory.equals(secondChildCategory));
		assertEquals(false, childCategory.equals(mainCategory));

	}
	
	void "test method IsChildOf()"() {
		Category mainCategory = new Category();
		mainCategory.setName("Lebensmittel");

		Category childCategory = new Category();
		childCategory.setName("Fertigprodukte");
		childCategory.setParentCategory(mainCategory);

		Category secondChildCategory = new Category();
		secondChildCategory.setName("Pizza");
		secondChildCategory.setParentCategory(childCategory);

		assertEquals(true, childCategory.isChildOf(mainCategory));
		assertEquals(true, secondChildCategory.isChildOf(mainCategory));
		assertEquals(true, secondChildCategory.isChildOf(childCategory));
		
		assertEquals(false, mainCategory.isChildOf(childCategory));
		assertEquals(false, mainCategory.isChildOf(secondChildCategory));
		assertEquals(false, childCategory.isChildOf(secondChildCategory))
		
		assertEquals(false, mainCategory.isChildOf(mainCategory));
		assertEquals(false, secondChildCategory.isChildOf(secondChildCategory));
		assertEquals(false, childCategory.isChildOf(childCategory));
	}
	
	void "test method IsParentOf()"() {
		Category mainCategory = new Category();
		mainCategory.setName("Lebensmittel");

		Category childCategory = new Category();
		childCategory.setName("Fertigprodukte");
		childCategory.setParentCategory(mainCategory);

		Category secondChildCategory = new Category();
		secondChildCategory.setName("Pizza");
		secondChildCategory.setParentCategory(childCategory);

		assertEquals(true, mainCategory.isParentOf(childCategory));
		assertEquals(true, mainCategory.isParentOf(secondChildCategory));
		assertEquals(true, childCategory.isParentOf(secondChildCategory));

		assertEquals(false, secondChildCategory.isParentOf(childCategory));
		assertEquals(false, secondChildCategory.isParentOf(secondChildCategory));
		assertEquals(false, childCategory.isParentOf(mainCategory));
		assertEquals(false, childCategory.isParentOf(childCategory));
		assertEquals(false, secondChildCategory.isParentOf(mainCategory));
		
		assertEquals(false, mainCategory.isParentOf(mainCategory));
		assertEquals(false, childCategory.isParentOf(childCategory));
		assertEquals(false, secondChildCategory.isParentOf(secondChildCategory));
	}
	
	void "test method getCategoryByFullName()"() {
		Category mainCategory = new Category();
		mainCategory.setName("Lebensmittel");
		mainCategory.save()
		
		Category childCategory = new Category();
		childCategory.setName("Fertigprodukte");
		childCategory.setParentCategory(mainCategory);
		childCategory.save()
		
		Category secondChildCategory = new Category();
		secondChildCategory.setName("Pizza");
		secondChildCategory.setParentCategory(childCategory);
		secondChildCategory.save()
		
		assertEquals(mainCategory, Category.getCategoryByFullName(mainCategory.toString()));
		assertEquals(childCategory, Category.getCategoryByFullName(childCategory.toString()));
		assertEquals(secondChildCategory, Category.getCategoryByFullName(secondChildCategory.toString()));
	}
	
	void "test method parseCategoryNames()"() {
		Category mainCategory = new Category();
		mainCategory.setName("Lebensmittel");
		mainCategory.save()
		
		Category childCategory = new Category();
		childCategory.setName("Fertigprodukte");
		childCategory.setParentCategory(mainCategory);
		childCategory.save()
		
		Category secondChildCategory = new Category();
		secondChildCategory.setName("Pizza");
		secondChildCategory.setParentCategory(childCategory);
		secondChildCategory.save()
		
		Category thirdChildCategory = new Category();
		thirdChildCategory.setName("Lasagne");
		thirdChildCategory.setParentCategory(childCategory);
		thirdChildCategory.save()
		
		List<Category> allCategories = new ArrayList<Category>();
		allCategories.add(mainCategory)
		allCategories.add(childCategory)
		allCategories.add(secondChildCategory)
		allCategories.add(thirdChildCategory)
		
		assertEquals(allCategories, Category.parseCategoriesByFullName(allCategories.toString()))
		
		String allCategoriesString = "${mainCategory}, ${childCategory}, ${secondChildCategory}, ${thirdChildCategory}"
		String allCategoriesStringWithSpaces = "   ${mainCategory}  ,${childCategory},   ${secondChildCategory} ,       ${thirdChildCategory}"
		assertEquals(allCategories, Category.parseCategoriesByFullName(allCategoriesString))
		assertEquals(allCategories, Category.parseCategoriesByFullName(allCategoriesStringWithSpaces))
		
		allCategoriesString = "[${mainCategory}, ${childCategory}, ${secondChildCategory}, ${thirdChildCategory}]"
		allCategoriesStringWithSpaces = "[   ${mainCategory}  ,${childCategory},   ${secondChildCategory} ,       ${thirdChildCategory}]"
		assertEquals(allCategories, Category.parseCategoriesByFullName(allCategoriesString))
		assertEquals(allCategories, Category.parseCategoriesByFullName(allCategoriesStringWithSpaces))
		
		List<String> allCategoriesAsStringList = new ArrayList<String>()
		allCategoriesAsStringList.add("${mainCategory}")
		allCategoriesAsStringList.add("  ${childCategory}")
		allCategoriesAsStringList.add("${secondChildCategory}")
		allCategoriesAsStringList.add("  ${thirdChildCategory}  ")
		
		assertEquals(allCategories, Category.parseCategoriesByFullName(allCategoriesAsStringList))
		
		List<Category> testList1 = new ArrayList<Category>();
		testList1.add(childCategory)
		testList1.add(secondChildCategory)
		testList1.add(thirdChildCategory)
		
		assertEquals(testList1, Category.parseCategoriesByFullName(testList1.toString()))
		
		String testList1CategoriesString = "${childCategory}, ${secondChildCategory}, ${thirdChildCategory}"
		String testList1CategoriesStringWithSpaces = "   ${childCategory},   ${secondChildCategory} ,       ${thirdChildCategory}"
		assertEquals(testList1, Category.parseCategoriesByFullName(testList1CategoriesString))
		assertEquals(testList1, Category.parseCategoriesByFullName(testList1CategoriesStringWithSpaces))
		
		testList1CategoriesString = "[${childCategory}, ${secondChildCategory}, ${thirdChildCategory}]"
		testList1CategoriesStringWithSpaces = "[   ${childCategory},   ${secondChildCategory} ,       ${thirdChildCategory}]"
		assertEquals(testList1, Category.parseCategoriesByFullName(testList1CategoriesString))
		assertEquals(testList1, Category.parseCategoriesByFullName(testList1CategoriesStringWithSpaces))
		
		List<String> testList1CategoriesAsStringList = new ArrayList<String>()
		testList1CategoriesAsStringList.add("  ${childCategory}")
		testList1CategoriesAsStringList.add("${secondChildCategory}")
		testList1CategoriesAsStringList.add("  ${thirdChildCategory}  ")
		
		assertEquals(testList1, Category.parseCategoriesByFullName(testList1CategoriesAsStringList))
	}
}