angular.module('nsoc')
    .controller('generalController', function ($scope, $rootScope, getDataService, newD3Service, $http, _, $interval, $timeout, Flash) {

        $scope.getData = function (selector) {
            $scope.actualSelector = selector;
            updateChart();
        };

        $scope.changeMode = function (newMode) {
            $scope.actualMode = newMode;
            $timeout(function () {
                if (!$rootScope.loading) {
                    const element = document.querySelectorAll('.general>.mainInfo .round.active');
                    const scope = angular.element(element).scope();
                    if (scope.obj) {
                        $scope.changeGraph(scope.obj);
                    }
                }
            }, 0);
        };

        $scope.changeGraph = function (target) {
            const oldGraph = angular.copy($scope.actualGraph);
            if (target) {
                $scope.actualGraph = target;
            } else if (this.obj) {
                $scope.actualGraph = this.obj;
            }
            if (!oldGraph || oldGraph.name !== $scope.actualGraph.name || oldGraph.type !== $scope.actualGraph.type) {
                updateChart();
            }
        };


        function updateChart() {
            const startDate = moment().startOf($scope.actualSelector.value).format('X');
            const endDate = moment().format('X');
            getDataService.get(startDate, endDate, $scope.actualGraph.type, $scope.actualGraph.name, (data) => {
                if (data.length != 0) {
                    newD3Service.updateCurrentData(data, $scope.actualGraph.unit);
                    newD3Service.update();
                } else {
                    Flash.create('info', 'No data for this configuration');
                }
            });
        }

        function addNewDataToChard(data) {
            const currentData = newD3Service.getCurrentData();
            if (currentData.type === data.type && currentData.name == data.name) {
                newD3Service.addRowToCurrentSet(data);
                newD3Service.update();
            }
        };

        function drawChart() {
            const startDate = moment().startOf($scope.actualSelector.value).format('X');
            const endDate = moment().format('X');
            getDataService.get(startDate, endDate, $scope.actualGraph.type, $scope.actualGraph.name, (data) => {
                newD3Service.appendGradient();
                newD3Service.updateCurrentData(data, $scope.actualGraph.unit);
                newD3Service.init();
            });
        }


        function getHomeBackgroundGradient(value) {
            let topColor;
            let bottomColor;
            if (value >= 80 && value < 100) {
                topColor = '#00CD30';
                bottomColor = '#42CBA5';
            }
            if (value >= 60 && value < 80) {
                topColor = '#00CD98';
                bottomColor = '#42C8CB';
            }
            if (value >= 40 && value < 60) {
                topColor = '#FFCD7F';
                bottomColor = '#F49058';
            }
            if (value >= 20 && value < 40) {
                topColor = '#F49058';
                bottomColor = '#F85D6B';
            }
            if (value >= 0 && value < 20) {
                topColor = '#F52929';
                bottomColor = '#F09191';
            }
            $rootScope.backgroundGradient = {'background-image': 'linear-gradient(-180deg, ' + topColor + ' 0%, ' + bottomColor + ' 100%)'};
        }

        function getHouseHealth(value) {
            if (value >= 0 && value < 25) {
                $scope.houseHealth = 'very bad';
            } else if (value >= 25 && value < 50) {
                $scope.houseHealth = 'bad';
            } else if (value >= 50 && value < 75) {
                $scope.houseHealth = 'ok';
            } else if (value >= 75 && value <= 100) {
                $scope.houseHealth = 'great';
            } else {
                $scope.houseHealth = 'abnormally';
            }
        }

        function displayHouseInfo(obj) {
            if (obj.date) {
                obj.lastUpdate = moment(obj.date).fromNow();
            }
            if (obj.data) {
                obj.data = Math.round(obj.data * 10) / 10;
            }
            if (obj.type === 'indicator') {
                obj.unit = '%';
            }
            if (obj.type === 'indicator' && obj.name === 'global') {
                $scope.globalIndicator = obj;
                getHomeBackgroundGradient(obj.data);
                getHouseHealth(obj.data);
            } else {
                let index = -1;
                $scope.data.some((object, i) => {
                    if (object.name == obj.name && object.type == obj.type) {
                        index = i;
                        return true;
                    }
                });
                if (index !== -1) {
                    $scope.data[index].date = obj.date;
                    $scope.data[index].data = obj.data;
                    $scope.data[index].lastUpdate = obj.lastUpdate;
                    if (obj.connected !== undefined) {
                        $scope.data[index].connected = obj.connected;
                    }
                } else {
                    $scope.data.push(obj);
                }
            }
        };

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
                    obj.lastUpdate = moment(obj.date).fromNow();
                });
                $scope.globalIndicator.lastUpdate = moment($scope.globalIndicator.date).fromNow();
            }, 60000);
        }

        getFirstData = function () {
            $http({
                method: 'GET',
                url: '/getFirstData'
            }).then(function success(res) {
                if ($scope.actualTab.name !== $scope.tabs[0].name) {
                    $scope.tabs[0].notifications++;
                }
                Flash.create('success', 'Received new data!');
                res.data.forEach((obj) => {
                    displayHouseInfo(obj);
                });
                $scope.actualGraph = $scope.globalIndicator;
                drawChart();
                updateDisplayedDates();
                $rootScope.loading = false;
            }, function error(err) {
                console.log(err);
                if (err.status === 403) {
                    $cookies.put('authenticate', false);
                    $location.path('/login');
                    Flash.create('danger', 'Access denied');
                }
            });
        }

        function init() {
            $scope.data = [];
            $scope.selectors = [
                {name: 'Monthly', value: 'month'},
                {name: 'Weekly', value: 'week'},
                {name: 'Daily', value: 'day'},
            ];
            $scope.modes = ['indicator', 'sensor'];
            $scope.actualSelector = $scope.selectors[0];
            $scope.changeMode($scope.modes[1]);
            getFirstData();
            $scope.$on('data', (event, data) => {
                displayHouseInfo(data);
                addNewDataToChard(data);
            });
        }

        init();
    });
