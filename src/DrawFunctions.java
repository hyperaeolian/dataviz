import processing.core.*;


public class DrawFunctions extends PApplet{
		public void setup(){}
		public void draw(){}
		/* Modified from vormplus.be/blog/article/drawing-a-cylinder-with-processing */
		public void drawCone(int sides, float r1, float r2, double d, String s){
			float angle = 360 / sides;
			float halfHeight = (float) d;// / 2;
			text(s,r1*2,r2*2,(float) d);
			//fill(200);
			fill(50,50,50,50);
			beginShape();
			for (int i = 0; i < sides; i++){
				float x = cos(radians(i*angle)) * r1;
				float y = sin(radians(i*angle)) * r1;
				vertex(x,y,-halfHeight);
			}
			endShape(CLOSE);
			beginShape();
			for (int i = 0; i < sides; i++){
				float x = cos(radians(i*angle)) * r2;
				float y = cos(radians(i*angle)) * r2;
				vertex(x,y,halfHeight);
			}
			endShape(CLOSE);
			beginShape(TRIANGLE_STRIP);
			
			for (int i = 0; i < sides + 1; i++){
				float x1 = cos(radians(i*angle)) * r1;
				float y1 = sin(radians(i*angle)) * r1;
				float x2 = cos(radians(i*angle)) * r2;
				float y2 = sin(radians(i*angle)) * r2;
				vertex(x1,y1,-halfHeight);
				vertex(x2,y2,halfHeight);
			}
			endShape(CLOSE);
		}
	public void drawToroid(){
		int pts = 40;
		float angle = 0;
		double radius = 18.0;//60.0;
		
		int segments = 60;
		float latheAngle = 0;
		double latheRadius = 50.0;//100.0;
		
		PVector vertices[], vertices2[];
		
		boolean isWireFrame = false;
		boolean isHelix = false;
		double helixOffset = 5.0;
		
		 if (isWireFrame){
			    stroke(255, 255, 150);
			    noFill();
			  } 
			  else {
			    //noStroke();
			    stroke(0,100,100);
			    //fill(150, 195, 125);
			    
			  }
			  //center and spin toroid
			  translate(width/2, height/2, -100);

			 // rotateX(frameCount*PI/150);
			 // rotateY(frameCount*PI/170);
			 // rotateZ(frameCount*PI/90);

			  // initialize point arrays
			  vertices = new PVector[pts+1];
			  vertices2 = new PVector[pts+1];

			  // fill arrays
			  for(int i=0; i<=pts; i++){
			    vertices[i] = new PVector();
			    vertices2[i] = new PVector();
			    vertices[i].x = (float) (latheRadius + sin(radians(angle))*radius);
			    if (isHelix){
			      vertices[i].z = (float) (cos(radians(angle))*radius-(helixOffset* segments)/2);
			    } 
			    else{
			      vertices[i].z = (float) (cos(radians(angle))*radius);
			    }
			    angle+=360.0/pts;
			  }

			  // draw toroid
			  latheAngle = 0;
			  for(int i=0; i<=segments; i++){
			    beginShape(QUAD_STRIP);
			    for(int j=0; j<=pts; j++){
			      if (i>0){
			        vertex(vertices2[j].x, vertices2[j].y, vertices2[j].z);
			      }
			      vertices2[j].x = cos(radians(latheAngle))*vertices[j].x;
			      vertices2[j].y = sin(radians(latheAngle))*vertices[j].x;
			      vertices2[j].z = vertices[j].z;
			      // optional helix offset
			      if (isHelix){
			        vertices[j].z+=helixOffset;
			      } 
			      vertex(vertices2[j].x, vertices2[j].y, vertices2[j].z);
			    }
			    // create extra rotation for helix
			    if (isHelix){
			      latheAngle+=720.0/segments;
			    } 
			    else {
			      latheAngle+=360.0/segments;
			    }
			    endShape();
			  }
			}

}
