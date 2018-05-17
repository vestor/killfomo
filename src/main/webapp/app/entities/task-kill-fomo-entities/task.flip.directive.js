/**
 * Created by manishs on 17/05/18.
 */
(function() {
    'use strict';

    angular
        .module('killfomoApp')
        .filter('millSecondsToTimeString', function() {
            return function(millseconds) {
                var oneSecond = 1000;
                var oneMinute = oneSecond * 60;
                var oneHour = oneMinute * 60;
                var oneDay = oneHour * 24;

                var seconds = Math.floor((millseconds % oneMinute) / oneSecond);
                var minutes = Math.floor((millseconds % oneHour) / oneMinute);
                var hours = Math.floor((millseconds % oneDay) / oneHour);
                var days = Math.floor(millseconds / oneDay);

                var timeString = '';
                if (days !== 0) {
                    timeString += (days !== 1) ? (days + ' days ') : (days + ' day ');
                }
                if (hours !== 0) {
                    timeString += (hours !== 1) ? (hours + ' hours ') : (hours + ' hour ');
                }
                if (minutes !== 0) {
                    timeString += (minutes !== 1) ? (minutes + ' minutes ') : (minutes + ' minute ');
                }
                if (seconds !== 0 || millseconds < 1000) {
                    timeString += (seconds !== 1) ? (seconds + ' seconds ') : (seconds + ' second ');
                }

                return timeString;
            };
        })
        .filter('fdpriority', function() {
            return function (item) {
                return fdMap[item];
            };

        })
        .filter('dueFilter', function() {
            return function (item) {
                var d = new Date();
                return new Date(item).getMilliseconds() - d.getMilliseconds();
            };

        })
        .filter('greaterThanZero', function() {
            return function (item) {
                return item > 0;
            };

        })
        .directive('draggable', function() {
            return function(scope, element, attrs) {
                // Get the native element
                var el = element[0];
                el.draggable = true; // Make dragable

                // Add event listeners
                el.addEventListener(
                    'dragstart',
                    function(e) {
                        e.dataTransfer.effectAllowed = 'move';
                        e.dataTransfer.setData('item_id', this.id);
                        e.dataTransfer.setData('origin_id', el.parentElement.id);
                        this.classList.add('dragging');
                        return false;
                    }, false
                );

                el.addEventListener(
                    'dragend',
                    function(e) {
                        this.classList.remove('dragging');
                        return false;
                    },
                    false
                );
            }
        })
        .directive('droppable', function() {
            return function(scope, element, attrs) {
                // Get the native element
                var el = element[0];

                // Add event listeners
                el.addEventListener(
                    'dragover',
                    function(e) {
                        e.preventDefault(); // Allow the drop

                        // Set effects
                        e.dataTransfer.dropEffect = 'move';
                        this.classList.add('dragover');
                        return false;
                    }, false
                );

                el.addEventListener(
                    'dragenter',
                    function(e) {
                        this.classList.add('dragover');
                        return false;
                    }, false
                );

                el.addEventListener(
                    'dragleave',
                    function(e) {
                        this.classList.remove('dragover');
                        return false;
                    }, false
                );

                el.addEventListener(
                    'drop',
                    function(e) {
                        this.classList.remove('dragover');
                        console.log(this.id);
                        // Get the data
                        var destination = this.id;
                        var item_to_move = e.dataTransfer.getData('item_id');
                        var origin = e.dataTransfer.getData('origin_id');

                        // Call the scope move function
                        scope.MoveItem(origin, destination, item_to_move);

                        return false;
                    }, false
                );
            }
        })
        .directive("front", function() {
            return {
                restrict: "E",
                template: "<div class='front tile' ng-transclude></div>",
                transclude: true
            };
        })
        .directive("back", function() {
            return {
                restrict: "E",
                template: "<div class='back tile' ng-transclude></div>",
                transclude: true
            };
        })
        .controller("flipperDemo", ['$scope',function($scope) {
            $scope.flipped = false;
            $scope.flip = function() {
                $scope.flipped = !$scope.flipped;
            };
        }])
        .directive("flipper", function() {
            return {
                controller: 'flipperDemo',
                restrict: "E",
                template: "<div class='flipper' ng-click='flip()' ng-transclude ng-class='{ flipped: flipped }'></div>",
                transclude: true,
                scope: {
                    flipped: "="
                }
            };
        })


})();

var fdMap = {
    1 : 'Low',
    2 : 'Medium',
    3 : 'High',
    4 : 'Urgent'
};
