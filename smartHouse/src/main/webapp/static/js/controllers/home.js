angular.module('nsoc')
.controller('homeController', ($scope, $rootScope, $http, $location, _, websocketService) => {
	$scope.tabs = ['general', 'data', 'settings'];
	$scope.actualTab = $scope.tabs[0];
	$scope.sensors = [];

	$scope.changeTab = (newTab) => {
		$scope.actualTab = newTab;
	};
	// Rediriger vers login si on reçoit un forbidden (refresh de la page mais plus authentifié, le serveur renvoie 403)

	$scope.signOut = function () {
		$scope.GoogleAuth.signOut().then(() => {
			$http({
				method: 'GET',
				url: '/logout'
			}).then(function success(res) {
				console.log('User signed out.');
				$rootScope.authenticated = false;
				$location.path('/login');
			}, function error(err) {
				console.log(err);
				console.log('Please try to logout again');
			});
		});
	}

	$scope.$on('signOut', function() {
		$scope.signOut();
	});
});
