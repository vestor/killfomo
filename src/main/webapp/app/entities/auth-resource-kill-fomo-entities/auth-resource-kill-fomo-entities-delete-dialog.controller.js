(function() {
    'use strict';

    angular
        .module('killfomoApp')
        .controller('AuthResourceKillFomoEntitiesDeleteController',AuthResourceKillFomoEntitiesDeleteController);

    AuthResourceKillFomoEntitiesDeleteController.$inject = ['$uibModalInstance', 'entity', 'AuthResource'];

    function AuthResourceKillFomoEntitiesDeleteController($uibModalInstance, entity, AuthResource) {
        var vm = this;

        vm.authResource = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AuthResource.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
