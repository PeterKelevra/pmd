package com.peter.pmd.managers;

public class Constants {

	public static final String QUALITY_URL 		=	"http://mp3-island.in/site/evaluate";
	public static final String API_URL 			=	"http://api.mp3-island.in/api.php";
	public static final String TOKEN 			=	"a468bPYPMBhmv1ehaDI8HyrWd3B751UW";
	public static final String FETCH_URL 		= 	API_URL + "?token=" + TOKEN + "&method=fetch";
	public static final String PROGRESS_URL 	=	API_URL + "?token=" + TOKEN + "&method=progress";
	public static final String DOWNLOAD_URL 	=	API_URL + "?token=" + TOKEN + "&method=download&file=";
	
	public static final String ID = "id="; 

}
