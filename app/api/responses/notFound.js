module.exports = function notFound(err, viewOrRedirect) {

  // Get access to `req` & `res`
  var req = this.req;
  var res = this.res;

  // Log error to console
  this.req._sails.log.verbose('Sent 404 ("Not Found") response');
  if (err) {
    this.req._sails.log.verbose(err);
  }
  
  // Return custom message
  return res.json(404, { message: "Resource does not exist." } );
};