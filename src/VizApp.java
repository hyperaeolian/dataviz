import processing.core.*;
import processing.data.Table;
import peasy.*;
import controlP5.*;


public class VizApp extends PApplet {
	
	ControlP5 cp5;
	PeasyCam cam;
	Table acbkTable, jcbkTable, accdTable, jccdTable, acmusTable, bcdvdTable, acfoldTable;
	LoadData acbk,accd,acmus,bcdvd,acfold,jcbk,jccd;
	Textarea statistics;
	
	String label;
	int MAX_SHAPES = 150;
	public final static int UPPER_BOUND = 175;
	int radius = 0;
	float sWidth;
	int currentMode;
	
	boolean showTitle, showLabels, straightLine;
	
	PFont font;
	
	/******* SETUP ******/
	public void setup(){
		
		size(1200,800,P3D);
		font = createFont("Helvetica", 40); 
		textFont(font);
		//textSize(25);
		
		cp5 = new ControlP5(this);
		cp5.addButton("Adult_Books")   .setValue(0).setPosition(50,60).setSize(80,20).setId(1);
		cp5.addButton("Adult_CDs")     .setValue(1).setPosition(50,90).setSize(80,20).setId(1);
		cp5.addButton("Juvenile_Books").setValue(2).setPosition(50,120).setSize(80,20).setId(1);
		cp5.addButton("Juvenile_CDs")  .setValue(3).setPosition(50,150).setSize(80,20).setId(1);
		cp5.addButton("Musical_Scores").setValue(4).setPosition(50,180).setSize(80,20).setId(1);
		cp5.addButton("Adult_DVDs")    .setValue(5).setPosition(50,210).setSize(80,20).setId(1);
		cp5.addButton("Folders")       .setValue(6).setPosition(50,240).setSize(80,20).setId(1);
		cp5.addToggle("Arrange").setValue(true).setPosition(50,270).setSize(80, 20).setMode(ControlP5.SWITCH);
		statistics = cp5.addTextarea("txt").setPosition(50,335).setSize(100,200).setFont(createFont("arial",12))
						.setLineHeight(14).setColor(color(128)).setColorBackground(color(255,100)).setColorForeground(color(255,100));
		cp5.setAutoDraw(false);
		cp5.addSlider("changeWidth").setRange(100,400).setValue(200)
		   .setPosition(50,20).setSize(100,19);
		     
		cp5.addSlider("changeHeight").setRange(100,400).setValue(200)
		   .setPosition(50,40).setSize(100,19);	
		cp5.addSlider("Objects").setRange(50,200).setValue(150).setPosition(25,305).setSize(80,20);

		fill(300);
		
		cam = new PeasyCam(this, 300, 0, -600, 1200);
		cam.setMinimumDistance(50);
		cam.setMaximumDistance(1600);
		cam.setSuppressRollRotationMode();
		
		acbk = new LoadData();
		jcbk = new LoadData();
		accd = new LoadData();
		jccd = new LoadData();
		acmus = new LoadData();
		bcdvd = new LoadData();
		acfold = new LoadData();
		
		acbkTable = loadTable("spl_data/acbk.csv","header");
		jcbkTable = loadTable("spl_data/jcbk.csv","header");
		accdTable = loadTable("spl_data/accd.csv","header");
		jccdTable = loadTable("spl_data/jccd.csv","header");
		acmusTable = loadTable("spl_data/acmus.csv","header");
		bcdvdTable = loadTable("spl_data/bcdvd.csv","header");
		acfoldTable = loadTable("spl_data/acfold.csv","header");
		
		acbk.loadCSVData(acbkTable);
		jcbk.loadCSVData(jcbkTable);
		accd.loadCSVData(accdTable);
		jccd.loadCSVData(jccdTable);
		acmus.loadCSVData(acmusTable);
		bcdvd.loadCSVData(bcdvdTable);
		acfold.loadCSVData(acfoldTable);
		
		//Default item type to show
		currentMode = 0;
		showTitle = showLabels = true;
		straightLine = false;
		
		for (int i = 0; i < MAX_SHAPES; i++){
			int indexMax = (int) random(acbkTable.getRowCount()-1);
			int indexMax2 = (int) random(bcdvdTable.getRowCount()-1);
			
			acbk.copies[i]     = acbk.copies[indexMax];
			acbk.duration[i]   = sqrt(acbk.duration[indexMax]);
			acbk.popularity[i] = acbk.popularity[indexMax];
			
			jcbk.copies[i]     = jcbk.copies[indexMax];
			jcbk.duration[i]   = sqrt(jcbk.duration[indexMax]);
			jcbk.popularity[i] = jcbk.popularity[indexMax];
			
			accd.copies[i]     = accd.copies[indexMax];
			accd.duration[i]   = sqrt(accd.duration[indexMax]);
			accd.popularity[i] = accd.popularity[indexMax];
			
			jccd.copies[i]     = jccd.copies[indexMax2];
			jccd.duration[i]   = sqrt(jccd.duration[indexMax2]);
			jccd.popularity[i] = jccd.popularity[indexMax2];
			
			acmus.copies[i]     = acmus.copies[indexMax];
			acmus.duration[i]   = sqrt(acmus.duration[indexMax]);
			acmus.popularity[i] = acmus.popularity[indexMax];
			
			bcdvd.copies[i]     = bcdvd.copies[indexMax2];
			bcdvd.duration[i]   = sqrt(bcdvd.duration[indexMax2]);
			bcdvd.popularity[i] = bcdvd.popularity[indexMax2];
				
			acfold.copies[i]     = acfold.copies[indexMax2];
			acfold.duration[i]   = sqrt(acfold.duration[indexMax2]);
			acfold.popularity[i] = acfold.popularity[indexMax2];	
		}
	
		
	} 
/******* DRAW FUNCTION ******/
	public void draw(){
		//rotateY(map(mouseX,0,width,-PI,PI));
		//rotateX(map(mouseY,0,height,-PI,PI));
		//rotateX((float) -.5);
		//rotateY((float) -.08);
		//rotateZ((float) -.5);
		background(5);
		smooth();
		radius = 800;
		float arc = 0;
		
		for (int i = 0; i < MAX_SHAPES; i++){
			
			acbk.popularity[i] = checkBounds(acbk.popularity[i]);
			jcbk.popularity[i] = checkBounds(jcbk.popularity[i]);
			accd.popularity[i] = checkBounds(accd.popularity[i]);
			jccd.popularity[i] = checkBounds(jccd.popularity[i]);
			bcdvd.popularity[i] = checkBounds(bcdvd.popularity[i]);
			acfold.popularity[i] = checkBounds(acfold.popularity[i]);
			acmus.popularity[i] = checkBounds(acmus.popularity[i]);
			
			//Organize cones in spiral
			sWidth = acbk.popularity[i];
			arc += sWidth;
			float theta = PI + arc / radius;
			theta *= 40;
					
			pushMatrix();
			switch (currentMode){
				//Books-Adult
				case 0:
					colorMode(HSB,100+pow(acbk.duration[i],2),60,100,80);
					for (int x = 0; x < MAX_SHAPES/3; x++) stroke(x,x+1,100);
					if (straightLine == true) {translate(i*2*acbk.popularity[i],0,0);}
					else { translate(radius*cos(theta),radius*sin(theta),-i*25); rotate(theta+PI/2);}
					label = str(acbk.copies[i]);
					drawCone(10,acbk.popularity[i],(float)0.1,acbk.copies[i],label);
					statistics.setText(printStats(acbk.copies,acbk.popularity,acbk.duration));
					break;
				//Books-Juvenile
				case 1:
					colorMode(HSB,100+pow(jcbk.duration[i],2),60,100,80);
					for (int x = 0; x < MAX_SHAPES/3; x++) stroke(x,x+1,100);
					if (straightLine == true) {translate(i*2*jcbk.popularity[i],0,0);}
					else { translate(radius*cos(theta),radius*sin(theta),-i*25); rotate(theta+PI/2);}
					label = str(jcbk.copies[i]);
					drawCone(10,jcbk.popularity[i],(float)0.1,jcbk.copies[i],label);
					statistics.setText(printStats(jcbk.copies,jcbk.popularity,jcbk.duration));
					break;
				//CDs-Adult
				case 2:
					colorMode(HSB,100+pow(accd.duration[i],2),60,100,80);
					for (int x = 0; x < MAX_SHAPES/3; x++) stroke(x,x+1,100);
					if (straightLine == true) {translate(i*2*accd.popularity[i],0,0);}
					else { translate(radius*cos(theta),radius*sin(theta),-i*25); rotate(theta+PI/2);}
					label = str(accd.copies[i]);
					drawCone(10,accd.popularity[i],(float)0.1,accd.copies[i],label);
					statistics.setText(printStats(accd.copies,accd.popularity,accd.duration));
					break;
				//CDs-Juvenile
				case 3:
					colorMode(HSB,100+pow(jccd.duration[i],2),60,100,80);
					for (int x = 0; x < MAX_SHAPES/3; x++) stroke(x,x+1,100);
					if (straightLine == true) {translate(i*2*jccd.popularity[i],0,0);}
					else { translate(radius*cos(theta),radius*sin(theta),-i*25); rotate(theta+PI/2);}
					label = str(jccd.copies[i]);
					drawCone(10,jccd.popularity[i],(float)0.1,jccd.copies[i],label);
					statistics.setText(printStats(jccd.copies,jccd.popularity,jccd.duration));
					break;
				//Musical Scores
				case 4:
					colorMode(HSB,100+pow(acmus.duration[i],2),60,100,80);
					for (int x = 0; x < MAX_SHAPES/3; x++) stroke(x,x+1,100);
					if (straightLine == true) {translate(i*2*acmus.popularity[i],0,0);}
					else { translate(radius*cos(theta),radius*sin(theta),-i*25); rotate(theta+PI/2);}
					label = str(acmus.copies[i]);
					drawCone(10,acmus.popularity[i],(float)0.1,acmus.copies[i],label);
					statistics.setText(printStats(acmus.copies,acmus.popularity,acmus.duration));
					break;
				//DVDs
				case 5:
					colorMode(HSB,100+pow(bcdvd.duration[i],2),60,100,80);
					for (int x = 0; x < MAX_SHAPES/3; x++) stroke(x,x+1,100);
					if (straightLine == true) {translate(i*2*bcdvd.popularity[i],0,0);}
					else { translate(radius*cos(theta),radius*sin(theta),-i*25); rotate(theta+PI/2);}
					label = str(bcdvd.copies[i]);
					drawCone(10,bcdvd.popularity[i],(float)0.1,bcdvd.copies[i],label);
					statistics.setText(printStats(bcdvd.copies,bcdvd.popularity,bcdvd.duration));
					break;
				//Folders
				case 6:
					colorMode(HSB,100+pow(acfold.duration[i],2),60,100,80);
					for (int x = 0; x < MAX_SHAPES/3; x++) stroke(x,x+1,100);
					if (straightLine == true) {translate(i*2*acfold.popularity[i],0,0);}
					else { translate(radius*cos(theta),radius*sin(theta),-i*25); rotate(theta+PI/2);}
					label = str(acfold.copies[i]);
					drawCone(10,acfold.popularity[i],(float)0.1,acfold.copies[i],label);
					statistics.setText(printStats(acfold.copies,acfold.popularity,acfold.duration));
					break;

			}
			popMatrix();	
			gui();
			arc += sWidth/2;
			//save("259final.png");
		}
	}
	/* Modified from vormplus.be/blog/article/drawing-a-cylinder-with-processing */
	public void drawCone(int sides, float r1, float r2, double d, String s){
		float angle = 360 / sides;
		float halfHeight = (float) d;
		fill(250,250,250,100);
		if (showLabels == true){
			text(s,r1*2,r2*2,(float) d);
			fill(50,50,50,100);
		}
		fill(50,50,50,100);
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
	
	public float checkBounds(float item){
		if (item > UPPER_BOUND){
			return sqrt(item);
		}
		return item;
	}
	
	public float computeMean(float[] list){
		float sum = 0;
		for (int i = 0; i < list.length; i++) sum += list[i];
		return sum/list.length;
	}
	
	public float computeVariance(float[] list){
		float sum = 0;
		float mean = computeMean(list);
		for (int i = 0; i < list.length; i++) sum += ((list[i]-mean)*(list[i]-mean));
		return sum / list.length;
	}
	
	public float computeStdDev(float[] list){
		float variance = computeVariance(list);
		return sqrt(variance);
	}
	//Stats to be displayed in gui
	public String printStats(float[] copies, float[] popularity, float[] duration){
		float p_mean = computeMean(popularity);
		float p_var = computeVariance(popularity);
		float p_std = computeStdDev(popularity);
		
		float c_mean = computeMean(copies);
		float c_var = computeVariance(copies);
		float c_std = computeStdDev(copies);
		
		float d_mean = computeMean(duration);
		float d_var = computeVariance(duration);
		float d_std = computeStdDev(duration);
		
		String txt = "Copies:\n" + "\t\tMean: " + c_mean + "\n\t\tVariance: " + c_var + "\n\t\tStd. Dev.: " + c_std +
						"\n\nPopularity:\n" + "\t\tMean: " + p_mean + "\n\t\tVariance: " + p_var + "\n\t\tStd. Dev.: " + p_std +
						"\n\nDuration:\n" + "\t\tMean: " + d_mean + "\n\t\tVariance: " + d_var + "\n\t\tStd. Dev.: " + d_std; 
		return txt;
		
	}
	
	public void gui(){
		hint(DISABLE_DEPTH_TEST);
		cam.beginHUD();
		cp5.draw();
		if (showTitle == true){
			title();
		}
		cam.endHUD();
		hint(ENABLE_DEPTH_TEST);
	}
	
	public void keyPressed(){
		switch (key){
		case 't':
			showTitle = true;
			break;
		case 'o':
			showTitle = false;
			break;
		case 'l':
			showLabels = true;
			break;
		case 'k':
			showLabels = false;
			break;
		case 's':
			save("output.pdf");
			break;
		case '1':
			cam.lookAt(400, -600, -1600);
			break;
		case '2':
			cam.lookAt(-800, 400, -200);
			break;
		case '3':
			cam.lookAt(600, -300, -1500);
			break;
		case 'r':
			cam.reset();
			break;
		}
	}
	
	public void title(){
		textAlign(LEFT, TOP);
		 fill(255);
		  textSize(15);
		  textAlign(RIGHT, TOP);
		  textSize(38);
		  text("COPIES",width-100,height-700);
		  fill(255);
	}
	
	/*Button Listeners*/
	public void Adult_Books   (int value){currentMode = value;}
	public void Adult_CDs     (int value){currentMode = value;}
	public void Juvenile_Books(int value){currentMode = value;}
	public void Juvenile_CDs  (int value){currentMode = value;}
	public void Musical_Scores(int value){currentMode = value;}
	public void Adult_DVDs	  (int value){currentMode = value;}
	public void Folders	      (int value){currentMode = value;}
	public void changeWidth(int theValue){ statistics.setWidth(theValue);}
	public void changeHeight(int theValue){ statistics.setHeight(theValue);}
	public void Objects		  (int value){MAX_SHAPES = value;}

	public void Arrange(boolean theFlag){
		if (theFlag == true){
			straightLine = true;
		} else
			straightLine = false;
		}
}
