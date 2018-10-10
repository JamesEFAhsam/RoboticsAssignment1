package net.robotics.main;

class RGBFloat{
	private float R, G, B;
	private float HR, HG, HB;

	public RGBFloat(float red, float green, float blue){
		this.R = red;
		this.G = green;
		this.B = blue;
		
		comparative = false;
	}
	
	public RGBFloat(RGBFloat low, RGBFloat high){
		this.HR = high.getR();
		this.HG = high.getG();
		this.HB = high.getB();
		
		this.LR = low.getR();
		this.LG = low.getG();
		this.LB = low.getB();
		
		this.comparative = true;
		
		
		this.R = (this.HR + this.LR)/2f;
		this.G = (this.HG + this.LG)/2f;
		this.B = (this.HB + this.LB)/2f;
	}
	
	public boolean Compare(RGBFloat comp){
		RGBFloat subject, comparator;
		
		
		if(comp.comparative){
			comparator = comp;
			subject = this;
		} else if(this.comparative){
			comparator = this;
			subject = comp;
		} else {
			return false;
		}
		
		return RGBFloat.Compare(subject, comparator);
	}
	
	public static boolean Compare(RGBFloat subject, RGBFloat comparator){
		return subject.getR() >= comparator.getLR() &&
				subject.getR() <= comparator.getHR() &&
				subject.getG() >= comparator.getLG() &&
				subject.getG() <= comparator.getHG() &&
				subject.getB() >= comparator.getLB() &&
				subject.getB() <= comparator.getHB();
	}
	
	public float getHR() {
		return HR;
	}

	public float getHG() {
		return HG;
	}

	public float getHB() {
		return HB;
	}

	public float getLR() {
		return LR;
	}

	public float getLG() {
		return LG;
	}

	public float getLB() {
		return LB;
	}

	public boolean isComparative() {
		return comparative;
	}

	private float LR, LG, LB;
	
	public boolean comparative;
	
	public float getR() {
		return R;
	}

	public void setR(float r) {
		R = r;
	}

	public float getG() {
		return G;
	}

	public void setG(float g) {
		G = g;
	}

	public float getB() {
		return B;
	}

	public void setB(float b) {
		B = b;
	}
	
}