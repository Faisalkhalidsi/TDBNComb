import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sourceMyCode on 05/12/2016.
 */
public class AMC {
    ArrayList rootTX = new ArrayList();
    ArrayList rootTY = new ArrayList();
    ArrayList rootTZ = new ArrayList();

    ArrayList lowerbackTX = new ArrayList();
    ArrayList lowerbackTY = new ArrayList();
    ArrayList lowerbackTZ = new ArrayList();

    ArrayList upperbackTX = new ArrayList();
    ArrayList upperbackTY = new ArrayList();
    ArrayList upperbackTZ = new ArrayList();

    ArrayList thoraxTX = new ArrayList();
    ArrayList thoraxTY = new ArrayList();
    ArrayList thoraxTZ = new ArrayList();

    ArrayList lowerneckTX = new ArrayList();
    ArrayList lowerneckTY = new ArrayList();
    ArrayList lowerneckTZ = new ArrayList();

    ArrayList uppperneckTX = new ArrayList();
    ArrayList uppperneckTY = new ArrayList();
    ArrayList uppperneckTZ = new ArrayList();

    ArrayList headTX = new ArrayList();
    ArrayList headTY = new ArrayList();
    ArrayList headTZ = new ArrayList();

    ArrayList rclavicleTX = new ArrayList();
    ArrayList rclavicleTY = new ArrayList();

    ArrayList rhumerusTX = new ArrayList();
    ArrayList rhumerusTY = new ArrayList();
    ArrayList rhumerusTZ = new ArrayList();

    ArrayList rradiusTX = new ArrayList();

    ArrayList rwristTX = new ArrayList();

    ArrayList rhandTX = new ArrayList();
    ArrayList rhandTY = new ArrayList();

    ArrayList rfingersTX = new ArrayList();

    ArrayList rthumbTX = new ArrayList();
    ArrayList rthumbTY = new ArrayList();

    ArrayList lclavicleTX = new ArrayList();
    ArrayList lclavicleTY = new ArrayList();

    ArrayList lhumerusTX = new ArrayList();
    ArrayList lhumerusTY = new ArrayList();
    ArrayList lhumerusTZ = new ArrayList();

    ArrayList lradiusTX = new ArrayList();

    ArrayList lwristTX = new ArrayList();

    ArrayList lhandTX = new ArrayList();
    ArrayList lhandTY = new ArrayList();

    ArrayList lfingersTX = new ArrayList();

    ArrayList lthumbTX = new ArrayList();
    ArrayList lthumbTY = new ArrayList();

    ArrayList rfemurTX = new ArrayList();
    ArrayList rfemurTY = new ArrayList();
    ArrayList rfemurTZ = new ArrayList();

    ArrayList rtibiaTX = new ArrayList();

    ArrayList rfootTX = new ArrayList();
    ArrayList rfootTY = new ArrayList();

    ArrayList rtoesTX = new ArrayList();

    ArrayList lfemurTX = new ArrayList();
    ArrayList lfemurTY = new ArrayList();
    ArrayList lfemurTZ = new ArrayList();

    ArrayList ltibiaTX = new ArrayList();

    ArrayList lfootTX = new ArrayList();
    ArrayList lfootTY = new ArrayList();

    ArrayList ltoesTX = new ArrayList();

    AMC(String amcFile){
        String line;
        try {
            FileReader fr = new FileReader(amcFile);
            BufferedReader amcData = new BufferedReader(fr);
            while (true) {
                line = amcData.readLine();
                if (line == null) break;
                if (line.charAt(0) == '#') continue;
                if (line.charAt(0) == ':') continue;
                if (line.split(" ").length == 1) continue;
                String[] spaceSplit;
                spaceSplit = line.split(" ");

                switch(spaceSplit[0]) {
                    case "root" :
                        rootTX.add(Double.parseDouble(spaceSplit[4]));
                        rootTY.add(Double.parseDouble(spaceSplit[5]));
                        rootTZ.add(Double.parseDouble(spaceSplit[6]));
                        break;
                    case "lowerback":
                        lowerbackTX.add(Double.parseDouble(spaceSplit[1]));
                        lowerbackTY.add(Double.parseDouble(spaceSplit[2]));
                        lowerbackTZ.add(Double.parseDouble(spaceSplit[3]));
                        break;
                    case "upperback":
                        upperbackTX.add(Double.parseDouble(spaceSplit[1]));
                        upperbackTY.add(Double.parseDouble(spaceSplit[2]));
                        upperbackTZ.add(Double.parseDouble(spaceSplit[3]));
                        break;
                    case "thorax":
                        thoraxTX.add(Double.parseDouble(spaceSplit[1]));
                        thoraxTY.add(Double.parseDouble(spaceSplit[2]));
                        thoraxTZ.add(Double.parseDouble(spaceSplit[3]));
                        break;
                    case "lowerneck":
                        lowerneckTX.add(Double.parseDouble(spaceSplit[1]));
                        lowerneckTY.add(Double.parseDouble(spaceSplit[2]));
                        lowerneckTZ.add(Double.parseDouble(spaceSplit[3]));
                        break;
                    case "upperneck":
                        uppperneckTX.add(Double.parseDouble(spaceSplit[1]));
                        uppperneckTY.add(Double.parseDouble(spaceSplit[2]));
                        uppperneckTZ.add(Double.parseDouble(spaceSplit[3]));
                        break;
                    case "head":
                        headTX.add(Double.parseDouble(spaceSplit[1]));
                        headTY.add(Double.parseDouble(spaceSplit[2]));
                        headTZ.add(Double.parseDouble(spaceSplit[3]));
                        break;
                    case "rclavicle":
                        rclavicleTX.add(Double.parseDouble(spaceSplit[1]));
                        rclavicleTY.add(Double.parseDouble(spaceSplit[2]));
                        break;
                    case "rhumerus":
                        rhumerusTX.add(Double.parseDouble(spaceSplit[1]));
                        rhumerusTY.add(Double.parseDouble(spaceSplit[2]));
                        rhumerusTZ.add(Double.parseDouble(spaceSplit[3]));
                        break;
                    case "rradius":
                        rradiusTX.add(Double.parseDouble(spaceSplit[1]));
                        break;
                    case "rwrist":
                        rwristTX.add(Double.parseDouble(spaceSplit[1]));
                        break;
                    case "rhand":
                        rhandTX.add(Double.parseDouble(spaceSplit[1]));
                        rhandTY.add(Double.parseDouble(spaceSplit[2]));
                        break;
                    case "rfingers":
                        rfingersTX.add(Double.parseDouble(spaceSplit[1]));
                        break;
                    case "rthumb":
                        rthumbTX.add(Double.parseDouble(spaceSplit[1]));
                        rthumbTY.add(Double.parseDouble(spaceSplit[2]));
                        break;
                    case "lclavicle":
                        lclavicleTX.add(Double.parseDouble(spaceSplit[1]));
                        lclavicleTY.add(Double.parseDouble(spaceSplit[2]));
                        break;
                    case "lhumerus":
                        lhumerusTX.add(Double.parseDouble(spaceSplit[1]));
                        lhumerusTY.add(Double.parseDouble(spaceSplit[2]));
                        lhumerusTZ.add(Double.parseDouble(spaceSplit[3]));
                        break;
                    case "lradius":
                        lradiusTX.add(Double.parseDouble(spaceSplit[1]));
                        break;
                    case "lwrist":
                        lwristTX.add(Double.parseDouble(spaceSplit[1]));
                        break;
                    case "lhand":
                        lhandTX.add(Double.parseDouble(spaceSplit[1]));
                        lhandTY.add(Double.parseDouble(spaceSplit[2]));
                        break;
                    case "lfingers":
                        lfingersTX.add(Double.parseDouble(spaceSplit[1]));
                        break;
                    case "lthumb":
                        lthumbTX.add(Double.parseDouble(spaceSplit[1]));
                        lthumbTY.add(Double.parseDouble(spaceSplit[2]));
                        break;
                    case "rfemur":
                        rfemurTX.add(Double.parseDouble(spaceSplit[1]));
                        rfemurTY.add(Double.parseDouble(spaceSplit[2]));
                        rfemurTZ.add(Double.parseDouble(spaceSplit[2]));
                        break;
                    case "rtibia":
                        rtibiaTX.add(Double.parseDouble(spaceSplit[1]));
                        break;
                    case "rfoot":
                        rfootTX.add(Double.parseDouble(spaceSplit[1]));
                        rfootTY.add(Double.parseDouble(spaceSplit[2]));
                        break;
                    case "rtoes":
                        rtoesTX.add(Double.parseDouble(spaceSplit[1]));
                        break;
                    case "lfemur":
                        lfemurTX.add(Double.parseDouble(spaceSplit[1]));
                        lfemurTY.add(Double.parseDouble(spaceSplit[2]));
                        lfemurTZ.add(Double.parseDouble(spaceSplit[3]));
                        break;
                    case "ltibia":
                        ltibiaTX.add(Double.parseDouble(spaceSplit[1]));
                        break;
                    case "lfoot":
                        lfootTX.add(Double.parseDouble(spaceSplit[1]));
                        lfootTY.add(Double.parseDouble(spaceSplit[2]));
                        break;
                    case "ltoes":
                        ltoesTX.add(Double.parseDouble(spaceSplit[1]));
                        break;
                }
            }
        }
        catch (IOException ioe) {
            ioe.getMessage();
        }
    }
}
