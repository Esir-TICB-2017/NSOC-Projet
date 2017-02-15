/**
* Created by loulou on 04/02/2017.
*/

angular.module('nsoc')
.controller('settingsController', ($scope, $rootScope, $http, _, websocketService) => {

	$scope.changeValue = function(setting) {
		console.log(setting);
		// websocketService.send(JSON.stringify({key: 'settings', setting_id: setting.id, value: setting.current.itemValue}));
	}

	getSettings = function () {
			$http({
					method: 'GET',
					url: '/getSettings'
			}).then(function success(res) {
					$rootScope.settings = _.groupBy(res.data.settings, setting => setting.type);
					$scope.actualSettingView = 'general';
					if ($scope.role === 'admin') {
						$rootScope.settings.users = res.data.users;
					}
					getUserSettings();
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
					displayUserDefaultSettings();
			}, function error(err) {
					console.log(err);
			});
	}

	displayUserDefaultSettings = function () {
		const keys = _.keys($rootScope.settings);
		$scope.userSettings.forEach((userSetting) => {
			keys.forEach((key) => {
				const index = _.findIndex($rootScope.settings[key], (setting) => setting.id === userSetting.setting_id);
				if (index !== -1) {
					const i = _.findIndex($rootScope.settings[key][index].allowedValues, (allowedValue) => allowedValue.itemValue === userSetting.value);
					if (i !== -1) {
							$rootScope.settings[key][index].currentValue = $rootScope.settings[key][index].allowedValues[i];
					}
				}
			});
		});
	}

	function init() {
		getSettings();
	}

	init();
});
