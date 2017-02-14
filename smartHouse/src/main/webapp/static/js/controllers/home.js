angular.module('nsoc')
    .controller('homeController', ($scope, $rootScope, $http, $cookies, $location, _, websocketService, utils, Flash, newD3Service) => {

        $scope.changeTab = function (newTab) {
            $scope.actualTab = newTab;
            $scope.actualTab.notifications = 0;
        };

        $scope.signOut = function () {
            $scope.GoogleAuth.signOut().then(function () {
                $http({
                    method: 'GET',
                    url: '/logout'
                }).then(function success(res) {
                    $rootScope.loading = false;
                    console.log('User signed out.');
                    $cookies.put('authenticate', false);
                    $location.path('/login');
                }, function error(err) {
                    console.log(err);
                    console.log('Please try to logout again');
                });
            });
        }

        initSocket = function () {
            if (utils.getBoolean($cookies.get('authenticate'))) {
                websocketService.start('ws://127.0.0.1:8080/?' + $cookies.get('idtoken'),
                    function onOpen(websocket) {
                    },
                    function onClose() {
                        $scope.signOut();
                    },
                    function onMessage(evt) {
                        const data = JSON.parse(evt.data);
                        const currentData = newD3Service.getCurrentData();
                        if (currentData.type === data.type && currentData.name == data.name) {
                            newD3Service.addRowToCurrentSet(data);
                            newD3Service.update();
                        }
                        if ($scope.actualTab.name !== $scope.tabs[0].name) {
                            $scope.tabs[0].notifications++;
                        }
                        const message = 'New <strong>' + data.type + '</strong> value. <strong>' + data.name + '</strong> : ' + data.data;
                        // Flash.create('success', message);
                        $scope.$broadcast('data', data);
                    });
            }
        };

        init = function () {
            $scope.tabs = [{name: 'general', notifications: 0}, {name: 'settings', notifications: 0}];
            $scope.actualTab = $scope.tabs[0];
            $scope.userInfo = {
                givenName: $cookies.get('givenName').charAt(0).toUpperCase() + $cookies.get('givenName').slice(1),
                pictureUrl: $cookies.get('pictureUrl'),
            };
            $rootScope.loading = true;
            initSocket();
        }

        init();

    });
