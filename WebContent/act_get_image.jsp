<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page language="java" import="com.tinyhomeutil.homedisk.action.*" %>
<%@ page language="java" import="java.util.*" %>
<%@ page language="java" import="com.tinyhomeutil.homedisk.action.*" %>
<%@ page language="java" import="com.tinyhomeutil.homedisk.i18n.*" %>
<%@ page language="java" import="com.tinyhomeutil.homedisk.util.*" %>
<%@ page language="java" import="com.tinyhomeutil.homedisk.pojo.*" %>
<%
//request.setCharacterEncoding("utf-8");

IFore action = (IFore)ActionFacotry.getInstance().getAction(SwitchAction.FORE_GET_IMAGE_FOR_AUDIO_PLAY);
Resource r = action.getImageResourceForAudioPlay(session, request, response);
out.print(Toolkit.resource2Json(r));
%>