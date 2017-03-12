// Dependencies
var mongoose = require('mongoose');

// Schema
var voterSchema = new mongoose.Schema({
  voterId: String,
  numVotes: Number
}, {
  versionKey: false
});

// Return model to be accessed from outside
VoterInfo = module.exports = mongoose.model('VoterInfo', voterSchema);

// Queries on voter information

// Create a new entry in the voter table
module.exports.newVoter = function(voter, callback) {
  VoterInfo.create(voter, callback);
};

// Get total number of votes made for given voterId
module.exports.getNumVotes = function(voterId, callback) {
  VoterInfo.find({ voterId: voterId }, callback);
};

// Increment vote count for given voterId
module.exports.updateNumVotes = function (voterId, newNumVotes, options, callback) {
  var query = { voterId: voterId };
  var update = { numVotes: newNumVotes };
  VoterInfo.findOneAndUpdate(query, update, options, callback);
};