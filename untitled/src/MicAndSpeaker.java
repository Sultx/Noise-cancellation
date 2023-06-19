import javax.sound.sampled.*;


public class MicAndSpeaker {
    AudioFormat audioFormat;
    SourceDataLine sourceLine;
    DataLine.Info sourceInfo;
    TargetDataLine targetLine;
    DataLine.Info targetInfo;
    boolean isRunning = true;
    byte[] data;

    public MicAndSpeaker() throws LineUnavailableException {
        audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,90000,16,2,4,60000,false);

        sourceInfo = new DataLine.Info(SourceDataLine.class,audioFormat);
        sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
        sourceLine.open();



        targetInfo = new DataLine.Info(TargetDataLine.class,audioFormat);
        targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
        targetLine.open();
        new Thread(this.readFromMic()).start();
        new Thread(this.writeToSpeaker()).start();
    }
    public Runnable readFromMic(){

        return () -> {
            targetLine.start();

            data = new byte[1024*256];

            while (isRunning) {
                targetLine.read(data, 0, data.length);
            }
        };

    }
    public Runnable writeToSpeaker(){
        return () -> {
            sourceLine.start();

            while(isRunning){
                sourceLine.write(data, 0, data.length);
            }

        };
    }


    public static void invertMicAudio(byte[] inputData){

        for (int i = 0; i < inputData.length; i += 4) {
            for (int j = 0; j < 4; j++) {
                inputData[i + j] = (byte) ~inputData[i + j];
            }
        }
    }


}
