package info.pascalkrause.costanalyzr;

import java.util.Collection;
import java.util.Iterator;
import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsAnnotationConfiguration;
import org.hibernate.MappingException;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.RootClass;

/**
*
* @author Pascal Krause
* @version 1.0
*
*/
public class PreventForeignKeyCreation extends GrailsAnnotationConfiguration {
	
	private static final long serialVersionUID = 1;

	private boolean _alreadyProcessed;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void secondPassCompile() throws MappingException {
		super.secondPassCompile();

		if (_alreadyProcessed) {
			return;
		}

		for (PersistentClass pc : (Collection<PersistentClass>) classes
				.values()) {
			if (pc instanceof RootClass) {
				RootClass root = (RootClass) pc;
				if ("info.pascalkrause.costanalyzr.Category".equals(root
						.getClassName())) {
					for (Iterator iter = root.getTable()
							.getForeignKeyIterator(); iter.hasNext();) {
						ForeignKey fk = (ForeignKey) iter.next();
						fk.setName("category_fk_parent_id");
					}
				}
				if ("info.pascalkrause.costanalyzr.Article".equals(root
						.getClassName())) {
					for (Iterator iter = root.getTable()
							.getForeignKeyIterator(); iter.hasNext();) {
						ForeignKey fk = (ForeignKey) iter.next();
						fk.setName("article_fk_category_id");
					}
				}
				if ("info.pascalkrause.costanalyzr.Store".equals(root
						.getClassName())) {
					for (Iterator iter = root.getTable()
							.getForeignKeyIterator(); iter.hasNext();) {
						ForeignKey fk = (ForeignKey) iter.next();
						fk.setName("store_fk_concern_id");
					}
				}
				if ("info.pascalkrause.costanalyzr.Invoice".equals(root
						.getClassName())) {
					for (Iterator iter = root.getTable()
							.getForeignKeyIterator(); iter.hasNext();) {
						ForeignKey fk = (ForeignKey) iter.next();
						fk.setName("invoice_fk_store_id");
					}
				}
				if ("info.pascalkrause.costanalyzr.InvoiceItem".equals(root
						.getClassName())) {
					for (Iterator iter = root.getTable()
							.getForeignKeyIterator(); iter.hasNext();) {
						ForeignKey fk = (ForeignKey) iter.next();
						if(fk.getReferencedEntityName().equals("info.pascalkrause.costanalyzr.Article")) {
							fk.setName("invoice_item_fk_article_id");	
						}
						if(fk.getReferencedEntityName().equals("info.pascalkrause.costanalyzr.Invoice")) {
							fk.setName("invoice_item_fk_invoice_id");	
						}
						
					}
				}
			}
		}
		_alreadyProcessed = true;
	}
}