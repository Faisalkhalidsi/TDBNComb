/*
 * This program demonstrates the motion generation by Temporal Deep Belief Net.
 * Even though there are only walking and running motions for training data,
 * TDBN can generate gait transition motions.
 * 
 * Requirements: Java
 * 
 * core.jar is a library from Processing (www.processing.org).
 * Training motions are obtained from CMU database (http://mocap.cs.cmu.edu/).
 */

import org.jetbrains.annotations.NotNull;
import processing.core.PApplet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class demo extends PApplet {

    public static final int BATCH_SIZE = 20;            // originally 20
    public static final int RBM_EPOCH_LIMIT = 250;      // originally 200, also good: 40
    public static final int CRBM_EPOCH_LIMIT = 40;      // originally 40, also good: 10

    public static final String PLOT_DIR = "D:/Tesis/v3/tdbnDemo/fig/selasa/";
    public static final String PLOT_FILENAME = "tdbn";

    public static final int PLOT_EVERY = 1;             // originally 10
    public static final boolean WITH_REGULARIZATION = false;     // originally false
    public static final double REGULARIZATION_CONSTANT = 0.0000001; //originally 0.00001

    public static final boolean WITH_SPARSITY = false;
    public static final double SPARSITY_CONSTANT = 0.000000002; //originally 0.0000002

    public static final int N_HIDDEN_UNIT_RBM = 30;     // originally 30
    public static final int N_HIDDEN_UNIT_CRBM = 15;    // originally 15

    public static final int FRAME_RATE = 30;            // originally 30

    public static final int END_OFFSET = 400;           // originally 350

    public static final int auto = 0; // 1 for on, 0 for off

    public static final double firstMotionCompotition = 1;
    public static final double secondMotionCompotition = 0.6;

    TDBN net;
    PlayerSkel skel;
    int control = 0; // 1 for run, 0 for walk
    String[][] testdata;
    boolean testMode = false;

    // 1 is on, 0 is off
    public static final int partOne = 0;
    public static final int partTwo = 0;
    public static final int partThree = 0;
    public static final int partFour = 0;
    public static final int partFive = 0;

    // motions used in training. First half is walking, and last half is running
    @NotNull
    String[] training_files = {
//            "data/05_10.amc",
//            "data/05_10.amc",
//            "data/35_01.amc",
//            "data/05_10.amc",
//            "data/05_10.amc",
//            "data/35_02.amc"

//            // walk and laugh
//            "data/CMU/laugh/13_14.amc",
//            "data/35_01.amc",
//            "data/CMU/laugh/13_14.amc",
//            "data/35_02.amc"

//            "data/35_01.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/35_02.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/35_01.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/35_02.amc"



//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc"


//            "data/35_01.amc",
//            "data/35_02.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/35_01.amc",
//            "data/35_02.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc"

//            //// water and walk water
////
            "data/CMU/drinkingWater/79_38.amc",
            "data/35_01.amc",
            "data/35_02.amc",
            "data/CMU/drinkingWater/79_38.amc",
            "data/35_01.amc",
            "data/35_02.amc",
            "data/CMU/drinkingWater/79_38.amc",
            "data/35_01.amc"


            //// walk drink water
//
//            "data/35_01.amc",
//            "data/CMU/drinkingWater/79_38.amc",
//            "data/CMU/drinkingWater/79_38.amc",
//            "data/35_02.amc",
//            "data/CMU/drinkingWater/79_38.amc",
//            "data/CMU/drinkingWater/79_38.amc",
//            "data/35_01.amc",
//            "data/CMU/drinkingWater/79_38.amc"


////motorcycle and walk
//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/35_02.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/35_01.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/CMU/motorcycle/91_06.amc",
//            "data/35_01.amc",
//            "data/35_02.amc"
            //////////////////////////////////////////////////




// walk and laugh
//            "data/35_01.amc",
//            "data/35_02.amc",
//            "data/CMU/laugh/13_14.amc",
//            "data/CMU/laugh/13_14.amc"

//      bisa jalan minum
//            "data/35_01.amc",
//            "data/35_02.amc",
//            "data/CMU/drinkSoda/14_37.amc",       //soda
//            "data/CMU/drinkSoda/14_37.amc",       //soda
//            "data/CMU/drinkSoda/14_37.amc",       //soda
//            "data/CMU/drinkSoda/14_37.amc",       //soda
//            "data/CMU/drinkSoda/14_37.amc"



//            "data/CMU/swim/Breast/125_01.amc",
//            "data/CMU/swim/Breast/125_01.amc",
//            "data/CMU/swim/Breast/125_01.amc",
//            "data/CMU/swim/Breast/125_01.amc",
//            "data/CMU/swim/Breast/125_01.amc",
//            "data/CMU/swim/Breast/125_01.amc",
//            "data/CMU/swim/Breast/125_01.amc",
//            "data/CMU/swim/Breast/125_01.amc",
//            "data/CMU/swim/freeStyle/126_11.amc",
//            "data/CMU/swim/Breast/125_01.amc"
//            "data/35_03.amc",
//            "data/35_04.amc",
//            "data/35_05.amc",
//            "data/35_17.amc",
//            "data/35_18.amc",
//            "data/35_20.amc",
//            "data/35_21.amc",
//            "data/35_22.amc"

//            "data/49_04.amc",
//            "data/49_04.amc",
//            "data/49_04.amc",
//            "data/49_04.amc",
//            "data/49_04.amc",
//            "data/49_05.amc",
//            "data/49_05.amc",
//            "data/49_05.amc",
//            "data/49_05.amc",
//            "data/49_05.amc"

//            "data/126_11.amc",
//            "data/126_11.amc",
//            "data/126_11.amc",
//            "data/126_11.amc",
//            "data/126_11.amc",
//            "data/126_11.amc",
//            "data/126_11.amc",
//            "data/126_11.amc",
//            "data/126_11.amc",
//            "data/126_11.amc"
//
//            "data/120_10.amc",
//            "data/120_10.amc",
//            "data/120_10.amc",
//            "data/120_10.amc",
//            "data/120_10.amc"



    };
    double rotX = 0.5;
    double rotY = 0.5;

    private static boolean done = false;

    private static final String fileName = demo.class.getName();
    private final Logger logger = Logger.getLogger(fileName);

    private void setCustomFormatter() {
        Handler[] handlers = Logger.getLogger("").getHandlers();
        for (Handler handler : handlers) {
            handler.setFormatter(new CustomFormatter());
        }
    }

    mocap mc = new mocap();

    int testModeFrame = 0;
    int frame = 0;

    String[][] temp= new String[400][20];

    public void setup() {


        // clean up log dir once
        if (!done) {
            File dir = new File(LoggerSetup.LOG_DIR);
            try {
                //noinspection ConstantConditions
                for (File file : dir.listFiles()) //noinspection ResultOfMethodCallIgnored
                    file.delete();
            } catch (NullPointerException e) {
                logger.warning(e.getMessage());
            }
            done = true;
        }

        LoggerSetup.setFileHandler(logger, fileName);
        setCustomFormatter();   // only once for all handlers
        LoggerSetup.setHandlerLevel(logger, Level.FINE);

        boolean isNetNull = (net == null);
        logger.fine("Start the program!.... is net null?: " + isNetNull);

//        mc.setDrawSkel();

        URL asfBase = null;
        URL amcBase = null;

        try {
            asfBase = this.getClass().getResource("data/35.asf");
            amcBase = this.getClass().getResource("data/05_10.amc");
            logger.fine("amcBase: " + amcBase.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            assert asfBase != null;
            skel = new PlayerSkel(asfBase.getFile());
            mc.setSkel(skel);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            exit();
        }

        int rbmTrainEpoch = RBM_EPOCH_LIMIT;
        int crbmTrainEpoch = CRBM_EPOCH_LIMIT;
        logger.fine("rbmTrainEpoch: "+rbmTrainEpoch+", crbmTrainEpoch: "+crbmTrainEpoch);
        logger.fine("testMode: "+testMode);

        if (testMode) {
            try {
                // for testing the player
                assert amcBase != null;
                testdata = loadAMC(amcBase.getFile());
            } catch (IOException e) {
                e.printStackTrace();
                exit();
            }
        } else if (isNetNull) {
            try {
                ArrayList<URL> bases = new ArrayList<>();
                for (String rec : training_files) {
                    URL base = this.getClass().getResource(rec);
                    bases.add(base);
                }
                net = new TDBN(bases);
                mc.setNet(net);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                exit();
            }

            net.train(rbmTrainEpoch);// train the lower RBMs
            net.train2(crbmTrainEpoch);// train the upper CRBM
            net.initRealtime();

        }

        mc.setDrawSkel();

        size(700, 500, P3D);
        frameRate(FRAME_RATE);
        sphereDetail(5);
        noSmooth();

    }

    public void draw() {
//        println("demo frameRate: "+frameRate);
        background(255);
//        drawHiddenUnits();
        drawBody();


        frame++;
    }

    private void drawBody() {
        // draw body
        stroke(100);
        strokeWeight(10);
        translate(width / 2, 3 * height / 4, 0);

        rotateX((float) (PI / 2.0));
        rotateY((float) (PI / 2.0));
        rotateZ((float) (PI / 2.0));

        translate(0, 200, 0);

        // change the view-angle by mouse drag
        rotateY((float) ((rotY - 0.5) * PI * 1.5));
        rotateZ((float) ((rotX - 0.5) * PI * 1.5));

        fill(255, 0, 0);

        if (testMode) {
            // just play one of training motions
            if (testdata[testModeFrame][0] == null) testModeFrame = 0;
            skel.draw(this, testdata[testModeFrame]);
            testModeFrame++;
        } else {
            // generate each testModeFrame by TDBN
            skel.draw(this, net.stepRealtime(control));
            if(frame < 400){
                temp[frame] = net.stepRealtime(control);
            }else if(frame == 400){
                arrayToText();
            }
        }
    }

    private void arrayToText(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    "D:/Tesis/v3/tdbnDemo/fig/selasa/output.amc"));
            // Write these lines to the file.
            // ... We call newLine to insert a newline character.wr
            for (int i = 0; i < temp.length; i++) {
                // Maybe:
                if(i ==0){
                    writer.write("#!OML:ASF E:/inclinewalkrobotboard/Project1/Captureday1/Session1/subject1.ASF");
                    writer.newLine();
                    writer.write(":FULLY-SPECIFIED");
                    writer.newLine();
                    writer.write(":DEGREES");
                    writer.newLine();
                }

                for (int j = 0; j < temp[i].length; j++) {
                    if(j==0){
                        temp[i][j]=Integer.toString(i+1);
                    }
                    writer.write(temp[i][j]+"");
                    writer.newLine();
                }
            }
            writer.flush();
            writer.close();
            System.out.println("AMC was generated ");
        }catch(Exception e){}
    }

    private void drawHiddenUnits() {
        // draw activity of hidden units
        strokeWeight(1);
        fill(200, 200, 200);
        rect(0, 0, net.outputhidden2.length, 700);
        int y = 20;
        textSize(18);
        fill(0, 0, 0);
        text("control layer", 210, y + 10);
        for (int j = 0; j < net.outputhidden2.length; j++) {
            for (int i = 0; i < net.outputhidden2[j].length; i++) {
                stroke((int) (net.outputhidden2[j][i] * 255));
                point(j, i + y);
            }
        }
        y += net.outputhidden2[0].length + 50;
        fill(0, 0, 0);
        text("hidden layers", 210, y + 70);
        for (int k = 0; k < net.outputhidden.length; k++) {
            for (int j = 0; j < net.outputhidden[k].length; j++)
                for (int i = 0; i < net.outputhidden[k][j].length; i++) {
                    stroke((int) (net.outputhidden[k][j][i] * 255));
                    point(j, i + y);
                }
            y += net.outputhidden[k][0].length + 5;
        }
        y += 40;
        fill(0, 0, 0);
        text("visible layer", 210, y + 30);
        for (int j = 0; j < net.output.length; j++) {
            for (int i = 0; i < net.output[j].length; i++) {
                stroke((int) ((net.output[j][i] * 0.1 + 0.5) * 255));
                point(j, i + y);
            }
        }
    }

    // loads .amc file into a string array
    @NotNull
    private String[][] loadAMC(@NotNull String path) throws IOException {
        BufferedReader amc = new BufferedReader(new FileReader(path));
        String line;
        String data[][] = new String[10000][100];
        int frame = -1;
        int boneCnt = 0;
        while (true) {
            line = amc.readLine();
            if (line == null) break;
            if (line.charAt(0) == '#') continue;
            if (line.charAt(0) == ':') continue;
            if (line.split(" ").length == 1) continue;
            if (line.startsWith("root")) {
                boneCnt = 0;
                frame++;
            }
            data[frame][boneCnt] = line;
            boneCnt++;
        }

        return data;
    }

    public void mouseDragged() {
        rotY = 1.0 * mouseX / width;
        rotX = 1.0 * mouseY / width;
    }

    // change the control value by keyboard
    public void keyPressed() {
        if (key == 'r') {
            logger.info("r dipencet");
            control = 1; // for running
            mc.setControl(1);
        }
        if (key == 'w') {
            logger.info("w dipencet");
            control = 0; // for walking
            mc.setControl(0);
        }
//        if (key == 'p') {
//            logger.info("p dipencet");
//            skel.showRootData();
//            skel.showLhandData("LHand");
//            skel.showRhandData("RHand");
//            skel.showLFootData("LFoot");
//            skel.showRFootData("RFoot");
//            skel.showLHumerusData("LHumerus");
//            skel.showRHumerusData("RHumerus");
//            skel.showLFemurData("LFemur");
//            skel.showRFemurData("RFemur");
//            skel.showLowerbackData("Lowerback");
//            skel.showThoraxData("Thorax");
//            skel.showLowerneckData("Lowerneck");
//            skel.showUpperneckData("Upperneck");
//            skel.measureTotalSmoothness();
//            skel.closeWriter();
//        }
        if (key == 'c') {
            logger.info("c dipendet");
            mc.resetChildApplet();
        }
        if (key == 's') {
            logger.info("s dipendet");
            mc.saveScreenshot();
        }

    }

}
