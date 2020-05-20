pragma solidity ^0.5.3;

contract arithmetic {

    function arithmetic_add(uint a, uint b) public pure returns(uint d) {
        return a + b;
    }

    function arithmetic_subtract(uint a, uint b) public pure returns(uint d) {
        return a - b;
    }

    function arithmetic_multiply(uint a, uint b) public pure returns(uint d) {
        return a * b;
    }

    function arithmetic_divide(uint a, uint b) public pure returns(uint d) {
        return a / b;
    }
}

// solcjs --bin arithmetic.sol
// solcjs --abi arithmetic.sol
