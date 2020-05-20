package cc.youchain.protocol.core.methods.response;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class Validator {

    private String mainAddress;
    private String name;
    private String operatorAddress;
    private String coinbase;
    private String mainPubKey;
    private String blsPubKey;
    private String token;
    private String stake;
    private BigInteger status;
    private BigInteger role;
    private boolean expelled;
    private BigInteger expelExpired;
    private BigInteger lastInactive;
    private String selfToken;
    private String selfStake;
    private String rewardsDistributable;
    private String rewardsTotal;
    private BigInteger rewardsLastSettled;
    private BigInteger acceptDelegation;
    private BigInteger commissionRate;
    private BigInteger riskObligation;
    private List<Delegation> delegations;
    private Map ext;

    public Validator() {
    }

    public Validator(String mainAddress, String name, String operatorAddress, String coinbase, String mainPubKey, String blsPubKey, String token, String stake, BigInteger status, BigInteger role, boolean expelled, BigInteger expelExpired, BigInteger lastInactive, String selfToken, String selfStake, String rewardsDistributable, String rewardsTotal, BigInteger rewardsLastSettled, BigInteger acceptDelegation, BigInteger commissionRate, BigInteger riskObligation, List<Delegation> delegations, Map ext) {
        this.mainAddress = mainAddress;
        this.name = name;
        this.operatorAddress = operatorAddress;
        this.coinbase = coinbase;
        this.mainPubKey = mainPubKey;
        this.blsPubKey = blsPubKey;
        this.token = token;
        this.stake = stake;
        this.status = status;
        this.role = role;
        this.expelled = expelled;
        this.expelExpired = expelExpired;
        this.lastInactive = lastInactive;
        this.selfToken = selfToken;
        this.selfStake = selfStake;
        this.rewardsDistributable = rewardsDistributable;
        this.rewardsTotal = rewardsTotal;
        this.rewardsLastSettled = rewardsLastSettled;
        this.acceptDelegation = acceptDelegation;
        this.commissionRate = commissionRate;
        this.riskObligation = riskObligation;
        this.delegations = delegations;
        this.ext = ext;
    }

    public String getMainAddress() {
        return mainAddress;
    }

    public void setMainAddress(String mainAddress) {
        this.mainAddress = mainAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperatorAddress() {
        return operatorAddress;
    }

    public void setOperatorAddress(String operatorAddress) {
        this.operatorAddress = operatorAddress;
    }

    public String getCoinbase() {
        return coinbase;
    }

    public void setCoinbase(String coinbase) {
        this.coinbase = coinbase;
    }

    public String getMainPubKey() {
        return mainPubKey;
    }

    public void setMainPubKey(String mainPubKey) {
        this.mainPubKey = mainPubKey;
    }

    public String getBlsPubKey() {
        return blsPubKey;
    }

    public void setBlsPubKey(String blsPubKey) {
        this.blsPubKey = blsPubKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStake() {
        return stake;
    }

    public void setStake(String stake) {
        this.stake = stake;
    }

    public BigInteger getStatus() {
        return status;
    }

    public void setStatus(BigInteger status) {
        this.status = status;
    }

    public BigInteger getRole() {
        return role;
    }

    public void setRole(BigInteger role) {
        this.role = role;
    }

    public boolean isExpelled() {
        return expelled;
    }

    public void setExpelled(boolean expelled) {
        this.expelled = expelled;
    }

    public BigInteger getExpelExpired() {
        return expelExpired;
    }

    public void setExpelExpired(BigInteger expelExpired) {
        this.expelExpired = expelExpired;
    }

    public BigInteger getLastInactive() {
        return lastInactive;
    }

    public void setLastInactive(BigInteger lastInactive) {
        this.lastInactive = lastInactive;
    }

    public String getSelfToken() {
        return selfToken;
    }

    public void setSelfToken(String selfToken) {
        this.selfToken = selfToken;
    }

    public String getSelfStake() {
        return selfStake;
    }

    public void setSelfStake(String selfStake) {
        this.selfStake = selfStake;
    }

    public String getRewardsDistributable() {
        return rewardsDistributable;
    }

    public void setRewardsDistributable(String rewardsDistributable) {
        this.rewardsDistributable = rewardsDistributable;
    }

    public String getRewardsTotal() {
        return rewardsTotal;
    }

    public void setRewardsTotal(String rewardsTotal) {
        this.rewardsTotal = rewardsTotal;
    }

    public BigInteger getRewardsLastSettled() {
        return rewardsLastSettled;
    }

    public void setRewardsLastSettled(BigInteger rewardsLastSettled) {
        this.rewardsLastSettled = rewardsLastSettled;
    }

    public BigInteger getAcceptDelegation() {
        return acceptDelegation;
    }

    public void setAcceptDelegation(BigInteger acceptDelegation) {
        this.acceptDelegation = acceptDelegation;
    }

    public BigInteger getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigInteger commissionRate) {
        this.commissionRate = commissionRate;
    }

    public BigInteger getRiskObligation() {
        return riskObligation;
    }

    public void setRiskObligation(BigInteger riskObligation) {
        this.riskObligation = riskObligation;
    }

    public List<Delegation> getDelegations() {
        return delegations;
    }

    public void setDelegations(List<Delegation> delegations) {
        this.delegations = delegations;
    }

    public Map getExt() {
        return ext;
    }

    public void setExt(Map ext) {
        this.ext = ext;
    }

    public static class Extension {
        private String version;
        private String data;

        public Extension() {
        }

        public Extension(String version, String data) {
            this.version = version;
            this.data = data;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static class Delegation {
        private String delegator;
        private BigInteger stake;
        private BigInteger token;
        public Delegation(){}
        public Delegation(String delegator, BigInteger stake, BigInteger token) {
            this.delegator = delegator;
            this.stake = stake;
            this.token = token;
        }

        public String getDelegator() {
            return delegator;
        }

        public void setDelegator(String delegator) {
            this.delegator = delegator;
        }

        public BigInteger getStake() {
            return stake;
        }

        public void setStake(BigInteger stake) {
            this.stake = stake;
        }

        public BigInteger getToken() {
            return token;
        }

        public void setToken(BigInteger token) {
            this.token = token;
        }
    }
}
