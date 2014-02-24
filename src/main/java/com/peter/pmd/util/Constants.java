package com.peter.pmd.util;

public class Constants {

	public static final String PAGE_URL			=	"http://www.mp3-island.in/";
	public static final String QUALITY_URL 		=	PAGE_URL + "site/evaluate";
	public static final String SEARCH_URL 		=	PAGE_URL + "search/";
	public static final String API_URL 			=	"http://api.mp3-island.in/api.php";
	public static final String TOKEN 			=	"a468bPYPMBhmv1ehaDI8HyrWd3B751UW";
	public static final String FETCH_URL 		= 	API_URL + "?token=" + TOKEN + "&method=fetch";
	public static final String PROGRESS_URL 	=	API_URL + "?token=" + TOKEN + "&method=progress";
	public static final String DOWNLOAD_URL 	=	API_URL + "?token=" + TOKEN + "&method=download&file=";
	public static final String DOWNLOAD_FOLDER 	=	"/usr/share/google-music-manager/downloads/";
	//public static final String DOWNLOAD_FOLDER 	=	"/Users/Peter/Downloads/";
	public static final String MP3_CRY_URL			=	"http://mp3cry.com/";
	public static final String ID = "id=";

}
