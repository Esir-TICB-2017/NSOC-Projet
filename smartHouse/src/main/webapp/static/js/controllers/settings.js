/**
 * Created by loulou on 04/02/2017.
 */

angular.module('nsoc')
    .controller('settingsController', ($scope, $rootScope, $http, _, websocketService, toastService) => {
        $scope.changeValue = function (setting, newValue) {

<<<<<<< HEAD
            websocketService.send(JSON.stringify({key: 'settings', setting_id: setting.id, value: newValue.itemValue}));
            //changer role : {key: userRole, role, email}
            // add user: {key: addUser, role, email}
            // supprimer: {key: deleteUser, email}
        }

        updateParameter = function (parameter) {
            let setting;
            if (parameter.setting_id) {
                setting = getSetting({id: parameter.setting_id});
                setting.currentValue = getAllowedValue(setting, parameter.value);
            } else if (parameter.email) {
                setting = getSetting({email: parameter.email});
                switch (parameter.key) {
                    case 'userRole':
                        break;
                    case 'addUser' :
                        break;
                    case 'deleteUser':
                        break;
                }
            }
        }
=======
	$scope.changeValue = function(setting, newValue) {
		websocketService.send(JSON.stringify({key: 'settings', setting_id: setting.id, value: newValue}));
		//changer role : {key: userRole, role, email}
		// add user: {key: addUser, role, email}
		// supprimer: {key: deleteUser, email}
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
>>>>>>> c0a87d297a99000c3b862f9fb1fa951ba89b9392

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

<<<<<<< HEAD
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
=======
	getUserSettings = function () {
		$http({
			method: 'GET',
			url: '/getUserSettings'
		}).then(function success(res) {
			$scope.userSettings = res.data;
			console.log(res.data);
			displayUserDefaultSettings();
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
>>>>>>> c0a87d297a99000c3b862f9fb1fa951ba89b9392

        displayUserDefaultSettings = function () {
            $scope.userSettings.forEach((userSetting) => {
                const setting = getSetting({id: userSetting.setting_id});
                if (setting !== -1) {
                    const allowedValue = getAllowedValue(setting, userSetting.value);
                    if (allowedValue !== -1) {
                        setting.currentValue = allowedValue;
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

<<<<<<< HEAD
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
=======
	function init() {
		getSettings();
		$scope.$on('settings', (event, parameter) => {
			updateParameter(parameter);
		});
	}
>>>>>>> c0a87d297a99000c3b862f9fb1fa951ba89b9392

        function init() {
            getSettings();
            $scope.$on('settings', (event, parameter) => {
                updateParameter(parameter);
            });
        }

        init();
    });
