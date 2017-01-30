/**
 * Created by loulou on 30/01/2017.
 */
angular.module('nsoc').factory('getDataService', ($http) => {
    return {
        get: function (startDateTimestamp, endDateTimestamp, callback) {
            $http({
                method: 'POST',
                url: '/data',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                params: {
                    "startDate": startDateTimestamp,
                    "endDate": endDateTimestamp,
                    "sensorName": "temperature"
                }
            }).then(function success(res) {
                console.log(res)
                callback(res.data);
            }, function error(err) {
                console.log(err);
            });
        }
    }
});