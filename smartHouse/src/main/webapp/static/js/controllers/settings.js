/**
* Created by loulou on 04/02/2017.
*/

angular.module('nsoc')
.controller('settingsController', ($scope, $http, _, websocketService) => {

	$scope.changeValue = function(setting) {
		websocketService.send('settings', JSON.stringify({key: 'settings', setting_id: setting.id, value: setting.defaultValue.itemValue}));
	}

	getSettings = function () {
			$http({
					method: 'GET',
					url: '/getSettings'
			}).then(function success(res) {
					const data = _.sortBy(res.data, setting => setting.order);
					$scope.settings = _.groupBy(data, setting => setting.type);
					const keys = _.keys($scope.settings);
					keys.forEach((key) => {
							$scope.settings[key].forEach((setting) => {
									const index = _.findIndex(setting.allowedValues, (allowedValue) => allowedValue.itemValue === setting.defaultValue);
									if (index !== -1) {
											setting.defaultValue = setting.allowedValues[index];
									}
							});
					});
					$scope.actualSettingView = $scope.settings[keys[0]];
			}, function error(err) {
					console.log(err);
			});
	};

	$scope.changeSettingView = function(view) {
		$scope.actualSettingView = view;
	};

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
