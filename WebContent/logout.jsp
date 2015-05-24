<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page language="java" import="com.tinyhomeutil.homedisk.action.*" %>
<%
session.setAttribute(Constant.PARAM_SESSION_USER_OBJECT, null);
session.setAttribute(Constant.PARAM_SESSION_USER_ID, null);
response.sendRedirect("index.jsp");
%>