module.exports = function serverError(err, viewOrRedirect) {

  // Get access to `req` & `res`
  var req = this.req;
  var res = this.res;

  // Log error to console
  this.req._sails.log.verbose('Sent 500 ("Server Error") response');
  if (err) {
    this.req._sails.log.verbose(err);
  }
  
  // Return custom message
  return res.json(500, { message: "Internal server error." } );
};