angular.module('nsoc')
.controller('mainController', function($scope, $rootScope, $http, $location, $cookies, utils) {
	gapi.load('auth2', function() {
		gapi.auth2.init(
			{
				client_id: "299325628592-hqru0vumh16bp0hhhvj9qr35lglm8gqu.apps.googleusercontent.com",
			}
		);
		$scope.GoogleAuth  = gapi.auth2.getAuthInstance();
	});
/*
    $httpProvider.interceptors.push(function($q, $cookies) {
        return {
            'request': function(config) {
                config.headers['Authorization'] = 'Bearer ' + $cookies.get('token');
                return config;
            }
        };
    });
    */

	$rootScope.$on('$routeChangeStart', function (e) {
		const authenticate = $cookies.get('token');
		if($location.path() === '/home' && !authenticate){
			e.preventDefault();
			console.log('Please login first');
			$location.path('/login');
		} else if ($location.path() === '/login' && authenticate) {
			e.preventDefault();
			console.log('Already logged in');
			$location.path('/home');
		}
	});

});
