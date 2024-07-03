function fetchSettlementTarget() {
    function getLectureDays() {
        return $("#lecture-days").val();
    }

    function getStartDate() {
        return $("#start-date").val();
    }

    function getEndDate() {
        return $("#end-date").val();
    }

    function getCourseNumber() {
        return $(".courseId-filter option:selected").text();
    }

    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    const raw = JSON.stringify({
        "startDate": getStartDate(),
        "endDate": getEndDate(),
        "courseNumber": 277,
        "lectureDays": getLectureDays()
    });

    const requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
    };
    let result = "";
    fetch("/benefits?page=1", requestOptions)
        .then((res) => res.json())
        .then((data) => {
            const dataList = data.result;
            for (let i = 0; i < dataList.length; i++) {
                result += `<div class="board-row">
                <div class="benefitSettlement-checkbox">
                    <input type="checkbox" name=""/>
                </div>
                <div class="benefitSettlement-courseId">
                    <span>기수</span>
                </div>
                <div class="benefitSettlement-hrd-net-id">
                    <span>hrd-net-id</span>
                </div>
                <div class="benefitSettlement-name">
                    <span>이름</span>
                </div>
                <div class="benefitSettlement-bank">
                    <span>은행</span>
                </div>
                <div class="benefitSettlement-account">
                    <span>계좌번호</span>
                </div>
                <div class="benefitSettlement-training-aid">
                    <span>훈련수당(원)</span>
                </div>
                <div class="benefitSettlement-meal-aid-amount">
                    <span>식비(원)</span>
                </div>
                <div class="benefitSettlement-settlement_aid_amount">
                    <span>정착지원금(원)</span>
                </div>
                <div class="benefitSettlement-total-amount">
                    <span>합계(원)</span>
                </div>
            </div>`
            }
        })
        .catch((error) => console.error(error));

    $("#benefit-table-contents").html("");
    $("#benefit-table-contents").append(result);

}


$(".filter-search-btn").click(() => {
    fetchSettlementTarget();
})

