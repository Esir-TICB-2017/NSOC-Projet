/**
* Created by loulou on 30/01/2017.
*/
angular.module('nsoc')
.controller('generalController', ($scope, $rootScope, $cookies, getDataService, d3ChartService, $http, _) => {
	$scope.selectors = [
		{name: 'Monthly', value: 'month'},
		{name: 'Weekly', value: 'week'},
		{name: 'Daily', value: 'day'},
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

	// $scope.getData($scope.selectors[0]);

	$scope.$on('data', (event, data) => {
		if (_.isArray(data)) {
			data.forEach((obj) => {
				displayHouseInfo(obj);
				$rootScope.loading = false;
			});
		} else {
			displayHouseInfo(data);
		}
	});

	function displayHouseInfo(obj) {
		$scope.$apply(() => {
			obj.data = Math.round(obj.data * 10) / 10;
			if (obj.type === 'indicator') {
				if (obj.name === 'global') {
					$scope.globalIndicator = obj;
					getHouseHealth();
				} else {
					const indicatorIndex = _.findIndex($scope.indicators, indicator => indicator.name === obj.name);
					if (indicatorIndex !== -1) {
						$scope.indicators[indicatorIndex] = obj;
					} else {
						$scope.indicators.push(obj);
					}
				}
			} else if (obj.type === 'sensor'){
				const sensorIndex = _.findIndex($scope.sensors, sensor => sensor.name === obj.name);
				if (sensorIndex !== -1) {
					$scope.sensors[sensorIndex] = obj;
				} else {
					$scope.sensors.push(obj);
				}
			}
		});
	};

	function getHouseHealth() {
		if ($scope.globalIndicator.data <= 33) {
			$rootScope.houseHealth = 'bad';
		} else if ($scope.globalIndicator.data > 33 && $scope.globalIndicator.data <66) {
			$rootScope.houseHealth = 'ok';
		} else {
			$rootScope.houseHealth = 'great';
		}
	}
});
