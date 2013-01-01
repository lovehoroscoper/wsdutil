$(document).ready(function(){
    //focus username field
    //$("input:visible:enabled:first").focus();
    //flash error box
    $('#msg.errors').animate({ backgroundColor: 'rgb(187,0,0)' }, 30).animate({ backgroundColor: 'rgb(255,238,221)' }, 500);

    //flash success box
    $('#msg.success').animate({ backgroundColor: 'rgb(51,204,0)' }, 30).animate({ backgroundColor: 'rgb(221,255,170)' }, 500);
    
    $('#resetBtn').click(function(){
    	$("#fm1")[0].reset();
    });    

    $("div[class=input] input").each(function(){
    	$(this).focus(function(){
    		$(this).next("label").hide();
    	})
    	.blur(function(){
    		if($.trim($(this).val()) == ""){
    			$(this).val("").next("label").show();
    		}
    	});
    });
    $("#id_email").val("");
    $("#id_password").val("");
    
	//查询
	$("#submitBtn").click(function(){
		var returnInfo = "";
		alert("23");
		if(returnInfo != ""){
			alert(returnInfo);
		}else{
			$("#pageform").submit();
		}
	 });
    
    
});

