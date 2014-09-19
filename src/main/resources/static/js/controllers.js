var openLrsControllers = angular.module('openLrsControllers', []);

openLrsControllers.controller('OpenLRSController', function($scope, $http, $window) {
	
	$scope.statements = null;
    $scope.query = '';
	
	// FIXME
	var token = $window.btoa('openlrs:openlrs');
	// END
	$http({
		method  : 'GET',
		url     : '/xAPI/statements',
		headers : { 'Content-Type': 'application/json', 
					'X-Experience-API-Version' : '1.0.1', 
					'Authorization' : 'Basic '+token }
	})
	.then(function (response) {
		$scope.statements = response.data.statements;
	});
});