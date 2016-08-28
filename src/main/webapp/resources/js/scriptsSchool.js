var tpj=jQuery;
var revapi4;
tpj(document).ready(function() {
    if(tpj("#rev_slider_4_1").revolution == undefined){
        revslider_showDoubleJqueryError("#rev_slider_4_1");
    }else{
        revapi4 = tpj("#rev_slider_4_1").show().revolution({
            sliderType:"standard",
            jsFileLocation:"revolution/js/",
            sliderLayout:"auto",
            dottedOverlay:"none",
            delay:9000,
            navigation: {
                keyboardNavigation:"off",
                keyboard_direction: "horizontal",
                mouseScrollNavigation:"off",
                onHoverStop:"off",
                touch:{
                    touchenabled:"on",
                    swipe_threshold: 75,
                    swipe_min_touches: 1,
                    swipe_direction: "horizontal",
                    drag_block_vertical: false
                }
                ,
                arrows: {
                    style:"zeus",
                    enable:true,
                    hide_onmobile:true,
                    hide_under:600,
                    hide_onleave:true,
                    hide_delay:200,
                    hide_delay_mobile:1200,
                    tmp:'<div class="tp-title-wrap">  	<div class="tp-arr-imgholder"></div> </div>',
                    left: {
                        h_align:"left",
                        v_align:"center",
                        h_offset:30,
                        v_offset:0
                    },
                    right: {
                        h_align:"right",
                        v_align:"center",
                        h_offset:30,
                        v_offset:0
                    }
                }
                ,

                bullets: {
                    enable:true,
                    hide_onmobile:false,
                    hide_under:600,
                    style:"metis",
                    hide_onleave:true,
                    hide_delay:200,
                    hide_delay_mobile:1200,
                    direction:"horizontal",
                    h_align:"center",
                    v_align:"bottom",
                    h_offset:0,
                    v_offset:30,
                    space:10,
                    tmp:'<span class="tp-bullet-img-wrap">  <span class="tp-bullet-image"></span></span><span class="tp-bullet-title">{{title}}</span>'
                }
            },
            viewPort: {
                enable:true,
                outof:"pause",
                visible_area:"80%"
            },
            responsiveLevels:[1240,1024,778,480],
            gridwidth:[1240,1024,778,480],
            gridheight:[600,600,500,400],
            lazyType:"none",
            parallax: {
                type:"mouse",
                origo:"slidercenter",
                speed:2000,
                levels:[2,3,4,5,6,7,12,16,10,50],
            },
            shadow:0,
            spinner:"off",
            stopLoop:"off",
            stopAfterLoops:-1,
            stopAtSlide:-1,
            shuffle:"off",
            autoHeight:"off",
            hideThumbsOnMobile:"off",
            hideSliderAtLimit:0,
            hideCaptionAtLimit:0,
            hideAllCaptionAtLilmit:0,
            debugMode:false,
            fallbacks: {
                simplifyAll:"off",
                nextSlideOnWindowFocus:"off",
                disableFocusListener:false,
            }
        });
    }


});	/*ready*/


(function($) {
		"use strict";
		$(window).scroll(function () {
            if ($(document).scrollTop() <= 40) {
                $('#header-full').removeClass('small');
                $('.tabs-blur').removeClass('no-blur');
                $('#main-header').removeClass('small');
                $('#logoName').show();
                $('#schoolLogoImg').addClass('subschoolLogo');
            } else {
                $('#header-full').addClass('small');
                $('.tabs-blur').addClass('no-blur');
                $('#main-header').addClass('small');
                $('#logoName').hide();
                $('#schoolLogoImg').removeClass('subschoolLogo');
            }
        });
        
        $("a[data-rel^='prettyPhoto']").prettyPhoto({
			default_width: 600,
			default_height: 420,
			social_tools: false
		});

    $('#slider-news.flexslider').flexslider({
        animation: "slide",
        pauseOnAction: true
    });


        //$('#slideshow-tabs').tabs({ show: { effect: "fade", duration: 200 }, hide: { effect: "fade", duration: 300 },event: "mouseover" });

        // starting index
        var currTab = 0;

// count of all tabs
        var totalTabs = $("#slideshow-tabs > a").length;

// function to pass to setInterval
        function cycle() {

            // simulate click on current tab
            $("#slideshow-tabs > a").eq(currTab).click();

            // increment counter
            currTab++;

            // reset if we're at the last one
            if (currTab == totalTabs) {
                currTab = 0;
            }
        }

// go!
        var i = setInterval(cycle, 1000);


        $('#tabs-content-bottom').tabs({ show: { effect: "fade", duration: 200 }, hide: { effect: "fade", duration: 300 } });

        $( ".accordion" ).accordion({
	        heightStyle: "content"
        });
		$('a[data-rel]').each(function() {
			$(this).attr('rel', $(this).data('rel'));
		});
		$('img[data-retina]').retina({checkIfImageExists: true});
		$(".open-menu").click(function(){
		    $("body").addClass("no-move");
		});
		$(".close-menu, .close-menu-big").click(function(){
		    $("body").removeClass("no-move");
		});
	})(jQuery);