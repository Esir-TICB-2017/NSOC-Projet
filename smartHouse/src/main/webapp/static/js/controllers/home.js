angular.module('nsoc')
.controller('homeController', ($scope, $rootScope, $cookies, $http, $location, _, websocketService) => {
	$scope.tabs = ['general', 'data', 'settings'];
	$scope.actualTab = $scope.tabs[0];
	$rootScope.loading = true;
	$scope.sensors = [];

	$scope.changeTab = (newTab) => {
		$scope.actualTab = newTab;
	};

	$scope.signOut = function () {
		$scope.GoogleAuth.signOut().then(() => {
			$http({
				method: 'GET',
				url: '/logout'
			}).then(function success(res) {
				console.log('User signed out.');
				$cookies.put('authenticated', false);
				$location.path('/login');
			}, function error(err) {
				console.log(err);
				console.log('Please try to logout again');
			});
		});
	}

	initSocket = function () {
		if ($cookies.get('authenticated')) {
			websocketService.start('ws://127.0.0.1:8080/token?tokenid='+$cookies.get('idtoken'),
			function onOpen(websocket) {
			},
			function onClose() {
				// reconnect 1 fois au moins avant de rediriger vers login
				$scope.signOut();
			},
			function onMessage(evt) {
				var obj = JSON.parse(evt.data);
				if (obj.globalIndicator && obj.lastValues) {
					$scope.$broadcast('firstData', obj);
				} else {
					$scope.$broadcast('newValue', obj);
				}
			});
		}
		// Rediriger vers login si on reçoit un forbidden (refresh de la page mais plus authentifié, le serveur renvoie 403)
	};

	initSocket();
});
