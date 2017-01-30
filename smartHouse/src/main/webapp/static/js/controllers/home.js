angular.module('nsoc')
    .controller('homeController', ($scope, $http, $location, websocketService, _) => {
        $scope.tabs = ['general', 'data', 'settings'];
        $scope.actualTab = $scope.tabs[0];
        $scope.sensors = [];


        $scope.changeTab = (newTab) => {
            $scope.actualTab = newTab;
        };
        $scope.signOut = function () {
            $scope.GoogleAuth.signOut().then(() => {
                $http({
                    method: 'GET',
                    url: '/logout'
                }).then(function success(res) {
                    console.log(res);
                    console.log('User signed out.');
                    $location.path('/login');
                }, function error(err) {
                    // TODO Force logout on server
                    console.log(err);
                    console.log('Please try to logout again');
                });
            });
        }
        $http({
            method: 'GET',
            url: '/getLastIndicators',
            params: {indicator: 'global'},
        }).then(function success(res) {
            console.log(res);
        }, function error(err) {
            console.log(err);
        });
        websocketService.start('ws://127.0.0.1:8080/', (evt) => {
            var obj = JSON.parse(evt.data);
            if (obj.key && obj.value) {
                $scope.$broadcast('sensorValue', obj);
            }
        });
    });
