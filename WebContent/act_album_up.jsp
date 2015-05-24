<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page language="java" import="com.tinyhomeutil.homedisk.action.*" %>

<%
request.setCharacterEncoding("utf-8");

FileUploadAction.getInstance().albumSwitchAction(session,request,response);

%>