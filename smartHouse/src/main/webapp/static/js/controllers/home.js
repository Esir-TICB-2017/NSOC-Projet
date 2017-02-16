angular.module('nsoc')
.controller('homeController', ($scope, $rootScope, $http, $cookies, $location, _, websocketService, utils, toastService) => {

	$scope.changeTab = function (newTab) {
		$scope.actualTab = newTab;
		$scope.actualTab.notifications = 0;
	};

	$scope.signOut = function () {
		$scope.GoogleAuth.signOut().then(function () {
			$http({
				method: 'GET',
				url: '/logout'
			}).then(function success(res) {
				$rootScope.loading = false;
				console.log('User signed out.');
				$cookies.put('authenticate', false);
				$location.path('/login');
			}, function error(err) {
				console.log(err);
				console.log('Please try to logout again');
			});
		});
	}

	initSocket = function () {
		if (utils.getBoolean($cookies.get('authenticate'))) {
			websocketService.start('ws://127.0.0.1:8080/?' + $cookies.get('idtoken'),
			function onOpen(websocket) {
			},
			function onClose() {
				$scope.signOut();
			},
			function onMessage(evt) {
				const data = JSON.parse(evt.data);
				let message;
				if (data.name && data.name === 'setting') {
					if ($scope.actualTab.name !== $scope.tabs[1].name){
						$scope.tabs[1].notifications++;
					}
					if (data.settings.status === 'error') {
						message = '<strong>Error : </strong> Can\'t update setting';
						toastService.create('danger', message);
					} else if (data.settings.status === 'success'){
						message = '<strong>Setting </strong> updated';
						toastService.create('success', message);
					}
				} else {
					if ($scope.actualTab.name !== $scope.tabs[0].name){
						$scope.tabs[0].notifications++;
						message = 'New <strong>' + data.type + '</strong> value. <strong>' + data.name + '</strong> : ' + data.data;
					}
					$scope.$broadcast('data', data);
				}
			});
		}
	};


	init = function () {
		$scope.role = $cookies.get('role');
		$scope.tabs = [{name: 'general', notifications: 0}, {name: 'settings', notifications: 0}];
		$scope.actualTab = $scope.tabs[0];
		$scope.userInfo = {
			givenName: $cookies.get('givenName').charAt(0).toUpperCase() + $cookies.get('givenName').slice(1),
			pictureUrl: $cookies.get('pictureUrl'),
		};
		$rootScope.loading = true;
		initSocket();
        toastService.create('success', 'EZ');
	}
	init();
});
