/**
 * Created by loulou on 30/01/2017.
 */
angular.module('nsoc')
    .controller('generalController', ($scope, getDataService, d3ChartService) => {
        $scope.selectors = [
            {name: 'Monthly', value: 'month'},
            {name: 'Weekly', value: 'week'},
            {name: 'Daily', value: 'day'}
        ];
        $scope.actualSelector;
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
