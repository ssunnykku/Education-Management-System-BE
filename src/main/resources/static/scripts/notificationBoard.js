const notificationEnrollBtn = document.querySelector(
    '#notification-enroll-btn'
);
const notificationDeleteBtn = document.querySelector(
    '#notification-delete-btn'
);
let notificationCheckboxAll;

const notificationTable = document.querySelector("#notification-table");
const notificationRow = document.querySelector("#notification-table-row");
const notificationBoardNo = document.querySelector(".notificationBoard-no");

/**  Modal*/
const modal = document.querySelector(".modal-background ");

async function pageLoad() {
//await fetch("controller?cmd=notificationBoardAction")
    let urlParams = new URL(location.href).searchParams;
    let page = urlParams.get("page");
    await fetch("controller?cmd=notificationBoardPageAction&page=" + page)
        .then((response) => response.json())
        .then((data) => {
            console.log(data);
            $('#notification-table').append(getNotificationList(data));
            notificationCheckboxAll = document.querySelectorAll('.notificationBoard-check');
        })
}

function getNotificationList(data) {
    let result = '';
    for (var i = 0; i < data.length; i++) {
        const notification = data[i];

        result += `<div id="notification-table-row">
	<div class="notificationBoard-checkbox">
	<input type="checkbox" name="" class="notificationBoard-check"
		value="${notification.notificationId}" />
			</div>
	<div class="notificationBoard-no">
	<span>${notification.notificationId}</span>
	</div>
	<a href="controller?cmd=notificationPageUI&notificationId=${notification.notificationId}"  class="notificationBoard-title">
	<span>${notification.title}</span>
	</a>
	<div class="notificationBoard-enroll-date">
	<span>${notification.notificationDate}</span>
	</div>
	<div class="notificationBoard-writer">
	<span>${notification.name}</span>
	</div>
	</div>`
    }

    return result;
}

function openEnrollPage() {
    window.location.href = 'controller?cmd=addNotificationUI';
}


async function deletePage() {

    await notificationCheckboxAll.forEach((box) => {
        if (box.checked) {
            const url = "controller?cmd=deleteNotificationAction&notificationId=" + box.value
            fetch(url, {
                method: "DELETE",
            })
        }

    });

    $(".modal-background").css("dispoay", "none");
    $(".remove-wrapper").css("dispoay", "none");
    $("#notification-body").css("position", "");
    $("#notification-body").css("width", "");
    $("#notification-body").css("height", "");
    $("#notification-body").css("overflow", "");

    window.location.href = 'controller?cmd=notificationBoardUI';

// 여기에서 삭제 확인 해주기
}

let selected = [];
$("#notification-delete-btn").click(function () {
    $(".notificationBoard-check").each(function () {
        if ($(this).prop("checked")) {
            selected.push(this.value);
        }
    });
});

function openDeleteModal() {
//console.log("selected: "+ selected);

    if (selected.length != 0) {
        $(".modal-background").removeAttr("style");
        $("#notification-body").css("position", "fixed");
        $("#notification-body").css("width", "100%");
        $("#notification-body").css("height", "100%");
        $("#notification-body").css("overflow", " hidden");
        $(".remove-wrapper").removeAttr("style");
        return;
    }
    $(".modal-background").removeAttr("style");
    $(".select-alert-wrapper").removeAttr("style");

    $("#notification-body").css("position", "fixed");
    $("#notification-body").css("width", "100%");
    $("#notification-body").css("height", "100%");
    $("#notification-body").css("overflow", " hidden");

}

$("#notification-delete-Modal-btn").click(deletePage);
$("#notification-delete-Modal-cancel-btn").click(() => {
    $(".modal-background").css("display", "none");
    $(".remove-wrapper").removeAttr("style");
    $(".remove-wrapper").css("display", "none");
    selected = [];
})
$("#alert-confirm-btn").click(() => {
    $(".modal-background").css("display", "none");
    $(".select-alert-wrapper ").removeAttr("style");
    $(".select-alert-wrapper ").css("display", "none");
})


/** notificationPost  */
$("#notification-post-edit-btn").click(() => {
    const notificationId = $(".notificationPost-no").children("span").eq(1).text();
    window.location.href = "controller?cmd=setNotificationUI&notificationId=" + notificationId;
})

$("#setNotification-concel-btn").click(() => {
//	const notificationId = $(".notificationPost-no").children("span").eq(1).text();
//	window.location.href = "controller?cmd=notificationPageUI&notificationId="+notificationId;
    window.location.href = "controller?cmd=notificationBoardUI";
})

/** 아래 삭제 구현 해야함 **/

//$("notification-post-delete-btn").click(openDeleteModal);

//		()=>{
//	console.log("이거");
//	const notificationId = $(".notificationPost-no").children("span").eq(1).text();
//	const url = "controller?cmd=deleteNotificationAction&notificationId=" + notificationId ;
//	fetch(url, {
//		method: "DELETE",
//	})
//	window.location.href = "controller?cmd=notificationBoardUI";
//}
//)

/*** 체크박스 상단 전체 선택 ***/
$('#title-checkbox').change(() => {
    if ($('#title-checkbox').is(':checked')) {
        $(".notificationBoard-check").prop("checked", true);
        return;
    }
    $(".notificationBoard-check").prop("checked", false);
})

document.addEventListener("DOMContentLoaded", pageLoad);
notificationEnrollBtn.addEventListener('click', openEnrollPage);
notificationDeleteBtn.addEventListener('click', openDeleteModal);