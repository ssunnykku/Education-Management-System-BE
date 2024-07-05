let currentPage = 1;
const pageSize = 10; // 한 블록 당 페이지 수
let currentBlock = 1;

let totalPages = 0;

function getLectureDays() {
    return $("#lecture-days").val();
}

function getStartDate() {
    return $("#start-date").val();
}

$("#start-date").change(() => {
    const day = new Date($("#start-date").val());
    const endDate = new Date();
    endDate.setFullYear(day.getFullYear())
    endDate.setMonth(day.getMonth() + 1);
    endDate.setDate(day.getDate() - 1);

    const year = endDate.getFullYear();
    const month = (endDate.getMonth() + 1).toString().padStart(2, '0')
    const date = endDate.getDate().toString().padStart(2, '0');

    $("#end-date").val(year + '-' + month + '-' + date);

})

function getEndDate() {
    return $("#end-date").val();
}

function getCourseNumber() {
    return $(".courseId-filter option:selected").text();
}

function getName() {
    return $(".search-input").val();
}


fetchSettlementTarget();

async function fetchSettlementTarget() {

    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    const raw = JSON.stringify({
        "startDate": getStartDate(),
        "endDate": getEndDate(),
        "courseNumber": 277,
        "lectureDays": getLectureDays(),
        "name": getName()
    });

    const requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
    };

    fetch("/benefits?page=1", requestOptions)
        .then((res) => res.json())
        .then((data) => {
            const dataList = data.result;
            let result = '';
            for (let i = 0; i < dataList.length; i++) {
                result += `<div class="board-row">
                <div class="benefitSettlement-checkbox">
                    <input type="checkbox" name=""/>
                </div>
                <div class="benefitSettlement-courseId">
                    <span>${dataList[i].courseNumber}</span>
                </div>
                <div class="benefitSettlement-hrd-net-id">
                    <span>${dataList[i].hrdNetId}</span>
                </div>
                <div class="benefitSettlement-name">
                    <span>${dataList[i].name}</span>
                </div>
                <div class="benefitSettlement-bank">
                    <span>${dataList[i].bank}</span>
                </div>
                <div class="benefitSettlement-account">
                    <span>${dataList[i].account}</span>
                </div>
                <div class="benefitSettlement-training-aid">
                    <span>${dataList[i].trainingAidAmount}</span>
                </div>
                <div class="benefitSettlement-meal-aid-amount">
                    <span>${dataList[i].mealAidAmount}</span>
                </div>
                <div class="benefitSettlement-settlement_aid_amount">
                    <span>${dataList[i].settlementAidAmount}</span>
                </div>
                <div class="benefitSettlement-total-amount">
                    <span>${dataList[i].settlementAidAmount + dataList[i].mealAidAmount + dataList[i].trainingAidAmount}</span>
                </div>
            </div>`;
            }
            $("#benefit-table-contents").html("");
            $("#benefit-table-contents").append(result);
        })
        .catch((error) => console.error(error));

    fetch("/benefits/count", requestOptions)
        .then((res) => res.json())
        .then((data) => {
            $(".benefit-cnt-pages").html(`<span>총 ${data.result}</span>건`);

            totalPages = Math.ceil(data.result / 10);

        })
        .catch((error) => console.error(error));
}


$(".filter-search-btn").click(() => {
    fetchSettlementTarget();
})

