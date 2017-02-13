/**
* Created by loulou on 04/02/2017.
*/

angular.module('nsoc')
    .controller('settingsController', ($scope, $http) => {
    $http({
        method: 'GET',
        url: '/getSettings'
    }).then(function success(res) {
    console.log(res);
}, function error(err) {
    console.log(err);
});
});

angular.module('nsoc')
.controller('userSettingsController', ($scope, $http) => {
	$http({
		method: 'GET',
		url: '/getUserSettings'
	}).then(function success(res) {
		console.log(res);
	}, function error(err) {
		console.log(err);
	});
});


/*
angular.module('nsoc')
    .controller('userPostSettingsController', ($scope, $http) => {
    $http({
        method: 'POST',
        url: '/postSettings',
        data: "{“users_settings”: [{“setting_id”: 172,“value”: “thisisit”}, {“setting_id”: 174,“value”: “thisisit2”} ]}"
}).then(function success(res) {
    console.log(res);
}, function error(err) {
    console.log(err);
});
});
*/