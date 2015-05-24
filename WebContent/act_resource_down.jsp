<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page language="java" import="com.tinyhomeutil.homedisk.action.*" %>

<%
request.setCharacterEncoding("utf-8");

FileDownloadAction.getInstance().downloadResource(session, request, response);

//必须在此处加上下面两句，否则下载文件会出异常
out.clear();
out = pageContext.pushBody();
%>