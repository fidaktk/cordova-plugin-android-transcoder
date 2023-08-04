package cordova.plugin.android.transcoder;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;

import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.otaliastudios.transcoder.engine.TrackType;

import java.io.File;

public class TranscoderPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("transcode")) {
            String inputPath = args.getString(0);
            String outputPath = args.getString(1);
            this.transcode(inputPath, outputPath, callbackContext);
            return true;
        }
        return false;
    }

    private void transcode(String inputPath, String outputPath, CallbackContext callbackContext) {
        Transcoder.into(outputPath)
            .addDataSource(inputPath)
            .start(new TranscoderListener() {
                @Override
                public void onTranscodeProgress(double progress) {
                }

                @Override
                public void onTranscodeCompleted(int successCode) {
                    callbackContext.success("Transcoding completed with code: " + successCode);
                }

                @Override
                public void onTranscodeCanceled() {
                    callbackContext.error("Transcoding was cancelled");
                }

                @Override
                public void onTranscodeFailed(Exception exception) {
                    callbackContext.error("Transcoding failed with error: " + exception.getMessage());
                }
            });
    }
}