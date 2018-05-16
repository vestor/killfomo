(function() {
    'use strict';

    angular
        .module('killfomoApp')
        .controller('AuthResourceKillFomoEntitiesDetailController', AuthResourceKillFomoEntitiesDetailController);

    AuthResourceKillFomoEntitiesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AuthResource'];

    function AuthResourceKillFomoEntitiesDetailController($scope, $rootScope, $stateParams, previousState, entity, AuthResource) {
        var vm = this;

        vm.authResource = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('killfomoApp:authResourceUpdate', function(event, result) {
            vm.authResource = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
