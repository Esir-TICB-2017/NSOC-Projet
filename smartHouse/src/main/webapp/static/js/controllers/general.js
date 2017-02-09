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

	$scope.getData = function (selector) {
		$scope.actualSelector = selector.name;
		$scope.actualSelectorValue = selector.value;
	};

	$scope.changeMode = function() {
		if ($scope.indicatorMode) {
			$scope.mode = 'indicator';
		} else {
			$scope.mode = 'sensor';
		}
	};

	$scope.changeGraph = function(target) {
		if (target) {
			$scope.actualGraph = target.name;
		} else if (this.obj) {
			$scope.actualGraph = this.obj.name;
		}
		drawChart();
	};

	function drawChart() {
	 	let actualMode = $scope.mode;
		if ($scope.actualGraph === $rootScope.globalIndicator.name) {
			actualMode = $rootScope.globalIndicator.type;
		}
		const startDate = moment().startOf($scope.actualSelectorValue).format('X');
		const endDate = moment().format('X');
		getDataService.get(startDate, endDate, actualMode, $scope.actualGraph, (data) => {
			d3ChartService.draw(data, 'month', 'homeChart');
		});
	}

	function getHouseHealth() {
		if ($rootScope.globalIndicator.data <= 33) {
			$rootScope.houseHealth = 'bad';
		} else if ($rootScope.globalIndicator.data > 33 && $rootScope.globalIndicator.data < 66) {
			$rootScope.houseHealth = 'ok';
		} else {
			$rootScope.houseHealth = 'great';
		}
	}

	function displayHouseInfo(obj) {
		$scope.$apply(() => {
			if (obj.date) {
				obj.lastUpdate = moment(obj.date).fromNow();
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

	$scope.$on('data', (event, data) => {
		if (_.isArray(data)) {
			data.forEach((obj) => {
				displayHouseInfo(obj);
				$rootScope.loading = false;
			});
			// ONLY FOR TEST //
			// FILLING DATA ARRAY WITH INDICATORS //
			let tab = $scope.data;
			tab.forEach((obj) => {
				$scope.data.push({name: obj.name, data: 12, lastUpdate: obj.lastUpdate, type: 'indicator', state: 'disconnected'})
			});
			///////////////////
			$scope.changeMode();
			$scope.getData($scope.selectors[2]);
			$scope.changeGraph($rootScope.globalIndicator);
			updateDisplayedDates();
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
	function updateDisplayedDates() {
		$interval(() => {
			$scope.data.forEach((obj) => {
				obj.lastUpdate =  moment(obj.date).fromNow();
			});
			$rootScope.globalIndicator.lastUpdate = moment($rootScope.globalIndicator.date).fromNow();
		}, 1000);
	}
});
