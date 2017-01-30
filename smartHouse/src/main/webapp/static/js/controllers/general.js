/**
 * Created by loulou on 30/01/2017.
 */
angular.module('nsoc')
    .controller('generalController', ($scope, $cookies, getDataService, d3ChartService) => {
			$scope.selectors = [
					{name: 'Monthly', value: 'month'},
					{name: 'Weekly', value: 'week'},
					{name: 'Daily', value: 'day'}
			];
			$scope.actualSelector;
			$scope.houseIndicator = 93;
			$scope.sensors = [];

			$scope.userInfo = {
				givenName: $cookies.get('givenName').charAt(0).toUpperCase() + $cookies.get('givenName').slice(1),
				pictureUrl: $cookies.get('pictureUrl'),
			};

			$scope.$on('sensorValue', (event, obj) => {
				$scope.$apply(() => {
						const index = _.findIndex($scope.sensors, (sensor) => sensor.key === obj.key);
						if (index !== -1) {
							$scope.sensors[index].value = parseInt(obj.value);
						} else {
							let side = null;
							if ($scope.sensors[$scope.sensors.length - 1] && $scope.sensors[$scope.sensors.length - 1].side === 'left') {
								side = 'right';
							} else {
								side = 'left';
							}
							$scope.sensors.push({key: obj.key, value: parseInt(obj.value), side});
						}
				});
			});

        $scope.getData = function (selector) {
            $scope.actualSelector = selector.name;
            const startDate = moment().startOf(selector.value).format('X');
            const endDate = moment().format('X');
            getDataService.get(startDate, endDate, (data) => {
                d3ChartService.draw(data, 'homeChart');
            });
        };

        $scope.getData($scope.selectors[0]);
    });
