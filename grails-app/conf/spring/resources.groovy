// Place your Spring DSL code here
import info.pascalkrause.costanalyzr.ContextUtil

beans = {
	contextUtil(ContextUtil) { bean ->
		bean.factoryMethod = 'getInstance'
	}
}
