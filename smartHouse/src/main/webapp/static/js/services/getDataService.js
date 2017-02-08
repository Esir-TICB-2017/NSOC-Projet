/**
 * Created by loulou on 30/01/2017.
 */
angular.module('nsoc').factory('getDataService', ($http) => {
    return {
        get: function (startDateTimestamp, endDateTimestamp, objectType, objectName, callback) {
            let url;
            switch (objectType) {
                case 'sensor':
                     url = '/getValuesOverPeriod';
                    break;
                case 'indicator':
                     url = '/getIndicatorsOverPeriod';
                    break;
                default:
                     url = '/getIndicatorsOverPeriod';
            }
            $http({
                method: 'GET',
                url: url,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                params: {
                    "startDate": startDateTimestamp,
                    "endDate": endDateTimestamp,
                    "objectName": objectName,
                }
            }).then(function success(res) {
                callback(res.data);
            }, function error(err) {
                console.log(err);
            });
        }
    }
});
