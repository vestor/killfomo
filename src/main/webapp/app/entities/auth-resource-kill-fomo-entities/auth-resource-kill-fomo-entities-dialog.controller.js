(function() {
    'use strict';

    angular
        .module('killfomoApp')
        .controller('AuthResourceKillFomoEntitiesDialogController', AuthResourceKillFomoEntitiesDialogController);

    AuthResourceKillFomoEntitiesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AuthResource'];

    function AuthResourceKillFomoEntitiesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AuthResource) {
        var vm = this;

        vm.authResource = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.authResource.id !== null) {
                AuthResource.update(vm.authResource, onSaveSuccess, onSaveError);
            } else {
                AuthResource.save(vm.authResource, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('killfomoApp:authResourceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
