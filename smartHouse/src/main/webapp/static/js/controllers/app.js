angular.module('nsoc')
.controller('homeController', function($scope, $http, $location) {
	$scope.signOut = function() {
		var auth2 = gapi.auth2.getAuthInstance();
		auth2.signOut().then(function () {
			$http({
				method: 'GET',
				url: '/logout'
			}).then(function success(res) {
				console.log(res);
				console.log('User signed out.');
				$location.path('/login');
			}, function error(err) {
				console.log(err);
				console.log('Please try to logout again');
			});
		});
	}
});
