angular.module('nsoc')
.controller('mainController', function($scope, $rootScope, $http, $location, $cookies) {
	gapi.load('auth2', function() {
		gapi.auth2.init(
			{
				client_id: "299325628592-hqru0vumh16bp0hhhvj9qr35lglm8gqu.apps.googleusercontent.com",
			}
		);
		$scope.GoogleAuth  = gapi.auth2.getAuthInstance();
	});
	$scope.$on('$routeChangeStart', function (e) {
		if($location.path() == '/home' && !$cookies.get('authenticated')){
				e.preventDefault();
				console.log('Please login first');
				$location.path('/login');
		} else if ($location.path() == '/login' && $cookies.get('authenticated')) {
			e.preventDefault();
			console.log('Already logged in');
		}
	});
});
