package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

import java.math.BigInteger;
import java.util.Map;

public class YOUValidatorsStat extends Response<Map<String, YOUValidatorsStat.Stat>> {

    public Map<String, Stat> getValidatorsStat() {
        return getResult();
    }

    public static class Stat {
        private String onlineToken;
        private String onlineStake;
        private BigInteger onlineCount;
        private String offlineStake;
        private String offlineToken;
        private BigInteger offlineCount;
        private String rewardsResidue;
        private String rewardsTotal;

        public Stat() {
        }

        public Stat(String onlineToken, String onlineStake, BigInteger onlineCount, String offlineStake, String offlineToken, BigInteger offlineCount, String rewardsResidue, String rewardsTotal) {
            this.onlineToken = onlineToken;
            this.onlineStake = onlineStake;
            this.onlineCount = onlineCount;
            this.offlineStake = offlineStake;
            this.offlineToken = offlineToken;
            this.offlineCount = offlineCount;
            this.rewardsResidue = rewardsResidue;
            this.rewardsTotal = rewardsTotal;
        }

        public String getOnlineToken() {
            return onlineToken;
        }

        public void setOnlineToken(String onlineToken) {
            this.onlineToken = onlineToken;
        }

        public String getOnlineStake() {
            return onlineStake;
        }

        public void setOnlineStake(String onlineStake) {
            this.onlineStake = onlineStake;
        }

        public BigInteger getOnlineCount() {
            return onlineCount;
        }

        public void setOnlineCount(BigInteger onlineCount) {
            this.onlineCount = onlineCount;
        }

        public String getOfflineStake() {
            return offlineStake;
        }

        public void setOfflineStake(String offlineStake) {
            this.offlineStake = offlineStake;
        }

        public String getOfflineToken() {
            return offlineToken;
        }

        public void setOfflineToken(String offlineToken) {
            this.offlineToken = offlineToken;
        }

        public BigInteger getOfflineCount() {
            return offlineCount;
        }

        public void setOfflineCount(BigInteger offlineCount) {
            this.offlineCount = offlineCount;
        }

        public String getRewardsResidue() {
            return rewardsResidue;
        }

        public void setRewardsResidue(String rewardsResidue) {
            this.rewardsResidue = rewardsResidue;
        }

        public String getRewardsTotal() {
            return rewardsTotal;
        }

        public void setRewardsTotal(String rewardsTotal) {
            this.rewardsTotal = rewardsTotal;
        }
    }
}