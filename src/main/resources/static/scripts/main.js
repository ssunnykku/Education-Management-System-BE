/* 현재 날짜*/
let today = new Date();

let year = today.getFullYear();
let month = today.getMonth() + 1;
let date = today.getDate();
let day;

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
            console.log(user);
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
                        <span id="manager-info-location">${user.academyLocation} 교육장</span>
                    </div>`
            $("#manager-info-wrapper").append(result);
        }
    )
    .catch((error) => console.error(error));
