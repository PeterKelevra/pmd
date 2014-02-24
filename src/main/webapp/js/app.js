//Variable para almacenar constantes 
var Cons = {
		WAIT_IMG_URL 	: 'images/wait.gif',
		ERROR_IMG_URL 	: 'images/error.png'
};

$(function() {
	
	checkUncheckAll('#checkAll', '.check-song');
	checkUncheckAll('#mp3CryCheckAll', '.check-song');
	
	enableTabs('#tabs');
	
	$('#downloadButton').click(function(e) {
		var checked = $('#songList input[class=check-song]:checked');
		if (checked.length > 0) {
			checked.each(function(index, obj) {
				var data = $(obj).data('data');
				console.log(data);
				downloadMp3IslandSong(data);
			});
			showMessage('Download Start');
		} else {
			showMessage('Select songs to download', 'label-important');
		}
		e.preventDefault();
	});
	
	$('#qualityButton').click(function(e) {
		var checked = $('#songList input[class=check-song]:checked');
		if (checked.length > 0) {
			checked.each(function(index, obj) {
				getQuality($(obj).val());
			});
		} else {
			showMessage('Select song to check quality', 'label-important');
		}
	});
	
	$('#mp3CryDownload').click(function(e) {
		var checked = $('#mp3CryList input[class=check-song]:checked');
		if (checked.length > 0) {
			checked.each(function(index, obj) {
				var data = $(obj).data('data');
				console.log(data);
				downloadMp3CrySong(data);
			});
			showMessage('Download Start');
		} else {
			showMessage('Select songs to download', 'label-important');
		}
		e.preventDefault();
	});
	
	$('#searchForm').on('submit', function(e) {
		var query = $(this).find('input').val();
		if (query.trim()) {
			searchSongs(query);
		}
		e.preventDefault();
	});
});


//Accepts a url and a callback function to run.
var currentQuery = null;
function searchSongs( query ) {

	var serializeForm = query + $('#searchForm').serialize();
	if (serializeForm == currentQuery) {
		return;
	}
	currentQuery = serializeForm;
	
	$('input[name=page-search]:checked').each(function(index, obj) {
		var fn = window[$(obj).val()];
		// is object a function?
		if (typeof fn === "function") {
			fn(query);
		}
	});
}

/**
 * Realiza busqueda de canciones en el portal mp3-island
 * @param query
 */
function mp3IslandSearch(query) {

	$('#mp3IslandTab').addClass('searching');
	$.get('mp3island/search/'+query, function(data) {
		$('#mp3IslandTab').removeClass('searching');;	
		if ( data ) {
			$('#songList').find('td').remove();
			
			//Buscamos el mp3 de cada una de las canciones...
			var songsTr = $('tbody > tr', data);
			if(songsTr == null || songsTr.length == 0){
				showMessage("No songs found in mp3-island");
			}
			
			$.each(songsTr, function(index, obj) {
				var tds = $(obj).find('td');
				var songId = $(tds[3]).attr('id').replace('quality_', '');
				var songTitle = parseText($(tds[1]).text());
				var songArtist = parseText($(tds[2]).text());
				var checkbox = $('<input>', {type:'checkbox', 'class':'check-song', value:songId});
				var input = $('<td>').append(checkbox);
				var title = $('<td>').html(songTitle);
				var artist = $('<td>').html(songArtist);
				var quality = $('<td>', {id:'quality_' + songId, 'class':'quality-cell'});
				var tr = $('<tr>', {id:'tr_' + songId}).append(input).append(title).append(artist).append(quality);
				$('#songList > table').append(tr);
				
				checkbox.data('data', {id:songId, title:songTitle, artist:songArtist});
				
				//getQuality(songId);
			});
		}
	  });
}


/**
 * Realiza busqueda de canciones en el portal mp3cry
 * @param query
 */
function mp3CrySearch(query) {
	$('#mp3CryTab').addClass('searching');

	$.get('mp3cry/search/'+query, function(data) {
		if ( data ) {
			$('#mp3CryTab').removeClass('searching');
			$('#mp3CryList').find('td').remove();
			
			//Buscamos el mp3 de cada una de las canciones...
			var songsLinks = $('a[title=Play]', data);
			if(songsLinks == null || songsLinks.length == 0){
				showMessage("No songs found in mp3-cry");
			}
			
			$.each(songsLinks, function(index, obj) {
				var tds = $(obj).parent().parent().find('td');
				var downloadUrl = $(tds[2]).find('a').attr('href');
				var songTitle = parseText($(tds[3]).text());
				var link = $('<a>', {html:songTitle, target:'_blank', href:'http://mp3cry.com/' + downloadUrl});
				var checkbox = $('<input>', {type:'checkbox', 'class':'check-song', value:downloadUrl});
				var input = $('<td>').append(checkbox);
				var title = $('<td>').append(link);
				var tr = $('<tr>').append(input).append(title);
				$('#mp3CryList > table').append(tr);
				
				checkbox.data('data', {url:downloadUrl, title:songTitle});
				
			});
		}
	  });
}

/**
 * Obtiene la calidad de una canción consultando al portal mp3-island
 * @param id
 */
function getQuality(id){
	var td = $('#quality_' + id);
	//Si ya hemos comprobado calidad para esa canción no volvemos a comprobarla
	if (td.data('check')) {
		return;
	}
	
	td.css('background-image', 'url(' + Cons.WAIT_IMG_URL + ')');
	$.get(Cons.MP3_ISLAND_QUALITY_URL+id, function (data){
		
		if(data){
			td.css('background-image', 'url(' + data + ')');
			td.data('check', true);
		}else{
			td.css('background-image', 'url(' + Cons.ERROR_IMG_URL + ')');
		}
	});
}

function downloadMp3IslandSong(params) {
	params.data = JSON.stringify(params);
	$.post('mp3island/download', params, function (data){
		if(data){
			showMessage(data);
		}
	});
}

function downloadMp3CrySong(params) {
	params.data = JSON.stringify(params);
	$.post('mp3cry/download', params, function (data){
		if(data){
			showMessage(data);
		}
	});
}


function checkUncheckAll(chkId, chkClass) {
	$(chkId).click(function() {
		if ($(this).is(":checked")) {
			$(chkClass).prop('checked', true);
		} else {
			$(chkClass).prop('checked', false);
		}
	});
}

function enableTabs(id) {

	var ul = $(id);
	ul.find('a').each(function(index, obj){
		$(obj).click(function(){
			ul.find('li').removeClass('active');
			var a = $(this);
			a.parent().addClass('active');
			
			var activeTab = $('.activeTab');
			activeTab.hide();
			activeTab.removeClass('activeTab');
			$(a.attr('href')).addClass('activeTab').show();
			
		});
	});
}

function ajaxError(jqXHR, status, errorThrown) {
	  //the status returned will be "timeout"
	  if (status == 'timeout') {
		  showMessage('No se pudo conectar con el servidor, compruebe su conexi&oacute;n');
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
    		  callback(data.results[0]);
    		}
    	});
}

/**
 * Elimina multiples espacios y saltos de linea de una cadena, sustituye & por and. Por problemas al elminar nombres de archivo con ampersand
 * @param text
 */
function parseText(text) {
	text = text.replace( /\s\s+/g, ' ' );
	//text = text.replace(/&/g, ' and ');
	text = text.replace( /\n/g, ' ' );
	console.log(text);
	return text;
}

/**
 * Muestra mensaje por pantalla que desaparece automáticamente
 * @param msg
 */
function showMessage(msg, classes) {
	
	$("<div class='notification label " + (classes || 'label-info') + "'>"+msg+"</div>")
	.appendTo( '#notifications' ).delay( 3000 )
	.fadeOut( 400, function(){
		$(this).remove();
	});
}
