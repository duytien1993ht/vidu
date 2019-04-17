
// Validate form dang ki
$(document).ready(function(){

            $('#namSinh').datepicker({
              autoclose: true,
              todayHighlight: true,
              format: 'yyyy-mm-dd'
            }).on('change',function(){
                //$(this).valid();
            });

            $('.fa').click(function(){
                $('#namSinh').trigger("focus");
            });

            $('#cancel').click(function(e){
                window.location = "views/chuyentrang.jsp"
            });
            
            $('input[type="reset"]').on('click',function(e){
            	e.preventDefault();
            	$('.message-error ul').empty();
                $('.message-error').hide();
                $(this).closest('form').get(0).reset();
            });
            
            $('#select1').on('change',function(){
            	var city = $("option:selected",this).text();
            	$.ajax({
            		url:'${pageContext.request.contextPath}/ajaxRegister.do',
            		type:'get',
            		data: 'city='+city,
            		success:function(data){
            	      $('#select2').empty();
                      for(var i = 0;i < data.length; i++){
                        $('#select2').append(new Option(data[i],data[i]));
                      }
                   }
            	});
      
             });

            $.validator.addMethod('regexEmail',function(value,element){
                return this.optional(element)|| /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/i.test(value);
            },'E-mailアドレス must follow format email@example.com');
            
            $.validator.addMethod('fullSize',function(value,element){
                return this.optional(element)|| /^[^ -~｡-ﾟ\x00-\x1f\t]+$/u.test(value);
            },'Khong dung fullSize');

            $.validator.addMethod("validDate",function(value,element,arg){
               var arr = value.split("-");
               var yy = parseInt(arr[0]);
               var mm = parseInt(arr[1]);
               var dd = parseInt(arr[2]);
               var list = [31,28,31,30,31,30,31,31,30,31,30,31];
               if(mm==0||dd==0||yy==0) return false;
               if(mm==1||mm>2){
                if(dd>list[mm-1]){
                return false;
              }
            }
            if(mm==2){
              if((yy%4==0&&!yy%100==0)||yy%400==0){
                if(dd>29){
                    return false;
                }
                }else{
                if(dd>28){
                    return false;
                }
              }
            }
            return true;
            },"Vui long nhap dung ngay thang");

            $.validator.addMethod("formDate",function(value,element){
                return this.optional(element)||/^\d{4}\-(0[1-9]|1[012])\-(0[1-9]|[12][0-9]|3[01])$/i.test(value)
            },"生年月日には日付を表す文字列を(yyyy-mm-ddの形式で)入力してください。");

            var v = $('#form').validate({
                onfocusout: false,
                onkeyup: false,
                onclick: false,
                focusCleanup: false,
                rules:{
                   email:{
                    required:true,
                    regexEmail:true,
                    remote:{
                    	url:'${pageContext.request.contextPath}/ajaxExist.do',
                        type:'get',
                        data: {
                        	email: function(){
                        		return $('#email').val();
                        	}
                        }
                    }
                   },
                   password:{
                    required:true,
                    minlength:8
                   },
                   repassword:{
                    required:function(){
                        return $('#password').val().length>=8
                    },
                    equalTo:{
                        param: '#password',
                        depends:function(element){
                           return $('#password').val().length>=8
                        }
                    } 
                   },
                   ho:{
                    required:true,
                    fullSize:true
                   },
                   ten:{
                    required:true
                   },
                   namSinh:{
                	required:true,
                    date:true,
                    formDate:true,
                    validDate:true
                   }
                },
                messages:{
                   email:{
                    required:"E-mailアドレスを入力してください。",
                    remote:"Loi mat roi"
                   },
                   password:{
                    required:"パスワードを入力してください。",
                    minlength:"パスワード must have as least 8 characters"
                   },
                   repassword:{
                    required:"身分証明書を入力してください。",
                    equalTo: "パスワードの確認 doesn't match"
                   },
                   ho:{
                    required:"彼らを入力してください。"
                   },
                   ten:{
                    required:"名前を入力してください。"
                   },
                   namSinh:{
                    date:"生年月日には日付を表す文字列を(yyyy-mm-ddの形式で)入力してください。",
                    required:"生年月日には日付を表す文字列 not blank。"
                   }
                },
  /*               groups:{
                   vali : "email password repassword ho ten namSinh"
                }, */
                //errorLabelContainer: '#error',
                showErrors: function(errorMap, errorList) {
                    var errors = this.numberOfInvalids(); // <- NUMBER OF INVALIDS

                    console.log(errorMap);
                    $.each(errorMap, function(key, value) {
                    	$('.message-error ul').append($('<li>').append(value));
                        var parent = $('[name="' + key + '"]').parent();
                    });

                    this.defaultShowErrors(); // <- ENABLE default MESSAGES
                },
                errorClass:"validationError",
                highlight: function(element, errorClass, validClass) {
                   // $(element).addClass(errorClass).removeClass(validClass);// nếu dùng bắt buộc khai báo errorClass, validClass
                    $(element).addClass('mau');
                },
                unhighlight: function(element, errorClass, validClass) {
                   // $(element).removeClass(errorClass).addClass(validClass);
                    $(element).removeClass('mau');
                },
                errorPlacement: function(error,element) {
                    return true;
                },
                invalidHandler: function(form, validator) {
                  form.preventDefault();
                  var errors = validator.numberOfInvalids();
                  if (errors) {
                    $('.message-error').show();
                    var length = validator.errorList.length;                   
                    //validator.errorList[length-1].element.focus(); // chỉ định phần tử cuối cùng sẽ focus khi có lỗi
                    //validator.errorList[0].element.focus(); // chỉ định phần tử đầu tiên sẽ focus khi có lỗi
                    $('.message-error ul').empty();
                  }
                },
               submitHandler: function(form,event) {
            	    event.preventDefault();
            	    var email = form.email.value;
            	    $('.message-error ul').empty();
            	    $.ajax({
                        url:'${pageContext.request.contextPath}/ajaxExist.do',
                        type:'get',
                        data:'email='+email,
                        success:function(data){
                          if(data=='false'){
                        	  $('.message-error').show();
                        	  $('.message-error ul').append($('<li>').append('Email da ton tai'));
                          }else{
                        	  $('.message-error ul').empty();
                        	  $('.message-error').hide();
                        	  swal(  
                                      'Success!',
                                      'Register success',
                                      'success'
                              );
                        	  v.resetForm(); 
                              form.reset();
                          }
                       }
                    });
            	   
               }
            });
        });


-------------------------------------------------------------------------------------------------------
  // Validate danh sach table
  
  $(document).ready(function(){
    	   // kiem tra gia tri cac hang co giong nhau hay khong
    	   $.validator.addMethod('duplicate',function(value,element){
    		   var acc = [];
    		   var check = true;
    		   if(value.trim() != ""){
    			   $("#myTable tbody tr").each(function(index){
                       var a = $(this).find("td:nth-child(2) input[type='text']").val();
                       // neu == gia tri va chua co trong mang
                       if(value == a && acc.indexOf(a) < 0) {
                           acc.push(a);
                       }
                       // neu == gia tri va da co trong mang
                       else if(value == a && acc.indexOf(a) >= 0) {
                           check = false;
                           // thoat vong lap each
                           return false;
                       }
                   });
    		   }
    		   return check;
           },'message');
    	   
    	   var validator = $("form").validate({
    		   onfocusout: false,
               onkeyup: false,
               onclick: false,
               focusCleanup: false,
    	       showErrors: function(errorMap, errorList) {
    	            /* $(".errormsg").html($.map(errorList, function(el) {
    	                return el.message;
    	            }).join(", ")); */
    	            $('#check ul').empty();
    	            $.each(errorMap,function(key,value){
    	            	$('#check ul').append('<li>').append(value);
    	            });
    	            this.defaultShowErrors();
    	        },
    	        wrapper: "span",
    	        errorClass:"validationError",
    	        errorPlacement: function(error,element) {
                    return true;
                },
                highlight: function (element, errorClass) {
                    $(element).addClass('mau');
                },
                unhighlight: function (element, errorClass) {
                    $(element).removeClass('mau');
                }
    	    });
    	   
    	   
    	   $("input[type='submit']").click(function(e){
    		   
    		   $('#myTable tbody tr').each(function(index) {
                   index = index+1;
                   var district = $(this).find("td:nth-child(2) input[type='text']").val();
                   var city = $(this).find("td:nth-child(3) input[type='text']").val();
                   var arr2 = new Array();
                   arr2.push(district);
                   arr2.push(city);
                   
                   $(this).find("td:nth-child(2) input[type='text']").rules('add', {
                       required: function(){
                    	   // kiem tra so phan tu kieu String trong mang co >0 va <2
                    	   return (arr2.filter(String).length > 0 && arr2.filter(String).length < 2);
                       },
                       duplicate:true,
                       minlength: 2,
                       messages: {
                           required: "Dong "+index+" cot 1 : Enter Reg Number",
                           duplicate: function(params,element){
                        	   return "Dong so "+($(element).parents("tr").index()+1)+" bi trung lap ";
                           },
                           minlength: "Dong "+index+" cot 1 : Enter at least {0} characters",
                       }
                   });
                   $(this).find("td:nth-child(3) input[type='text']").rules('add', {
                       required: function(){
                           return (arr2.filter(String).length > 0 && arr2.filter(String).length < 2);
                       },
                       minlength: 2,
                       messages: {
                           required: "Dong "+index+" cot 2 : Enter Reg Number",
                           minlength: "Dong "+index+" cot 2 : Enter at least {0} characters",
                       }
                   });
               });
    	   });
    	   
    	   $("form").data("validator").settings.submitHandler = function (form){
    		   alert("Thanh cong");
    	   }
       });
