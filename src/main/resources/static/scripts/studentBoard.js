// filter
const courseIdSelect = document.querySelector(".courseId-filter");

// button
const settlementListBtn = document.querySelector("#get-settlement-list-btn");
const settlementBtn = document.querySelector("#settlement-btn");
const searchBtn = document.querySelector("#filter-search-btn");
const inputData = document.querySelector(".search-input");

function filterByCourseId() {
  // courseId의 값 가져와서 필터링 해주는 기능...
}

function getSettlementList() {
  // 정산내역 불러오기
}

function settlement() {
  // 정산이 완료되었습니다 안내 모달 보여주기
  console.log(inputData);
}

settlementListBtn.addEventListener("click", getSettlementList);
settlementBtn.addEventListener("click", settlement);
searchBtn.addEventListener("click", filterByCourseId);
