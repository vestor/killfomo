(function() {
    'use strict';

    angular
        .module('killfomoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('auth-resource-kill-fomo-entities', {
            parent: 'entity',
            url: '/auth-resource-kill-fomo-entities',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AuthResources'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/auth-resource-kill-fomo-entities/auth-resourceskillFomoEntities.html',
                    controller: 'AuthResourceKillFomoEntitiesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('auth-resource-kill-fomo-entities-detail', {
            parent: 'auth-resource-kill-fomo-entities',
            url: '/auth-resource-kill-fomo-entities/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AuthResource'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/auth-resource-kill-fomo-entities/auth-resource-kill-fomo-entities-detail.html',
                    controller: 'AuthResourceKillFomoEntitiesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'AuthResource', function($stateParams, AuthResource) {
                    return AuthResource.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'auth-resource-kill-fomo-entities',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('auth-resource-kill-fomo-entities-detail.edit', {
            parent: 'auth-resource-kill-fomo-entities-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auth-resource-kill-fomo-entities/auth-resource-kill-fomo-entities-dialog.html',
                    controller: 'AuthResourceKillFomoEntitiesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AuthResource', function(AuthResource) {
                            return AuthResource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('auth-resource-kill-fomo-entities.new', {
            parent: 'auth-resource-kill-fomo-entities',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auth-resource-kill-fomo-entities/auth-resource-kill-fomo-entities-dialog.html',
                    controller: 'AuthResourceKillFomoEntitiesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userId: null,
                                token: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('auth-resource-kill-fomo-entities', null, { reload: 'auth-resource-kill-fomo-entities' });
                }, function() {
                    $state.go('auth-resource-kill-fomo-entities');
                });
            }]
        })
        .state('auth-resource-kill-fomo-entities.edit', {
            parent: 'auth-resource-kill-fomo-entities',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auth-resource-kill-fomo-entities/auth-resource-kill-fomo-entities-dialog.html',
                    controller: 'AuthResourceKillFomoEntitiesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AuthResource', function(AuthResource) {
                            return AuthResource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('auth-resource-kill-fomo-entities', null, { reload: 'auth-resource-kill-fomo-entities' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('auth-resource-kill-fomo-entities.delete', {
            parent: 'auth-resource-kill-fomo-entities',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auth-resource-kill-fomo-entities/auth-resource-kill-fomo-entities-delete-dialog.html',
                    controller: 'AuthResourceKillFomoEntitiesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AuthResource', function(AuthResource) {
                            return AuthResource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('auth-resource-kill-fomo-entities', null, { reload: 'auth-resource-kill-fomo-entities' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
