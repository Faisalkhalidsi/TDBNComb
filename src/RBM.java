/*
 * Class representing a Restricted Boltzmann Machine
 */
import java.util.Random;
import java.util.logging.Logger;


public class RBM {
	Random rand;
	layer dummyLayer; // dummy layer with one unit with fixed value
	layer visibleLayer;
	layer hiddenLayer;
	edgeLayer vhEdges;
	edgeLayer vBias;
	edgeLayer hBias;
	double lrate = 0.001; // learning rate
	double mom = 0; // momentum
	int batchsz = demo.BATCH_SIZE; // batch size
	int batchcnt = 0;

    private static final String fileName = RBM.class.getName();
    private static final Logger logger = Logger.getLogger(fileName);

	RBM(int vNum,boolean vReal, 
			int hNum,boolean hReal){

        LoggerSetup.setFileHandler(logger, fileName);
//        LoggerSetup.setHandlerLevel(logger, Level.FINE); // may be conflict with TDBN lo level

        logger.info("vNum: " + vNum + ", vReal: " + vReal + ", hNum: " + hNum + ", hReal: " + hReal);

		if(vReal){
			visibleLayer = new relayer(vNum); // Gaussian units
		}else{
			visibleLayer = new bilayer(vNum); // binary units
		}
		if(hReal){
			hiddenLayer = new relayer(hNum);
		}else{
			hiddenLayer = new bilayer(hNum);
		}

		dummyLayer = new bilayer(1);
		dummyLayer.get(0).val = 1;

		vhEdges = new edgeLayer(visibleLayer, hiddenLayer);
		vBias = new edgeLayer(dummyLayer, visibleLayer);
		hBias = new edgeLayer(dummyLayer, hiddenLayer);
		
		rand = new Random();
	}

	double[] train(){
        double posError;
        double negError;
		hUpdate(true);
		posError = edgesCalc(lrate / batchsz); // positive phases
		gibbsSampling(1, true, false);
		negError = edgesCalc(- lrate / batchsz); // negative phase
		double[] returnee = update(); // masukkan regularisasi dan sparsity penalty
        logger.fine("posError: "+ posError);
        logger.fine("negError: "+ negError);
        logger.fine("energy: "+ returnee[0]);
        logger.fine("reconError: "+ returnee[1]);
        return returnee;
	}
	
	synchronized double[] update(){
	    double energy = 0;
        double reconError = 0;
        batchcnt++;
		if(batchcnt == batchsz){
			edgesUpdate(mom);
            energy += edgesEnergy();
            reconError += edgesReconError();
			batchcnt = 0;
		}
        double[] returnee = new double[2];
        returnee[0] = energy;
        returnee[1] = reconError;
        return returnee;
	}
	
	void gibbsSampling(int sample, boolean lastDeterministic,boolean updateHiddenFirst){
		for(int i = 0; i < sample; i++){
			if(updateHiddenFirst){
				hUpdate(true);
				vUpdate(true);
			}else{
				vUpdate(true);
				hUpdate(true);
			}
		}
		if(lastDeterministic){
			if(updateHiddenFirst){
				hUpdate(false);
				vUpdate(false);
			}else{
				vUpdate(false);
				hUpdate(false);
			}
		}
	}
	
	double edgesCalc(double rate){
        double error = vhEdges.calc(rate);
		vBias.calc(rate);
		hBias.calc(rate);
        return error;
	}
	
	void edgesUpdate(double momentum){
		vhEdges.update(momentum);
		vBias.update(momentum);
		hBias.update(momentum);
	}
    double edgesEnergy() {
        double vb = vBias.getBiasTerm();
        double hc = hBias.getBiasTerm();
        return vhEdges.getVHWTerm() - vb - hc;
//        double v2 = vhEdges.getSquareTerm();
//        return 0.5*v2 - vhEdges.getVHWTerm() - vb - hc;
//        return energy;
    }
    double edgesReconError() {
        return vhEdges.getReconError();
    }
	void hUpdate(boolean sample){
		hBias.sendUp();
		vhEdges.sendUp();
		hiddenLayer.update();
		if(sample) hiddenLayer.sample();
	}
	void vUpdate(boolean sample){
		vBias.sendUp();
		vhEdges.sendDown();
		visibleLayer.update();
		if(sample) visibleLayer.sample();
	}
}
