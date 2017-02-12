angular.module('nsoc')
.controller('homeController', ($scope, $rootScope, $http, $cookies, $location, _, websocketService) => {

	$scope.changeTab = (newTab) => {
		$scope.actualTab = newTab;
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
				$scope.$broadcast('data', JSON.parse(evt.data));
			});
		}
	};

	initialize = function () {
		$scope.tabs = ['general', 'settings'];
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
	}

	initialize();

});
