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
			   messages.getString("other_title");

IFore action = (IFore)ActionFacotry.getInstance().getAction(SwitchAction.FORE_OTHER_LIST);
Pages<List<Resource>> pageObject = action.getOtherResourceByAlbumId(session, request, response);
List<Resource> resource = pageObject.getList();

List<Album> albums =  action.getOtherAlbumList(session, request, response);

String paramAlbumId = request.getParameter(Constant.PARAM_ALBUM_ID);
if ( paramAlbumId == null || "".equals(paramAlbumId) ) {
	paramAlbumId = "-1";
}


String paramAlbumName = "";
if ( "-1".equals(paramAlbumId) ) {
	paramAlbumName = messages.getString("other_list_all_album");
} else {
	int albumId = Integer.valueOf(paramAlbumId);
	for ( Album a : albums ) {
		if ( a.getId() == albumId ) {
			paramAlbumName = a.getName();
			break;
		}
	}
}

IGlobal gAction = (IGlobal)ActionFacotry.getInstance().getAction(SwitchAction.GLOBAL_NOTICE);
String noticeDivId = "#modal-notice";
String noticeWindowId = "#notice";

String url = "other.jsp";
Map<String,String> params = new HashMap<String,String>();
params.put(Constant.PARAM_ALBUM_ID, paramAlbumId);
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
		  <%=action.getIndexTopMenuHTML(messages, 5) %>
		  <%=action.getLoginHtml(messages, session, request, response) %>
		</div><!-- /.navbar-collapse -->
	  </div><!-- /.container-fluid -->
	</nav>
	
	<p>&nbsp;</p>
	
    <div class="container">
	  <h4><%=paramAlbumName %></h4>
      <div class="row">
	  <div class="col-md-3 col-md-push-9">
	    <select id="albumList" multiple class="form-control" size="20">
	    <option value="-1"><%=messages.getString("other_list_all_album") %></option>
		<%
		for ( Album a : albums ) {
		%>
		<option value="<%=a.getId()%>"><%=a.getName() %></option>
		<%
		}
		%>
		</select>
	  </div>
	  <div class="col-md-9 col-md-pull-3">
		<table class="table table-striped">
		  <thead>
			<tr>
			  <th width="30">#</th>
			  <th width="400"><%=messages.getString("other_table_col_name_1") %></th>
			  <th width="150"><%=messages.getString("other_table_col_name_3") %></th>
			  <th width="150"><%=messages.getString("other_table_col_name_4") %></th>
			</tr>
		  </thead>
		  <tbody>
		    <%
		    for ( Resource r : resource ) {
		    %>
			<tr>
			  <th scope="row"><%=r.getId() %></th>
			  <td><%=r.getName() %></td>
			  <td><%=Toolkit.getDateString(r.getCreateTime()) %></td>
			  <td>
			  	<form method="post" action="act_fore_resource_down.jsp">
		  		    <input type="hidden" name="<%=Constant.PARAM_RESOURCE_ID %>" value="<%=r.getId()%>" />
		  		    <input type="hidden" name="<%=Constant.PARAM_PAGE %>" value="<%=pageObject.getCurrentPage() %>" />
		  		    <input type="hidden" name="<%=Constant.PARAM_ALBUM_ID %>" value="<%=paramAlbumId%>" />
                    <button type=submit class="btn btn-default btn-sm">
                      <span class="glyphicon glyphicon glyphicon-save" aria-hidden="true"></span>&nbsp;
                      <%=messages.getString("other_table_col_name_4") %>
                    </button>
              	</form>
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
    $(document).ready(function(){
		$("#albumList").change(function(){
			var id = $("#albumList").find("option:selected").val();
			window.location.href="other.jsp?<%=Constant.PARAM_ALBUM_ID %>=" + id;
		});
    });
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
