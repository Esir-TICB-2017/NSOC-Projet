/**
* Created by loulou on 04/02/2017.
*/

angular.module('nsoc')
.controller('settingsController', ($scope, $http, _) => {
	$scope.settings.forEach((setting) => {
		const index = _.findIndex(setting.allowedValues, (allowedValue) => allowedValue.itemValue === setting.defaultValue);
		if (index !== -1) {
			setting.defaultValue = setting.allowedValues[index];
		}
	});
	console.log($scope.settings);
	
	console.log($scope.userSettings);

	$scope.changeValue = function() {
	}
});
