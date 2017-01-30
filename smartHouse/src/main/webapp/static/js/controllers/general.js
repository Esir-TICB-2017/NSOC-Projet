/**
 * Created by loulou on 30/01/2017.
 */
angular.module('nsoc')
    .controller('generalController', ($scope) => {
			$scope.selectors = ['Monthly', 'Weekly', 'Daily'];
        $scope.getData = function () {
            console.log('ici');
        }

        const now = parseInt(new Date().getTime() / 1000);
    });
