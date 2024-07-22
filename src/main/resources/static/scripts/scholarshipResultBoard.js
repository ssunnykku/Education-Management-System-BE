let param = new URL(location.href).search.split("=")[1];

const myHeaders = new Headers();
myHeaders.append("Content-Type", "application/json");

let currentPage = 1;
const pageSize = 10; // 한 블록 당 페이지 수
let currentBlock = 1;

let totalPages = 0;

fetchScholarshipResultBoard(1);

function getScholarshipResultData(data) {
    const dataList = data.result;
    let result = "";
    for (let i = 0; i < dataList.length; i++) {
        result += ` <div class="scholarshipResultBoard-row">
                            <div class="scholarshipResultBoard-courseId">
                                <span>${dataList[i].courseNumber}</span>
                            </div>
                            <div class="scholarshipResultBoard-hrd-net-id">
                                <span>${dataList[i].hrdNetId}</span>
                            </div>
                            <div class="scholarshipResultBoard-name">
                                <span>${dataList[i].name}</span>
                            </div>
                            <div class="scholarshipResultBoard-bank"><span>${dataList[i].bank}</span></div>
                            <div class="scholarshipResultBoard-account">
                                <span>${dataList[i].account}</span>
                            </div>
                            <div class="scholarshipResultBoard-total-amount">
                                <span>${dataList[i].scholarshipAmount.toLocaleString('ko-KR')}</span>
                            </div>
                            <div class="scholarshipResultBoard-settlement-date">
                                <span>${dataList[i].settlementDate}</span>
                            </div>
                        </div>`

        $("#scholarship-result-table-contents").html("");
        $("#scholarship-result-table-contents").append(result);
    }
}

function searchInput() {
    if ($(".search-input").val() === null) {
        return '';
    }
    return $(".search-input").val();
}

function courseNumber() {
    console.log($(".scholarshipResult-courseId-filter option:selected").text());
    return $(".scholarshipResult-courseId-filter option:selected").text();
}

function settlementDate() {
    return $("#settlement-date").val();
}

async function fetchScholarshipResultBoard(param) {

    let urlParam = param == undefined ? 1 : param;

    $("#scholarship-result-table-contents").html("");
    const raw = JSON.stringify({
        "courseNumber": courseNumber() == "기수" ? "" : courseNumber(),
        "name": searchInput(),
        "settlementDate": settlementDate()
    });

    const requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
    };

    $("#scholarship-result-table-contents").html("");
    await fetch("/scholarships/result?page=" + urlParam, requestOptions)
        .then((res) => res.json())
        .then((data) => {
            getScholarshipResultData(data);

        })
        .catch((error) => console.error(error));

    await fetch("/scholarships/result/count", requestOptions)
        .then((res) => res.json())
        .then(async (data) => {
            $(".scholarship-cnt-pages").html(`<span>총 ${data.result}</span>건`);

            totalPages = Math.ceil(data.result / 10);

            updatePagination();
        })
        .catch((error) => console.error(error));
}

function updatePagination() {
    $("#page_number").html("");

    let firstPage = (currentBlock * pageSize) - pageSize + 1;
    let lastPage = totalPages <= currentBlock * pageSize ? totalPages : currentBlock * pageSize;
    let result = "";
    for (let i = firstPage; i <= lastPage; i++) {
        let num = i;
        let fontWeight = (num === currentPage) ? 'bold' : 'normal';

        result += `<li>
                <a class="page-link" style="font-weight: ${fontWeight}" onclick="fetchScholarshipResultBoard(${num})">${num}</a>
                </li>`;
    }
    $("#page-number").html('');
    $("#page-number").append(result);
}


$(".filter-search-btn").click(async () => {

        await fetchScholarshipResultBoard(param)
    }
);

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
        $(".scholarshipResult-courseId-filter").append(result);

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