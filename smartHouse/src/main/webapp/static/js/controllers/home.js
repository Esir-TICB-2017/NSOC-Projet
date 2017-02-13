angular.module('nsoc')
.controller('homeController', ($scope, $rootScope, $http, $cookies, $location, _, websocketService, utils, Flash) => {

	$scope.changeTab = function(newTab) {
		$scope.actualTab = newTab;
		$scope.actualTab.notifications = 0;
	};

	$scope.signOut = function () {
		$scope.GoogleAuth.signOut().then(function() {
			$http({
				method: 'GET',
				url: '/logout'
			}).then(function success(res) {
				$rootScope.loading = false;
				console.log('User signed out.');
				$cookies.remove('token');
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
			Flash.create('success', 'Received new data!');
			$scope.$broadcast('firstData', res.data);
		}, function error(err) {
			Flash.create('danger', 'Can\'t receive any data!');
			console.log(err);
		});
	}

	initSocket = function () {
		if ($cookies.get('token')) {
			websocketService.start('ws://127.0.0.1:8080/?'+$cookies.get('token'),
			function onOpen(websocket) {
			},
			function onClose() {
				$scope.signOut();
			},
			function onMessage(evt) {
				const data = JSON.parse(evt.data);
				console.log(evt);
				if ($scope.actualTab.name !== $scope.tabs[0].name) {
					$scope.tabs[0].notifications++;
				}
				const message = 'New <strong>' + data.type + '</strong> value. <strong>' + data.name + '</strong> : ' + data.data + data.unit;
				Flash.create('success', message);
				$scope.$broadcast('data', data);
			});
		}
	};

	getSettings = function() {
		$http({
			method: 'GET',
			url: '/getSettings'
		}).then(function success(res) {
			$scope.settings = res.data;
		}, function error(err) {
			console.log(err);
		});
	}

	getUserSettings = function() {
		$http({
			method: 'GET',
			url: '/getUserSettings'
		}).then(function success(res) {
			$scope.userSettings = res.data;
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
