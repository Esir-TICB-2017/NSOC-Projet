angular.module('nsoc')
.controller('loginController', function ($scope, $rootScope, $http, $location, $cookies, websocketService) {
	$scope.options = {
		'onsuccess': function (googleUser) {
			$rootScope.loading = true;
			// Useful data for our client-side scripts:
			var profile = googleUser.getBasicProfile();
			$cookies.put('givenName', profile.ofa);
			$cookies.put('pictureUrl', profile.Paa);
			// The ID token we need to pass to your backend:
			var id_token = googleUser.getAuthResponse().id_token;
			$cookies.put('idtoken', id_token);
			$http({
				method: 'POST',
				url: '/login',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded',
				},
				data: "idtoken=" + id_token,
			}).then(function success(res) {
				parts = res.data.split(".");
				role = atob(parts[1]);
				$cookies.put('role',JSON.parse(role).aud);
				$cookies.put('authenticate', true);
				$location.path('/home');
			}, function error(err) {
				$cookies.put('authenticate', false);
				$rootScope.loading = false;
				console.log(err);
				console.log('Please try to login again');
			});
		},
		'onerror': function (err) {
			console.log("error", err);
		},
	}
})
.directive('googleSignInButton', function () {
	return {
		scope: {
			buttonId: '@',
			options: '&'
		},
		template: '<div></div>',
		link: function (scope, element, attrs) {
			var div = element.find('div')[0];
			div.id = attrs.buttonId;
			gapi.signin2.render(div.id, scope.options());
		}
	};
});
