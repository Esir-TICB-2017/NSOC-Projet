/**
 * Created by loulou on 30/01/2017.
 */
angular.module('nsoc')
    .controller('generalController', ($scope) => {
			$scope.selectors = ['Monthly', 'Weekly', 'Daily'];
			$scope.actualSelector = $scope.selectors[0];
        $scope.getData = function (selector) {
					$scope.actualSelector = selector;
            console.log(selector);
/*
            const startDate = moment().startOf(test).format('X');
            const endDate = moment().format('X');
            getDataService.get(startDate, endDate, (data) => {
                d3ChartService.draw(data, 'homeChart');
            })
            */
        }
    });
