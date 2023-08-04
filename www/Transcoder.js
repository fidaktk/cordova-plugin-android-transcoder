var exec = require('cordova/exec');

var Transcoder = function() {};

Transcoder.transcode = function(inputPath, outputPath, successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'Transcoder', 'transcode', [inputPath, outputPath]);
};

module.exports = Transcoder;
