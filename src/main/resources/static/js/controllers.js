(function () {
  'use strict';

  angular.module('openlrs.controllers')
    .controller('OpenLRSController', ['$scope', '$http', '$window', function($scope, $http, $window){
    	$scope.statements = null;
    	$scope.data = {};
    	$scope.activeStatement = null;
        $scope.query = '';
        
        $scope.reset = function () {
        	$scope.query = '';
        	$scope.activeStatement = null;
        };
        
        $scope.expandStatement = function (statement) {
        	//console.log(statement);
        	$scope.activeStatement = statement;
        };
    	
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
    		$scope.data.data =	_.chain($scope.statements)
    			.groupBy(function(value){
    				var student = 'unknown';
    				if (value.actor) {
    					if (value.actor.mbox) {
    						student = value.actor.mbox;
    					}
    				}
    				
    				return student;
    			})
    			.map(function(value,key){
    				var numberOfStatements = 0;
    				if (value && value != null) {
    					numberOfStatements = value.length;
    				}
    				
    				return {
    					x:key,
    					y: [numberOfStatements]
    				}
    			})
    			.value();
    			
    		//console.log($scope.data.data);
    	});
    	
    	
		$scope.config = {
		    tooltips: false,
		    labels: true,
		    mouseover: function() {},
		    mouseout: function() {},
		    click: function(d) {
		    	if (d) {
		    		$scope.query = d.data.x;
		    	}    	
		    },
		    legend: {
		      display: false,
		      //could be 'left, right'
		      position: 'left'
		    }
		  };
}]);

}());
