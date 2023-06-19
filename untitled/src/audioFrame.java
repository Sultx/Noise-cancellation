import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;




public class audioFrame extends JFrame {
    JButton normalSound;
    JButton InvertedSound;
    JButton startBoth;
    JButton sync;
    JButton openMic;
    JLabel normalSoundLabel;
    JLabel InvertedSoundLabel;
    Clip normalClip;
    Clip invertedClip;
    public audioFrame() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.setTitle("Audio Player");



        File song = getFile();
        //File song1 = new File("original.wav");

        normalClip = AudioSystem.getClip();
        normalClip.open(getAudio(song));

        invertedClip = AudioSystem.getClip();
        invertedClip.open(invertAudio(getAudio(song)));



        initLabel();
        initInvertLabel();
        initButtonNormalSound();
        initButtonInvertedSound();
        initStartBoth();
        initSyncButton();
        initOpenMic();

        this.setLayout(null);
        this.add(normalSoundLabel);
        this.add(InvertedSoundLabel);
        this.add(openMic);
        this.add(startBoth);
        this.add(sync);
        this.add(normalSound);
        this.add(InvertedSound);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initInvertLabel() {
        InvertedSoundLabel = new JLabel("Running");
        InvertedSoundLabel.setBounds(325,80,150,80);
        InvertedSoundLabel.setFont(new Font("Comic sans",Font.BOLD,20));
        InvertedSoundLabel.setForeground(Color.red);
        InvertedSoundLabel.setFocusable(false);
        InvertedSoundLabel.setVisible(false);
    }

    private void initLabel() {
        normalSoundLabel = new JLabel("Running");
        normalSoundLabel.setBounds(15,80,150,80);
        normalSoundLabel.setFont(new Font("Comic sans",Font.BOLD,20));
        normalSoundLabel.setForeground(Color.red);
        normalSoundLabel.setFocusable(false);
        normalSoundLabel.setVisible(false);

    }

    private void initStartBoth() {
        startBoth = new JButton("startBoth");
        startBoth.setBounds(165,10,150,80);
        startBoth.setFont(new Font("Comic sans",Font.PLAIN,26));
        startBoth.setBackground(new Color(82,153,124));
        startBoth.setFocusable(false);
        startBoth.setBorder(new LineBorder(Color.BLACK, 2));
        startBoth.addActionListener(e -> {
            invertedClip.start();
            normalClip.start();
            normalSoundLabel.setVisible(true);
            InvertedSoundLabel.setVisible(true);
        });
    }

    private void initOpenMic() {
        openMic = new JButton("Open mic ");
        openMic.setBounds(320,250,150,80);
        openMic.setFont(new Font("Comic sans",Font.PLAIN,26));
        openMic.setBackground(new Color(82,153,124));
        openMic.setFocusable(false);

        openMic.setBorder(new LineBorder(Color.BLACK, 2));

            openMic.addActionListener(e -> {

                try {
                     new MicAndSpeaker();
                } catch (LineUnavailableException exception) {
                    System.out.println("Ex");
                }

            });

    }

    private void initButtonInvertedSound() {
        InvertedSound = new JButton("inverted");
        InvertedSound.setBounds(320,10,150,80);
        InvertedSound.setFont(new Font("Comic sans",Font.PLAIN,26));
        InvertedSound.setBackground(new Color(82,153,124));
        InvertedSound.setFocusable(false);
        InvertedSound.setBorder(new LineBorder(Color.BLACK, 2));
        InvertedSound.addActionListener(e -> {
            if (!invertedClip.isRunning()) {
                invertedClip.start();
                InvertedSoundLabel.setVisible(true);
            }
            else {
                invertedClip.stop();
                InvertedSoundLabel.setVisible(false);
            }
        });
    }
    private void initSyncButton() {

        sync = new JButton("sync ");
        sync.setBounds(10,250,150,80);
        sync.setFont(new Font("Comic sans",Font.PLAIN,26));
        sync.setBackground(new Color(82,153,124));
        sync.setFocusable(false);

        sync.setBorder(new LineBorder(Color.BLACK, 2));


        sync.addActionListener(e -> syncTwoAudios());
    }

    private void initButtonNormalSound() {
        normalSound = new JButton("Normal");

        normalSound.setBounds(10,10,150,80);
        normalSound.setFont(new Font("Comic sans",Font.PLAIN,26));
        normalSound.setBackground(new Color(82,153,124));
        normalSound.setFocusable(false);


        normalSound.setBorder(new LineBorder(Color.BLACK, 2));
        normalSound.addActionListener(e -> {
            if (!normalClip.isRunning()) {
                normalClip.start();
                normalSoundLabel.setVisible(true);
            }
            else {
                normalClip.stop();
                normalSoundLabel.setVisible(false);
            }
        });
    }


    private File getFile() {
        String userHomeFolder = System.getProperty("user.home");
        JFileChooser jFileChooser = new JFileChooser();

        jFileChooser.setCurrentDirectory(new File(userHomeFolder +"\\desktop"));
        jFileChooser.setFileFilter(new FileNameExtensionFilter(".wav", "wav"));


        int action = jFileChooser.showOpenDialog(this);
        if (action == JFileChooser.APPROVE_OPTION) {
            return jFileChooser.getSelectedFile();

        }

        return new File("");
    }

    public static AudioInputStream getAudio(File song) throws IOException, UnsupportedAudioFileException {
        return AudioSystem.getAudioInputStream(song);
    }


    /*
    This function reads the input data into a byte array using a while loop, and then inverts the data by manipulating
    the individual bytes in the array using the bitwise NOT operator (~).
    this function may still not work for all audio formats and may need to be further modified.
    */
    public static AudioInputStream invertAudio(AudioInputStream audioInputStream) throws IOException {
        // Get the audio format and length of the input stream
        AudioFormat format = audioInputStream.getFormat();
        long length = audioInputStream.getFrameLength();

        // Read the input audio data into a byte array
        byte[] inputData = new byte[(int) (length * format.getFrameSize())];
        int bytesRead = 0;
        while (bytesRead < inputData.length) {
            int result = audioInputStream.read(inputData, bytesRead, inputData.length - bytesRead);
            if (result == -1) {
                break;
            }
            bytesRead += result;
        }

        // Invert the audio data by manipulating the byte array
        for (int i = 0; i < inputData.length; i += format.getFrameSize()) {
            for (int j = 0; j < format.getFrameSize(); j++) {
                inputData[i + j] = (byte) ~inputData[i + j];
            }
        }

        // Create a new ByteArrayInputStream from the inverted audio data
        ByteArrayInputStream invertedStream = new ByteArrayInputStream(inputData);

        // Return a new AudioInputStream from the inverted data and format
        return new AudioInputStream(invertedStream, format, length);
    }

    public void syncTwoAudios(){
        if (!(normalClip.isRunning() && invertedClip.isRunning())){
            JOptionPane.showMessageDialog(null,"Both clips have to be Running","",JOptionPane.WARNING_MESSAGE);
            return;
        }

        int frame = normalClip.getFramePosition();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                normalClip.setFramePosition(frame);
            }
        };
        TimerTask timerTask2 = new TimerTask(){
            @Override
            public void run() {
                invertedClip.setFramePosition(frame);
            }
        };

        new Thread(() -> timer.schedule(timerTask,0)).start();
        new Thread(() -> timer.schedule(timerTask2,0)).start();
    }

}
