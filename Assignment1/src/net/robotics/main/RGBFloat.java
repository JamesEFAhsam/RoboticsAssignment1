package net.robotics.main;

class RGBFloat{
	private float R, G, B;
	private RGBFloat HIGH, LOW;
	public boolean comparative;

	public RGBFloat(float red, float green, float blue){
		this.R = red;
		this.G = green;
		this.B = blue;
		
		comparative = false;
	}
	
	public RGBFloat(float[] samples){
		this(samples[0], samples[1], samples[2]);
	}
	
	public RGBFloat(float[] low, float[] high){
		this(new RGBFloat(low), new RGBFloat(high));
	}
	
	public RGBFloat(RGBFloat low, RGBFloat high){
		this.HIGH = high;
		this.LOW = low;
		
		this.comparative = true;
		
		
		this.R = (this.HIGH.getR() + this.LOW.getR())/2f;
		this.G = (this.HIGH.getG() + this.LOW.getG())/2f;
		this.B = (this.HIGH.getB() + this.LOW.getB())/2f;
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
		if(HIGH != null)
			return HIGH.getR();
		
		return 0f;
	}

	public float getHG() {
		if(HIGH != null)
			return HIGH.getG();
		
		return 0f;
	}

	public float getHB() {
		if(HIGH != null)
			return HIGH.getB();
		
		return 0f;
	}

	public float getLR() {
		if(LOW != null)
			return LOW.getR();
		
		return 0f;
	}

	public float getLG() {
		if(LOW != null)
			return LOW.getG();
		
		return 0f;
	}

	public float getLB() {
		if(LOW != null)
			return LOW.getB();
		
		return 0f;
	}

	public boolean isComparative() {
		return comparative;
	}
	
	public RGBFloat getHigh(){
		return HIGH;
	}

	public RGBFloat getLow(){
		return LOW;
	}
	
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