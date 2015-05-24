<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page language="java" import="java.util.*" %>
<%@ page language="java" import="com.tinyhomeutil.homedisk.action.*" %>
<%@ page language="java" import="com.tinyhomeutil.homedisk.i18n.*" %>
<%@ page language="java" import="com.tinyhomeutil.homedisk.util.*" %>
<%
Locale locale = request.getLocale();
ResourceBundle messages = LocaleToolkit.getBundle(locale);
String title = ProductInfo.getProductName() + " - " +
			   messages.getString("login_title");

IFore action = (IFore)ActionFacotry.getInstance().getAction(SwitchAction.FORE_INDEX);

%>
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
	<link href="css/signin.css" rel="stylesheet">
	
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

    <div class="container">
      <form class="form-signin" method="post" action="act.jsp">
        <input type="hidden" name="m" value="<%=SwitchAction.FORE_LOGIN %>" />
        <h2 class="form-signin-heading">
          <%=messages.getString("login_input_title") %>
        </h2>
        <label for="inputUserName" class="sr-only"><%=messages.getString("login_input_username") %></label>
        <input type="text" name="<%=Constant.PARAM_USER_NAME %>" class="form-control" placeholder="<%=messages.getString("login_input_username") %>" required autofocus>
        <label for="inputPassword" class="sr-only">
          <%=messages.getString("login_input_pwd") %>
        </label>
        <input type="password" name="<%=Constant.PARAM_PWD %>" class="form-control" placeholder="<%=messages.getString("login_input_pwd") %>" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">
          <%=messages.getString("login_btn") %>
        </button>
      </form>

    </div> <!-- /container -->
	
	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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

	<!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="bootstrap/js/jquery.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="js/ie10-viewport-bug-workaround.js"></script>
    
<% 
String result = (String)request.getParameter(Constant.PARAM_RETURN);
boolean hidden = true;
String notice = "";
if ( result != null ) {
	hidden = false;
	if ( Constant.PARAM_RETURN_SUCCESS.equals(result) ) {
		notice = LocaleToolkit.getString(messages, request, Constant.PARAM_RETURN_SUCCESS);
	} else {
		notice = LocaleToolkit.getString(messages, request, Constant.PARAM_RETURN_FAIL);
	}
%>
<script>
$('#modal-notice').html('<%=notice%>');
$('#myModal').modal('show');
</script>
<%	
}
%>
    
  </body>
</html>
