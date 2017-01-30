/**
 * Created by loulou on 30/01/2017.
 */
angular.module('nsoc')
    .factory('websocketService', () => {
    return {
        start: function (url, callback) {
            var websocket = new WebSocket(url);
            websocket.onopen = () => {
                console.log("Opened!");
                websocket.send("Hello Server");
            };
            websocket.onclose = () => {
                console.log("Closed!");
            };
            websocket.onmessage = (evt) => {
                callback(evt);
            };
            websocket.onerror = (err) => {
                console.log("Error: " + err);
            };
        }
    }
});
