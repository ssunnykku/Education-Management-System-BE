$(document).ready(() => {
    $("#nav-ul").mouseenter(() => {
        $("#nav-wrapper-dropdown").removeAttr("style");
    })

    $("#nav-wrapper-dropdown").mouseleave(() => {
        $("#nav-wrapper-dropdown").css("display", "none");
    })


})





//let urlParams = new URL(location.href).searchParams;
//let cmd = urlParams.get('cmd');
