/**
 * @type {Object}
 * @namespace PatientController
 */
module.exports = {
  findList: function (req, res) {
  	Patient.find({select: ['id', 'first_name', 'last_name', 'image_url', 'blood_type']})
    .exec(function(err, results) {
      return res.json(results);
    });
  }
};
