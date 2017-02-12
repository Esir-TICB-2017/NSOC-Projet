angular.module('nsoc')
.factory('utils', () => {
	return {
		getBoolean: function(boolean) {
			if (boolean === 'true') {
				return true;
			} else if (boolean === 'false') {
				return false;
			} else {
				return undefined;
			}
		}
	};
});
