package com.maststudios.rectangle;

import android.graphics.PointF;

//NOTE :- THIS CLASS IS NOT NEEDED ANY MORE BECAUSE WE ARE USING MATRICES INSTEAD.
//object of this class converts the the coordinates of a grid form grid to pixels.

public class Converter {
	private PointF topleft;
	private float gridwidth,gridheight;
	
	public Converter(float top, float left, float gridwidth, float gridheight){
		topleft=new PointF(left, top);
		this.gridheight=gridheight;
		this.gridwidth=gridwidth;
	}
	
	public PointF convert(float x,float y){
		float newX,newY;
		newX=x*gridwidth+topleft.x;
		newY=y*gridheight+topleft.y;
		return new PointF(newX,newY);
	}
	
	public PointF convert(PointF p){
		return convert(p.x,p.y);
	}
}
