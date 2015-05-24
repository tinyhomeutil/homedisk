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
			   messages.getString("audio_title");

IFore action = (IFore)ActionFacotry.getInstance().getAction(SwitchAction.FORE_AUDIO_PLAY);
List<Resource> list = action.getMediaResourceByAlbumId(session, request, response, Constant.ALBUM_AUDIO);
if ( list == null ) {
	String link = Toolkit.failLink("audio.jsp", "resource_view_null_notice");
	response.sendRedirect(link);	
	return;
}
Resource res = list.get(0);
String resourceJson = Toolkit.resourceList2Json(list);

String pageStr = request.getParameter(Constant.PARAM_PAGE);
if ( pageStr == null || "".equals(pageStr) || "null".equals(pageStr)  ) {
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
		  <%=action.getIndexTopMenuHTML(messages, 4) %>
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
						<img src="<%=Constant.ALBUM_AUDIO_DEFAULT_IMAGE%>" id="activeImage" alt="..."/>
						<div class="caption">
							<h3 class="text-style-1" id="audioName"><%=res.getName()%></h3>
							<p class="text-style-1" id="audioRemark"><%=Toolkit.nullFilter(res.getRemark(), messages.getString("remark_null_notice")) %>&nbsp;</p>
							<p>
								<audio id="audio" controls="controls">
									<source src="<%=res.getWebUrl()%>" type="audio/ogg">
									<source src="<%=res.getWebUrl()%>" type="audio/mpeg">
									<source src="<%=res.getWebUrl()%>" type="audio/wav">
									<%=messages.getString("can_not_support_html5_audio")%>
								</audio>
							</p>
						</div>
					</div>
				</div>
				
				<div class="col-md-4">
					
					<ol class="breadcrumb text-style-1 margin7">
					  <li><a href="index.jsp"><%=messages.getString("top_menu_1") %></a></li>
					  <li><a href="audio.jsp<%=pageStr%>"><%=messages.getString(res.getAlbumParent()) %></a></li>
					  <li class="active"><%=res.getAlbumName() %></li>
					</ol>
					
					<select id="audioList" multiple class="form-control" size="20">
					  
					</select>
					
				</div>
								
			</div>

	  </div>
	  
	  <hr>	  
	
      <footer class="text-center">
        <p>&copy; NoneSole.com 2015</p>
      </footer>
    </div>
		
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="bootstrap/js/jquery.min.js"></script>
	<script>
	var maxSize = 0;
	var currentIndex = 0;
	var audio = $("#audio")[0];  
	audio.play();  
	
	window.setInterval(showalert, 1000); 
	function showalert() { 
		if ( audio.ended ) {			
			if ( currentIndex < maxSize-1 ) {
				currentIndex++;
			} else {
				currentIndex = 0;
			}
			$("#audioList").val(currentIndex);
			$("#audioList").trigger("change");
		}
	} 
	
	//自动调整图片的长宽
	var imgAutoSize = function (maxWidth,maxHeight) {
    	var img = new Image();
        img.src = $('#activeImage').attr("src");
        img.onload = function(){
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
                                    
            $('#activeImage').width(width);
            $('#activeImage').height(height);
        }
        
    };
    
	//调整CSS样式
	var imgMaxHeight = 300;
	var imgMaxWidth = $("#leftPlayArea").width()-10;
	$("#activeImage").css("margin-top","5px");
	$("#audio").width($("#leftPlayArea").width()-15);
	imgAutoSize(imgMaxWidth,imgMaxHeight);
	
	//将JSON转换为JavaScript对象
	var resourceList = <%=resourceJson %>;
	maxSize = resourceList.length;
	var nullNotice = "<%=messages.getString("remark_null_notice") %>";
	
	//初始化资源列表
	var initResources = function(){
		$("#audioList").empty();
		$(resourceList).each(function(index) { 
	        var val = resourceList[index]; 
	        var option = $("<option>").val(index).text(index+1 + ". " + val.name);
		    $("#audioList").append(option);
		});
		currentIndex = 0;
		$("#audioList").val(currentIndex);
	}
	initResources();
	
	//下拉列表值变化的响应事件
	$(document).ready(function(){
		$("#audioList").change(function(){
			var index = $("#audioList").find("option:selected").val();
			var val = resourceList[index]; 
			$("#audioName").text(val.name);
			if ( val.remark == null || val.remark == "" ){
				$("#audioRemark").val(nullNotice);
			} else {
				$("#audioRemark").val(val.remark);
			}		
			currentIndex = index;
			audio.src = val.weburl;
			audio.play();
			
			getImage();
		});
	});
	
	//每首歌自动切换图片
	var imageId = 0;
	var imageurl = "act_get_image.jsp";
	var getImage = function(){
		var postdata = { <%=Constant.IMAGE_ID %> : imageId };
		jQuery.getJSON(imageurl,postdata,function(res){
			if ( res.id == undefined ) {
				imageId = 0;
				$("#activeImage").attr("src",<%=Constant.ALBUM_IMAGE_DEFAULT_IMAGE%>);
			} else {
				imageId = res.id;
				$("#activeImage").attr("src",res.weburl);
			}
			imgAutoSize(imgMaxWidth,imgMaxHeight);
		});

	}
	getImage();
	
	</script>
	
	<script>
    //turn page
    var turn = function(url){
    	window.location.href=url;
    }
    </script>
  </body>
</html>
