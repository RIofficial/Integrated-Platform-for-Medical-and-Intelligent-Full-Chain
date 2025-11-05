// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract SimpleBlockchainRecord {
    mapping(string => string) public recordTxHashes; // pid => transaction hash

    event RecordUpdated(string pid, string transactionHash, uint256 timestamp);

    function setRecordTxHash(string memory _pid, string memory _transactionHash) public {
        recordTxHashes[_pid] = _transactionHash;
        emit RecordUpdated(_pid, _transactionHash, block.timestamp);
    }

    function getRecordTxHash(string memory _pid) public view returns (string memory) {
        return recordTxHashes[_pid];
    }
}
