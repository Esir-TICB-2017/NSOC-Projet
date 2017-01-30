/**
 * Created by loulou on 30/01/2017.
 */
angular.module('nsoc')
    .controller('generalController', ($scope, getDataService, d3ChartService) => {
        $scope.selectors = ['Monthly', 'Weekly', 'Daily'];
        $scope.actualSelector = $scope.selectors[0];
        $scope.getData = function (selector) {
            $scope.actualSelector = selector;
            const startDate = moment().startOf(selector.toLowerCase()).format('X');
            console.log(selector.toLowerCase(),startDate);
            const endDate = moment().format('X');
            getDataService.get(startDate, endDate, (data) => {
                d3ChartService.draw(data, 'homeChart');
            })

        }
    });
