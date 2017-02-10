angular.module('nsoc')
.controller('generalController', ($scope, $rootScope, getDataService, d3ChartService, $http, _, $interval) => {
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

	$scope.$on('data', (event, data) => {
		displayHouseInfo(data);
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

	function getFirstData() {
		$http({
			method: 'GET',
			url: '/getFirstData'
		}).then(function success(res) {
			res.data.forEach((obj) => {
				displayHouseInfo(obj);
			});
			$scope.getData($rootScope.selectors[0]);
			$scope.changeGraph($rootScope.globalIndicator);
			updateDisplayedDates();
			$rootScope.loading = false;
		}, function error(err) {
			console.log(err);
		});
	};

	function initialize () {
		if ($scope.data.length == 0) {
			getFirstData();
		}
		$scope.changeMode();
		if ($rootScope.globalIndicator) {
			drawChart();
		}
	}

	initialize();
});
