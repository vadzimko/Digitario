var TIMER = function (timer) {
    if (timer) {
        var deadline = Number(new Date().getTime()) + Number(timer * 1000);
        document.getElementById("timer").innerHTML = getTimeToEnd(deadline);

        var x = setInterval(function () {
            if (new Date().getTime() < deadline)
            document.getElementById("timer").innerHTML = getTimeToEnd(deadline);
            else {
                clearInterval(x);
                window.setTimeout(reload,1000);
            }
        }, 200);
    }
};

function getTimeToEnd(deadline) {
    var t = getTimeLeft(deadline) * 1. / 1000;
    var hours = Math.floor(t / 3600);
    if (hours < 10)
        hours = "0" + hours;
    var minutes = Math.floor((t % 3600) / 60);
    if (minutes < 10)
        minutes = "0" + minutes;
    var seconds = Math.floor(t % 60);
    if (seconds < 10)
        seconds = "0" + seconds;
    return "Timer: " + hours + ":" + minutes + ":" + seconds + "";
}

function getTimeLeft(deadline) {
    var now = new Date().getTime();
    return deadline - now;
}

function reload() {
    window.location.reload(false);
}

if (window.location.hash && window.location.hash == '#_=_') {
    window.location.hash = '';
}