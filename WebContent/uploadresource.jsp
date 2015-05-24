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
			   messages.getString("resource_upload");

IUser action = (IUser)ActionFacotry.getInstance().getAction(SwitchAction.BACK_USER_UPLOAD_RESOURCE);
String albumsJSON = action.getAllAlbums(session,request, response);

IGlobal gAction = (IGlobal)ActionFacotry.getInstance().getAction(SwitchAction.GLOBAL_NOTICE);
String noticeDivId = "#modal-notice";
String noticeWindowId = "#notice";
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
    
    <!--引入CSS-->
	<link rel="stylesheet" type="text/css" href="webuploader/webuploader.css">
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
            <li>
              <a href="album.jsp">
              <span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span>&nbsp;
              <%=messages.getString("user_page_album") %>
              </a>
            </li>
            <li class="active">
              <a href="resource.jsp">
              <span class="glyphicon glyphicon-file" aria-hidden="true"></span>&nbsp;
              <%=messages.getString("user_page_resource") %><span class="sr-only">(current)</span>
              </a>
            </li>
          </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h1 class="page-header">
            <span class="glyphicon glyphicon-upload" aria-hidden="true"></span>&nbsp;
            <%=messages.getString("resource_upload") %>
          </h1>
          <div class="table-responsive">
             <form method="post" action="act.jsp">
				  <input type="hidden" name="m" value="<%=SwitchAction.BACK_USER_SAVE_RESOURCE %>" />
				  <div class="form-group col-xs-7">
					<label for="resource"><%=messages.getString("album_parent") %></label>
					<select class="form-control" id="album-parent">
						<option id="update-album-parent-video" value="<%=Constant.ALBUM_VIDEO%>"><%=messages.getString(Constant.ALBUM_VIDEO) %></option>
						<option id="update-album-parent-audio" value="<%=Constant.ALBUM_AUDIO%>"><%=messages.getString(Constant.ALBUM_AUDIO) %></option>
						<option id="update-album-parent-image" value="<%=Constant.ALBUM_IMAGE%>"><%=messages.getString(Constant.ALBUM_IMAGE) %></option>
						<option id="update-album-parent-other" value="<%=Constant.ALBUM_OTHER%>"><%=messages.getString(Constant.ALBUM_OTHER) %></option>
					</select>
				  </div>
				  <div class="form-group col-xs-7">
					<label for="resource"><%=messages.getString("album_child") %></label>
					<select class="form-control" id="album-child"  name="<%=Constant.PARAM_ALBUM_ID %>">
					</select>
				  </div>
				  <div class="form-group col-xs-7">
					<label for="resource"><%=messages.getString("resource_remark") %></label>
					<input type="text" class="form-control" placeholder="" name="<%=Constant.PARAM_RESOURCE_REMARK %>">
				  </div>
				  <div class="form-group col-xs-7">
					<label for="resource"><%=messages.getString("resource_upload_choose") %></label>
					<p class="help-block"><%=messages.getString("resource_upload_notice") %></p>
					<div>
					    <button type="button" class="btn btn-default" id="selectButton" >
			              <span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span>&nbsp;
			              <%=messages.getString("resource_upload_choose_btn") %>
			            </button>
					    &nbsp;&nbsp;
					    <button type="button" class="btn btn-default" id="uploadButton" >
			              <span class="glyphicon glyphicon-open" aria-hidden="true"></span>&nbsp;
			              <%=messages.getString("resource_upload_btn") %>
			            </button>
			            <table class="table table-striped" >
			              <thead>
							<th><%=messages.getString("resource_name") %></th>
							<th><%=messages.getString("resource_size") %></th>
							<th><%=messages.getString("resource_rate_of_progress") %></th>
			              </thead>
			              <tbody id="processContainer" style="display:none;">
			                <tr>
			                  <td width="400">{FileName}</td>
			                  <td width="100">{FileSize}</td>
			                  <td width="200">{UploadPercentage}</td>
			                </tr>
			              </tbody>  
			            </table>	
					</div>	
				  </div>
				  <div class="modal-footer col-xs-7">
					<button type="submit" class="btn btn-default" >
			          <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>&nbsp;
			          <%=messages.getString("resource_upload_save_btn") %>
			        </button>
				  </div>	
			  </form>
          </div>
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
	
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="bootstrap/js/jquery.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="bootstrap/js/ie10-viewport-bug-workaround.js"></script>
	<script type="text/javascript" src="html5upload/upload.js"></script>
	
    <script>	
	//get parent to switch children
	$("#album-parent").bind("change", function() { 
		//alert($(this).val()); 
		if ( $(this).val() == "<%=Constant.ALBUM_VIDEO%>" ) {
			newAlbumChildren(albumObj.video);
		} else if ( $(this).val() == "<%=Constant.ALBUM_AUDIO%>" ) {
			newAlbumChildren(albumObj.audio);
		} else if ( $(this).val() == "<%=Constant.ALBUM_IMAGE%>" ) {
			newAlbumChildren(albumObj.image);
		} else if ( $(this).val() == "<%=Constant.ALBUM_OTHER%>" ) {
			newAlbumChildren(albumObj.other);
		} else {
			$("#album-child").empty();
		}
	}); 

	//将JSON转换为JavaScript对象
	var albumObj = <%=albumsJSON %>;
	
	//分类联动
	var newAlbumChildren = function(album){
		$("#album-child").empty();
		$(album).each(function(index) { 
	        var val = album[index]; 
	        var option = $("<option>").val(val.id).text(val.name);
		    $("#album-child").append(option);
		});
	}
	
	//预设子分类
	newAlbumChildren(albumObj.video);
	
	var initUploader = function () {
	    $ling.html5Upload.config = {
	        selectButtonId: "selectButton",
	        uploadButtonId: "uploadButton",
	        fileProgressContainerId: "processContainer",
	        url: "act_resource_up.jsp",
	        //url: "resupact.jsp",
	        //fileTypes: "|.jpg|.jpeg|.png|",
	        fileSize: 2000 * 1024 * 1024,
	        //回调可以调用三个参数 总数,成功数和失败数,如果不需要可以不传
	        //callback: function (allCount, successCount, failCount) { alert(allCount + "/" + successCount + "/" + failCount); }
	    };
	    $ling.html5Upload.init();
	};
	
	initUploader();
    </script>
    <%=gAction.getNoticeHTML(messages, request, noticeDivId, noticeWindowId)%>
  </body>
</html>
