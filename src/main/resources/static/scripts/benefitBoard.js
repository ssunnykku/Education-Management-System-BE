function getLectureDays() {
    return $("#lecture-days").val();
}

function getStartDate() {
    return $("#start-date").val();
}

function getEndDate() {
    return $("#end-date").val();
}

function courseNumber() {
    return $(".courseId-filter option:selected").text();
}

function getName() {
    return $(".search-input").val();
}

function getCourseNumber() {
    return $('.benefitSettlement-courseId').val();
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
        $(".courseId-filter").append(result);

    })
    .catch((error) => console.error(error));


async function fetchSettlementTarget() {

    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    const raw = JSON.stringify({
        "settlementDurationStartDate": getStartDate(),
        "settlementDurationEndDate": getEndDate(),
        "courseNumber": courseNumber(),
        "lectureDays": getLectureDays(),
        "name": ""
    });

    const requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
    };

    function getSettlementData(dataList) {
        let result = '';
        for (let i = 0; i < dataList.length; i++) {
            result += `<div class="board-row">
                <div class="benefitSettlement-checkbox">
                    <span>${i + 1}</span>
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

        $(".benefit-cnt-pages").html("");
        $(".benefit-cnt-pages").append(`<span> 총 ${dataList.length}건 </span>`);
    }

    function handleError(message) {
        console.error('Error:', message);
        $("#error").html("");
        $("#error").append(`<span style="color: red">${message}</span>`)
    }

    fetch("/benefits", requestOptions)
        .then((res) => res.json())
        .then(async (data) => {
            if (data.error) {
                handleError(data.message);
            } else {
                const dataList = data.result;
                getSettlementData(dataList)

            }

        })
        .catch((error) => console.error(error));


}

function fetchSettlement(data) {
    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    const raw = JSON.stringify({
        "settlementDurationStartDate": getStartDate(),
        "settlementDurationEndDate": getEndDate(),
        "courseNumber": courseNumber(),
        "lectureDays": getLectureDays(),
        "name": ""
    });

    const requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
        redirect: "follow"
    };

    fetch("/benefits/settlement", requestOptions)
        .then((res) => res.json())
        .then((data) => {
            if (data.error) {
                handleError(data.message);
            }
        })
        .catch((error) => console.error(error));
}

$(".filter-search-btn").click(async () => {
    $("#error").html("");
    await fetchSettlementTarget();

})

$("#settlement-btn").click(async () => {
    await fetchSettlement();
})

