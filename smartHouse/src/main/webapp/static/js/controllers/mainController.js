angular.module('nsoc')
.controller('mainController', function($scope, $http, $location) {
	// $scope.$on('$routeChangeStart', function (e) {
	// 	if($location.path() !== '/login'){
	// 		$http({
	// 			method: 'GET',
	// 			url: '/isAuthenticated'
	// 		}).then(function success(res) {
	// 			console.log('Authorized acces');
	// 		}, function error(err) {
	// 			e.preventDefault();
	// 			$location.path('/login');
	// 			console.log(err.status + ' : ' + err.statusText);
	// 			console.log('Please login first');
	// 		});
	// 	}
	// });
})
