angular.module('nsoc')
.controller('homeController', ($scope, $rootScope, $http, $cookies, $location, _, websocketService) => {

	$scope.changeTab = (newTab) => {
		$scope.actualTab = newTab;
		$scope.actualTab.notifications = 0;
	};

	$scope.signOut = function () {
		$scope.GoogleAuth.signOut().then(() => {
			$http({
				method: 'GET',
				url: '/logout'
			}).then(function success(res) {
				$rootScope.loading = false;
				console.log('User signed out.');
				$cookies.put('authenticated', false);
				$location.path('/login');
			}, function error(err) {
				console.log(err);
				console.log('Please try to logout again');
			});
		});
	}

	getFirstData = function () {
		$http({
			method: 'GET',
			url: '/getFirstData'
		}).then(function success(res) {
			if ($scope.actualTab.name !== $scope.tabs[0].name) {
				$scope.tabs[0].notifications++;
			}
			$scope.$broadcast('firstData', res.data);
		}, function error(err) {
			console.log(err);
		});
	}

	initSocket = function () {
		const authenticate = $cookies.get('authenticated') === 'true'? true : false;
		if (authenticate) {
			websocketService.start('ws://127.0.0.1:8080/?'+$cookies.get('idtoken'),
			function onOpen(websocket) {
			},
			function onClose() {
				$scope.signOut();
			},
			function onMessage(evt) {
				if ($scope.actualTab.name !== $scope.tabs[0].name) {
					$scope.tabs[0].notifications++;
				}
				$scope.$broadcast('data', JSON.parse(evt.data));
			});
		}
	};

	getSettings = function() {
		$http({
			method: 'GET',
			url: '/getSettings'
		}).then(function success(res) {
			console.log('settings : ', res);
		}, function error(err) {
			console.log(err);
		});
	}

	getUserSettings = function() {
		$http({
			method: 'GET',
			url: '/getUserSettings'
		}).then(function success(res) {
			console.log('user settings : ', res);
		}, function error(err) {
			console.log(err);
		});
	}

	initialize = function () {
		$scope.tabs = [{name: 'general', notifications: 0}, {name: 'settings', notifications: 0}];
		$scope.actualTab = $scope.tabs[0];
		$scope.userInfo = {
			givenName: $cookies.get('givenName').charAt(0).toUpperCase() + $cookies.get('givenName').slice(1),
			pictureUrl: $cookies.get('pictureUrl'),
		};
		$scope.data = [];
		$rootScope.loading = true;
		$rootScope.indicatorMode = false;
		initSocket();
		getFirstData();
		getSettings();
		getUserSettings();
	}

	initialize();

});
