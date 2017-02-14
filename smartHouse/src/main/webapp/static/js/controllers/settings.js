/**
* Created by loulou on 04/02/2017.
*/

angular.module('nsoc')
.controller('settingsController', ($scope, $http, _) => {
	console.log($scope.settings);

	console.log($scope.userSettings);

	$scope.changeValue = function() {
		console.log('here');
	}
});
