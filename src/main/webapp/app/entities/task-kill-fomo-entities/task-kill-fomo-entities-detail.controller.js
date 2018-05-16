(function() {
    'use strict';

    angular
        .module('killfomoApp')
        .controller('TaskKillFomoEntitiesDetailController', TaskKillFomoEntitiesDetailController);

    TaskKillFomoEntitiesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Task'];

    function TaskKillFomoEntitiesDetailController($scope, $rootScope, $stateParams, previousState, entity, Task) {
        var vm = this;

        vm.task = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('killfomoApp:taskUpdate', function(event, result) {
            vm.task = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
