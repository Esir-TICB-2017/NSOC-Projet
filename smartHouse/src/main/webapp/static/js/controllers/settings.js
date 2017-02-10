/**
* Created by loulou on 04/02/2017.
*/
/**
* Created by loulou on 30/01/2017.
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
