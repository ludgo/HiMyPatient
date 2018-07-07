module.exports = function badRequest(err, viewOrRedirect) {

  // Get access to `req` & `res`
  var req = this.req;
  var res = this.res;

  // Log error to console
  this.req._sails.log.verbose('Sent 400 ("Bad Request") response');
  if (err) {
    this.req._sails.log.verbose(err);
  }
  
  // Return custom message
  return res.json(400, { message: "Malformed query on resource." } );
};