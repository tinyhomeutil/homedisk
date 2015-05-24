<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page language="java" import="java.util.*" %>
<%@ page language="java" import="com.tinyhomeutil.homedisk.action.*" %>
<%@ page language="java" import="com.tinyhomeutil.homedisk.i18n.*" %>
<%@ page language="java" import="com.tinyhomeutil.homedisk.util.*" %>
<%@ page language="java" import="com.tinyhomeutil.homedisk.pojo.*" %>
<%@ page language="java" import="com.tinyhomeutil.homedisk.dao.*" %>
<%
Locale locale = request.getLocale();
ResourceBundle messages = LocaleToolkit.getBundle(locale);
String title = ProductInfo.getProductName() + " - " +
			   messages.getString("video_title");

IFore action = (IFore)ActionFacotry.getInstance().getAction(SwitchAction.FORE_VIDEO_LIST);
Pages<List<Album>> pageObject = action.getVideoAlbumList(session, request, response);
List<Album> albums = pageObject.getList();

IGlobal gAction = (IGlobal)ActionFacotry.getInstance().getAction(SwitchAction.GLOBAL_NOTICE);
String noticeDivId = "#modal-notice";
String noticeWindowId = "#notice";

String url = "videos.jsp";
Map<String,String> params = new HashMap<String,String>();
String pagesBtnCode = gAction.getPagesHTML(
		messages, url, params, pageObject.getFirstPage(), pageObject.getPreviousPage() , 
		pageObject.getNextPage(), pageObject.getLastPage(), pageObject.getCurrentPage());

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
		  <%=action.getIndexTopMenuHTML(messages, 3) %>
		  <%=action.getLoginHtml(messages, session, request, response) %>
		</div><!-- /.navbar-collapse -->
	  </div><!-- /.container-fluid -->
	</nav>
	
	<p>&nbsp;</p>
	
    <div class="container">
      <%=action.getRowHtml(albums, 7, 1, Constant.ALBUM_VIDEO, Constant.ALBUM_VIDEO_DEFAULT_IMAGE, "videoview.jsp") %>

      <%=action.getRowHtml(albums, 7, 2, Constant.ALBUM_VIDEO, Constant.ALBUM_VIDEO_DEFAULT_IMAGE, "videoview.jsp") %>
      </div>
	  
	  <p>&nbsp;</p>
	  
	  <div class="row text-center">
		  <%=pagesBtnCode %>
	  </div>
	  		    
	  <p>&nbsp;</p>
	  
	  <hr>	  
	
      <footer class="text-center">
        <p>&copy; NoneSole.com 2015</p>
      </footer>
      
      <div class="modal fade" id="notice" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-sm">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			<h4 class="modal-title" id="myModalLabel"><%=messages.getString("notice") %></h4>
		  </div>
		  <div class="modal-body" id="modal-notice">
			
		  </div>
		  <div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal"><%=messages.getString("close") %></button>
		  </div>
		</div>
	  </div>
	  </div>
	  
    </div>
		
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="bootstrap/js/jquery.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="bootstrap/js/ie10-viewport-bug-workaround.js"></script>
    <script>
    //turn page
    var turn = function(url){
    	window.location.href=url;
    }
    </script>
  <%=gAction.getNoticeHTML(messages, request, noticeDivId, noticeWindowId)%>
  </body>
</html>
