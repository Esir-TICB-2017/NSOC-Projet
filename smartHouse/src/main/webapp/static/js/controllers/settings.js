/**
* Created by loulou on 04/02/2017.
*/

angular.module('nsoc')
.controller('settingsController', ($scope, $rootScope, $http, _, websocketService) => {

	$scope.changeValue = function(setting, newValue) {
		websocketService.send(JSON.stringify({key: 'settings', setting_id: setting.id, value: newValue.itemValue}));
	}

	updateParameter = function(parameter) {
		if (parameter.id) {
		} else if (parameter.email) {

		}
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
		$scope.userSettings.forEach((userSetting) => {
			const setting = getSettingIndex(userSetting.setting_id);
			if (setting!== -1) {
				const i = _.findIndex(setting.allowedValues, (allowedValue) => allowedValue.itemValue === userSetting.value);
				if (i !== -1) {
					setting.currentValue = setting.allowedValues[i];
				}
			}
		});
	}

	getSettingIndex = function (id) {
		for (var key in $rootScope.settings) {
			const index = _.findIndex($rootScope.settings[key], (setting) => setting.id === id);
			if (index !== -1) {
				return $rootScope.settings[key][index];
			}
		}
		return -1;
	}

	function init() {
		getSettings();
		$scope.$on('settings', (parameter) => updateParameter(parameter));
	}

	init();
});
