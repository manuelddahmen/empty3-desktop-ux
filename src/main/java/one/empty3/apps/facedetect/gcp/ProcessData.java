package one.empty3.apps.facedetect.gcp;

import java.util.Map;

public class ProcessData implements Runnable {
    boolean isRunning = true;
    public ProcessData(Map<String, String> data) {
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void run() {
        isRunning = false;
    }
}
