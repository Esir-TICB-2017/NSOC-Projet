/**
 * Created by Loulou on 15/02/2017.
 */
/**
 * Created by loulou on 30/01/2017.
 */
angular.module('nsoc').factory('toastService', ($rootScope, ngToast, _) => {
    return {
        create: function (type, message) {
            ngToast.create('a toast message...');
            if($rootScope.settings) {
                const globalNotifications = _.find($rootScope.settings.general, (item) => {
                    return item.name === 'global notifications';
                });
                if(globalNotifications.currentValue.itemValue === 'true') {
                    ngToast.create({
                        className: type,
                        content: message,
                    });
                    console.log('ici')
                }
            }
        }
    }
});

