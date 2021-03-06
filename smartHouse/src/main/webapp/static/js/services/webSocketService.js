/**
* Created by loulou on 30/01/2017.
*/
angular.module('nsoc')
.factory('websocketService', () => {
	return {
		start: function (url, onOpen, onClose, onMessage) {
			this.websocket = new WebSocket(url);
			this.websocket.onopen = () => {
				console.log("Opened!");
				onOpen(this.websocket);
            };
			this.websocket.onclose = () => {
				console.log("Closed!");
				onClose();
			};
			this.websocket.onmessage = (evt) => {
				onMessage(evt);
			};
			this.websocket.onerror = (err) => {
				console.log("Error: " + err);
			};

		},
		send: function(name, message) {
			this.websocket.send(name, message);
		},
	}
});
