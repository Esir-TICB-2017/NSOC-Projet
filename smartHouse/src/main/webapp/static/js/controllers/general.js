/**
 * Created by loulou on 30/01/2017.
 */
angular.module('nsoc')
    .controller('generalController', ($scope, getDataService, d3ChartService) => {
        $scope.getData = function (test) {
            const startDate = moment().startOf(test).format('X');
            const endDate = moment().format('X');
            getDataService.get(startDate, endDate, (data) => {
                d3ChartService.draw(data, 'homeChart');
            })
        };
    });
