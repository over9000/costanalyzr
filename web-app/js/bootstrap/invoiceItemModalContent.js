$(function() {
	$("a[data-target=#invoiceItemModal]").click(function(ev) {
	    ev.preventDefault();
	    var target = $(this).attr("href");

	    // load the url and show modal on success
	    $("#invoiceItemModal").load(target, function() { 
	         $("#invoiceItemModal").modal("show");
	    });
	});
})