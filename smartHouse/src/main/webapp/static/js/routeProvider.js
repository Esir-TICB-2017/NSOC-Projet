angular.module('nsoc', ['ngRoute'])
.controller('routeProviderController', function($scope, $http, $location) {
	$scope.obj = "Ce que j'écris la est sensé tout le temps rester ici";
	$scope.$on('$routeChangeStart', function (e) {
		$http({
			method: 'GET',
			url: '/isAuthenticated'
		}).then(function success(res) {
			console.log('Authorized acces');
		}, function error(err) {
			e.preventDefault();
			$location.path('/login');
			console.log(err.status + ' : ' + err.statusText);
			console.log('Please login first');
		});
	});
})
.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	$routeProvider
	.when('/login', {
			templateUrl: 'static/templates/login.html',
			controller: 'loginController',
		})
	.when('/home', {
		templateUrl: 'static/templates/home.html',
		controller: 'homeController',
        resolve: {
            factory: checkRouting,
        }
	})
	.otherwise({
		redirectTo: '/login'
	});
}]);

var checkRouting= function ($q, $location) {
    if ($rootScope.userProfile) {
        return true;
    } else {
        var deferred = $q.defer();
        $http.post("/loadUserProfile", { userToken: "blah" })
            .success(function (response) {
                $rootScope.userProfile = response.userProfile;
                deferred.resolve(true);
            })
            .error(function () {
                deferred.reject();
                $location.path("/");
            });
        return deferred.promise;
    }
};
