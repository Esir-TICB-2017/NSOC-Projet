angular.module('nsoc')
.controller('homeController', function($scope, $http, $window) {
	$scope.signOut = function() {
		var auth2 = gapi.auth2.getAuthInstance();
		auth2.signOut().then(function () {
			$http({
				method: 'GET',
				url: '/logout'
			}).then(function success(res) {
				console.log(res);
				console.log('User signed out.');
				$window.location.href='#/login';
			}, function error(err) {
				console.log(err);
				console.log('Please try to logout again');
			});
		});
	}
});
