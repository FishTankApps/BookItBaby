package com.fishtankapps.animation;

class LinearInterpolation implements TimeInterpolation {

	public float applyInterpolation(float percent) {
		return Math.max(Math.min(percent, 1), 0);
	}

}
