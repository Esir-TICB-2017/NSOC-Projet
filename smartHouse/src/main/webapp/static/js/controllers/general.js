/**
* Created by loulou on 30/01/2017.
*/
angular.module('nsoc')
.controller('generalController', ($scope, $rootScope, $cookies, getDataService, d3ChartService, $http) => {
	$scope.selectors = [
		{name: 'Monthly', value: 'month'},
		{name: 'Weekly', value: 'week'},
		{name: 'Daily', value: 'day'}
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
		getDataService.get(startDate, endDate, (data) => {
			d3ChartService.draw(data, selector.value, 'homeChart');
		});
	};

	$scope.getData($scope.selectors[0]);

	$scope.$on('firstData', (event, data) => {
		$scope.$apply(() => {
			if (data.globalIndicator.key === 'global') {
				$rootScope.houseIndicator = data.globalIndicator.value;
				console.log($rootScope.houseIndicator);
				getHouseHealth();
			}
			data.lastValues.forEach((sensor) => {
				if (sensor.type === "sensor") {
					$scope.sensors.push(sensor);
				}
			});
		});
		$rootScope.loading = false;
	});

	$scope.$on('newSensorValue', (event, data) => {
		$scope.$apply(() => {
			$scope.sensors[data.key] = data.value;
		});
	});

	$scope.$on('newGlobalIndicatorValue', (event, data) => {
		$scope.$apply(() => {
			$rootScope.houseIndicator = data.global.value;
			getHouseHealth();
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
