package cordova.plugin.android.transcoder;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;

import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.otaliastudios.transcoder.engine.TrackType;

public class Transcoder extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("transcode")) {
            this.transcode(args, callbackContext);
            return true;
        }
        return false;
    }

    private void transcode(JSONArray args, CallbackContext callbackContext) {
        try {
            String inputPath = args.getString(0);
            String outputPath = args.getString(1);

            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        boolean result = Transcoder.into(outputPath)
                                .addDataSource(inputPath)
                                .setListener(new TranscoderListener() {
                                    public void onTranscodeProgress(double progress) {}

                                    public void onTranscodeCompleted(int successCode) {
                                        if (Transcoder.SUCCESS_TRANSCODED == successCode) {
                                            callbackContext.success();
                                        } else if (Transcoder.SUCCESS_NOT_NEEDED == successCode) {
                                            callbackContext.error("Transcoding not needed");
                                        }
                                    }

                                    public void onTranscodeCanceled() {
                                        callbackContext.error("Transcoding cancelled");
                                    }

                                    public void onTranscodeFailed(@NonNull Throwable exception) {
                                        callbackContext.error(exception.getMessage());
                                    }
                                })
                                .transcode();

                        if (!result) {
                            callbackContext.error("Transcoder not available");
                        }
                    } catch (Exception e) {
                        callbackContext.error("Transcoding error: " + e.getMessage());
                    }
                }
            });
        } catch (JSONException e) {
            callbackContext.error("Invalid arguments: " + e.getMessage());
        }
    }
}
