<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<section id="nav-wrapper-dropdown" style="display: none">
		<div id="top-logo">
			<h1></h1>
		</div>
		<ul id="nav-ul" class = "dromdown-ul" >
			<li class="nav-li-dropdown">
				<div id ="notification-underline" class="header-underline"></div> 
				<a class="dropdown-notification" href="controller?cmd=notificationBoardUI&page=1">공지사항</a>
			</li>
			<li class="nav-li-dropdown">
				<div id ="course-underline" class="header-underline"></div> 
				<a class="dropdown-course" href="controller?cmd=courseBoardUI">과정관리</a>
			</li>
			<li class="nav-li-dropdown">
				<div id ="certification-underline" class="header-underline"></div> 
				<a class="dropdown-certification" href="controller?cmd=certificationBoardUI">수료 대상 관리</a>
			</li>
			<li class="nav-li-dropdown">
				<div id ="scholarship-underline" class="header-underline"></div>
				<a class="dropdown-scholarship" href="controller?cmd=scholarshipBoardUI">장학금 정산</a> 
				<a class="dropdown-scholarship" href="controller?cmd=scholarshipResultBoardUI">정산 결과 조회</a>
			</li>
			<li class="nav-li-dropdown">
				<div id ="benefit-underline" class="header-underline"></div> 
				<a class="dropdown-benefit" href="controller?cmd=benefitBoardUI">지원금 정산</a> 
				<a class="dropdown-benefit" href="controller?cmd=benefitResultBoardUI">정산 결과조회</a>
			</li>
			<li class="nav-li-dropdown">
				<div id ="student-underline" class="header-underline"></div> 
				<a class="dropdown-student" href="controller?cmd=studentBoardUI">수강생 정보</a> 
				<a class="dropdown-student" href="controller?cmd=attendanceBoardUI">출결 관리</a> 
				<a class="dropdown-student" href="controller?cmd=pointBoardUI">포인트 관리</a>
			</li>
		</ul>
		<div id="header-btns">
			<div></div>
			<div></div>
		</div>
	</section>
