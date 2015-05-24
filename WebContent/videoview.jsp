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

IFore action = (IFore)ActionFacotry.getInstance().getAction(SwitchAction.FORE_VIDEO_PLAY);
List<Resource> list = action.getMediaResourceByAlbumId(session, request, response, Constant.ALBUM_VIDEO);
if ( list == null ) {
	String link = Toolkit.failLink("videos.jsp", "resource_view_null_notice");
	response.sendRedirect(link);	
	return;
}

Resource res = list.get(0);
String resourceJson = Toolkit.resourceList2Json(list);

String pageStr = request.getParameter(Constant.PARAM_PAGE);
if ( pageStr == null || "".equals(pageStr) || "null".equals(pageStr) ) {
	pageStr = "1";
} 
pageStr = "?"+ Constant.PARAM_PAGE + "=" + pageStr;

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
	 
	<div class="container w2 margin5">
		<div class="w3 pd1">
			<div class="row">
				<div class="col-sm-6 col-md-8 clearfix">
					<div class="thumbnail" id="leftPlayArea">
						<div class="caption">
							<p>
								<video id="videoplayer" controls="controls">
									<source id="playerSrc" src="<%=list.get(0).getWebUrl()%>" type="video/ogg">
									<source src="<%=list.get(0).getWebUrl()%>" type="video/mp4">
									<source src="<%=list.get(0).getWebUrl()%>" type="video/webm">
									<%=messages.getString("can_not_support_html5_video")%>
								</video>
							</p>
							<p>
								<input type="checkbox" id="biggerWindow">&nbsp;<%=messages.getString("bigger_window") %>
							</p>
							<h3 class="text-style-1" id="videoName"><%=res.getName()%></h3>
							<p class="text-style-1" id="videoRemark"><%=Toolkit.nullFilter(res.getRemark(),
					messages.getString("remark_null_notice"))%>&nbsp;
							</p>
						</div>
					</div>
				</div>

				<div class="col-md-4">

					<ol class="breadcrumb text-style-1 margin7">
						<li><a href="index.jsp"><%=messages.getString("top_menu_1")%></a></li>
						<li><a href="videos.jsp<%=pageStr%>"><%=messages.getString(res.getAlbumParent())%></a></li>
						<li class="active"><%=res.getAlbumName()%></li>
					</ol>

					<select id="videoList" multiple class="form-control" size="20">

					</select>

				</div>

			</div>

		</div>

		<hr>	  
	  
      <footer class="text-center">
        <p>&copy; NoneSole.com 2015</p>
      </footer>
    	  
      <!-- data-backdrop="static" 点击遮蔽处不关闭弹出窗口 -->
	  <div class="modal fade" id="viewWindow" role="dialog" tabindex="-1" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
	  <div class="modal-dialog modal-lg">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" aria-label="Close" id="dialogXCloseBtn"><span aria-hidden="true">&times;</span></button>
			<h4 class="modal-title" id="dialogVideoName"></h4>
		  </div>
		  <div class="modal-body" id="modal-notice">
			<div class="caption">
				<p>
				<video id="dialogVideoPlayer" controls="controls">
				<source src="" type="video/ogg">
				<source src="" type="video/mp4">
				<source src="" type="video/webm">
					<%=messages.getString("can_not_support_html5_video")%>
				</video>
			</div>
		  </div>
		  <div class="modal-footer">
			<button type="button" class="btn btn-default" id="dialogCloseBtn"><%=messages.getString("close") %></button>
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
    var maxSize = 0;
	var currentIndex = 0;
	var currentVideoUrl = $("#playerSrc").attr("src");
	var videoplayer = $("#videoplayer")[0];  
	var dialogVideoPlayer = $("#dialogVideoPlayer")[0];
	videoplayer.play();  
	
	window.setInterval(showalert, 1000); 
	function showalert() { 
		if ( videoplayer.ended ) {			
			if ( currentIndex < maxSize-1 ) {
				currentIndex++;
			} else {
				currentIndex = 0;
			}
			$("#videoList").val(currentIndex);
			$("#videoList").trigger("change");
		}
	} 
	
	//调整CSS样式
	$("#videoplayer").width($("#leftPlayArea").width()-20);
	
	//将JSON转换为JavaScript对象
	var resourceList = <%=resourceJson %>;
	maxSize = resourceList.length;
	var nullNotice = "<%=messages.getString("remark_null_notice") %>";
	
	//初始化资源列表
	var initResources = function(){
		$("#videoList").empty();
		$(resourceList).each(function(index) { 
	        var val = resourceList[index]; 
	        var option = $("<option>").val(index).text(index+1 + ". " + val.name);
		    $("#videoList").append(option);
		});
		currentIndex = 0;
		$("#videoList").val(currentIndex);
	}
	initResources();
	
	//下拉列表值变化的响应事件
	$(document).ready(function(){
		var closeModel = function(){
			dialogVideoPlayer.pause();
			$('#viewWindow').modal('hide');
			$("#biggerWindow").attr("checked",false);
			dialogVideoPlayer.src = "";
			$("#dialogVideoName").text("");
		}
		$("#videoList").change(function(){
			var index = $("#videoList").find("option:selected").val();
			var val = resourceList[index]; 
			currentIndex = index;
			$("#videoName").text(val.name);
			if ( val.remark == null || val.remark == "" ){
				$("#videoRemark").val(nullNotice);
			} else {
				$("#videoRemark").val(val.remark);
			}
			videoplayer.src = val.weburl;
			currentVideoUrl = val.weburl;
			videoplayer.play();
		});
		$("#biggerWindow").click(function(){
			if($("#biggerWindow").is(':checked')) {
				videoplayer.pause();
				$("#dialogVideoName").text($("#videoName").text());
				$("#dialogVideoPlayer").width(870);
				dialogVideoPlayer.src = currentVideoUrl;
				dialogVideoPlayer.play();
				$('#viewWindow').modal('show');
			}
		});
		$("#dialogXCloseBtn").click(function(){
			closeModel();
		});
		$("#dialogCloseBtn").click(function(){
			closeModel();
		});
	});
	
    </script>
    <script>
    //turn page
    var turn = function(url){
    	window.location.href=url;
    }
    </script>
  </body>
</html>
