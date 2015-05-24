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
			   messages.getString("album_title");

IUser action = (IUser)ActionFacotry.getInstance().getAction(SwitchAction.BACK_USER_ALBUM_LIST);
Pages<List<Album>> pageObject =action.getAlbums(session,request, response);
List<Album> albums = pageObject.getList();

IGlobal gAction = (IGlobal)ActionFacotry.getInstance().getAction(SwitchAction.GLOBAL_NOTICE);
String noticeDivId = "#modal-notice";
String noticeWindowId = "#notice";

String url = "album.jsp";
Map<String,String> params = new HashMap<String,String>();
String albumName = request.getParameter(Constant.PARAM_ALBUM_NAME);
String albumParent = request.getParameter(Constant.PARAM_ALBUM_PARENT);
params.put(Constant.PARAM_ALBUM_NAME, albumName==null?"":albumName);
params.put(Constant.PARAM_ALBUM_PARENT, albumParent==null?"":albumParent);
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
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title><%=title %></title>

    <!-- Bootstrap core CSS -->
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="css/01.css" rel="stylesheet">
	<link href="css/dashboard.css" rel="stylesheet">
	
    <!-- Custom styles for this template -->
    <link href="css/starter-template.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="js/html5shiv.min.js"></script>
      <script src="js/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#"><%=ProductInfo.getProductName() %></a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <%
            User user = (User)session.getAttribute(Constant.PARAM_SESSION_USER_OBJECT);
            %>
            <li>
            <p class="navbar-text navbar-right">
            <label>
            <span class="glyphicon glyphicon-user" aria-hidden="true"></span>&nbsp;
            <%=user.getName() %><%=messages.getString("user_login_welcome") %>&nbsp;&nbsp;&nbsp;&nbsp;
            </label>
            </p>
            </li>
            <li>
              <a href="index.jsp">
              <span class="glyphicon glyphicon-home" aria-hidden="true"></span>&nbsp;
              <%=messages.getString("back_to_index") %>
              </a>
            </li>
            <li>
              <a href="help.jsp">
              <span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span>&nbsp;
              <%=messages.getString("user_page_help") %>
              </a>
            </li>
            <li>
              <a href="logout.jsp">
              <span class="glyphicon glyphicon-log-out" aria-hidden="true"></span>&nbsp;
              <%=messages.getString("logout_btn") %>
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
          <ul class="nav nav-sidebar">
            <li>
              <a href="overview.jsp">
              <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>&nbsp;
              <%=messages.getString("user_page_overview") %>
              </a>
            </li>
            <li>
              <a href="pwd.jsp">
              <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;
              <%=messages.getString("user_page_pwd") %>
              </a>
            </li>
            <li class="active">
              <a href="album.jsp">
              <span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span>&nbsp;
              <%=messages.getString("user_page_album") %><span class="sr-only">(current)</span>
              </a>
            </li>
            <li>
              <a href="resource.jsp">
              <span class="glyphicon glyphicon-file" aria-hidden="true"></span>&nbsp;
              <%=messages.getString("user_page_resource") %>
              </a>
            </li>
          </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h1 class="page-header">
            <span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span>&nbsp;
            <%=messages.getString("album_title") %>
          </h1>
          <div class="table-responsive">
		    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#addAlbum">
		      <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>&nbsp;
		      <%=messages.getString("album_add_new") %>
		    </button>
            &nbsp;
            <button type="button" class="btn btn-default" data-toggle="modal" data-target="#searchDialog">
              <span class="glyphicon glyphicon-search" aria-hidden="true"></span>&nbsp;
              <%=messages.getString("query_btn") %>
            </button>
            <table class="table table-striped">
              <thead>
			    <th>#</th>
				<th><%=messages.getString("album_name") %></th>
				<th><%=messages.getString("album_parent") %></th>
				<th><%=messages.getString("album_update") %></th>
				<th><%=messages.getString("album_private_set") %></th>
				<th><%=messages.getString("album_delete") %></th>
              </thead>
              <tbody>
                <%for ( Album a : albums ) {%>
                <tr>
                  <td id="album-id-<%=a.getId() %>"><%=a.getId() %></td>
                  <td id="album-name-<%=a.getId() %>"><%=a.getName() %></td>
				  <td>
				    <input id="album-parent-<%=a.getId() %>" type="hidden" value="<%=a.getParent()%>" />
				    <%=messages.getString(a.getParent()) %>
				  </td>  
				  <td>
				    <button type="button" class="btn btn-default" 
				      onclick="updateAlbum(<%=a.getId() %>)">
				      <span class="glyphicon glyphicon-erase" aria-hidden="true"></span>&nbsp;
				      <%=messages.getString("album_update") %>
				    </button>
				  </td>
				  <td>
                    <form method="post" action="act.jsp">
		  		    <input type="hidden" name="m" value="<%=SwitchAction.BACK_USER_VIEW_SET %>" />
		  		    <input type="hidden" name="<%=Constant.PARAM_ALBUM_ID %>" value="<%=a.getId()%>" />
		  		    <input type="hidden" name="<%=Constant.PARAM_PAGE %>" value="<%=Toolkit.getCurrentPage(request)%>" />
		  		    <%if ( a.getPrivateView() == 1 ) { %>
		  		    <input type="hidden" name="<%=Constant.PARAM_ALBUM_PRIVATE %>" value="0" />
                    <button type="submit" class="btn btn-default" >
                      <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>&nbsp;
                      <%=messages.getString("album_private_set_btn_public") %>
                    </button>
                    <%} else { %>
                    <input type="hidden" name="<%=Constant.PARAM_ALBUM_PRIVATE %>" value="1" />
                    <button type="submit" class="btn btn-default">
                      <span class="glyphicon glyphicon-eye-close" aria-hidden="true"></span>&nbsp;
                      <%=messages.getString("album_private_set_btn_private") %>
                    </button>
                    <%}%>
                    </form>
                  </td>
                  <td>
                    <form method="post" action="act.jsp">
		  		    <input type="hidden" name="m" value="<%=SwitchAction.BACK_USER_DEL_ALBUM %>" />
		  		    <input type="hidden" name="<%=Constant.PARAM_ALBUM_ID %>" value="<%=a.getId()%>" />
		  		    <input type="hidden" name="<%=Constant.PARAM_PAGE %>" value="<%=Toolkit.getCurrentPage(request)%>" />
                    <button type="submit" class="btn btn-default">
                      <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>&nbsp;
                      <%=messages.getString("album_delete") %>
                    </button>
                    </form>
                  </td>
                </tr>
                <%} %>
              </tbody>
            </table>
            <div class="text-center">
			  <%=pagesBtnCode %>
		    </div>
          </div>
        </div>
      </div>
    </div>
    
    
	<div class="modal fade" id="addAlbum" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-sm">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			<h4 class="modal-title" id="myModalLabel"><%=messages.getString("album_title") %></h4>
		  </div>
		  <form method="post" action="act_album_up.jsp" enctype="multipart/form-data">
		  <div class="modal-body">
			  <input type="hidden" name="m" value="<%=SwitchAction.BACK_USER_ADD_ALBUM %>" />
			  <div class="form-group">
				<label for="album"><%=messages.getString("album_name") %></label>
				<input type="text" class="form-control" id="exampleInputEmail1" placeholder="" name="<%=Constant.PARAM_ALBUM_NAME %>">
			  </div>
			  <div class="form-group">
				<label for="album"><%=messages.getString("album_parent") %></label>
				<select class="form-control" name="<%=Constant.PARAM_ALBUM_PARENT %>">
					<option value="<%=Constant.ALBUM_VIDEO%>"><%=messages.getString(Constant.ALBUM_VIDEO) %></option>
					<option value="<%=Constant.ALBUM_AUDIO%>"><%=messages.getString(Constant.ALBUM_AUDIO) %></option>
					<option value="<%=Constant.ALBUM_IMAGE%>"><%=messages.getString(Constant.ALBUM_IMAGE) %></option>
					<option value="<%=Constant.ALBUM_OTHER%>"><%=messages.getString(Constant.ALBUM_OTHER) %></option>
				</select>
			  </div>
			  <div class="form-group">
				<label for="album"><%=messages.getString("album_image") %></label>
				<input type="file" id="exampleInputFile" name="<%=Constant.PARAM_ALBUM_IMAGE_URL %>" name="image">
				<p class="help-block"><%=messages.getString("album_image_notice") %></p>
			  </div>
			
		  </div>
		  <div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">
			  <span class="glyphicon glyphicon-log-out" aria-hidden="true"></span>&nbsp;
			  <%=messages.getString("close") %>
			</button>
			<button type="submit" class="btn btn-default" >
              <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>&nbsp;
              <%=messages.getString("confirm") %>
            </button>
		  </div>
		  </form>
		</div>
	  </div>
	</div>
	
	<div class="modal fade" id="updateAlbum" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-sm">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			<h4 class="modal-title" id="myModalLabel"><%=messages.getString("album_title") %></h4>
		  </div>
		  <form method="post" action="act_album_up.jsp" enctype="multipart/form-data">
		  <div class="modal-body">
			  <input type="hidden" name="m" value="<%=SwitchAction.BACK_USER_UPDATE_ALBUM %>" />
			  <input type="hidden" id="update-album-id" name="<%=Constant.PARAM_ALBUM_ID %>" value="" />
			  <input type="hidden" name="<%=Constant.PARAM_PAGE %>" value="<%=Toolkit.getCurrentPage(request)%>" />
			  <div class="form-group">
				<label for="album"><%=messages.getString("album_name") %></label>
				<input type="text" class="form-control" id="update-album-name"  placeholder="" name="<%=Constant.PARAM_ALBUM_NAME %>">
			  </div>
			  <div class="form-group">
				<label for="album"><%=messages.getString("album_parent") %></label>
				<select class="form-control" id="update-album-parent"  name="<%=Constant.PARAM_ALBUM_PARENT %>">
					<option id="update-album-parent-video" value="<%=Constant.ALBUM_VIDEO%>"><%=messages.getString(Constant.ALBUM_VIDEO) %></option>
					<option id="update-album-parent-audio" value="<%=Constant.ALBUM_AUDIO%>"><%=messages.getString(Constant.ALBUM_AUDIO) %></option>
					<option id="update-album-parent-image" value="<%=Constant.ALBUM_IMAGE%>"><%=messages.getString(Constant.ALBUM_IMAGE) %></option>
					<option id="update-album-parent-other" value="<%=Constant.ALBUM_OTHER%>"><%=messages.getString(Constant.ALBUM_OTHER) %></option>
				</select>
			  </div>
			  <div class="form-group">
				<label for="album"><%=messages.getString("album_image") %></label>
				<input type="file" id="album" name="<%=Constant.PARAM_ALBUM_IMAGE_URL %>" name="image">
				<p class="help-block"><%=messages.getString("album_image_notice_for_update") %></p>
			  </div>			
		  </div>
		  <div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">
			  <span class="glyphicon glyphicon-log-out" aria-hidden="true"></span>&nbsp;
			  <%=messages.getString("close") %>
			</button>
			<button type="submit" class="btn btn-default" >
              <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>&nbsp;
              <%=messages.getString("confirm") %>
            </button>
		  </div>
		  </form>
		</div>
	  </div>
	</div>
	
	
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
			<button type="button" class="btn btn-default" data-dismiss="modal">
			  <span class="glyphicon glyphicon-log-out" aria-hidden="true"></span>&nbsp;
			  <%=messages.getString("close") %>
			</button>
		  </div>
		</div>
	  </div>
	</div>
	
	<div class="modal fade" id="searchDialog" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-sm">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			<h4 class="modal-title" id="myModalLabel"><%=messages.getString("query_title") %></h4>
		  </div>
		  <form method="get" action="album.jsp">
		  <div class="modal-body" id="modal-notice">
			  <div class="form-group">
				<label for="album"><%=messages.getString("album_name") %></label>
				<input type="text" class="form-control" id="update-album-name"  placeholder="" name="<%=Constant.PARAM_ALBUM_NAME %>">
			  </div>
			  <div class="form-group">
				<label for="album"><%=messages.getString("album_parent") %></label>
				<select class="form-control" id="update-album-parent"  name="<%=Constant.PARAM_ALBUM_PARENT %>">
				    <option id="update-album-parent-video" value="<%=Constant.ALBUM_ALL%>"><%=messages.getString(Constant.ALBUM_ALL) %></option>
					<option id="update-album-parent-video" value="<%=Constant.ALBUM_VIDEO%>"><%=messages.getString(Constant.ALBUM_VIDEO) %></option>
					<option id="update-album-parent-audio" value="<%=Constant.ALBUM_AUDIO%>"><%=messages.getString(Constant.ALBUM_AUDIO) %></option>
					<option id="update-album-parent-image" value="<%=Constant.ALBUM_IMAGE%>"><%=messages.getString(Constant.ALBUM_IMAGE) %></option>
					<option id="update-album-parent-other" value="<%=Constant.ALBUM_OTHER%>"><%=messages.getString(Constant.ALBUM_OTHER) %></option>
				</select>
			  </div>
		  </div>
		  <div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">
			  <span class="glyphicon glyphicon-log-out" aria-hidden="true"></span>&nbsp;
			  <%=messages.getString("close") %>
			</button>
			<button type="submit" class="btn btn-default" >
              <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>&nbsp;
              <%=messages.getString("confirm") %>
            </button>
		  </div>
		  </form>
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
    var select = function(albumid,optionid){
    	var source = $('#album-parent-'+albumid).val();
    	var option = $('#'+optionid).val();
		if ( source == option ) {
			$('#update-album-parent').val(option);
		}
	}
	var updateAlbum = function(id){
		$('#update-album-id').val($('#album-id-'+id).html());
		$('#update-album-name').val($('#album-name-'+id).html());
		select(id,'update-album-parent-video');
		select(id,'update-album-parent-audio');
		select(id,'update-album-parent-image');
		select(id,'update-album-parent-other');		
		$('#updateAlbum').modal('show');
	}
	
    </script>
    
    <%=gAction.getNoticeHTML(messages, request, noticeDivId, noticeWindowId)%>

  </body>
</html>
