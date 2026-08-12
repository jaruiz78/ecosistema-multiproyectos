// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

/**
 * @title EnergyTokenRWA
 * @dev Smart Contract that mints an RWA token representing physical energy dispatch 
 * calculated by PyPSA and validated off-chain.
 */
contract EnergyTokenRWA is ERC721, Ownable {
    uint256 private _nextTokenId;
    
    // Maps a PyPSA proof hash to its token ID to prevent double minting
    mapping(string => uint256) public pypsaProofTokens;

    constructor() ERC721("EnergyRWA", "ERWA") Ownable(msg.sender) {}

    /**
     * @dev Mints a new Energy RWA token based on a PyPSA dispatch proof.
     * @param to The recipient of the token (the producer/consumer).
     * @param pypsaProofHash Cryptographic hash proving the LPOPF calculation.
     */
    function mintEnergyToken(address to, string memory pypsaProofHash) public onlyOwner {
        require(pypsaProofTokens[pypsaProofHash] == 0, "Proof already tokenized");
        
        uint256 tokenId = ++_nextTokenId;
        pypsaProofTokens[pypsaProofHash] = tokenId;
        
        _safeMint(to, tokenId);
    }
}
