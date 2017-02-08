angular.module('nsoc')
.controller('homeController', ($scope, $rootScope, $http, $cookies, $location, _, websocketService) => {
	$scope.tabs = ['general', 'settings'];
	$scope.actualTab = $scope.tabs[0];
	$rootScope.loading = true;
	$scope.userInfo = {
		givenName: $cookies.get('givenName').charAt(0).toUpperCase() + $cookies.get('givenName').slice(1),
		pictureUrl: $cookies.get('pictureUrl'),
	};
	$scope.data = [];

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
				$scope.$broadcast('data', JSON.parse(evt.data));
			});
		}
		// Rediriger vers login si on reçoit un forbidden (refresh de la page mais plus authentifié, le serveur renvoie 403)
	};

	initSocket();
});
