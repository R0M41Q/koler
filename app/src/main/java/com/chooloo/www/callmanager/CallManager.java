package com.chooloo.www.callmanager;

import android.content.Context;
import android.telecom.Call;
import android.telecom.VideoProfile;

import com.chooloo.www.callmanager.activity.OngoingCallActivity;

import timber.log.Timber;

public class CallManager {

    // Variables
    public static Call sCall;

    // -- Call Actions -- //

    /**
     * Answers incoming call
     */
    public static void sAnswer() {
        if (sCall != null) {
            sCall.answer(VideoProfile.STATE_AUDIO_ONLY);
        }
    }

    /**
     * Ends call
     * If call ended from the other side, disconnects
     */
    public static void sReject() {
        if (sCall != null) {
            if (sCall.getState() == Call.STATE_RINGING) {
                sCall.reject(false, null);
            } else {
                // Try both disconnecting and rejecting
                try {
                    sCall.disconnect();
                } catch (Exception e1) {
                    Timber.e("Couldn't disconnect call (Trying to reject): %s", e1);
                    try {
                        sCall.reject(false, null);
                    } catch (Exception e2) {
                        Timber.e("Couldn't end call: %s", e2);
                    }
                }
            }
        }
    }

    /**
     * Registers a Callback object to the current call
     *
     * @param callback the callback to register
     */
    public static void registerCallback(OngoingCallActivity.Callback callback) {
        if (sCall == null) return;
        sCall.registerCallback(callback);
    }

    /**
     * Unregisters the Callback from the current call
     *
     * @param callback the callback to unregister
     */
    public static void unregisterCallback(Call.Callback callback) {
        if (sCall == null) return;
        sCall.unregisterCallback(callback);
    }

    // -- Getters -- //

    /**
     * Gets the phone number of the contact from the end side of the current call
     * in the case of a voicemail number, returns "Voicemail"
     *
     * @return String - phone number, or voicemail. if not recognized, return null.
     */
    public static Contact getDisplayContact(Context context) {
        if (sCall == null) return Contact.UNKNOWN;
        String uri = sCall.getDetails().getHandle().toString();
        if (uri.contains("voicemail"))
            return Contact.VOICEMAIL;

        String telephoneNumber = null;
        if (uri.contains("tel"))
            telephoneNumber = uri.replace("tel:", "");

        if (telephoneNumber == null || telephoneNumber.isEmpty()) return Contact.UNKNOWN;

        Contact contact = ContactsManager.getContactByPhoneNumber(context, telephoneNumber);
        if (contact == null) return Contact.UNKNOWN;
        return contact;
    }

    /**
     * Gets the current state of the call from the Call object (named sCall)
     *
     * @return Call.State
     */
    public static int getState() {
        if (sCall == null) return Call.STATE_DISCONNECTED;
        return sCall.getState();
    }
}
