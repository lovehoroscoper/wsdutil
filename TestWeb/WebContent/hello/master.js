/* ******************************************* 
*
*	Fat-Man Collective - www.fat-man-collective.com
*
*	Coded by Yann Lorber - www.yannlorber.fr
*	August 2009
*
******************************************* */


var currentId = "";	

 
function jumpSection() {

	var target = currentId;
	var target_offset = $("#"+target+"_box").offset();
	var target_top = target_offset.top;		
	
	$(document).ready(function() 
	{		
		var animating = true;	
		$('html, body').animate({scrollTop:(target_top-30)}, 400, "backout", function() { 
			if(animating) {
				animating=false; 				
				
				//Call Flash
				if (navigator.appName.indexOf("Microsoft") != -1) {
					window["sample"].fatJumped();
				} else {
					return document["sample"].fatJumped();
				}
			}
		});					
	});	
}

		/* ******************************************* 
		*
		*	Flash interaction
		*
		******************************************* */
		
		function thisMovie(movieName)
		{

		}	


/* ******************************************* 
*
*	On document ready
*
******************************************* */
$(document).ready(function() 
{
		
		$(".biggy").css("text-shadow","#999999 1px 1px 2px");
		$(".shadow").css("text-shadow","#cccccc 1px 1px 3px");
		$("h2,h3").css("text-shadow","#000000 0 0 0");
		
		var main_height = $("#main").height();
		var currentY = 0; 			// Y position is stored when curtain is called
		var diffY = 0; 				// Y difference to detect scroll up or down
		var status_curtain = 0; 	// curtain is open or not		
		var state = "";				// we, love, etc...
			
		/* ******************************************* 
		*
		*	On window load		
		*
		******************************************* */
		$(window).load(preloadImages);


		/* ******************************************* 
		*
		*	Preload images		
		*
		******************************************* */
		function preloadImages() 
		{
			var site_images = new Array();
			site_images = (
				"img/menu_bg.gif",
				"img/menu_on_hello.gif",
				"img/menu_on_made.gif",
				"img/menu_on_shminking.gif",
				"img/menu_on_we.gif",
				"img/menu_on_love.gif",	
				"img/icon_sprite.jpg",																
				"work/thumb/awards.gif",
				"work/thumb/barcelona.jpg",
				"work/thumb/cadbury2.jpg",
				"work/thumb/digital-creatives.gif",
				"work/thumb/digital-creatives2.gif",
				"work/thumb/digital-creatives3.gif",
				"work/thumb/digital-creatives4.gif",
				"work/thumb/digital-creatives5.gif",												
				"work/thumb/dodaq.gif",
				"work/thumb/iamstudios.gif",
				"work/thumb/london.jpg",
				"work/thumb/nokia.gif",
				"work/thumb/nokiangage.jpg",
				"work/thumb/ua.gif",
				"work/thumb/fat.gif",
				"work/thumb/fatv2.gif",
				"work/thumb/fatv3.gif",								
				"work/thumb/zwyer.gif"
			);		

			for(var i = 0; i<site_images.length; i++)
				$("<img>").attr("src", site_images[i]);
			
			
			// Intro of the 2 columns
			$("#main").fadeIn("slow", initCurtain);	
			$("#sidebar").animate({opacity: 1}, 500).fadeIn("slow", hideIcons);							


			// Gets Y Position
			this.checkPosition;			
		}

		


		/* ******************************************* 
		*
		*	Checks Y Position		
		*
		******************************************* */
		function checkPosition()
		{

				
			var posy = $(window).scrollTop();


        	if  (posy <= 300)
        	{
	        	state = "we";        	
				$('#we img').fadeIn();
			}else{
				$('#we img').fadeOut();
	
			} 
			      	
        	if  (posy > 300 && posy <= 780)
        	{
	        	state = "love";
	        	$('#love img').fadeIn();
			}else{
				$('#love img').fadeOut();
			} 

        	if  (posy > 780 && posy <= 1300)
        	{
	        	state = "made";              	
        	
				$('#made img').fadeIn();
			}else{
				$('#made img').fadeOut();
			} 

        	if  (posy > 1300 && posy <= 1900)
        	{
	        	state = "alchemy";  
	        	$('#pusher').fadeIn(200);      	
				$('#alchemy img').fadeIn();
			}else{
				$('#alchemy img').fadeOut();
	        	$('#pusher').fadeOut();   
			} 
			
        	if  (posy > 1900 && posy <=  2350)
        	{
	        	state = "shminking";           	
				$('#shminking img').fadeIn();
			}else{
				$('#shminking img').fadeOut();
	
			} 	
											
        	if  (posy >  2350)
        	{
	        	state = "hello";           	        	
				$('#hello img').fadeIn();
			}else{
				$('#hello img').fadeOut();
	
			} 		

        }


		$(".alchemy").hover(function() {			
				$('#pusher').fadeIn(200);
			},function () {		
		});



		/* ********************************************
		 * Side menu effect 
		 * *******************************************/

		//WE zone
		
		$(window).scroll( checkPosition );
		

		
	

		/* ******************************************* 
		*
		*	Curtain: init
		*
		******************************************* */

		function initCurtain() {
			

			//$("#blackcurtain").css("height",  $(document).height() );	 // height of the curtain	
		
			// get url
			var source = window.location.toString(); 
			var urlanchor = source.split("#");
			

			

			// is there a curtain to load ?	
			if( urlanchor[1] != "" && urlanchor[1] 
			&& urlanchor[1]!="we" 
			&& urlanchor[1]!="love" 
			&& urlanchor[1]!="made" 
			&& urlanchor[1]!="alchemy" 
			&& urlanchor[1]!="shminking") {
				openCurtain( source );			
			}
			
			
			

		}

		/* ******************************************* 
		*
		*	Curtain: open
		*
		******************************************* */
		
		if( $(".curtainIt").length )
		{
			$(".curtainIt").click(function(event){
				//prevent the default action for the click event
				event.preventDefault();

				// call the curtain
				openCurtain( this.href);
			});  		
		}
		
		function openCurtain( full_url ) {	
			
			currentY = $(window).scrollTop();
			
			// Hide content
			$("#slideshow_info").hide();
																	
			// Y position of content		
			$("#blackcurtaincontent").css("top", (currentY+40)+"px");
			
			// Bring the curtain in						
			$("#blackcurtain").animate({ left: "0px" }, '300', bringInfo)
			$("#closeBtn").animate({ left: "720px" }, '300');
				

			window.location = full_url;	// Changes URL
			status_curtain = 1; // Curtain is active		
		}


		/* ******************************************* 
		*
		*	Curtain: close
		*
		******************************************* */

		// Actions for the close button
		if( $(".close").length ) 
		{
			$(".close").click(closeCurtain);		
		}
		
		// Actions for the sidemenu buttons
		if($(".scroll").length)
		{
			$(".scroll").click(closeCurtainAndGo);
		}		

		function closeCurtain()
		{
			status_curtain = 0; // Curtain is deactivated
				
			$("#blackcurtain").stop().animate({ left: "-720px" }, '500', hideInfo);
			$("#blackcurtaincontent").height( main_height ).css( "top","0" );			
			$("#closeBtn").stop().animate({ left: "-36px" }, '500');	
			
			if( currentY != window.pageYOffset ) {
				$('html, body').animate({scrollTop:currentY}, 400, "easeout");
			}

			currentY = 0;	
		}

		function closeCurtainAndGo()
		{
			currentId = $(this).attr("id");
		
			if ( status_curtain == 1)
			{
				status_curtain = 0; // Curtain is deactivated											
			
				$("#blackcurtain").stop().animate({ left: "-720px" }, '500', hideInfo);
				$("#blackcurtaincontent").height( main_height ).css( "top","0" );
				$("#closeBtn").stop().animate({ left: "-36px" }, '500');		
			}
			
			if(currentId != state) {
				thisMovie("sample").fatStartJump();
			} 	
		}		

				
		/* ******************************************* 
		*
		*	Curtain Info: hide
		*
		******************************************* */
		
		function hideInfo() {			
			$("#slideshow_info").fadeOut(200);
			$("#curtainHello").fadeOut(200, emptyInfo);			
		}
		
		function emptyInfo() {
			$("#slideshow-container").empty().append("<div id='slideshow_holder'></div>");
			$("#curtainTitle").empty();
			$("#description").empty();			
		}

		function bringInfo() {
			setTimeout(showInfo,300);
			
			//split the url by # and get the anchor target name - home in mysitecom/index.htm#home
			var source = window.location.toString();
			var anchor = source.split("#");			

			// Ajax call		
			$.get("work/slideshow.php?ref="+anchor[1], function(data){
 				$("#slideshow_info").html(data);
			});	
			

		}

		function showInfo() {
			$("#slideshow_holder").fadeIn(200);
			$("#slideshow_info").fadeIn(200);
			
			var source = window.location.toString(); 
			var urlanchor = source.split("#");
			if( urlanchor[1] == "barcelona" || urlanchor[1] == "london" )
			{					
			}else{
				$("#curtainHello").fadeIn(200);	
			}			
			
	

		}

		/* ******************************************* 
		*
		*	Say Hello FLAGS
		*
		******************************************* */		
		$(".hello").hover(function() { $("#flags").fadeIn();	},function () { $("#flags").fadeOut(); });
		
						
		/* ******************************************* 
		*
		*	Flash interaction
		*
		******************************************* */
		
		function thisMovie(movieName)
		{
			if (navigator.appName.indexOf("Microsoft") != -1) {
				return window[movieName]
			} else {
				return document[movieName];
			}
		}	

				
		/* ******************************************* 
		*
		*	Scroll Event
		*
		******************************************* */
			
		function scrollFunc(e)
		{
		
			var  YOffset;
			if(document.all) 	YOffset = (document.documentElement.scrollTop) ? document.documentElement.scrollTop : document.body.scrollTop;
			else 				YOffset = window.pageYOffset; 
			
    		if ( typeof scrollFunc.x == 'undefined' )
    		{
        		scrollFunc.y=YOffset;
	    	}
			
			//Call Flash
			talkToFlash("fatpos", "false");		      			
  			talkToFlash("myText2", YOffset);			
			
			diffY=scrollFunc.y-YOffset;
			
			if( diffY<0 )// Scroll Down
			{
			}
			else if( diffY>0 )// Scroll up
			{
				if ( status_curtain == 1 ) // If curtain is opened				
				{
					if ( YOffset < currentY ) 
					{						
						$('html, body').animate({opacity: 1}, 500).animate({scrollTop:currentY}, 300);
					}
					else 
					{
						$('html, body').stop();
					}					
				}
    		}
    		else
    		{
				
		    }
		    		    
		    scrollFunc.x=window.pageXOffset;
    		scrollFunc.y=YOffset;
		}


		window.onscroll=scrollFunc;
	
	
	
	

		/* ******************************************* 
		*
		*	Sidemenu: icon fade out	
		*
		******************************************* */
		function hideIcons()
		{
			$("li#we img").fadeIn();
		}	
	
		
		/* ******************************************* 
		*
		*	Sidemenu: hover states
		*
		******************************************* */

		$("li#we").hover(function(){
			if( state != "we" ) $("li#we img").fadeIn();
			$("#indicator span").html("We");			
			$("#indicator").css("left","13px");	
			$("#indicator").css("background-position","left top");							
		},function () {
			if( state != "we" ) $("li#we img").fadeOut();
			$("#indicator").css("left","-9000px");							
		});
		
		$("li#love").hover(function(){
			if( state != "love" ) $("li#love img").fadeIn();
			$("#indicator span").html("Love");				
			$("#indicator").css("left","35px");	
			$("#indicator").css("background-position","center top");		
		},function () {
			if( state != "love" ) $("li#love img").fadeOut();
			$("#indicator").css("left","-9000px");							
		});
		
		$("li#made").hover(function(){
			if( state != "made" ) $("li#made img").fadeIn();
			$("#indicator span").html("Made");							
			$("#indicator").css("left","70px");	
			$("#indicator").css("background-position","center top");			
		},function () {
			if( state != "made" ) $("li#made img").fadeOut();
			$("#indicator").css("left","-9000px");							
		});
		
		$("li#alchemy").hover(function(){
			if( state != "alchemy" ) $("li#alchemy img").fadeIn();
			$("#indicator").css("left","94px");	
			$("#indicator").css("background-position","center top");		
			$("#indicator span").html("Alchemy");			
		},function () {
			if( state != "alchemy" ) $("li#alchemy img").fadeOut();
			$("#indicator").css("left","-9000px");							
		});
		
		$("li#shminking").hover(function(){
			if( state != "shminking" ) $("li#shminking img").fadeIn();
			$("#indicator").css("left","125px");	
			$("#indicator").css("background-position","center top");					
			$("#indicator span").html("Shminking");					
		},function () {
			if( state != "shminking" ) $("li#shminking img").fadeOut();
			$("#indicator").css("left","-9000px");							
		});
		
		$("li#hello").hover(function(){
			if( state != "hello" ) $("li#hello img").fadeIn();
			$("#indicator").css("left","151px");	
			$("#indicator").css("background-position","right top");					
			$("#indicator span").html("Say&nbsp;Hello");				
		},function () {
			if( state != "hello" ) $("li#hello img").fadeOut();
			$("#indicator").css("left","-9000px");							
		});		

	
	
	
	
		/* ******************************************* 
		*
		*	Target Blank replacement		
		*
		******************************************* */	

		$("a.ext").live("click", function(){
			window.open(this.href);
			return false;			
		});
				
		/* ******************************************* 
		*
		*	Tooltip		
		*
		******************************************* */	

		if( $("a.screenshot").length )
		{
			xOffset = 210;
			yOffset = -132;

			$("a.screenshot").hover(function(e){

				var imgurl = $(this).attr("id");
		
				$("body").append("<div id='screenshot'><div id='screenshotBox'><img src='work/thumb/"+ imgurl +"'  /></div><div class='triangle'></div>"+"</div>");								 
				$("#screenshot").css("top",(e.pageY - xOffset) + "px")
								.css("left",(e.pageX + yOffset) + "px")
								.fadeIn("fast");						
				},function(){	
				
				$("#screenshot").remove();
    		});	
			
			$("a.screenshot").mousemove(function(e){
			
				$("#screenshot").css("top",(e.pageY - xOffset) + "px")
								.css("left",(e.pageX + yOffset) + "px");
			});	
		}
		
	
	/* ******************************************* 
	*
	*	Keyboard		
	*
	******************************************* */	
		
	$(".elemnt").click(function(event){
		//prevent the default action for the click event
		event.preventDefault();

		$(".elemnt").removeClass("elemnthover");		
		$(this).addClass("elemnthover");
		talkToFlash("fatpos", $(this).attr("id"));		
		thisMovie("sample").myFunctionId( $(this).attr("id") );
		
	});	
		

	function talkToFlash( type, str ){	
		thisMovie("sample").SetVariable(type, str);		
	}

	function keyPressed( key ) 
	{
		$("#"+key).addClass("elemnthover");
		$(".elemnt:not('#"+key+"')").removeClass("elemnthover");
		talkToFlash("fatpos", key);		

		thisMovie("sample").myFunctionId( key );


	}
		
		
    $(document).keypress(function(e){

		switch(e.which)
		{
			case 113 : //q 		
				keyPressed("q");
				break;
			case 81 :  //Q 		
				keyPressed("q");
				break;

			case 119 :  //w 
				keyPressed("w");
  				break;
 			case 87 :  //W 
				keyPressed("w");
  				break; 				  				

			case 121 :  //y 
				keyPressed("y");
  				break;
 			case 89 :  //Y 
				keyPressed("y");
  				break;	
  				
			case 117 :  //U 
				keyPressed("u");
  				break;	
			case 85 :  //U 
				keyPressed("u");
  				break;	  				
  				  						
			case  97 :  //A 		
				keyPressed("a");
  				break;
			case  65 :  //A 		
				keyPressed("a");
  				break;  				
			
			case 115 :  //S 		
				keyPressed("s");
  				break;
			case 83 :  //S 		
				keyPressed("s");
	  			break;
				
			case 100 :  //D 		
				keyPressed("d");
  				break;
			case 68 :  //D 		
				keyPressed("d");
  				break;
				
			case 102 :  //F 		
				keyPressed("f");
  				break;
			case 70 :  //F 		
				keyPressed("f");
  				break;

			case 103 :  //G 		
				keyPressed("g");
	  			break;
			case 71 :  //G 		
				keyPressed("g");
  				break;
  			
			case 104 :  //H 		
				keyPressed("h");
  				break;
			case 72 :  //H 		
				keyPressed("h");
  				break;
  			
  			
  			
			case 106 :  //J 		
				keyPressed("j");
  				break;
			case 74 :  //J 		
				keyPressed("j");
  				break;  			


			case 122 :  //Z 		
				keyPressed("z");
  				break;
			case 90 :  //Z 		
				keyPressed("z");
  				break;  			
  			

			case 120 :  //X 		
				keyPressed("x");
  				break;
			case 88 :  //X 		
				keyPressed("x");
  				break;
  			
  			
			case  99 :  //C 		
				keyPressed("c");
  				break;
			case 67 :  //C 		
				keyPressed("c");
  				break;  			

			case 118 :  //V 		
				keyPressed("v");
  				break;
			case 86 :  //V 		
				keyPressed("v");
  				break;  			

			case  98 :  //B 		
				keyPressed("b");
  				break;
			case  66 :  //B 		
				keyPressed("b");
  				break;  			
			
			case 110 :  //N 		
				keyPressed("n");
  				break;
			case 78 :  //N 		
				keyPressed("n");
  				break;
  					
			case 109 :  //M 		
				keyPressed("m");
  				break;
			case 77 :  //M 		
				keyPressed("m");
  				break;																																	
		}
    });
		
});