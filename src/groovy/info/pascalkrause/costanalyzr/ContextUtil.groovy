package info.pascalkrause.costanalyzr;

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware;

import groovy.lang.Singleton;
import javax.sql.DataSource

/**
 *
 * @author Pascal Krause
 * @version 1.0
 *
 */
@Singleton
class ContextUtil implements ApplicationContextAware {
    private ApplicationContext context

    void setApplicationContext(ApplicationContext context) {
        this.context = context
    }

    static ApplicationContext getApplicationContext() {
        getInstance().context
    }
	
	static Object getBean(String name) {
		getApplicationContext().getBean(name)
	 }
	
	static DataSource getDataSource() {
		getApplicationContext().getBean("dataSource")
	 }
}