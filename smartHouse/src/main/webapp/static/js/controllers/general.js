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

	$scope.$on('newValue', (event, obj) => {
		$scope.$apply(() => {
			if (obj.key === 'GlobalIndicators') {
				$rootScope.houseIndicator = obj.value;
				getHouseHealth();
			} else {
				const index = _.findIndex($scope.sensors, (sensor) => sensor.key === obj.key);
				if (index !== -1) {
					$scope.sensors[index].value = parseInt(obj.value);
				} else {
					$scope.sensors.push({key: obj.key, value: parseInt(obj.value)});
				}
			}
		});
	});

	$scope.getData = function (selector) {
		$scope.actualSelector = selector.name;
		const startDate = moment().startOf(selector.value).format('X');
		const endDate = moment().format('X');
		getDataService.get(startDate, endDate, (data) => {
			// d3ChartService.draw(data, 'homeChart');
		});
	};

	$scope.getData($scope.selectors[0]);

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
