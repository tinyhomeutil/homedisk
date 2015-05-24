<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page language="java" import="java.util.*" %>
<%@ page language="java" import="com.tinyhomeutil.homedisk.action.*" %>
<%@ page language="java" import="com.tinyhomeutil.homedisk.i18n.*" %>
<%@ page language="java" import="com.tinyhomeutil.homedisk.util.*" %>
<%@ page language="java" import="com.tinyhomeutil.homedisk.pojo.*" %>
<%
Locale locale = request.getLocale();
ResourceBundle messages = LocaleToolkit.getBundle(locale);
String title = ProductInfo.getProductName() +
			   messages.getString("product_index");

IFore action = (IFore)ActionFacotry.getInstance().getAction(SwitchAction.FORE_INDEX);
Map<String, List<Album>> map = action.getIndexAlbumList(session, request, response);

%>

<!DOCTYPE html>
<html lang="<%=locale.toLanguageTag()%>">
  <head>
    <meta name="renderer" content="webkit">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title><%=title %></title>

    <!-- Bootstrap core CSS -->
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="css/01.css" rel="stylesheet">
	
    <!-- Custom styles for this template -->
    <link href="css/starter-template.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="js/html5shiv.min.js"></script>
      <script src="js/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
	
    <nav class="navbar navbar-default navbar-fixed-top">
	  <div class="container-fluid">
	    <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
              <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand ft1" href="#"><%=ProductInfo.getProductName() %></a>
        </div>
		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
		  <%=action.getIndexTopMenuHTML(messages, 1) %>
		  <%=action.getLoginHtml(messages, session, request, response) %>
		</div><!-- /.navbar-collapse -->
	  </div><!-- /.container-fluid -->
	</nav>
	
	<p>&nbsp;</p>
	
    <div class="container">
	  <h4>
	    <span class="glyphicon glyphicon-picture" aria-hidden="true"></span>&nbsp;
	    <%=messages.getString("index_image_title") %>
	  </h4>
	  <%=action.getRowHtml(map.get(Constant.ALBUM_IMAGE), 7, 1, Constant.ALBUM_IMAGE, Constant.ALBUM_IMAGE_DEFAULT_IMAGE, "imageview.jsp") %>
	  

      <hr>
	  
      <h4>
        <span class="glyphicon glyphicon-film" aria-hidden="true"></span>&nbsp;
        <%=messages.getString("index_video_title") %>
      </h4>
      <%=action.getRowHtml(map.get(Constant.ALBUM_VIDEO), 7, 1, Constant.ALBUM_VIDEO, Constant.ALBUM_VIDEO_DEFAULT_IMAGE, "videoview.jsp") %>
       
      
	  <hr>
	  
	  <h4>
	    <span class="glyphicon glyphicon-music" aria-hidden="true"></span>&nbsp;
	    <%=messages.getString("index_audio_title") %>
	  </h4>
	  <%=action.getRowHtml(map.get(Constant.ALBUM_AUDIO), 7, 1, Constant.ALBUM_AUDIO, Constant.ALBUM_AUDIO_DEFAULT_IMAGE, "audioplay.jsp") %>
	   
	  <hr>
	  	  
      <footer class="text-center">
        <p>&copy; NoneSole.com 2015</p>
      </footer>
    </div>
  
    <p>&nbsp;</p>
		
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="js/ie10-viewport-bug-workaround.js"></script>
    <script>
    //turn page
    var turn = function(url){
    	window.location.href=url;
    }
    </script>
    
  </body>
</html>
