(function() {
    'use strict';

    angular
        .module('killfomoApp')
        .controller('TaskKillFomoEntitiesDetailController', TaskKillFomoEntitiesDetailController);

    TaskKillFomoEntitiesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Task'];

    function TaskKillFomoEntitiesDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Task) {
        var vm = this;

        vm.task = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('killfomoApp:taskUpdate', function(event, result) {
            vm.task = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
