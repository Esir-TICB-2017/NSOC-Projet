/**
* Created by loulou on 30/01/2017.
*/
angular.module('nsoc')
.controller('generalController', ($scope, $rootScope, $cookies, getDataService, d3ChartService, $http) => {
	$scope.selectors = [
		{name: 'Daily', value: 'day'},
		{name: 'Monthly', value: 'month'},
		{name: 'Weekly', value: 'week'}
	];
	$scope.userInfo = {
		givenName: $cookies.get('givenName').charAt(0).toUpperCase() + $cookies.get('givenName').slice(1),
		pictureUrl: $cookies.get('pictureUrl'),
	};
	$scope.actualSelector;

$scope.getData = function (selector) {
		$scope.actualSelector = selector.name;
		const startDate = moment().startOf(selector.value).format('X');
		const endDate = moment().format('X');
		// getDataService.get(startDate, endDate, (data) => {
		// 	d3ChartService.draw(data, selector.value, 'homeChart');
		// });
	};

	// $scope.getData($scope.selectors[0]);

	$scope.$on('firstData', (event, data) => {
		$scope.$apply(() => {
			if (data.globalIndicator.key === 'global') {
				$rootScope.houseIndicator = Math.round(data.globalIndicator.value);
				getHouseHealth();
			}
			data.lastValues.forEach((sensor) => {
				sensor.data = Math.round(sensor.data*10)/10;
			});
			$scope.sensors = data.lastValues;
		});
		$rootScope.loading = false;
	});

	$scope.$on('newValue', (event, data) => {
		$scope.$apply(() => {
			const key = Object.keys(data)[0];
			if (key === 'global') {
				$rootScope.houseIndicator = Math.round(data[key]);
				getHouseHealth();
			} else {
				$scope.sensors.forEach((sensor, index) => {
					if (sensor.name === key) {
						sensor.data = Math.round(data[key]*10)/10;
					}
				});
			}
		});
	});

	function getHouseHealth() {
		if ($rootScope.houseIndicator <= 33) {
			$rootScope.houseHealth = 'bad';
		} else if ($rootScope.houseIndicator > 33 && $rootScope.houseIndicator <66) {
			$rootScope.houseHealth = 'ok';
		} else {
			$rootScope.houseHealth = 'great';
		}
	}
});
