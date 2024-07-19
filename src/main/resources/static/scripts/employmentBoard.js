const params = new URL(location.href).search.split("=")[1];

let currentPage = 1;
const pageSize = 10; // 한 블록 당 페이지 수
let currentBlock = 1;

let totalPages = 0;


function searchInput() {
    return $(".search-input").val();
}

/*function courseNumber() {
    // console.log($(".employment-courseId-filter option:selected").text());
    return $(".employment-courseId-filter option:selected").text();
}
*/
/*$(".board-filter-search-btn").click(async function () {

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

    await fetchemploymentBoard(1);

    fetch("/employments/count", {
        method: "POST",
        headers: myHeaders,
        body: raw,
    })
        .then((res) => res.json())
        .then(async (data) => {
            $(".employment-cnt-pages").html(`<span>총 ${data.result}</span>건`);

            // const countPage = Math.ceil(data.result / 10);
            // const countPage = 12;  // Example total pages
            totalPages = Math.ceil(data.result / 10);
            console.log(totalPages);

            updatePagination();
        })
        .catch((error) => console.error(error));

});*/


function updatePagination() {
    $("#page_number").html("");

    let firstPage = (currentBlock * pageSize) - pageSize + 1;
    let lastPage = totalPages <= currentBlock * pageSize ? totalPages : currentBlock * pageSize;

    let result = "";
    for (let i = firstPage; i <= lastPage; i++) {
        let num = i;
        result += `<li>
                    <a class="page-link" onclick="fetchemploymentBoard(${num})">${num}</a>
                    </li>`;
    }
    $("#page_number").append(result);
}


async function fetchemploymentBoard(param) {
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

}


let selected = [];
$("#settlement-btn").click(function () {

    // $("#settlementListModal").removeClass('check-settlement-modal');

    // $(".checkbox").each(function () {
    //     if ($(this).prop("checked")) {
    //         fetch("http://localhost:8080/employments/settlement/" + this.value, {
    //             method: "POST",
    //         })
    //             .then((res) => res.json())
    //             .catch((error) => console.error(error));
    //     }
    // 모달창
    // 진행하시겠습니까? + 세부 내역 보여주기
    // 정산이 완료되었습니다.
    // });
});


$("#title-checkbox").on("change", toggleAll);

function toggleAll() {
    const isChecked = $("#title-checkbox").prop('checked');
    $("#employmentBoard-table input[type=checkbox]").prop('checked', isChecked);
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
        $(".employment-courseId-filter").append(result);

    })
    .catch((error) => console.error(error));

/*courseData filter 목록*/


/*fetch("/courses/course-year-list", {

    method: "GET",
})
    .then((res) => res.json())
    .then((data) => {
        const courseList = data.result;
        let result = '';
        for (const resultElement of courseList) {
            result += `<option value=${resultElement}>${resultElement}</option>`;
        }
        $(".employment-year-filter").append(result);

    })
    .catch((error) => console.error(error));



*/
 fetch("/courses/course-year-list", {
            method: "GET",
        })
        .then((res) => res.json())
        .then((data) => {
            const courseList = data.result;
            let result = '';
            for (const resultElement of courseList) {
                result += `<option value=${resultElement}>${resultElement}</option>`;
            }
            $(".employment-year-filter").append(result);

            // Set the default value to the most recent year
            if (courseList.length > 0) {
                const mostRecentYear = Math.max(...courseList);
                $(".employment-year-filter").val(mostRecentYear);
                $(".employment-year-filter").trigger("change"); // Trigger change event
            }
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