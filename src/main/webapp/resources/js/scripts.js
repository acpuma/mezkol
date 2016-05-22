	(function($) {
		"use strict";
		$(window).scroll(function () {
            if ($(document).scrollTop() <= 40) {
                $('#header-full').removeClass('small');
                $('.tabs-blur').removeClass('no-blur');
                $('#main-header').removeClass('small');
            } else {
                $('#header-full').addClass('small');
                $('.tabs-blur').addClass('no-blur');
                $('#main-header').addClass('small');
            }
        });
        
        $("a[data-rel^='prettyPhoto']").prettyPhoto({
			default_width: 600,
			default_height: 420,
			social_tools: false
		});
        $('#slideshow-tabs').tabs({ show: { effect: "fade", duration: 200 }, hide: { effect: "fade", duration: 300 },event: "mouseover" });

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