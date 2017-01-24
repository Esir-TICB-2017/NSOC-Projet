angular.module('nsoc')
.controller('loginController', function($scope, $http) {
	onSignIn = function(googleUser) {
		// Useful data for your client-side scripts:
		var profile = googleUser.getBasicProfile();
		// The ID token you need to pass to your backend:
		var id_token = googleUser.getAuthResponse().id_token;
		$http({
			method: 'POST',
			url: '/login',
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded',
			},
			data: {idtoken: id_token},
		}).then(function success(res) {
			console.log(res);
			console.log('User sign in.');
			$window.location.href='#/home';
		}, function error(err) {
			console.log(err);
			console.log('Please try to login again');
		});
	};
});
