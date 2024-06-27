const myHeaders = new Headers();
myHeaders.append("Content-Type", "application/json");
// myHeaders.append("Cookie", "JSESSIONID=4CD420A430A1D6B50F9D8EA6691F2420");

const raw = JSON.stringify({
    "name": "최",
    "courseNumber": "277"
});

const requestOptions = {
    method: "POST",
    headers: myHeaders,
    body: raw,
    redirect: "follow"
};

async function getSettlementList(data) {
    let result = '';
    for (let i = 0; i < data.length; i++) {
        result += `<div class="scholarshipBoard-row">
                        <div class="scholarshipBoard-checkbox">
                            <input type="checkbox" name="" id="checkbox"/>
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
                    </div>`
    }
    $("#scholarship-table-contents").html("");
    $("#scholarship-table-contents").append(result);
}


$(".board-filter-search-btn").click(function () {
    fetch("/scholarships?page=1", requestOptions)
        .then((res) => res.json())
        .then(async (data) => {
            // console.log(data.result);
            const dataList = data.result;
            const settlementHtml = await getSettlementList(dataList);
            // $("#scholarshipBoard-table").append(settlementHtml);

        }).catch((error) => console.error(error));

})