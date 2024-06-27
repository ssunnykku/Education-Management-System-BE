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

$(".board-filter-search-btn").click(async function () {
    $("#page_number").html("");
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

    await fetchScholarshipBoard(1);

    fetch("/scholarships/count", {
        method: "POST",
        headers: myHeaders,
        body: raw,
    })
        .then((res) => res.json())
        .then((data) => {
            $(".scholarship-cnt-pages").html(`<span>총 ${data.result}</span>건`)

            const countPage = Math.ceil(data.result / 10);
            // const countPage = 12;
            if (countPage > 10) {
                for (let i = 1; i < 11; i++) {
                    let num = i;
                    $("#page_number").append(`<a class="page-link" onclick={fetchScholarshipBoard(num)} >${num}</a>`)

                }

            } else {
                for (let i = 1; i < countPage + 1; i++) {
                    let num = i;

                    $("#page_number").append(`<a class="page-link" onclick={fetchScholarshipBoard(num)} >num</a>`)

                }
            }

        })
        .catch((error) => console.error(error));

})


// $("#next").click(() => {
//     const countPage = 12;
//
//     if (countPage > 10) {
//         $("#page_number").html("");
//         for (let i = 11; i < countPage + 1; i++) {
//             let num = i;
//             console.log(num);
//             $("#page_number").append(`<a class="page-link" onclick={fetchScholarshipBoard(num)} >num</a>`)
//         }
//     }
//
// })


async function fetchScholarshipBoard(param) {
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

    await fetch("/scholarships?page=" + param, requestOptions)
        .then((res) => res.json())
        .then(async (data) => {

            const dataList = data.result;
            const settlementHtml = await getSettlementList(dataList);


        }).catch((error) => console.error(error));
}

