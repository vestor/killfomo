(function() {
    'use strict';

    angular
        .module('killfomoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('task-kill-fomo-entities', {
            parent: 'entity',
            url: '/task-kill-fomo-entities?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tasks'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/task-kill-fomo-entities/taskskillFomoEntities2.html',
                    controller: 'TaskKillFomoEntitiesController',
                    controllerAs: 'vm',
                    styleUrls: ['./style.css']
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('task-kill-fomo-entities-detail', {
            parent: 'task-kill-fomo-entities',
            url: '/task-kill-fomo-entities/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Task'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/task-kill-fomo-entities/task-kill-fomo-entities-detail.html',
                    controller: 'TaskKillFomoEntitiesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Task', function($stateParams, Task) {
                    return Task.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'task-kill-fomo-entities',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('task-kill-fomo-entities-detail.edit', {
            parent: 'task-kill-fomo-entities-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-kill-fomo-entities/task-kill-fomo-entities-dialog.html',
                    controller: 'TaskKillFomoEntitiesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Task', function(Task) {
                            return Task.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('task-kill-fomo-entities.new', {
            parent: 'task-kill-fomo-entities',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-kill-fomo-entities/task-kill-fomo-entities-dialog.html',
                    controller: 'TaskKillFomoEntitiesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userId: null,
                                subject: null,
                                externalLink: null,
                                type: null,
                                dueBy: null,
                                state: null,
                                externalCreatedAt: null,
                                customJson: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('task-kill-fomo-entities', null, { reload: 'task-kill-fomo-entities' });
                }, function() {
                    $state.go('task-kill-fomo-entities');
                });
            }]
        })
        .state('task-kill-fomo-entities.edit', {
            parent: 'task-kill-fomo-entities',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-kill-fomo-entities/task-kill-fomo-entities-dialog.html',
                    controller: 'TaskKillFomoEntitiesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Task', function(Task) {
                            return Task.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('task-kill-fomo-entities', null, { reload: 'task-kill-fomo-entities' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('task-kill-fomo-entities.delete', {
            parent: 'task-kill-fomo-entities',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-kill-fomo-entities/task-kill-fomo-entities-delete-dialog.html',
                    controller: 'TaskKillFomoEntitiesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Task', function(Task) {
                            return Task.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('task-kill-fomo-entities', null, { reload: 'task-kill-fomo-entities' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
