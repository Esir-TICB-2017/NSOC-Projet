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
		$rootScope.actualSelector = selector;
	};

	$scope.changeMode = function() {
		if ($rootScope.indicatorMode) {
			$scope.mode = 'indicator';
		} else {
			$scope.mode = 'sensor';
		}
	};

	$scope.changeGraph = function(target) {
		if (target) {
			$rootScope.actualGraph = target.name;
		} else if (this.obj) {
			$rootScope.actualGraph = this.obj.name;
		}
		drawChart();
	};

	function drawChart() {
	 	let actualMode = $scope.mode;
		if ($rootScope.actualGraph === $rootScope.globalIndicator.name) {
			actualMode = $rootScope.globalIndicator.type;
		}
		const startDate = moment().startOf($rootScope.actualSelector.value).format('X');
		const endDate = moment().format('X');
		getDataService.get(startDate, endDate, actualMode, $rootScope.actualGraph, (data) => {
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
		}, 60000);
	}

	function initialize () {
		$scope.changeMode();
		if ($rootScope.globalIndicator) {
			drawChart();
		}
	}

	initialize();
});
