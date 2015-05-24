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
			   messages.getString("images_title");

IFore action = (IFore)ActionFacotry.getInstance().getAction(SwitchAction.FORE_IMAGE_PLAY);
List<Resource> list = action.getMediaResourceByAlbumId(session, request, response, Constant.ALBUM_IMAGE);
if ( list == null ) {
	String link = Toolkit.failLink("images.jsp", "resource_view_null_notice");
	response.sendRedirect(link);	
	return;
}

Resource res = list.get(0);

String resourceJson = "";
int preLoadCount = 50;
if ( list.size() <= 50 ) {
	  preLoadCount = list.size();
	  resourceJson = "[]";
} else {
	resourceJson = Toolkit.imageResourceList2Json(list,preLoadCount);
}

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
    <link rel='stylesheet' href='justifiedgallery/css/justifiedgallery.min.css' type='text/css' media='all' />
	<link rel='stylesheet' href='swipebox/swipebox.css' type='text/css' media='screen' />
    
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
		  <%=action.getIndexTopMenuHTML(messages, 2) %>
		  <%=action.getLoginHtml(messages, session, request, response) %>
		</div><!-- /.navbar-collapse -->
	  </div><!-- /.container-fluid -->
	</nav>
	
	<p>&nbsp;</p>
	   
	<div class="container w2 margin1">
		<div class="col-md-12 margin6 padding1">
			<ol class="breadcrumb text-style-1 margin7">
				<li><a href="index.jsp"><%=messages.getString("top_menu_1")%></a></li>
				<li><a href="images.jsp<%=pageStr%>"><%=messages.getString(res.getAlbumParent())%></a></li>
				<li class="active"><%=res.getAlbumName()%></li>
			</ol>
		</div>

		<div class="swipeboxEx" id="gallery">
      <%
      
      Resource r = null;
      for ( int i = 0 ; i < preLoadCount ; i++ ) {
    	  r = list.get(i);
      %>
	  <a href="<%=r.getWebUrl() %>" title="<%=r.getName() %>" class="swipebox">
        <img alt="<%=r.getName() %>" src="<%=Toolkit.getSmallImageName(r.getWebUrl()) %>" />
      </a>
	  <%
      }
	  %>
	  </div> 
	  
	  		  		    
	  <p>&nbsp;</p>
	  
	  <hr>	  
	
      <footer class="text-center">
        <p>&copy; NoneSole.com 2015</p>
      </footer>
    </div>
		
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="bootstrap/js/jquery.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="bootstrap/js/ie10-viewport-bug-workaround.js"></script>
    <script src='justifiedgallery/js/justifiedgallery.js'></script>
    <script src='swipebox/jquery.swipebox.min.js'></script>
    
    <script>
    //将JSON转换为JavaScript对象
	var resourceList = <%=resourceJson %>;
	var scrollCount = 12;
	var scrollIndex = 0;
	var maxIndex = 0;
	var maxSize = resourceList.length;
    $(document).ready(function () {
		$('#gallery').each(function (i, el) {
			$(el).justifiedGallery({rel: 'gal' + i}).on('jg.resize', function () {
				//if (i == 0) {
				//	$('.swipebox').swipebox(); //如果想调用1个相册对应i值为0，同理3个相册，i值为2。
				//}
			});
		});
	});
    
    //下拉时，实时加载图片，减少服务器端的负载
    $(window).scroll(function() {
        if($(window).scrollTop() + $(window).height() == $(document).height()) {
        	if ( (scrollIndex + scrollCount) < maxSize ) {
        		maxIndex = scrollIndex + scrollCount;
        	} else {
        		maxIndex = maxSize;
        	}
            for (var i = scrollIndex; i < maxIndex; i++) {
            	var val = resourceList[i]; 
            	$('#gallery').append('<a href="'+ val.weburl +'" title="'+ val.name +'" class="swipebox">' + 
                    '<img src="'+ val.smallweburl +'" alt="'+ val.name +'"/>' + 
                    '</a>');
                scrollIndex = scrollIndex+1;
            }
            $('#gallery').justifiedGallery('norewind');
            $('.swipebox').swipebox();
        }
    });
    
    $('.swipebox').swipebox();
	</script>
    <script>
    //turn page
    var turn = function(url){
    	window.location.href=url;
    }
    </script>
  </body>
</html>
