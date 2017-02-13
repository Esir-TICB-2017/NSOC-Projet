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
				console.log("test");
				this.websocket.send(JSON.stringify({key: "settings", setting_id: 14, value: "it works again"}));
				this.websocket.send(JSON.stringify({key: "userRole", email: "clement.cheron@gmail.com", role: "guest"}));
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
		}
	}
});
