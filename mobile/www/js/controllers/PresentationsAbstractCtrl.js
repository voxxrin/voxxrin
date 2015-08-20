'use strict';

angular.module('voxxrin')
    .controller('PresentationsAbstractCtrl', function ($stateParams, $scope, $ionicPopup, Day, Presentation, Reminder) {

        var slotFormat = 'HH[h]mm';
        var emailRegexp = /.+@.+[.].+/;

        var buildPresentationsMap = function (presentations) {
            return _.indexBy(presentations, '_id');
        };

        var buildSlots = function (presentations) {

            var slotIndex = 0;
            var slots = {};
            _.each(presentations, function (prez) {
                var from = moment(prez.from);
                var to = moment(prez.to);
                var slotName = from.format(slotFormat) + '-' + to.format(slotFormat);
                if (!slots[slotName]) {
                    slots[slotName] = {
                        index: slotIndex++,
                        presentations: []
                    };
                }
                prez.slot = {
                    name: slotName,
                    index: (slots[slotName].presentations.length)
                };
                slots[slotName].presentations.push(prez);
            });

            return slots;
        };

        /**
         * Compute model (slots, ids, ...)
         */
        var computeModel = function (presentations) {
            $scope.presentations = buildPresentationsMap(presentations);
            $scope.slots = buildSlots(presentations);
        };

        angular.extend($scope, {
            day: Day.get({id: $stateParams.dayId}),
            reminder: {},
            openReminder: function (presentation) {
                Reminder.save({presentationId: presentation._id})
                    .$promise
                    .then(function () {
                        $ionicPopup.alert({
                            title: 'Prevenez-moi.',
                            template: 'Votre demande a été prise en compte : vous serez notifié quand la' +
                            ' vidéo de cette présentation sera disponible !'
                        });
                    })
                    .catch(function () {
                        $ionicPopup.alert({
                            title: 'Prevenez-moi !',
                            template: 'Vous devez être connecté pour beneficier de cette fonctionnalité'
                        });
                    });
            },
            star: function (presentation) {
                // TODO
            }
        });

        Presentation.fromDay($stateParams.dayId).$promise.then(computeModel);

    });