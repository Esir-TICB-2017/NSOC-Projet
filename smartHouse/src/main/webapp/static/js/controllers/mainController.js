angular.module('nsoc')
.controller('mainController', function($scope, $http, $location) {
	$scope.$on('$routeChangeStart', function (e) {
		if($location.path() == '/home'){
			$http({
				method: 'GET',
				url: '/isAuthenticated'
			}).then(function success(res) {
				console.log('Authorized acces');
			}, function error(err) {
				e.preventDefault();
				$location.path('/login');
				console.log('Please login first');
			});
		}
	});
})
