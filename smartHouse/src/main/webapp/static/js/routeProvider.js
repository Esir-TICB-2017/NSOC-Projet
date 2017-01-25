angular.module('nsoc', ['ngRoute'])
.controller('routeProviderController', function($scope, $http, $location) {
	$scope.obj = "Ce que j'écris la est sensé tout le temps rester ici";
	$scope.$on('$routeChangeStart', function (e) {
		$http({
			method: 'GET',
			url: '/isAuthenticated'
		}).then(function success(res) {
			console.log('Authorized acces');
		}, function error(err) {
			e.preventDefault();
			$location.path('/login');
			console.log(err.status + ' : ' + err.statusText);
			console.log('Please login first');
		});
	});
})
.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	$routeProvider
	.when('/login', {
			templateUrl: 'static/templates/login.html',
			controller: 'loginController',
		})
	.when('/home', {
		templateUrl: 'static/templates/home.html',
		controller: 'homeController',
	})
	.otherwise({
		redirectTo: '/login'
	});
}]);
