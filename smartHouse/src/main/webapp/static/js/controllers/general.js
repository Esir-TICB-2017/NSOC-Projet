/**
* Created by loulou on 30/01/2017.
*/
angular.module('nsoc')
.controller('generalController', ($scope, $rootScope, $cookies, getDataService, d3ChartService, $http, _) => {
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
		getDataService.get(startDate, endDate, (data) => {
			d3ChartService.draw(data, selector.value, 'homeChart');
		});
	};

	// $scope.getData($scope.selectors[0]);

	$scope.$on('firstData', (event, obj) => {
		$scope.$apply(() => {
			$rootScope.houseIndicator = Math.round(obj.globalIndicator.value);
			getHouseHealth();
			obj.lastValues.forEach((sensor) => {
				sensor.data = Math.round(sensor.data*10)/10;
			});
			$scope.sensors = obj.lastValues;
		});
		$rootScope.loading = false;
	});

	$scope.$on('newValue', (event, obj) => {
		$scope.$apply(() => {
			if (obj.type === 'indicator') {
				if (obj.name === 'global') {
					$rootScope.houseIndicator = Math.round(obj.value);
					getHouseHealth();
				}
			} else if (obj.type === 'sensor'){
				$scope.sensors.some((sensor, index) => {
					if (sensor.name === obj.name) {
						$scope.sensors[index] = obj;
						return true;
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
