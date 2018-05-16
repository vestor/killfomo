(function() {
    'use strict';
    angular
        .module('killfomoApp')
        .factory('Task', Task);

    Task.$inject = ['$resource', 'DateUtils'];

    function Task ($resource, DateUtils) {
        var resourceUrl =  'api/tasks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dueBy = DateUtils.convertDateTimeFromServer(data.dueBy);
                        data.externalCreatedAt = DateUtils.convertDateTimeFromServer(data.externalCreatedAt);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
