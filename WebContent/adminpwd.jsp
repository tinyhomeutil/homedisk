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
String title = ProductInfo.getProductName() + " - " +
			   messages.getString("pwd_title");

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
              <a href="adminoverview.jsp">
              <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>&nbsp;
              <%=messages.getString("admin_page_overview") %>
              </a>
            </li>
			<li class="active">
			  <a href="adminpwd.jsp">
			  <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;
			  <%=messages.getString("admin_page_pwd") %><span class="sr-only">(current)</span>
			  </a>
			</li>
			 
            <li>
              <a href="adminusersmgr.jsp">
              <span class="glyphicon glyphicon-user" aria-hidden="true"></span>&nbsp;
              <%=messages.getString("admin_page_user") %>
              </a>
            </li>
            <li>
              <a href="admindelfile.jsp">
              <span class="glyphicon glyphicon-file" aria-hidden="true"></span>&nbsp;
              <%=messages.getString("admin_page_resource") %>              
              </a>
            </li>
          </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h1 class="page-header">
            <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;
            <%=messages.getString("pwd_title") %>
          </h1>
          <div class="table-responsive">
            <table class="table table-striped">
			  <thead>
			    <th><%=messages.getString("pwd_type") %></th>
				<th><%=messages.getString("pwd_input_1") %></th>
				<th><%=messages.getString("pwd_input_2") %></th>
				<th><%=messages.getString("pwd_action") %></th>
              </thead>
              <tbody>
			    <form method="post" action="act.jsp">
			    <input type="hidden" name="m" value="<%=SwitchAction.BACK_ADMIN_UPDATE_MANAGE_PWD %>" />
                <tr>
                  <td><h5><%=messages.getString("pwd_manage_pwd") %></h5></td>
                  <td><input type="password" class="form-control" value="" name="<%=Constant.PARAM_PWD1 %>" /></td>
				  <td><input type="password" class="form-control" value="" name="<%=Constant.PARAM_PWD2 %>" /></td>
				  <td>
				    <button type="submit" class="btn btn-default" data-target="#notice">
				      <span class="glyphicon glyphicon-erase" aria-hidden="true"></span>&nbsp;
				      <%=messages.getString("pwd_btn_update") %>
				    </button>
				  </td>
                </tr>
				</form>
              </tbody>
            </table>
          </div>
        </div>
      </div>
	  
	<!-- Modal -->
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

    </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="bootstrap/js/jquery.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="bootstrap/js/ie10-viewport-bug-workaround.js"></script>

    <%=gAction.getNoticeHTML(messages, request, noticeDivId, noticeWindowId)%>

  </body>
</html>
