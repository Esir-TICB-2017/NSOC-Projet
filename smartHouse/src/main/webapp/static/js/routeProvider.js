angular.module('nsoc', ['ngRoute'])
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
