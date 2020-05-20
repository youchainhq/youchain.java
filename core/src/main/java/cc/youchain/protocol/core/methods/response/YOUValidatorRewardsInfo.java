package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

public class YOUValidatorRewardsInfo extends Response<YOUValidatorRewardsInfo.RewardsInfo> {

    public RewardsInfo youValidatorRewardsInfo() {
        return getResult();
    }

    public static class RewardsInfo {
        private String settled;
        private String pending;

        public RewardsInfo() {
        }

        public RewardsInfo(String settled, String pending) {

            this.settled = settled;
            this.pending = pending;
        }

        public String getSettled() {
            return settled;
        }

        public void setSettled(String settled) {
            this.settled = settled;
        }

        public String getPending() {
            return pending;
        }

        public void setPending(String pending) {
            this.pending = pending;
        }
    }
}
