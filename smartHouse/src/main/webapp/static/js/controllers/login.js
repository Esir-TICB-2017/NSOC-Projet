angular.module('nsoc')
.controller('loginController', function($scope, $http, $location) {
	gapi.load('auth2', function() {
		gapi.auth2.init(
			{
				client_id: "299325628592-hqru0vumh16bp0hhhvj9qr35lglm8gqu.apps.googleusercontent.com"
			}
		);
		var GoogleAuth  = gapi.auth2.getAuthInstance();

		$scope.signIn=function(){
			GoogleAuth.signIn().then(function(googleUser){
				// Useful data for our client-side scripts:
				var profile = googleUser.getBasicProfile();
				// The ID token we need to pass to your backend:
				var id_token = googleUser.getAuthResponse().id_token;
				/*
                var xhr = new XMLHttpRequest();
                xhr.open('POST', '/login');
                xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                xhr.onload = function() {
                    console.log('Signed in as: ' + xhr.responseText);
                };
                xhr.send('idtoken=' + id_token);
                */

				$http({
					method: 'POST',
					url: '/login',
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded',
					},
					data: "idtoken=" + id_token,
				}).then(function success(res) {
					console.log('User signed in');
					$location.path('/home');
				}, function error(err) {
					console.log(err);
					console.log('Please try to login again');
				});

			});
		};
	});
});
