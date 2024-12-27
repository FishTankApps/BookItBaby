package com.fishtankapps.animation;

public class CubicBezierInterpolation implements TimeInterpolation {

	//private static final float y0 = 0, y1 = 0, y2 = 1, y3 = 1; 
	//private static final float x0 = 0, x3 = 1;
	
	private float x1, x2;

	
	public CubicBezierInterpolation(float startingStrength, float endingStrength) {
		x1 = startingStrength;
		x2 = 1-endingStrength;
	}
	
	@Override
	public float applyInterpolation(float percent) {
		percent = Math.max(0, Math.min(1, percent));
		
//		double d = -x0 + 3*x1 - 3*x2 + x3;
//		double a = 3*x0 - 6*x1 + 3*x2;
//		double b = -3*x0 + 3*x1;
//		double c = x0-percent;
		
		double d =  3*x1 - 3*x2 + 1;
		double a = -6*x1 + 3*x2;
		double b =  3*x1;
		double c = -percent;
		
		double[] result = solveCubic(d, a, b, c);

		double t = selectValidT(result);
		
		//double y = (1-t)^3 * y0 + 3*(1-t)^2 * t * y1 + 3*(1-t) * t^2 * Y2 + t^3 * Y3;
		//double y = (1-t)^3 * 0  + 3*(1-t)^2 * t * 0  + 3*(1-t) * t^2 * 1  + t^3 * 1;
		
		double y = 3*(1-t) * Math.pow(t, 2) + Math.pow(t, 3);
		
		return (float) Math.max(0, Math.min(1, y));
	}
	
	private static double selectValidT(double[] ts) {
		double t = 1;
		
		for(double tt : ts)
			if(tt <= 1.01 && tt >= -0.01)
				t = tt;
		
		return t;
	}

	//X(t) = (1-t)^3 * X0 + 3*(1-t)^2 * t * X1 + 3*(1-t) * t^2 * X2 + t^3 * X3
	//Y(t) = (1-t)^3 * Y0 + 3*(1-t)^2 * t * Y1 + 3*(1-t) * t^2 * Y2 + t^3 * Y3
	
	//dx^3 + ax^2 + bx + c
	public static double[] solveCubic(double d, double a, double b, double c) {
        double[] result;
        if (d != 1) {
            a = a / d;
            b = b / d;
            c = c / d;
        }

        double p = b / 3 - a * a / 9;
        double q = a * a * a / 27 - a * b / 6 + c / 2;
        double D = p * p * p + q * q;

        if (Double.compare(D, 0) >= 0) {
            if (Double.compare(D, 0) == 0) {
                double r = Math.cbrt(-q);
                result = new double[2];
                result[0] = 2 * r;
                result[1] = -r;
            } else {
                double r = Math.cbrt(-q + Math.sqrt(D));
                double s = Math.cbrt(-q - Math.sqrt(D));
                result = new double[1];
                result[0] = r + s;
            }
        } else {
            double ang = Math.acos(-q / Math.sqrt(-p * p * p));
            double r = 2 * Math.sqrt(-p);
            result = new double[3];
            for (int k = -1; k <= 1; k++) {
                double theta = (ang - 2 * Math.PI * k) / 3;
                result[k + 1] = r * Math.cos(theta);
            }

        }
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i] - a / 3;
        }
        return result;
    }
}
