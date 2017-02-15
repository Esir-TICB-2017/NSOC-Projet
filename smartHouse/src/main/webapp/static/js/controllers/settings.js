/**
* Created by loulou on 04/02/2017.
*/

angular.module('nsoc')
.controller('settingsController', ($scope, $http, _, websocketService) => {

	$scope.changeValue = function(setting) {
		// websocketService.send('settings', {key: 'settings', name: setting.name, value: setting.defaultValue.itemValue});
	}

	getSettings = function () {
			$http({
					method: 'GET',
					url: '/getSettings'
			}).then(function success(res) {
					const data = _.sortBy(res.data, setting => setting.order);
					$scope.settings = _.groupBy(data, setting => setting.type);
					for (key in $scope.settings) {
							$scope.settings[key].forEach((setting) => {
									const index = _.findIndex(setting.allowedValues, (allowedValue) => allowedValue.itemValue === setting.defaultValue);
									if (index !== -1) {
											setting.defaultValue = setting.allowedValues[index];
									}
							});
					}
					console.log($scope.settings);
			}, function error(err) {
					console.log(err);
			});
	}

	getUserSettings = function () {
			$http({
					method: 'GET',
					url: '/getUserSettings'
			}).then(function success(res) {
					$scope.userSettings = res.data;
					console.log(res.data);
			}, function error(err) {
					console.log(err);
			});
	}

	function init() {
		getSettings();
		getUserSettings();
	}

	init();
});
