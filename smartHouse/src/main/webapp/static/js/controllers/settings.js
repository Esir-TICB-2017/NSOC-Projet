/**
* Created by loulou on 04/02/2017.
*/

angular.module('nsoc')
.controller('settingsController', ($scope, $rootScope, $http, _, websocketService, toastService) => {

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
			$scope.settings.users.push(user);
		}
	}

	$scope.deleteUser = function (user) {
		if (user.email && $scope.role === 'admin') {
			const setting = getSettingKeyAndIndex(user);
			$rootScope.settings[setting.key].splice(setting.index, 1);
			websocketService.send(JSON.stringify({key: 'deleteUser', email: user.email}));
		}
	}

	getSettings = function () {
		const p = new Promise((resolve, reject) => {
			$http({
				method: 'GET',
				url: '/getSettings'
			}).then(function success(res) {
				$rootScope.settings = _.groupBy(res.data.settings, setting => setting.type);
				$scope.actualSettingView = 'general';
				if ($scope.role === 'admin') {
					$rootScope.settings.users = res.data.users;
				}
				getUserSettings(resolve, reject);
			}, function error(err) {
				console.log(err);
			});
		});
		$rootScope.promiseArray = [];
		$rootScope.promiseArray.push(p);

	};

	$scope.changeSettingView = function (view) {
		$scope.actualSettingView = view;
	};

	getUserSettings = function (resolve, reject) {
		$http({
			method: 'GET',
			url: '/getUserSettings'
		}).then(function success(res) {
			$scope.userSettings = res.data;
			displayUserDefaultSettings();
			resolve();
		}, function error(err) {
			reject();
			console.log(err);
		});
	}

	displayUserDefaultSettings = function () {
		$scope.userSettings.forEach((userSetting) => {
			const setting = getSettingKeyAndIndex({id: userSetting.setting_id});
			if (setting !== -1) {
				const index = _.findIndex($rootScope.settings[setting.key][setting.index].allowedValues, (allowedValue) => allowedValue.itemValue === userSetting.value);
				if (index !== -1) {
					$rootScope.settings[setting.key][setting.index].currentValue = $rootScope.settings[setting.key][setting.index].allowedValues[setting.index];
				} else {
					$rootScope.settings[setting.key][setting.index].currentValue = userSetting.value;
				}
			}
		});
	}

	getSettingKeyAndIndex = function (obj) {
		for (var key in $rootScope.settings) {
			let index = -1;
			if (obj.id) {
				index = _.findIndex($rootScope.settings[key], (setting) => setting.id === obj.id);
			} else if (obj.email) {
				index = _.findIndex($rootScope.settings[key], (setting) => setting.email === obj.email);
			}
			if (index !== -1) {
				return {key, index};
			}
		}
		return -1;
	}

	function init() {
		getSettings();
		$scope.newUser = {email: "", role: "member"};
	}

	init();
});
