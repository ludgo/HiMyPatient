/**
 * Development environment settings
 *
 * This file can include shared settings for a development team,
 * such as API keys or remote database passwords.  If you're using
 * a version control solution for your Sails app, this file will
 * be committed to your repository unless you add it to your .gitignore
 * file.  If your repository will be publicly viewable, don't add
 * any private information to this file!
 *
 */

module.exports = {
  models: {
    connection: 'dockerPsqlDb'
  },

  baseUrl: process.env.BASE_URL || (process.env.VIRTUAL_HOST ? ('http://' + process.env.VIRTUAL_HOST) : '') || 'http://localhost:8080',

  log: {
    level: process.env.LOGLEVEL || 'verbose'
  }
};
