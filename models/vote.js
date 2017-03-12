// Dependencies
var mongoose = require('mongoose');

// Schema
var candidateVotesSchema = new mongoose.Schema({
  candidateName: String,
  voteCount: Number
}, {
  versionKey: false
});

// Return model to be accessed from outside
VoteCount = module.exports = mongoose.model('VoteCount', candidateVotesSchema);

// Queries on candidate information

// Create a new entry in the candidate vote table
module.exports.newVote = function(vote, callback) {
  VoteCount.create(vote, callback);
};

// Get total number of votes for the given candidate
module.exports.getVotes = function(callback, candidate) {
  VoteCount.find({ candidateName: candidate }, callback);
};

// Update the vote count for the given candidate
module.exports.updateVote = function(candidateName, options, callback) {
  var query = { candidateName: candidateName };
  var update = { $inc: { voteCount: 1 } };
  VoteCount.findOneAndUpdate(query, update, options, callback);
};