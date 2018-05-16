(function() {
    'use strict';

    angular
        .module('killfomoApp')
        .controller('TaskKillFomoEntitiesDeleteController',TaskKillFomoEntitiesDeleteController);

    TaskKillFomoEntitiesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Task'];

    function TaskKillFomoEntitiesDeleteController($uibModalInstance, entity, Task) {
        var vm = this;

        vm.task = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Task.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
