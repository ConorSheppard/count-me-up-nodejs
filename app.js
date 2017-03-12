// Dependencies
var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
VoteCount = require('./models/vote');
VoterInfo = require('./models/voters');

app.use(bodyParser.json()); // support json encoded bodies
app.use(bodyParser.urlencoded({ extended: true })); // support encoded bodies

// MongoDB
mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost/vote');
var db = mongoose.connection;
var path = require('path');
app.use(express.static(path.join(__dirname, 'public')));
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// Routes

app.get('/', function(req, res, next) {
  var voterId = Math.floor(Math.random() * 100) + 1;
  res.render('index', {
    title: 'Count Me Up',
    voterId: voterId
  });
});

// Increment the vote count for the given candidate by 1
app.post('/vote/:voterId/:candidate', function (req, rs) {
  var voterId = req.params.voterId;
  var candidate = req.params.candidate;
  VoterInfo.getNumVotes(voterId, function (err, voter) {
    var numVotes = voter[0].numVotes;
    // Check if voter has cast 3 votes
    if (numVotes < 3) {
      // Update the number of votes cast for the given voter
      VoterInfo.updateNumVotes(voterId, {}, function (err, resDoc) {
        if (err) throw err;
      });
    } else {
      var errorMessage = "3 votes already cast";
      rs.send(JSON.stringify(errorMessage));
    }
  });
  // Update vote count for the given candidate
  VoteCount.updateVote(candidate, {}, function (err, response) {
    if (err) throw err;
    rs.render('index', {
      title: 'Count Me Up',
      voterId: voterId
    });
    // TODO: Check to see if the user has voted already
  });
});

// Return the number of votes for the given candidate
app.get('/vote/count/:candidate', function (req, res) {
  var candidate = req.params.candidate;
  VoteCount.getVotes(function(err, voteCount) {
    if (err) throw err;
    res.send(JSON.stringify(voteCount));
  }, candidate);
});

// DEBUG: Return the number of votes cast for the given voter
app.get('/votes/cast/:voterId', function (req, res) {
  var voterId = req.params.voterId;
  console.log("voterId: " + voterId);
  VoterInfo.getNumVotes(voterId, function (err, numVotes) {
    console.log("numVotes: " + numVotes);
    res.send(JSON.stringify(numVotes));
  });
});

// Initialize candidate in DB
// Used in InitializeDB.java only
app.post('/new/candidate/:candidateId', function (req, res) {
  var candidateName = req.params.candidateId;
  var newVote = {
    candidateName: candidateName,
    voteCount: 0
  };
  VoteCount.newVote(newVote, function (err, writeResult) {
    if (err) throw err;
    res.json(writeResult);
  });
});

// Initialize voter in DB
// Used in InitializeDB.java only
app.post('/new/voter/:voterId', function (req, res) {
  var voterId = req.params.voterId;
  var voter = {
    voterId: voterId,
    numVotes: 0
  };
  VoterInfo.newVoter(voter, function (err, writeResult) {
    if (err) throw err;
    res.json(writeResult);
  });
});

app.listen(3000);
console.log('Running on port 3000...');
