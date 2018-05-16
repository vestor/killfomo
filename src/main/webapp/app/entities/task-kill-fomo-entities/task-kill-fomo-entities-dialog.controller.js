(function() {
    'use strict';

    angular
        .module('killfomoApp')
        .controller('TaskKillFomoEntitiesDialogController', TaskKillFomoEntitiesDialogController);

    TaskKillFomoEntitiesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Task'];

    function TaskKillFomoEntitiesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Task) {
        var vm = this;

        vm.task = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.task.id !== null) {
                Task.update(vm.task, onSaveSuccess, onSaveError);
            } else {
                Task.save(vm.task, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('killfomoApp:taskUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dueBy = false;
        vm.datePickerOpenStatus.externalCreatedAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
