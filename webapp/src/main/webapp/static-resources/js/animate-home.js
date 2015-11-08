/**
 * 
 */
$(document).ready(function() {

	var div = $("#pacman");
	div.animate({
		left : '1000px',
		opacity : '0.4'
	}, 2500);
	div.animate({
		top : '+=300',
		opacity : '0.8'
	}, 2500);
	div.animate({
		left : '-=1000',
		opacity : '0.4'
	}, 2500);
	div.animate({
		top : '-=300',
		opacity : '0.8'
	}, 2500);

});