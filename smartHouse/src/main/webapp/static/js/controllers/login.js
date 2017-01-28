angular.module('nsoc')
.controller('loginController', function($scope, $http, $location) {
	$scope.options = {
		'onsuccess': function(googleUser) {
			// Useful data for our client-side scripts:
			var profile = googleUser.getBasicProfile();
			// The ID token we need to pass to your backend:
			var id_token = googleUser.getAuthResponse().id_token;
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

		}
	}
})
.directive('googleSignInButton', function() {
	return {
		scope: {
			buttonId: '@',
			options: '&'
		},
		template: '<div></div>',
		link: function(scope, element, attrs) {
			gapi.load('auth2', function() {
				gapi.auth2.init(
					{
						client_id: "299325628592-hqru0vumh16bp0hhhvj9qr35lglm8gqu.apps.googleusercontent.com",
					}
				);
			});
			var div = element.find('div')[0];
			div.id = attrs.buttonId;
			gapi.signin2.render(div.id, scope.options());
		}
	};
});
