$(document).ready(function() {
	$(document).on("change", ".buildInvoiceFlowAddFormFieldsetDisabler", buildInvoiceFlowToggleAddFormFieldset);
});


function buildInvoiceFlowToggleAddFormFieldset() {
	var selectedValue = $(".buildInvoiceFlowAddFormFieldsetDisabler").val();
	if(selectedValue === 'noSelection' ) {
		 $('.buildInvoiceFlowAddFormFieldset').prop('disabled', false);
	} else {
		 $('.buildInvoiceFlowAddFormFieldset').prop('disabled', true);
	}
}