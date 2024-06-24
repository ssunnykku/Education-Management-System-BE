// 글 등록 버튼
const
    enrollBtn = document.querySelector("#enroll-btn");

enrollBtn.addEventListener("click", function () {
    location.href = "controller?cmd=addCourseUI";
});

// 글 수정 버튼
const
    editBtn = document.querySelectorAll("#edit-btn");

for (var i = 0; i < editBtn.length; i++) {
    editBtn[i].addEventListener("click", function () {
        location.href = "controller?cmd=setCourseUI&courseId="
            + this.dataset["id"];
    });
}

// 체크박스 checked 진짜개극혐이네요 ....
/*
 * const checkbox = document.querySelectorAll(".checkbox[checked]"); checkbox =
 * document.querySelectorAll(".checkbox[checked=true]"); console.log("1 :
 * "+checkbox);
 *
 * checkbox.addEventListener("DOMContentLoaded", function() {
 *
 * for (var int = 0; int < checkbox.length; int++) {
 *
 * console.log("2 : "+checkbox[int].checked);
 *
 * if (checkbox[int].checked == false) { checkbox[int].checked = true; } else {
 * checkbox[int].checked = false; } } });
 */

/*
 * const clickedCheckbox = this; // 클릭된 체크박스 가져오기 console.log(clickedCheckbox);
 *
 * if (clickedCheckbox.checked == false) { clickedCheckbox.checked = true; }
 * else { clickedCheckbox.checked = false; }
 */

/*
 * const checkbox = document.querySelectorAll(".checkbox");
 *
 * checkbox.addEventListener("change", function() { this.checked =
 * !this.checked; });
 */

// 글 삭제 버튼
const
    deleteBtn = document.querySelector("#delete-btn");

deleteBtn.addEventListener("click", function () {

    const
        checkedId = document.querySelector(".checkbox:checked").id;

    const
        result = confirm("해당 글을 삭제하시겠습니까?");

    if (result) {
        location.href = "controller?cmd=removeCourseAction&courseId="
            + checkedId;
    }
});

// 검색 버튼

const
    searchBtn = document.querySelector(".filter-search-btn");

searchBtn.addEventListener("click", function () {

//	const
//	courseId = document.querySelector("select.course-id option:selected");

    const courseId = document.querySelector(".course-id");
    selectedCourseId = courseId.options[courseId.selectedIndex].value;

//	const
//	location = document.querySelector("select.location option:selected");

    const location = document.querySelector(".location");
    selectedLocation = location.options[location.selectedIndex].value;

    if (selectedCourseId != null) {
        location.href = "controller?cmd=getCourseByCourseId&courseId="
            + selectedCourseId;
    }

    if (location != null) {
        location.href = "controller?cmd=getCourseByLocation&location="
            + selectedLocation;
    }

});