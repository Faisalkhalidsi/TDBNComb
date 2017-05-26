import org.jetbrains.annotations.NotNull;

import java.util.Random;

class edge {
    @NotNull
    static Random rand = new Random();
    //	int wind;
    double w;
    double dw;
    double v;
    node n1;
    node n2;

    public edge() {
        w = rand.nextGaussian() * 0.01;
    }
}

class edgeLayer {
    edge[] edges;
    int count;
    layer l1, l2;

    int keg =-1;
    int index=-1;

    public edgeLayer(@NotNull layer l1, @NotNull layer l2) {
        this.l1 = l1;
        this.l2 = l2;
        count = l1.size() * l2.size();
        edges = new edge[count];
        int c = 0;
        for (int i1 = 0; i1 < l1.size(); i1++) {
            for (int i2 = 0; i2 < l2.size(); i2++) {
                edges[c] = new edge();
                edges[c].n1 = l1.get(i1);
                edges[c].n2 = l2.get(i2);
                c++;
            }
        }
    }

    public double calc(double rate) {
        double error = 0;
        double cek = 0;
        if (demo.auto == 0){
            for (int i = 0; i < count; i++) {
                if (index >= 0) {
                    if (keg == 100) {
                        double deltaW = edges[i].n1.val * edges[i].n2.val * rate;
                        edges[i].dw += deltaW*demo.firstMotionCompotition;
                        error += deltaW;
                    }else {
                        double deltaW = edges[i].n1.val * edges[i].n2.val * rate;
                        edges[i].dw += deltaW*demo.secondMotionCompotition;
                        error += deltaW;
                    }
                } else {
                    double deltaW = edges[i].n1.val * edges[i].n2.val * rate;
                    edges[i].dw += deltaW;
                    error += deltaW;
                }
            }
        }else {
            for (int i = 0; i < count; i++) {
                if (keg == 100) {
                    switch (index) {
                        case 0:
                            cek = demo.partOne;
                            break;
                        case 1:
                            cek = demo.partTwo;
                            break;
                        case 2:
                            cek = demo.partThree;
                            break;
                        case 3:
                            cek = demo.partFour;
                            break;
                        case 4:
                            cek = demo.partFive;
                            break;
                    }
                } else if (keg == 200) {
                    switch (index) {
                        case 0:
                            cek = 1 - demo.partOne;
                            break;
                        case 1:
                            cek = 1 - demo.partTwo;
                            break;
                        case 2:
                            cek = 1 - demo.partThree;
                            break;
                        case 3:
                            cek = 1 - demo.partFour;
                            break;
                        case 4:
                            cek = 1 - demo.partFive;
                            break;
                    }
                }

                if (index >= 0) {
                    if (cek == 1) {
                        double deltaW = edges[i].n1.val * edges[i].n2.val * rate;
                        edges[i].dw += deltaW;
                        error += deltaW;
                    }else {
//                    double deltaW = 0;
//                    edges[i].dw += deltaW;
                        error +=  edges[i].dw;
                    }
                } else {
                    double deltaW = edges[i].n1.val * edges[i].n2.val * rate;
                    edges[i].dw += deltaW;
                    error += deltaW;
                }
            }
        }

        return error;
    }

    public void sendUp() {
        for (int i = 0; i < count; i++)
            edges[i].n2.buff += edges[i].w * edges[i].n1.val;
    }

    public void sendDown() {
        for (int i = 0; i < count; i++)
            edges[i].n1.buff += edges[i].w * edges[i].n2.val;
    }

    public void update(double mom) {
        double accum = 0;
        if (demo.WITH_SPARSITY) {
            for (int i2 = 0; i2 < l2.size(); i2++) {
                double zi = l2.get(i2).val;
                double lzi = 1. / (1. + Math.exp(-zi));
                double oneP = Math.log(1 + lzi * lzi);
                accum += oneP;
            }
        }

        double cek=0;
        if (demo.auto == 0){
            for (int i = 0; i < count; i++) {
                if (index>=0){
                    if (keg == 100) {
                        edges[i].v = mom * edges[i].v + edges[i].dw;
                        edges[i].w += edges[i].v*demo.firstMotionCompotition;
                        edges[i].w -= edges[i].w * 0.000002;
                    }else {
                        edges[i].v = mom * edges[i].v + edges[i].dw;

                        edges[i].w += edges[i].v*demo.secondMotionCompotition;
                        edges[i].w -= edges[i].w * 0.000002;
                    }
                }else {
                    edges[i].v = mom * edges[i].v + edges[i].dw;
                    edges[i].w += edges[i].v;
                    edges[i].w -= edges[i].w * 0.000002;
                }
                edges[i].dw = 0;
            }
        }else {
            for (int i = 0; i < count; i++) {
                if (keg == 100) {
                    switch (index) {
                        case 0:
                            cek = demo.partOne;
                            break;
                        case 1:
                            cek = demo.partTwo;
                            break;
                        case 2:
                            cek = demo.partThree;
                            break;
                        case 3:
                            cek = demo.partFour;
                            break;
                        case 4:
                            cek = demo.partFive;
                            break;
                    }
                } else if (keg == 200) {
                    switch (index) {
                        case 0:
                            cek = 1- demo.partOne;
                            break;
                        case 1:
                            cek = 1-demo.partTwo;
                            break;
                        case 2:
                            cek = 1-demo.partThree;
                            break;
                        case 3:
                            cek = 1-demo.partFour;
                            break;
                        case 4:
                            cek = 1-demo.partFive;
                            break;
                    }
                }

                if (index>=0){
                    if (cek == 1) {
                        edges[i].v = mom * edges[i].v + edges[i].dw;
//                    edges[i].dw = 0;
                        edges[i].w += edges[i].v;
                        edges[i].w -= edges[i].w * 0.000002;
                    } else {
                        edges[i].v = edges[i].dw;
//                    edges[i].dw = 0;
                        edges[i].w += edges[i].v;
                        edges[i].w -= edges[i].w * 0.000002;
                    }
                }else {
//                System.out.println("ini crbm");
                    edges[i].v = mom * edges[i].v + edges[i].dw;

                    edges[i].w += edges[i].v;
                    edges[i].w -= edges[i].w * 0.000002;
                }
                edges[i].dw = 0;

                if (demo.WITH_REGULARIZATION) // regularization term
                    edges[i].w += (edges[i].w * edges[i].w) * demo.REGULARIZATION_CONSTANT;
                if (demo.WITH_SPARSITY) // sparsity term
                    edges[i].w += accum * demo.SPARSITY_CONSTANT;
            }
        }

    }

    public double getVHWTerm(){
		double e = 0;
		for(int i = 0; i < count; i++) {
            e -= edges[i].w * edges[i].n1.val * edges[i].n2.val;
        }
		return e;
	}

    public double getReconError() {
        double e = 0;
        for (int i1 = 0; i1 < l1.size(); i1++) {
            double diff = l1.get(i1).buff - l1.get(i1).val;
            double diff2 = diff * diff;
            e -= diff2;
        }
        return e;
    }

    public double getBiasTerm() {
        double sum = 0;
        for(int i = 0; i < count; i++)
            sum += edges[i].w * edges[i].n2.val;
        return sum;
    }

    //add 14 dec 2014
    public double getVisible(){
        double e = 0;
        for(int i = 0; i < count; i++)
            e += Math.pow((edges[i].n1.val),2);
        return e;
    }

    public double getSquareTerm() {
        double sum = 0;
        for (int i = 0; i < count; i++)
            sum += edges[i].n1.val * edges[i].n1.val;
        return sum;
    }


}