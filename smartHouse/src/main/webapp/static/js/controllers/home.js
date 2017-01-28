angular.module('nsoc')
.controller('homeController', function($scope, $http, $location, websocketService) {
	$scope.signOut = function() {
		$scope.GoogleAuth.signOut().then(function () {
			$http({
				method: 'GET',
				url: '/logout'
			}).then(function success(res) {
				console.log(res);
				console.log('User signed out.');
				$location.path('/login');
			}, function error(err) {
				// TODO Force logout on server
				console.log(err);
				console.log('Please try to logout again');
			});
		});
	}

	websocketService.start("ws://127.0.0.1:8080/", function (evt) {
		var obj = JSON.parse(evt.data);
		$scope.$apply(function () {
			$scope[obj.key] = parseInt(obj.value);
		});
	});
});

angular.module('nsoc').factory('websocketService', function () {
	return {
		start: function (url, callback) {
			var websocket = new WebSocket(url);
			websocket.onopen = function () {
				console.log("Opened!");
				websocket.send("Hello Server");
			};
			websocket.onclose = function () {
				console.log("Closed!");
			};
			websocket.onmessage = function (evt) {
				callback(evt);
			};
			websocket.onerror = function (err) {
				console.log("Error: " + err);
			};
		}
	}
}
);
