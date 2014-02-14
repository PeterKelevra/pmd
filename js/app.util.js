
//Variable para almacenar constantes 
var Cons = {
		SERVER_URL : 'http://spotube.com:8081/MusicOn/',
		EVALUATE_URL : 'http://mp3-island.in/site/evaluate',
		WAIT_IMG_URL : 'http://mp3-island.in/images/wait.gif',
		ERROR_IMG_URL : 'http://mp3-island.in/images/error.png'
};


function ajaxError(jqXHR, status, errorThrown) {
	  //the status returned will be "timeout"
	  if (status == 'timeout') {
		  alertMsg('No se pudo conectar con el servidor, compruebe su conexi&oacute;n');
	  }
	  console.log('error: '+status);
}

function yqlQuery( site, callback) {


	// Take the provided url, and add it to a YQL query. Make sure you encode it!
	var yql = 'http://query.yahooapis.com/v1/public/yql?q=' + encodeURIComponent('select * from html where url="' + site + '" and compat="html5"') + '&format=xml&callback=?';
	// Request that YSQL string, and run a callback function.
	// Pass a defined function to prevent cache-busting.

    
    $.ajax({ 
    	  url: yql, 
    	  dataType: 'json', 
    	  timeout: 8000, //8 second timeout
    	  error: ajaxError,
    	  success: function(data) { 
    	  	callback(data);
    		}
    	});
}
//Accepts a url and a callback function to run.
var currentQuery = null;
function searchSongs( query  ) {

	currentQuery = query;

	var site = 'http://www.mp3-island.in/search/'+encodeURIComponent(query);

	yqlQuery(site, function(data){
		  if ( data.results[0] ) {
			$('#songList').find('td').remove();
		  	// Strip out all script tags, for security reasons.
		  	// BE VERY CAREFUL. This helps, but we should do more.
		  	data = data.results[0].replace(/<script[^>]*>[\s\S]*?<\/script>/gi, '');
		  	data = data.replace(/<img[^>]*>[\s\S]*?<\/script>/gi, '');

		  	//Buscamos el mp3 de cada una de las canciones...
		  	var songsTr = $('tbody > tr', data);
		  	if(songsTr == null || songsTr.length == 0){
		  		//TODO: sacar popUp c	on mensaje de no se encontraron canciones...
		  	}
		  	
		  	$.each(songsTr, function(index, obj){
		  		var tds = $(obj).find('td');
		  		var songId = $(tds[3]).attr('id').replace('quality_', '');
		  		var input = $('<td>').append($('<input>', {type:'checkbox', value:songId}));
		  		var title = $('<td>').html($(tds[1]).html());
		  		var artist = $('<td>').html($(tds[2]).html());
		  		var quality = $('<td>', {id:'quality_' + songId})
		  		var tr = $('<tr>').append(input).append(title).append(artist).append(quality);
		  		$('#songList > table').append(tr);
		  		
				getQuality(songId);
		  	});
		  	
		  }
	  });
}

function getQuality(id){
		$('#quality_'+ id).html('<img src="'+Cons.WAIT_IMG_URL+'" />');
		/*
		$.ajax({
			type:"POST",
			crossDomain: true,
			dataType: 'json',
			url:Cons.EVALUATE_URL,data:{id:id},
				success: function(responseData, textStatus, jqXHR) {
					alert(responseData);
					var result = responseData.someKey;
					if(result==0){
						$('#quality_'+ id).html('<img src="'+Cons.ERROR_IMG_URL+'" />');
					}else{
						$('#quality_'+ id).html('<img src="'+result+'" />');}					
					},
				error: function (responseData, textStatus, errorThrown) {
					console.log('POST failed.');
				}			
			});
		*/
		// Add the iframe with a unique name
		var form = $('<form/>', {id:id, action:Cons.EVALUATE_URL, method:'POST'}).appendTo('body');
		form.append($('<input/>', {type:'hidden', name:'id', value:id}));
		form.ajaxForm({
			type: 'POST',
			dataType: 'jsonp',
			success: function(responseText, statusText, xhr, $form) { 
				alert(responseText);
				$form.remove();
			},
			error: function(obj) {  
				console.log(obj);
			}
		});
		
		form.submit();
		
		
}


/**
 * Muestra mensaje por pantalla que desaparece automáticamente
 * @param msg
 */
function alertMsg(msg) {
	$("<div class='ui-loader ui-overlay-shadow ui-body-e ui-corner-all'><h3>"+msg+"</h3></div>")
	.css({ display: "block", 
		opacity: 0.90, 
		position: "fixed",
		padding: "7px",
		"text-align": "center",
		width: "270px",
		left: ($(window).width() - 284)/2,
		top: $(window).height()/2 })
	.appendTo( 'body' ).delay( 1500 )
	.fadeOut( 400, function(){
		$(this).remove();
	});
}
