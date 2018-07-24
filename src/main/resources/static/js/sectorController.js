mainModule.controller('sectorController', function ($scope, $http, $rootScope, $localStorage, $timeout, HierarchyNodeService) {

    console.log('sectorController: ');

    $scope.users = {};
    $scope.users.availableUsers = [];
    $scope.users.selectedUser = null;
    $scope.selectDisabled = false;
    $scope.agreeChebox = false;
    $scope.selectedValue = [];

    findUsers();

    function findUsers() {
        var dataObj = {};

        dataObj.name = $scope.name;

        var response = $http({
            method: 'GET',
            url: '/catalog/users',
            contentType: 'application/x-www-form-urlencoded',
            params: dataObj,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).then(function (response) {
            $scope.users.availableUsers = response.data;
        });
    };

    $scope.hasSubmit = function () {
        if ($scope.agreeChebox && $scope.users.selectedUser && $scope.selectedValue.length > 0) {
            return false;
        }
        return true;
    };

    $scope.submit = function () {
        console.log("submit action");

        var dataObj = {};
        dataObj.user = {};

        dataObj.sectorIds = $scope.selectedValue.toString().replace("[", "").replace("]", "");
        var response = null;

        if ($scope.users.selectedUser.id) {
            dataObj.user = $scope.users.selectedUser;
            response = $http.put('/catalog/update', dataObj, {
                headers: {'Content-Type': 'application/json', 'Authorization': $localStorage.token.token}
            });
        } else {
            dataObj.user.name = $scope.users.selectedUser;
            dataObj.user.agree = $scope.agreeChebox;
            response = $http.put('/catalog/save', dataObj);
        }

        response.success(function (data) {
            console.log("data   " + data);

            if (data.token) {
                $scope.selectDisabled = true;
                $localStorage.token = data.token;
            }

            $scope.data = data;


        });
        response.error(function (data, status, headers, config) {

        });

    };

    $scope.getUsers = function (search) {
        var newUsers = $scope.users.availableUsers.slice();
        if (search && newUsers.indexOf(search) === -1) {
            newUsers.unshift(search);
        }
        return newUsers;
    };

    $rootScope.$on('selectedValueEvent', function (event, data) {
        console.log(data);

        $scope.selectedValue = data;
    });

    $scope.changeName = function () {

        if (!$scope.users.selectedUser || !$scope.users.selectedUser.id) {
            return;
        }

        if ($scope.users.selectedUser) {

            var dataObj = {};

            dataObj.user = $scope.users.selectedUser;

            var response = $http({
                method: 'GET',
                url: '/catalog/get/' + $scope.users.selectedUser.id,
                contentType: 'application/x-www-form-urlencoded',
                params: dataObj,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(function (response) {

                if (response.data.tokenObject) {
                    $scope.selectDisabled = true;
                    $localStorage.token = response.data.tokenObject;
                }

                $scope.agreeChebox = response.data.agree;

                $scope.baseList = response.data.sectors;
                $scope.list = $scope.baseList;
            });
        }
    };

}).directive('indeterminateCheckbox', function (HierarchyNodeService) {
    return {
        restrict: 'A',
        scope: {
            node: '='
        },
        link: function (scope, element, attr) {

            scope.$watch('node', function (nv) {

                var flattenedTree = HierarchyNodeService.getAllChildren(scope.node, []);
                flattenedTree = flattenedTree.map(function (n) {
                    return n.isSelected
                });
                var initalLength = flattenedTree.length;
                var compactedTree = _.compact(flattenedTree);

                var r = compactedTree.length > 0 && compactedTree.length < flattenedTree.length;
                element.prop('indeterminate', r);

            }, true);

        }
    }
});