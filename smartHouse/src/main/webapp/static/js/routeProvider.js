angular.module('nsoc', ['ngRoute', 'ngCookies', 'underscore'])
.config(['$routeProvider', function ($routeProvider) {
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
		redirectTo: '/home',
	});
}]);
