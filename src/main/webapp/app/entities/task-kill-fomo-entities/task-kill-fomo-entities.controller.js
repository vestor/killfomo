(function() {
    'use strict';

    angular
        .module('killfomoApp')
        .controller('TaskKillFomoEntitiesController', TaskKillFomoEntitiesController);

    TaskKillFomoEntitiesController.$inject = ['$scope', '$state', 'DataUtils', 'Task', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function TaskKillFomoEntitiesController($scope, $state, DataUtils, Task, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        $scope.MoveItem = function(origin, dest, item_id) {
            // Check if dropped in origin
            if (origin == dest) return;

            console.log(origin);
            // Find item in origin array
            for (var i = 0; i < vm.parts[origin].length; i++) {
                if (vm.parts[origin][i].id == item_id) {
                    // Splice the item from the origin array
                    var item = vm.parts[origin].splice(i, 1);
                    // Push to the destination array
                    vm.parts[dest].push(item[0]);
                    item[0].state = dest;
                    vm.isSaving = true;
                    Task.update(item[0], onSaveSuccess, onSaveError);
                    // End loop
                    break;
                }
            }

            // Update UI
            $scope.$apply();
        };

        function onSaveSuccess (result) {
            $scope.$emit('killfomoApp:taskUpdate', result);
            // $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        function loadAll () {
            Task.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.tasks = data;
                vm.page = pagingParams.page;
                vm.parts = {'TODO' : [], 'WIP' : [], 'DONE' : []};

                for(var counter in vm.tasks) {
                    var mtask  = vm.tasks[counter];
                    if(mtask.state == 'TODO') {
                        vm.parts.TODO.push(mtask);
                    } else if (mtask.state == 'WIP') {
                        vm.parts.WIP.push(mtask)
                    } else if (mtask.state == 'DONE') {
                        vm.parts.DONE.push(mtask);
                    }
                }

                console.log(vm.parts);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
