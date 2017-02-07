angular.module('nsoc')
.controller('loginController', function($scope, $rootScope, $http, $location, $cookies, websocketService) {
	$scope.options = {
		'onsuccess': function(googleUser) {
			$rootScope.loading = true;
			// Useful data for our client-side scripts:
			var profile = googleUser.getBasicProfile();
			const userId = profile.Eea;
			$cookies.put('givenName', profile.ofa);
			$cookies.put('pictureUrl', profile.Paa);
			// The ID token we need to pass to your backend:
			var id_token = googleUser.getAuthResponse().id_token;
			console.log(id_token)
			$http({
				method: 'POST',
				url: '/login',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded',
				},
				data: "idtoken=" + id_token,
			}).then(function success(res) {
				console.log('User signed in');
				// user is authenticated
				$rootScope.authenticated = true;
				// redirect on home
				$location.path('/home');
				// start web socket service
				websocketService.start('ws://127.0.0.1:8080/',
				function onOpen(websocket) {
					websocket.send(JSON.stringify({userId}));
				},
				function onClose() {
					$rootScope.$broadcast('signOut');
				},
				function onMessage(evt) {
					var obj = JSON.parse(evt.data);
                    if (obj.globalIndicator && obj.lastValues) {
						$rootScope.$broadcast('firstData', obj);
					}
				});
			}, function error(err) {
				$rootScope.loading = false;
				console.log(err);
				console.log('Please try to login again');
			});
		},
		'onerror': function(err) {
			console.log("error", err);
		},
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
			var div = element.find('div')[0];
			div.id = attrs.buttonId;
			gapi.signin2.render(div.id, scope.options());
		}
	};
});
