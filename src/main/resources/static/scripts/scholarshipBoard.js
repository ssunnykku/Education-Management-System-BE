const params = new URL(location.href).search.split("=")[1];

let currentPage = 1;
const pageSize = 10; // 한 블록 당 페이지 수
let currentBlock = 1;

let totalPages = 0;

async function getSettlementList(data) {
    let result = '';
    for (let i = 0; i < data.length; i++) {
        result += `<div class="scholarshipBoard-row">
                        <div class="scholarshipBoard-checkbox">
                            <input type="checkbox" name=${data[i].courseNumber} class="checkbox" value=${data[i].studentCourseSeq}>
                        </div>
                        <div class="scholarshipBoard-courseId">
                            <span id="courseNumber">${data[i].courseNumber}</span>
                        </div>
                        <div class="scholarshipBoard-course-name">
                            <span id="courseName">${data[i].courseName}</span>
                        </div>
                        <div class="scholarshipBoard-hrd-net-id">
                            <span id="hrdNetId">${data[i].hrdNetId}</span>
                        </div>
                        <div class="scholarshipBoard-name">
                            <span id="name">${data[i].name}</span>
                        </div>
                        <div class="scholarshipBoard-bank">
                            <span id="bank">${data[i].bank}</span>
                        </div>
                        <div class="scholarshipBoard-account">
                            <span id="account">${data[i].account}</span>
                        </div>
                        <div class="scholarshipBoard-point">
                            <span id="point">${data[i].totalPoint}</span>
                        </div>
                        <div class="scholarshipBoard-total-amount">
                            <span id="totalAmount">${data[i].scholarshipAmount}</span>
                        </div>
                    </div>`;
    }

    $("#scholarship-table-contents").html("");
    $("#scholarship-table-contents").append(result);
}

function searchInput() {
    return $(".search-input").val();
}

function courseNumber() {
    return $(".scholarship-courseId-filter option:selected").text();
}

$(".board-filter-search-btn").click(async function () {
    $("#page_number").html("");
    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    const raw = JSON.stringify({
        "name": searchInput(),
        "courseNumber": courseNumber() == "기수" ? "" : courseNumber()
    });

    const requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
        redirect: "follow"
    };

    await fetchScholarshipBoard(1);

    fetch("/scholarships/count", {
        method: "POST",
        headers: myHeaders,
        body: raw,
    })
        .then((res) => res.json())
        .then(async (data) => {
            $(".scholarship-cnt-pages").html(`<span>총 ${data.result}</span>건`);

            // const countPage = Math.ceil(data.result / 10);
            // const countPage = 12;  // Example total pages
            totalPages = Math.ceil(data.result / 10);

            updatePagination();
        })
        .catch((error) => console.error(error));
});

function updatePagination() {
    $("#page_number").html("");

    let firstPage = (currentBlock * pageSize) - pageSize + 1;
    let lastPage = totalPages <= currentBlock * pageSize ? totalPages : currentBlock * pageSize;

    for (let i = firstPage; i <= lastPage; i++) {
        let num = i;
        $("#page_number").append(`<a class="page-link" onclick="fetchScholarshipBoard(${num})">${num}</a>`);
    }
}

$("#next").click(() => {
    if (currentBlock * pageSize < totalPages) {
        currentBlock += 1;
        currentPage = (currentBlock * pageSize) - pageSize + 1;
        updatePagination();
    }
});

$("#before").click(() => {
    if (currentBlock > 1) {
        currentBlock -= 1;
        currentPage = (currentBlock * pageSize) - pageSize + 1;
        updatePagination();
    }
});


async function fetchScholarshipBoard(param) {
    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    const raw = JSON.stringify({
        "name": searchInput(),
        "courseNumber": courseNumber() == "기수" ? "" : courseNumber()
    });

    const requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
        redirect: "follow"
    };

    await fetch("/scholarships?page=" + param, requestOptions)
        .then((res) => res.json())
        .then(async (data) => {
            const dataList = data.result;
            await getSettlementList(dataList);
        })
        .catch((error) => console.error(error));
}

/*** 체크박스 상단 전체 선택 ***/
// $('#title-checkbox').change(() => {
//     if ($('#title-checkbox').is(':checked')) {
//         $(".checkbox").prop("checked", true);
//         return;
//     } else {
//         $(".checkbox").prop("checked", false);
//     }
// })
//
// let selected = [];
// $("#settlement-btn").click(function () {
//     $(".checkbox").each(function () {
//         if ($(this).prop("checked")) {
//             selected.push(this.value);
//             console.log(this.value);
//         }
//     });
// });

$("#title-checkbox").click(toggleAll())


async function removeCourse() {
    let selectedCourseSeqList = [];
    $("div.courseBoard-row").not("#courseBoard-title-row").each((index, item) => {
        if ($(item).find("input[type=checkbox]").prop("checked")) {
            const courseSeq = $(item).data("courseSeq");
            selectedCourseSeqList.push(courseSeq);
        }
    });
    if (!selectedCourseSeqList.length) {
        showMessage("삭제할 과정을 선택해주십시오.");
        return;
    }
}


function toggleAll() {
    const isChecked = $("#title-checkbox").prop('checked');
    $(".checkbox input[type=checkbox]").prop('checked', isChecked);
}

function showMessage(msg) {
    const messageModal = $("#removeCourseCompleteModal").get()[0];
    messageModal.querySelector("#message").innerText = msg;
    messageModal.showPopover();
}