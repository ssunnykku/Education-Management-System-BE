let urlParams = new URL(location.href).searchParams;


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

function searchInput() {
    return $(".search-input").val();
}

function courseNumber() {
    return $(".scholarship-courseId-filter option:selected").text();
}

$(".board-filter-search-btn").click(function () {
    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
// myHeaders.append("Cookie", "JSESSIONID=4CD420A430A1D6B50F9D8EA6691F2420");

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


    let page = urlParams.get('page');

    fetch("/scholarships?page=" + page, requestOptions)
        .then((res) => res.json())
        .then(async (data) => {

            const dataList = data.result;
            const settlementHtml = await getSettlementList(dataList);


        }).catch((error) => console.error(error));

    fetch("http://localhost:8080/scholarships/count", {
        method: "POST",
        headers: myHeaders,
        body: raw,
    })
        .then((res) => res.json())
        .then((data) => {
            $(".scholarship-cnt-pages").html(`<span>총 ${data.result}</span>건`)
        })
        .catch((error) => console.error(error));


})



