(function() {
    'use strict';
    angular
        .module('killfomoApp')
        .factory('AuthResource', AuthResource);

    AuthResource.$inject = ['$resource'];

    function AuthResource ($resource) {
        var resourceUrl =  'api/auth-resources/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
