const SimpleBlockchainRecord = artifacts.require("SimpleBlockchainRecord");

module.exports = function (deployer) {
  deployer.deploy(SimpleBlockchainRecord);
};