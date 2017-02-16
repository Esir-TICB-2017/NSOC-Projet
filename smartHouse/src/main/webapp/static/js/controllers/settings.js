/**
* Created by loulou on 04/02/2017.
*/

angular.module('nsoc')
.controller('settingsController', ($scope, $rootScope, $http, _, websocketService) => {

	$scope.updateParameterValue = function(setting, newValue) {
		if ($scope.role === 'admin' || $scope.role === 'member') {
			websocketService.send(JSON.stringify({key: 'settings', setting_id: setting.id, value: newValue}));
		}
		//changer role : {key: userRole, role, email}
		// add user: {key: addUser, role, email}
		// supprimer: {key: deleteUser, email}
	}

	$scope.addUser = function(user){
		if (user.email && user.role && $scope.role === 'admin') {
				websocketService.send(JSON.stringify({key: 'addUser', role: user.role, email: user.email}));
		}
	}

	$scope.deleteUser = function (user) {
		if (user.email && $scope.role === 'admin') {
			websocketService.send(JSON.stringify({key: 'deleteUser', email: user.email}));
		}
	}

	updateParameter = function(parameter) {
		let setting;
		if (parameter.setting_id) {
			setting = getSetting({id: parameter.setting_id});
		const allowedValue = getAllowedValue(setting, parameter.value);
			if (allowedValue !== -1) {
				setting.currentValue = allowedValue;
			} else {
				setting.currentValue = parameter.value;
			}
		} else if (parameter.email) {
			setting = getSetting({email: parameter.email});
			switch(parameter.key) {
				case 'userRole':
				break;
				case 'addUser' :
				break;
				case 'deleteUser':
				break;
			}
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
			console.log(res.data);
			displayUserDefaultSettings();
			console.log($scope.settings);
		}, function error(err) {
			console.log(err);
		});
	}

	displayUserDefaultSettings = function () {
		$scope.userSettings.forEach((userSetting) => {
			const setting = getSetting({id: userSetting.setting_id});
			if (setting!== -1) {
				const allowedValue = getAllowedValue(setting, userSetting.value);
				if (allowedValue !== -1) {
					setting.currentValue = allowedValue;
				} else {
					setting.currentValue = userSetting.value;
				}
			}
		});
	}

	getAllowedValue = function (setting, value) {
		const index = _.findIndex(setting.allowedValues, (allowedValue) => allowedValue.itemValue === value);
		if (index !== -1) {
			return setting.allowedValues[index];
		} else {
			return -1;
		}
	}

	getSetting = function (obj) {
		for (var key in $rootScope.settings) {
			let index = -1;
			if (obj.id) {
				index = _.findIndex($rootScope.settings[key], (setting) => setting.id === obj.id);
			} else if (obj.email) {
				index = _.findIndex($rootScope.settings[key], (setting) => setting.email === obj.email);
			}
			if (index !== -1) {
				return $rootScope.settings[key][index];
			}
		}
		return -1;
	}

	function init() {
		getSettings();
		$scope.newUser = {email: "", role: "member"};
		$scope.$on('settings', (event, parameter) => {
			updateParameter(parameter);
		});
	}

	init();
});
