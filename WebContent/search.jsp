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
			   messages.getString("search_title");

IFore action = (IFore)ActionFacotry.getInstance().getAction(SwitchAction.FORE_QUERY);
Pages<List<Resource>> pageObject = action.getResourceByKeyWords(session, request, response);

List<Resource> list = pageObject.getList();

String keyWord = request.getParameter(Constant.PARAM_KEYWORD);

//String k1 = new String(keyWord.getBytes("ISO-8859-1"),"utf-8");
//System.out.println(k1);

if ( keyWord == null ) {
	keyWord = "";
}

IGlobal gAction = (IGlobal)ActionFacotry.getInstance().getAction(SwitchAction.GLOBAL_NOTICE);
String noticeDivId = "#modal-notice";
String noticeWindowId = "#notice";

String url = "search.jsp";
Map<String,String> params = new HashMap<String,String>();
params.put(Constant.PARAM_KEYWORD, keyWord);
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
		  <%=action.getIndexTopMenuHTML(messages, -1) %>
		  <%=action.getLoginHtml(messages, session, request, response) %>
		</div><!-- /.navbar-collapse -->
	  </div><!-- /.container-fluid -->
	</nav>
	
	<p>&nbsp;</p>
	
    <div class="container">
	  <h4>
	    <%=messages.getString("search_keyword_title") %>&nbsp;<%=keyWord %>&nbsp;
	    <%=messages.getString("search_separator") %>&nbsp;
	    <%=messages.getString("search_total_number") %>&nbsp;<%=pageObject.getItems() %>
	  </h4>
      <div class="row">
	  <div class="col-md-12">
		<table class="table table-striped">
		  <thead>
			<tr>
			  <th width="30">#</th>
			  <th width="400"><%=messages.getString("search_table_col_name1") %></th>
			  <th width="200"><%=messages.getString("search_table_col_name2") %></th>
			  <th width="100"><%=messages.getString("search_table_col_name3") %></th>
			  <th width="100"><%=messages.getString("search_table_col_name4") %></th>
			</tr>
		  </thead>
		  <tbody>
		    <%
		    for ( Resource r : list ) {
		    %>
			<tr>
			  <th scope="row"><%=r.getId() %></th>
			  <td><%=r.getName() %></td>
			  <td><%=messages.getString(r.getAlbumParent()) %> / <%=r.getAlbumName() %></td>
			  <td>
			    <form method="post" action="act_fore_resource_down.jsp">
		  		    <input type="hidden" name="<%=Constant.PARAM_RESOURCE_ID %>" value="<%=r.getId()%>" />
		  		    <input type="hidden" name="<%=Constant.PARAM_PAGE %>" value="<%=pageObject.getCurrentPage() %>" />
		  		    <input type="hidden" name="<%=Constant.PARAM_KEYWORD %>" value="<%=keyWord%>" />
                    <button type=submit class="btn btn-default btn-sm">
                      <span class="glyphicon glyphicon glyphicon-save" aria-hidden="true"></span>&nbsp;
                      <%=messages.getString("search_table_col_name3") %>
                    </button>
              	</form>
			  </td>
			  <td>
			    <%
			    if ( !Constant.ALBUM_OTHER.equals(r.getAlbumParent()) ) {
			    %>
		        &nbsp;
				<%
			    %>
                <button type=button class="btn btn-default btn-sm" 
                onclick="openWindow('<%=r.getAlbumParent()%>','<%=r.getWebUrl()%>','<%=r.getName()%>')">
                <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>&nbsp;
                <%=messages.getString("search_table_col_name4") %>
                </button>
			    <%
		    	}
			    %>
			  </td>
			</tr>
			<%
		    }
			%>
		  </tbody>
		</table>
	  </div>
	</div>
	  
	  <div class="row text-center">
	      <%=pagesBtnCode %>
	  </div>
	  
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
	  
	  <div class="modal fade" id="videoWindow" role="dialog" tabindex="-1" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
	  <div class="modal-dialog modal-lg">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" aria-label="Close" id="xVideoClose"><span aria-hidden="true">&times;</span></button>
			<h4 class="modal-title" id="dialogVideoName"></h4>
		  </div>
		  <div class="modal-body center" id="modal-notice">
			<div class="caption">
				<video id="videoplayer" controls="controls">
				<source src="" type="video/ogg">
				<source src="" type="video/mp4">
				<source src="" type="video/webm">
					<%=messages.getString("can_not_support_html5_video")%>
				</video>
			</div>
		  </div>
		  <div class="modal-footer">
			<button type="button" class="btn btn-default" id="videoClose"><%=messages.getString("close") %></button>
		  </div>
		</div>
	  </div>
	  </div>
	  
	  <div class="modal fade" id="imageWindow" role="dialog" tabindex="-1" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
	  <div class="modal-dialog modal-lg">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" aria-label="Close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
			<h4 class="modal-title" id="imageName"></h4>
		  </div>
		  <div class="modal-body" id="modal-notice">
			<div class="caption center" id="imageDiv">
				<img id="imageViewer" src="" >
			</div>
		  </div>
		  <div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal"><%=messages.getString("close") %></button>
		  </div>
		</div>
	  </div>
	  </div>
	  
	  <div class="modal fade" id="audioWindow" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-sm" id="audioDiv">
		<div class="modal-content" >
		  <div class="modal-header">
			<button type="button" class="close" id="xAudioClose" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			<h4 class="modal-title" id="dialogAudioName"></h4>
		  </div>
		  <div class="modal-body center" >
			<audio id="audioplayer" controls="controls">
				<source src="" type="audio/ogg">
				<source src="" type="audio/mpeg">
				<source src="" type="audio/wav">
				<%=messages.getString("can_not_support_html5_audio")%>
			</audio>
		  </div>
		  <div class="modal-footer">
			<button type="button" class="btn btn-default" id="audioClose"><%=messages.getString("close") %></button>
		  </div>
		</div>
	  </div>
	  </div>
	  
    </div>
  
    <p>&nbsp;</p>
		
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="bootstrap/js/jquery.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="bootstrap/js/ie10-viewport-bug-workaround.js"></script>
    <script>
    var audioplayer = $("#audioplayer")[0];  
    var videoplayer = $("#videoplayer")[0];  
    
    var openWindow = function(albumparent,weburl,resourceName){
    	if ( albumparent === "<%=Constant.ALBUM_AUDIO %>" ) {
    		$('#dialogAudioName').text(resourceName);
    		$("#audioplayer").width($("#audioDiv").width()-30);
    		$('#audioWindow').modal('show');
    		audioplayer.src = weburl;
    		audioplayer.play();  
    	} else if ( albumparent === "<%=Constant.ALBUM_IMAGE %>" ) {
    		$('#imageName').text(resourceName);
    		$('#imageViewer').attr("src",weburl);
    		$('#imageWindow').modal('show');
    		imgAutoSize(870,500);
    		
    	} else if ( albumparent === "<%=Constant.ALBUM_VIDEO %>" ) {
    		$('#dialogVideoName').text(resourceName);
    		$('#videoWindow').modal('show');
    		$("#videoplayer").width(870);
    		$("#videoplayer").height(500);
    		videoplayer.src = weburl;
    		videoplayer.play();  
    	}
    }
    
    var closeAudioDialog = function(){
    	$('#audioWindow').modal('hide');
    	audioplayer.pause();
    	audioplayer.src = "";
    }
    
    var closeVideoDialog = function(){
    	$('#videoWindow').modal('hide');
    	videoplayer.pause();
    	videoplayer.src = "";
    }
    
    $(document).ready(function(){
		$("#audioClose").click(function(){
			closeAudioDialog();
		});
		$("#xAudioClose").click(function(){
			closeAudioDialog();
		});
		$("#videoClose").click(function(){
			closeVideoDialog();
		});
		$("#xVideoClose").click(function(){
			closeVideoDialog();
		});
	});
    
    //自动调整图片的长宽
    var imgAutoSize = function (maxWidth,maxHeight) {
    	var img = new Image();
        img.src = $('#imageViewer').attr("src");
        img.onload = function(){
        	console.log("1,"+img.width+","+img.height);
        	
        	var w = img.width;
            var h = img.height;
            var width,height;
            
            var width = w / h * maxHeight;
        	var height = maxHeight;
            if ( w > h ) {
            	if ( width > maxWidth ) {
            		width = maxWidth;
            		height = h / w * maxWidth;
            	}
            } 
            
            //console.log(w+","+h);
            console.log(width+","+height);
                        
            $('#imageViewer').width(width);
            $('#imageViewer').height(height);
        }
        
    };
    
    </script>
    
    <script>
    //turn page
    var turn = function(url){
    	window.location.href=url;
    }
    </script>
    
	<%=gAction.getNoticeHTML(messages, request, noticeDivId, noticeWindowId)%>

  </body>
</html>
