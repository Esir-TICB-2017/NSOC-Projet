angular.module('nsoc')
.controller('generalController', function($scope, $rootScope, getDataService, d3ChartService, $http, _, $interval, $timeout, Flash) {
	$rootScope.selectors = [
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
		$timeout(function() {
			if (!$rootScope.loading) {
				const element = document.querySelectorAll('.general>.mainInfo .round.active');
				const scope = angular.element(element).scope();
				if (scope.obj) {
					$scope.changeGraph(scope.obj);
				}
			}
		}, 0);
	};

	$scope.changeGraph = function(target) {
		const oldGraph = angular.copy($rootScope.actualGraph);
		if (target) {
			$rootScope.actualGraph = target;
		} else if (this.obj) {
			$rootScope.actualGraph = this.obj;
		}
		if (!oldGraph || oldGraph.name !== $rootScope.actualGraph.name || oldGraph.type !== $rootScope.actualGraph.type) {
			drawChart();
		}
	};

	function drawChart() {
		const startDate = moment().startOf($rootScope.actualSelector.value).format('X');
		const endDate = moment().format('X');
		getDataService.get(startDate, endDate, $rootScope.actualGraph.type, $rootScope.actualGraph.name, (data) => {
			d3ChartService.draw(data, 'month', 'homeChart');
		});
	}

	function getHomeBackgroundGradient(value) {
		let fromValue = value - 10;
		let toValue = value + 10;
		if (fromValue < 0) {
			fromValue = 0;
		} else if (toValue > 100) {
			toValue = 100;
		}
		$rootScope.backgroundGradient = {'background-image':'linear-gradient(-180deg, '+getColor(fromValue)+' 0%, '+getColor(toValue)+' 100%)'};
	}

	function getHouseHealth(value) {
		if (value >= 0 && value < 25) {
			$rootScope.houseHealth = 'very bad';
		} else if (value >= 25 && value < 50) {
			$rootScope.houseHealth = 'bad';
		} else if (value >= 50 && value < 75) {
			$rootScope.houseHealth = 'ok';
		} else if (value >= 75 && value <= 100) {
			$rootScope.houseHealth = 'great';
		} else {
			$rootScope.houseHealth = 'abnormally';
		}
	}

	function getColor(value){
		var hue=(value * 1.75).toString(10);
		return "hsl("+hue+",50%,50%)";
	}
	let monte = true;

	function displayHouseInfo(obj) {
		if (obj.date) {
			obj.lastUpdate = moment(obj.date).fromNow();
		}
		if (obj.data) {
			obj.data = Math.round(obj.data * 10) / 10;
		}
		obj.unit = transformUnit(obj.unit);

		if (obj.type === 'indicator' && obj.name === 'global') {
			$rootScope.globalIndicator = obj;
			getHomeBackgroundGradient(obj.data);
			getHouseHealth(obj.data);
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
	};

	function transformUnit(unit) {
		switch(unit) {
			case 'celsius':
					return String.fromCharCode(176) + 'C';
					break;
			case 'ppm':
					return 'ppm';
					break;
			case 'w/h':
					return 'w/h';
					break;
			case 'perrcentage':
					return '%';
					break;
			default:
					return '%';
					break;
		}
	}

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

	$scope.$on('firstData', (e, data) => {
		data.forEach((obj) => {
			displayHouseInfo(obj);
		});
		const defaultParameters = {selector: $rootScope.selectors[0], graph: $rootScope.globalIndicator};
		$scope.getData(defaultParameters.selector);
		$scope.changeGraph(defaultParameters.graph);
		updateDisplayedDates();
		$rootScope.loading = false;
	});

	$scope.$on('data', (event, data) => {
		displayHouseInfo(data);
	});

	$scope.changeMode();
});
