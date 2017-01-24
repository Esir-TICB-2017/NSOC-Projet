angular.module('nsoc', ['ngRoute'])
.controller('routeProviderController', function($scope) {
	$scope.obj = "Ce que j'écris la est sensé tout le temps rester ici";
})
.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	$routeProvider
	.when('/login', {
			templateUrl: 'templates/login.html',
			controller: 'loginController',
		})
	.when('/home', {
		templateUrl: 'templates/home.html',
		controller: 'homeController',
	})
	.otherwise({
		redirectTo: '/login'
	});
}]);
