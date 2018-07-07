/**
 * logRequest.js
 *
 * @module      :: Policy
 * @description :: "Policy" logging every request
 * @docs        :: http://sailsjs.org/#!/documentation/concepts/Policies
 *
 */
module.exports = function (req, res, next) {
	let socketId;
	if (req.isSocket) {
		socketId = sails.sockets.getId(req);
	}

	sails.log.debug(req.method + ' ' + req.url + ' (' + req.options.controller + '.' + req.options.action + ')', JSON.stringify(req.allParams()));
	sails.log.silly('socket ' + socketId + ', cookie ' + req.signedCookies['sails.sid']);
	return next();
};
