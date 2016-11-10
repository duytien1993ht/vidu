/**
 * 
 */
$(document).ready(function(){
	$('#left ul li > a').click(function(){
		$(this).next().toggleClass('active');
	});
	$('.a1').click(function(){
		$('.content').toggle(5000);
	});
	$('.a2').click(function(){
		$('.content').css({
			'background':'none'
			});
	});
	$('.a3').click(function(){
		$('.content').css({
			'background-color':'white'
			});
	});
	$('#fr').validate({
		 errorPlacement: function (error, element) {
		      error.insertAfter(element);
		    },
		    rules: {
		    	ten: {
		    		required: true,
		    		maxlength: 20,
		    		email: true
		    	},
		        pass: {
		        	required: true,
		        	minlength: 6,
		        	maxlength: 20
		        }
		    },
		    messages: {
		    	ten: {
		    		email: '<span style=\"color: red; font-weight: bold;\">Username không đúng định dạng email</span>',
		    		required: '<span style=\"color: red; font-weight: bold;\">Username không được để trống</span>',
		    		maxlength: '<span style=\"color: red; font-weight: bold;\">Username phải nhỏ hơn 20 kí tự</span>'
		    	},
		    	pass: {
		    		required: '<span style=\"color: red; font-weight: bold;\">Password không được để trống</span>',
		    		minlength: '<span style=\"color: red; font-weight: bold;\">Password phải lớn hơn 6 kí tự</span>',
		    		maxlength: '<span style=\"color: red; font-weight: bold;\">Password phải nhỏ hơn 20 kí tự</span>'
		    	}
		    }
	});
});