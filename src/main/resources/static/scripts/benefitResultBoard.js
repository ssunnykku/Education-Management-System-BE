const params = new URL(location.href).search.split("=")[1];

let currentPage = 1;
const pageSize = 10; // 한 블록 당 페이지 수
let currentBlock = 1;

let totalPages = 0;

function handleError(message) {
    $("#error").html("");
    $("#error").append(`<span style="color: red">${message}</span>`)
}

function getName() {
    return $(".search-input").val();
}

function getCourseNumber() {
    return $(".courseId-filter option:selected").text();
}

function getBenefitSettlementDate() {
    return $("#settlement-date").val();
}


$(document).ready(() => {
    fetchResultData(1);
})


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
        $(".courseId-filter").append(result);

    })
    .catch((error) => console.error(error));


function getResultData(dataList) {
    let result = '';
    for (let i = 0; i < dataList.length; i++) {
        result += `<div class="result-board-row">
                <div class="benefitSettlementResult-courseId">
                    <span>${dataList[i].courseNumber}</span>
                </div>
                <div class="benefitSettlementResult-hrd-net-id">
                    <span>${dataList[i].hrdNetId}</span>
                </div>
                <div class="benefitSettlementResult-name">
                    <span>${dataList[i].name}</span>
                </div>
                 <div class="benefitSettlementResult-bank">
                    <span>${dataList[i].bank}</span>
                </div>
                <div class="benefitSettlementResult-account">
                    <span>${dataList[i].account}</span>
                </div>
                <div class="benefitSettlementResult-settlement-duration">
                    <span>${dataList[i].settlementDurationStartDate} ~ ${dataList[i].settlementDurationEndDate}</span>
                </div>
                <div class="benefitSettlementResult-training-aid">
                    <span>${dataList[i].trainingAidAmount.toLocaleString('ko-KR')}</span>
                </div>
                <div class="benefitSettlementResult-meal-aid-amount">
                    <span>${dataList[i].mealAidAmount.toLocaleString('ko-KR')}</span>
                </div>
                <div class="benefitSettlementResult-settlement_aid_amount">
                    <span>${dataList[i].settlementAidAmount.toLocaleString('ko-KR')}</span>
                </div>
                <div class="benefitSettlementResult-total-amount">
                    <span>${dataList[i].totalAmount.toLocaleString('ko-KR')}</span>
                </div>
                <div class="benefit-result-settlement-date">
                    <span>${dataList[i].benefitSettlementDate}</span>
                </div>
            </div>
            <div style="display: contents" id="result-contents">
            </div>`;

    }
    $("#result-contents").html("");
    $("#result-contents").append(result);


}

function fetchResultData(page) {

    const requestOptions = {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            "name": getName(),
            "courseNumber": getCourseNumber() == "기수" ? "" : getCourseNumber(),
            "benefitSettlementDate": getBenefitSettlementDate()
        }),
        redirect: "follow"
    };

    fetch("/benefits/result?page=" + page, requestOptions)
        .then((res) => res.json())
        .then(async (data) => {
            const dataList = data.result;

            await getResultData(dataList)

            if (data.result.length === 0) {
                $("#result-contents").html("");
                $("#result-contents").append(`
                        <div style="width: 100%; text-align: center; height: 100px; display: flex; flex-direction: column; justify-content: center">
                        <span style="color: red; font-size: 16px">데이터를 찾을 수 없습니다.</span>
                        </div>`);
            } else {
                $(".benefitResult-cnt-pages").html(`<span>총 ${data.result.length}건</span>`);

                totalPages = Math.ceil(data.result.length / 10);
                updatePagination();
            }


        })
        .catch((error) => console.error(error));
}

$(".filter-search-btn").click(async () => {
    console.log("이거" + getBenefitSettlementDate());
    if (getBenefitSettlementDate() == '' || getBenefitSettlementDate() == null) {
        handleError('정산 기간을 입력하세요');
        return;
    }
    await fetchResultData(1);
})

/*pagenation*/

function updatePagination() {
    $("#page_number").html("");

    let firstPage = (currentBlock * pageSize) - pageSize + 1;
    let lastPage = totalPages <= currentBlock * pageSize ? totalPages : currentBlock * pageSize;

    let result = "";
    for (let i = firstPage; i <= lastPage; i++) {

        let num = i;
        let fontWeight = (num == currentPage) ? 'bold' : 'normal';

        result += `<li>
                    <a class="page-link" style="font-weight: ${fontWeight}"  onclick="fetchResultData(${num})">${num}</a>
                    </li>`;
    }
    $("#page_number").html('');
    $("#page_number").append(result);
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


