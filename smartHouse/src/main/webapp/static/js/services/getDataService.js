/**
 * Created by loulou on 30/01/2017.
 */
angular.module('nsoc').factory('getDataService', ($http) => {
    return {
        get: function (startDateTimestamp, endDateTimestamp, callback) {
            $http({
                method: 'GET',
                url: '/getIndicatorsOverPeriod',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                params: {
                    "startDate": startDateTimestamp,
                    "endDate": endDateTimestamp,
                    "indicator": "global"
                }
            }).then(function success(res) {
                console.log(res.data);
            }, function error(err) {
                console.log(err);
            });
        }
    }
});
