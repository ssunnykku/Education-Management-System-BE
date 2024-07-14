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