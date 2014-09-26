(function () {
  'use strict';

  // create the angular app
  var openlrs = angular.module('openlrs', ['ngRoute', 'angularCharts', 'openlrs.controllers']);

  // setup dependency injection
  //angular.module('d3', []);
  angular.module('openlrs.controllers', []);
  //angular.module('openlrs.directives', ['d3']);

  openlrs.config(['$routeProvider', function($routeProvider) {}]);

}());
