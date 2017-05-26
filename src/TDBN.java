import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

class TDBN {
    Random rand;

    RBM[] rbm1;
    CRBM rbm2;

    dataset ds;

    int degree = 3;
    int step = 3;

    double[][] output;
    double[][] outputB;
    double[][][] outputhidden;
    double[][] outputhidden2;

    private static final String fileName = TDBN.class.getName();
    private static final Logger logger = Logger.getLogger(TDBN.class.getName());

    PrintWriter writer = null;
    ArrayList<String> eLines = new ArrayList<>();
    ArrayList<String> reLines = new ArrayList<>();

    public TDBN(ArrayList<URL> files) throws IOException {

        LoggerSetup.setFileHandler(logger, fileName);
        LoggerSetup.setHandlerLevel(logger, Level.FINE);

        logger.fine("files.size():" + files.size());

        ds = new dataset(files);
        rand = new Random();

        boneGroup[] bodyParts = ds.bodyParts;
        int nBodyParts = bodyParts.length;
        logger.fine("nBodyParts: " + nBodyParts);

        logger.fine("Initialize RBMs...!");
        rbm1 = new RBM[nBodyParts];

        for (int i = 0; i < rbm1.length; i++)
            rbm1[i] = new RBM(ds.bodyParts[i].DOF, true,
                    demo.N_HIDDEN_UNIT_RBM, false);

        logger.fine("Initialize CRBM...!");
        int hn = 0;
        for (RBM aRbm1 : rbm1) hn += aRbm1.hiddenLayer.size();

        rbm2 = new CRBM(
                hn
                , false,
                demo.N_HIDDEN_UNIT_CRBM, false, degree);

        for (int i = 0; i < rbm2.hiddenLayer.size(); i++)
            rbm2.hiddenLayer.get(i).fixed = true;
    }


    public void train(int train_epoch) {

        logger.finer("train_epoch: " + train_epoch);

        DescriptiveStatistics mean = new DescriptiveStatistics();
        int mode, chosen;
        double means=0;
        setPlotOutputFile();

        for (int epoch = 0; epoch < train_epoch; epoch++) {

            double energy = 0;
            double reconError = 0;

            if (epoch % 10 == 0)
                for (RBM aRbm1 : rbm1) aRbm1.hiddenLayer.temp *= 0.99;

            if (epoch > 5)
                for (RBM aRbm1 : rbm1) aRbm1.mom = 0.9;

            int totalFrameLength = ds.getTotalFrameLength();
            for (int t = 0; t < totalFrameLength; t++) {
                mode = rand.nextInt(ds.modeNum); // inputFile index
                chosen = rand.nextInt(ds.getFrameLength(mode));
                initMode(mode, chosen);
                int groupData=0;
                int[] motion1 = {0,3,6};
                int[] motion2 = {1,2,4,5,7};

                for (int j=0;j<motion1.length;j++){
                    if(motion1[j]==mode){
                        groupData = 100;
                    }
                }

                for (int j=0;j<motion2.length;j++){
                    if(motion2[j]==mode){
                        groupData =200;
                    }
                }

                for (int ii=0; ii<rbm1.length;ii++){
                    rbm1[ii].vhEdges.keg = groupData;
                    rbm1[ii].hBias.keg = groupData;
                    rbm1[ii].vBias.keg = groupData;

                    rbm1[ii].vhEdges.index = ii;
                    rbm1[ii].hBias.index =ii;
                    rbm1[ii].vBias.index =ii;

                    if((groupData!=100)&&(groupData!=200)){
                        System.out.println("eeor");
                    }
                    double[] returnee = rbm1[ii].train();
                    energy += returnee[0];
                    reconError += returnee[1];
                }

//                for (RBM aRbm1 : rbm1) {
//                    double[] returnee = aRbm1.train();
//                    energy += returnee[0];
//                    reconError += returnee[1];
//                }
                //add 30 oct
                //mean.addValue(energy);
            }

            if (epoch % demo.PLOT_EVERY == 0) {
                logger.fine("epoch: " + epoch + ", energy: " + energy);
                mean.addValue(energy);
                assert writer != null;
            }
        }
    }

    private void setPlotOutputFile() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("HH_mm_ss");
            String currTime = sdf.format(cal.getTime());
            writer = new PrintWriter(demo.PLOT_DIR + demo.PLOT_FILENAME + "_" +
                     currTime + ".out", "UTF-8");
        } catch (IOException e) {
            logger.warning(e.getMessage());
            logger.warning("writer is a null");
        }

        // write metadata
        writer.println("# MetaData");
        writer.println("# BATCH_SIZE: " + demo.BATCH_SIZE);
        writer.println("# RBM_EPOCH_LIMIT: " + demo.RBM_EPOCH_LIMIT);
        writer.println("# CRBM_EPOCH_LIMIT: " + demo.CRBM_EPOCH_LIMIT);
        writer.println("# PLOT_EVERY: " + demo.PLOT_EVERY);
        writer.println("# WITH_REGULARIZATION: " + demo.WITH_REGULARIZATION);
        writer.println("# REGULARIZATION_CONSTANT: " + demo.REGULARIZATION_CONSTANT);
        writer.println("# WITH_SPARSITY: "+ demo.WITH_SPARSITY);
        writer.println("# SPARSITY_CONSTANT: "+ demo.SPARSITY_CONSTANT);
        writer.println("# N_HIDDEN_UNIT_RBM: "+ demo.N_HIDDEN_UNIT_RBM);
        writer.println("# N_HIDDEN_UNIT_CRBM: "+ demo.N_HIDDEN_UNIT_CRBM);
        writer.println("# FRAME_RATE: "+ demo.FRAME_RATE);
        writer.println("# ");

    }

    public void train2(int train_epoch) {
        int mode, chosen;
        for (RBM aRbm1 : rbm1) aRbm1.hiddenLayer.temp = 0.6;

        eLines.clear();
        reLines.clear();
        int epoch = 0;
        while (epoch < train_epoch) {
//            logger.fine("epoch " + epoch);

            double energy = 0;
            double reconError = 0;

            if (epoch > 5) rbm2.mom = 0.9;

            if (epoch % 2 == 0) {
                rbm2.visibleLayer.temp *= 0.99;
                rbm2.hiddenLayer.temp *= 0.99;
            }

            for (int t = 0; t < ds.getTotalFrameLength(); t++) {
                mode = rand.nextInt(ds.modeNum);
                chosen = rand.nextInt(ds.getFrameLength(mode) - step * (degree));
                initMode2(mode, chosen);
                double[] returnee = rbm2.train();
                energy += returnee[0];
                reconError += returnee[1];
            }
            logger.fine("epoch: " + epoch + ", energy: " + energy);

            String se = epoch + " " + energy;
            String sre = epoch + " " + reconError;
            eLines.add(se);
            reLines.add(sre);

            epoch++;
        }
    }

    public void initRealtime() {
        for (RBM aRbm1 : rbm1) aRbm1.hiddenLayer.temp = 0.6;
        rbm2.visibleLayer.temp = 0.6;
        rbm2.hiddenLayer.temp = 0.6;
        int recordLen = 200;
        output = new double[recordLen][ds.getDOF()];
        outputB = new double[1][ds.getDOF()];
        outputhidden = new double[rbm1.length][][];
        for (int i = 0; i < rbm1.length; i++) {
            outputhidden[i] = new double[recordLen][rbm1[i].hiddenLayer.size()];
        }
        outputhidden2 = new double[recordLen][rbm2.hiddenLayer.size()];

        initMode2(1, 0);
    }

    @NotNull
    public String[] stepRealtime(double control) {
        for (int i = 0; i < rbm2.hiddenLayer.size(); i++) {
            rbm2.hiddenLayer.get(i).val = control;
        }

        rbm2.vUpdate(false);

        transferValuesDown();
        for (RBM aRbm1 : rbm1) aRbm1.vUpdate(false);


        for (int frame = outputhidden2.length - 1; frame > 0; frame--) {
            System.arraycopy(output[frame - 1], 0, output[frame], 0, output[frame].length);
            for (double[][] anOutputhidden : outputhidden)
                System.arraycopy(anOutputhidden[frame - 1], 0, anOutputhidden[frame], 0, anOutputhidden[frame].length);
            System.arraycopy(outputhidden2[frame - 1], 0, outputhidden2[frame], 0, outputhidden2[frame].length);
        }

        int b = 0;
        for (RBM aRbm1 : rbm1) {
            int j = 0;
            while (j < aRbm1.visibleLayer.size()) {
                output[0][b] = aRbm1.visibleLayer.get(j).val;
                outputB[0][b] = aRbm1.visibleLayer.get(j).val;
                j++;
                b++;
            }
        }
        for (int i = 0; i < rbm1.length; i++)
            for (int j = 0; j < rbm1[i].hiddenLayer.size(); j++)
                outputhidden[i][0][j] = rbm1[i].hiddenLayer.get(j).val;

        for (int i = 0; i < rbm2.hiddenLayer.size(); i++)
            outputhidden2[0][i] = rbm2.hiddenLayer.get(i).val;


        rbm2.passValues();
        StringBuilder sb = new StringBuilder();
        ds.printAMC(outputB, sb);

        return sb.toString().split("\n");
    }

    public void initMode(int mode, int chosen) {
        for (int i = 0; i < rbm1.length; i++)
            for (int vnum = 0; vnum < rbm1[i].visibleLayer.size(); vnum++)
                rbm1[i].visibleLayer.get(vnum).val = ds.getNormVal(i, mode, chosen, vnum);
    }

    public void initMode2(int mode, int chosen) {
        for (int k = 0; k < degree + 1; k++) {
            rbm2.passValues();
            initMode(mode, chosen);

            for (RBM aRbm1 : rbm1) aRbm1.hUpdate(false);

            transferValuesUp();
            chosen += step;
        }

        for (int i = 0; i < rbm2.hiddenLayer.size(); i++)
            rbm2.hiddenLayer.get(i).val = 0;
    }

    void transferValuesUp() {
        int n = 0;
        for (RBM aRbm1 : rbm1) {
            int i = 0;
            while (i < aRbm1.hiddenLayer.size()) {
                rbm2.visibleLayer.get(n).val = aRbm1.hiddenLayer.get(i).val;
                n++;
                i++;
            }
        }
    }

    void transferValuesDown() {
        int n = 0;
        for (RBM aRbm1 : rbm1) {
            for (int i = 0; i < aRbm1.hiddenLayer.size(); i++) {
                aRbm1.hiddenLayer.get(i).val = rbm2.visibleLayer.get(n).val;
                n++;
            }
        }
    }
}


