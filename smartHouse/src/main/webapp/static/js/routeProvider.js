angular.module('nsoc', ['ngRoute'])
.controller('routeProviderController', function($scope) {
	$scope.obj = "Ce que j'écris la est sensé tout le temps rester ici";
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
