(function() {
    'use strict';

    angular
        .module('killfomoApp')
        .controller('AuthResourceKillFomoEntitiesController', AuthResourceKillFomoEntitiesController);

    AuthResourceKillFomoEntitiesController.$inject = ['AuthResource'];

    function AuthResourceKillFomoEntitiesController(AuthResource) {

        var vm = this;

        vm.authResources = [];

        loadAll();

        function loadAll() {
            AuthResource.query(function(result) {
                vm.authResources = result;
                vm.searchQuery = null;
            });
        }
    }
})();
