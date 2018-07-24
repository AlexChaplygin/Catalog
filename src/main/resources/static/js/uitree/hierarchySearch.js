(function () {
    angular.module('mainModule.directives', [])
        .directive('hierarchySearch', function ($http, $rootScope, HierarchyNodeService, $timeout) {

            return {
                restrict: 'E',
                templateUrl: 'js/uitree/hierarchySearch.tpl.html',
                scope: {
                    list: '=',
                    select: '='
                },
                controller: function ($scope) {
                    $scope.numSelected = 0;

                    var dataObj = {};

                    dataObj.name = $scope.name;

                    var response = $http({
                        method: 'GET',
                        url: '/catalog/sectors',
                        contentType: 'application/x-www-form-urlencoded',
                        params: dataObj,
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    }).then(function (response) {
                        $scope.list = response.data;


                        $scope.options = {};

                        $scope.expandNode = function (n, $event) {
                            $event.stopPropagation();
                            n.toggle();
                        }

                        $scope.itemSelect = function (item) {
                            var rootVal = !item.isSelected;
                            HierarchyNodeService.selectChildren(item, rootVal)

                            HierarchyNodeService.findParent($scope.list[0], null, item, selectParent);
                            var s = _.compact(HierarchyNodeService.getAllChildren($scope.list[0], []).map(function (c) {
                                return c.isSelected && !c.items;
                            }));
                            $scope.numSelected = s.length;
                        }

                        $scope.$watch('list', function (nv, ov) {
                            if (!nv) return;

                            $scope.selected = [];

                            angular.forEach(nv, function (rootNode) {

                                var a = HierarchyNodeService.getSelected(rootNode, []);
                                $scope.selected = $scope.selected.concat(HierarchyNodeService.getSelectedArray(a));
                            })

                            $rootScope.$emit('selectedValueEvent', $scope.selected);


                        }, true);

                        function selectParent(parent) {
                            var children = HierarchyNodeService.getAllChildren(parent, []);

                            if (!children) return;
                            children = children.slice(1).map(function (c) {
                                return c.isSelected;
                            });

                            parent.isSelected = children.length === _.compact(children).length;
                            HierarchyNodeService.findParent($scope.list[0], null, parent, selectParent)
                        }

                        $scope.nodeStatus = function (node) {
                            var flattenedTree = getAllChildren(node, []);
                            flattenedTree = flattenedTree.map(function (n) {
                                return n.isSelected
                            });

                            return flattenedTree.length === _.compact(flattenedTree);
                        }
                    })
                },
                link: function (scope, $scope, $rootScope, el, attr) {

                    // scope.$watch('list',function(nv,ov) {
                    //     if(!nv) return;
                    //
                    //     scope.selected = [];
                    //
                    //     angular.forEach(nv, function(rootNode){
                    //
                    //     var a = HierarchyNodeService.getSelected(rootNode,[]);
                    //         scope.selected = scope.selected.concat(HierarchyNodeService.getSelectedArray(a));
                    //     })
                    //
                    //     // $rootScope.$emit('myCustomEvent', 'Data to send');
                    //
                    //
                    // },true);
                }
            }
        })
}).call(this);