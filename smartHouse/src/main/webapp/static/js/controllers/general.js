/**
 * Created by loulou on 30/01/2017.
 */
angular.module('nsoc')
    .controller('generalController', ($scope) => {
			$scope.selectors = ['Monthly', 'Weekly', 'Daily'];
			$scope.actualSelector = $scope.selectors[0];
        $scope.getData = function (selector) {
					$scope.actualSelector = selector;
        }

        const now = parseInt(new Date().getTime() / 1000);
    });
