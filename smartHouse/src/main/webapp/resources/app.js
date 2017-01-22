var ws = new WebSocket("ws://127.0.0.1:8080/");

ws.onopen = function() {
    console.log("Opened!");
    ws.send("Hello Server");
};

ws.onmessage = function (evt) {
    console.log("Message: " + evt.data);
};

ws.onclose = function() {
    console.log("Closed!");
};

ws.onerror = function(err) {
    console.log("Error: " + err);
};


// var xmlHttp = new XMLHttpRequest();
//    xmlHttp.onreadystatechange = function() {
//        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
//            callback(xmlHttp.responseText);
//    }
//    xmlHttp.open("GET", "signin", true); // true for asynchronous
//    xmlHttp.send(null);

//var url = "getValuesOnPeriod";
//var params = "startDate=1484757557&endDate=1484786852";
//var xhr = new XMLHttpRequest();
//xhr.open("POST", url, true);
//
////Send the proper header information along with the request
//xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
//
//xhr.send(params);
//
//xhr.onreadystatechange = function()
//        {
//            if (xhr.readyState == 4 && xhr.status == 200)
//            {
//                callback(xhr.responseText); // Another callback here
//            }
//        };
//
function callback(data) {
   console.log(JSON.parse(data));
}