/**
 * Bootstrap
 * (sails.config.bootstrap)
 *
 * An asynchronous bootstrap function that runs before your Sails app gets lifted.
 * This gives you an opportunity to set up your data model, run jobs, or perform some special logic.
 *
 * For more information on bootstrapping your app, check out:
 * http://sailsjs.org/#!/documentation/reference/sails.config/sails.config.bootstrap.html
 */

/* global Settings */

module.exports.bootstrap = function (cb) {
  //process.env.TZ = 'Europe/Bratislava';
  process.env.TZ = 'UTC';

  // Load settings (available in ejs local settings, sails.config.views.locals.settings or res.locals.settings)
  // Settings.load(() => {
  //   sails.log.info('Settings loaded successfully.');
  // });

  // It's very important to trigger this callback method when you are finished
  // with the bootstrap!  (otherwise your server will never lift, since it's waiting on the bootstrap)
  cb();
};
