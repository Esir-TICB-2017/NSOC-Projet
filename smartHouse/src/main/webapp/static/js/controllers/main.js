angular.module('nsoc')
.controller('mainController', function($scope, $http, $location) {
	gapi.load('auth2', function() {
		gapi.auth2.init(
			{
				client_id: "299325628592-hqru0vumh16bp0hhhvj9qr35lglm8gqu.apps.googleusercontent.com",
			}
		);
		$scope.GoogleAuth  = gapi.auth2.getAuthInstance();
	});
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
