package practice.d20261013.g;

/**
 * The "charger reports FAILED" flow (status message from SQS). Applies the event to the campaign; if THIS event made the campaign
 * ABORTED, send cancelUpdate(deviceId, campaignId + ":" + deviceId) through the gateway to every device still in NOTIFIED or
 * DOWNLOADING (ascending device id order), exactly once.
 */
public class StatusConsumer {
    public StatusConsumer(Campaign campaign, DeviceGateway gateway) {
        // TODO
    }

    /** Returns whether the event was applied (false = duplicate or illegal, nothing else happens). */
    public boolean handle(String deviceId, DeviceState to) {
        throw new UnsupportedOperationException("TODO");
    }
}
