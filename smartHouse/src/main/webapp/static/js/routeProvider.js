angular.module('nsoc')
.config(['$routeProvider', function ($routeProvider) {
	$routeProvider
	.when('/', {
		redirectTo: '/login',
	})
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
