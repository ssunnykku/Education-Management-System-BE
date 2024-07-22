const params = new URL(location.href).search.split("=")[1];

let currentPage = 1;
const pageSize = 10; // 한 블록 당 페이지 수
let currentBlock = 1;

let totalPages = 0;

async function fetchScholarshipBoard(page) {
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

    await fetch("/scholarships?page=" + page, requestOptions)
        .then((res) => res.json())
        .then(async (data) => {
            const dataList = data.result;
            await getSettlementList(dataList);
        })
        .catch((error) => console.error(error));
}


$(document).ready(() => {
    fetchCountScholarship();
    fetchScholarshipBoard(1)
})

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
                            <span id="totalAmount">${data[i].scholarshipAmount.toLocaleString('ko-KR')}</span>
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

function fetchCountScholarship() {

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

    fetch("/scholarships/count", {
        method: "POST",
        headers: myHeaders,
        body: raw,
    })
        .then((res) => res.json())
        .then(async (data) => {
            $(".scholarship-cnt-pages").html(`<span>총 ${data.result}</span>건`);

            totalPages = Math.ceil(data.result / 10);

            updatePagination();
        })
        .catch((error) => console.error(error));

}

$(".board-filter-search-btn").click(async function () {
    $("#page_number").html("");

    await fetchScholarshipBoard(1);
    await fetchCountScholarship()
});

function updatePagination() {
    $("#page_number").html("");

    let firstPage = (currentBlock * pageSize) - pageSize + 1;
    let lastPage = totalPages <= currentBlock * pageSize ? totalPages : currentBlock * pageSize;

    let result = "";
    for (let i = firstPage; i <= lastPage; i++) {

        let num = i;
        let fontWeight = (num == currentPage) ? 'bold' : 'normal';

        result += `<li>
                    <a class="page-link" style="font-weight: ${fontWeight}"  onclick="fetchScholarshipBoard(${num})">${num}</a>
                    </li>`;
    }
    $("#page-number").append(result);

    let selected = [];
    $("#settlement-btn").click(function () {

        $("#settlementListModal").removeClass('check-settlement-modal');

        $(".checkbox").each(function () {
            if ($(this).prop("checked")) {
                fetch("/scholarships/settlement/" + this.value, {
                    method: "POST",
                })
                    .then((res) => res.json())
                    .then((data) => {
                        if (data.result) {
                            // Bootstrap 모달 띄우기
                            $('#settlementSuccessModal').modal('show');

                            // 모달이 닫힐 때 페이지 리디렉션
                            $('#settlementSuccessModal').on('hidden.bs.modal', function () {
                                location.href = "/ems/scholarships";
                            });
                        }
                    })
                    .catch((error) => console.error(error));
            }
        });
    });


    $("#title-checkbox").on("change", toggleAll);

    function toggleAll() {
        const isChecked = $("#title-checkbox").prop('checked');
        $("#scholarshipBoard-table input[type=checkbox]").prop('checked', isChecked);
    }


    /*course 목록*/

    fetch("/courses/course-number-list?excludeExpired=false", {
        method: "GET",
    })
        .then((res) => res.json())
        .then((data) => {
            const courseList = data.result;
            let result = '';
            for (const resultElement of courseList) {
                result += `<option value=${resultElement}>${resultElement}</option>`;
            }
            $(".scholarship-courseId-filter").append(result);

        })
        .catch((error) => console.error(error));

    /*pagenation*/

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
}