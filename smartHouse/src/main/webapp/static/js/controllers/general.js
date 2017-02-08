/**
* Created by loulou on 30/01/2017.
*/
angular.module('nsoc')
.controller('generalController', ($scope, $rootScope, $cookies, getDataService, d3ChartService, $http, _, $interval) => {
	$scope.selectors = [
		{name: 'Monthly', value: 'month'},
		{name: 'Weekly', value: 'week'},
		{name: 'Daily', value: 'day'},
	];


	$scope.changeMode = function() {
		if ($scope.sensorMode) {
			$scope.mode = 'sensor';
		} else {
			$scope.mode = 'indicator';
		}
		drawChart();
	};

	$scope.getData = function (selector) {
		$scope.actualSelector = selector.name;
		$scope.actualSelectorValue = selector.value;
	};

	$scope.drawChart = function(target) {
		if (target) {
			$scope.actualGraph = target.name;
		} else {
			$scope.actualGraph = this.obj.name;
		}
		drawChart();
	};

	function drawChart() {
	 	let actualMode = $scope.mode;
		if ($scope.actualGraph === 'global') {
			actualMode = 'indicator';
		}
		const startDate = moment().startOf($scope.actualSelectorValue).format('X');
		const endDate = moment().format('X');
		console.log($scope.actualSelectorValue, actualMode, $scope.actualGraph);
		getDataService.get(startDate, endDate, actualMode, $scope.actualGraph, (data) => {
			d3ChartService.draw(data, 'month', 'homeChart');
		});
	}

	function displayHouseInfo(obj) {
		$scope.$apply(() => {
			if (obj.date) {
				obj.date = moment(obj.date);
				obj.lastUpdate = obj.date.fromNow();
			}
			if (obj.data) {
				obj.data = Math.round(obj.data * 10) / 10;
			}
			if (obj.type === 'indicator' && obj.name === 'global') {
				$rootScope.globalIndicator = obj;
				getHouseHealth();
			} else {
				const index = _.findIndex($scope.data, (object) => {
					object.name === obj.name && object.type === obj.type;
				});
				if (index !== -1) {
					$scope.data[index] = obj;
				} else {
					$scope.data.push(obj);
				}
			}
		});
	};

	function getHouseHealth() {
		if ($rootScope.globalIndicator.data <= 33) {
			$rootScope.houseHealth = 'bad';
		} else if ($rootScope.globalIndicator.data > 33 && $rootScope.globalIndicator.data < 66) {
			$rootScope.houseHealth = 'ok';
		} else {
			$rootScope.houseHealth = 'great';
		}
	}

	$scope.$on('data', (event, data) => {
		if (_.isArray(data)) {
			data.forEach((obj) => {
				displayHouseInfo(obj);
				$rootScope.loading = false;
			});
			// ONLY FOR TEST //
			// FILLING DATA ARRAY WITH INDICATORS //
			const tab = $scope.data;
			tab.forEach((obj) => {
				$scope.data.push({name: obj.name, data: 12, lastUpdate: obj.lastUpdate, type: 'indicator'})
			});
			///////////////////
			$scope.getData($scope.selectors[2]);
			$scope.drawChart($rootScope.globalIndicator);
		} else {
			displayHouseInfo(data);
		}
	});

	$scope.displayDate = function (round) {
		if (round) {
			round.showDate = true;
		} else {
			this.showDate = true;
		}
	}

	$scope.hideDate = function (round) {
		if (round) {
			round.showDate = false;
		} else {
			this.showDate = false;
		}
	}

	$interval(() => {
		$scope.data.forEach((obj) => {
			obj.lastUpdate = obj.date.fromNow();
		});
		$rootScope.globalIndicator.lastUpdate = $rootScope.globalIndicator.date.fromNow();
	}, 60000);
});
