<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
<link rel="stylesheet" href="css/header.css" />
<div id="header">
	<section id="nav-wrapper">
		<div id="top-logo">
			<img src="assets/kosta-logo.png" alt="kosta-edu-logo" />
		</div>
		<ul id="nav-ul">
			<li  class="nav-li notification-menu">
				<a id="nav-notification" href="#">공지사항</a></li>
			<li id="nav-course" class="nav-li course-menu">
				<a href="#">과정 관리</a></li>
			<li id="nav-certification" class="nav-li certification-menu">
				<a href="#">수료증 관리</a></li>
			<li id="nav-scholarship" class="nav-li scholarship-menu">
				<a href="#">장학금 관리</a></li>
			<li id="nav-benefit-manage" class="nav-li benefit-menu">
				<a href="#">지원금 관리</a></li>
			<li id="nav-student" class="nav-li student-menu">
				<a href="#">수강생 관리</a></li>
		</ul>
		<div id="header-btns">
			<form action="controller?cmd=logoutAction" method="post"
				class="btn-wrapper">
				<button id="logout-btn" type="submit">로그아웃</button>
			</form>
			<div class="btn-wrapper" id="setting-btn-wrapper">
				<button id="setting-btn" type="button"></button>
			</div>
		</div>
	</section>
	<jsp:include page="./dropdown.jsp" />
	<script src="scripts/common.js">
	</script>
</div>
