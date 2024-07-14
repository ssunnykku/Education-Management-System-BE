/* 현재 날짜*/
let today = new Date();

let year = today.getFullYear();
let month = today.getMonth() + 1;
let date = today.getDate();
let day;

const currentDate = year + '-' + (month = month >= 10 ? month : '0' + month) + '-' + (date = date >= 10 ? date : '0' + date);

switch (today.getDay()) {
    case 0:
        day = '일요일';
        break;
    case 1:
        day = '월요일';
        break;
    case 2:
        day = '화요일';
        break;
    case 3:
        day = '수요일';
        break;
    case 4:
        day = '목요일';
        break;
    case 5:
        day = '금요일';
        break;
    case 6:
        day = '토요일';
        break;
}

$("#today").append(`<p>${year}. ${month}. ${date} ${day}</p>`);

/* 관리자 정보*/

const myHeaders = new Headers();
myHeaders.append("Content-Type", "application/json");

const requestOptions = {
    method: "GET",
    headers: myHeaders,
};

fetch("/managers", requestOptions)
    .then((res) => res.json())
    .then((data) => {
            const user = data.result;
            let result = `<div id="manager-info-name-wrapper" class="manager-info-item">
                        <span>이름</span>
                        <span>${user.name}</span>
                    </div>
                    <div id="manager-info-job-wrapper" class="manager-info-item">
                        <span class="manager-info-item">담당</span>
                        <span id="manager-info-job">${user.position}</span>
                    </div>
                    <div id="manager-info-location-wrapper" class="manager-info-item">
                        <span class="manager-info-item">교육장 </span>
                        <span id="manager-info-location">${user.academyLocation}</span>
                    </div>`
            $("#manager-info-wrapper").append(result);

            $('#course-info-location-wrapper').append(`<span id="course-info-location">${user.academyLocation} 교육장</span>`)
        }
    )
    .catch((error) => console.error(error));


/* 현재 과정 정보  */

fetch("/courses/current-list?currentDate=" + currentDate, {
    method: "GET",

})
    .then((res) => res.json())
    .then((data) => {
        const resultList = data.result;
        let result;
        for (const resultElement of resultList) {
            console.log(resultElement);
            result += `<tr>
                <td>${resultElement.courseNumber}</td>
                <td>${resultElement.courseName}</td>
                <td>${resultElement.professorName}</td>
                <td>${resultElement.courseStartDate}</td>
                <td>${resultElement.courseEndDate}</td>
            </tr>`
        }
        $("#course-info-table").append(result);
    })
    .catch((error) => console.error(error));