package myGameEngine;

import a1.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rml.Angle;
import ray.rml.Degreef;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class RotateCameraUpAction extends AbstractInputAction{
	
	private MyGame myGame;
	
	public RotateCameraUpAction(MyGame g) {
		
		myGame = g;
		
	}
	
	public void performAction(float time, Event e) {
		
			if(myGame.camera.getMode() =='n') {
				
		       Angle deg = Degreef.createFrom(5.0f);
		       myGame.dolphinNode.pitch(deg);
		       
		       //Console output
		       System.out.println("Rotate Camera Up Action riding on dolphin");       
		       
			}
			else{
				
		       Angle deg = Degreef.createFrom(-5.0f);
		       Vector3 uVector = myGame.camera.getRt();
		       Vector3 vVector = myGame.camera.getUp();
		       Vector3 nVector = myGame.camera.getFd();
		       Vector3 vTransform = (vVector.rotate(deg, uVector)).normalize();
		       Vector3 nTransform = (nVector.rotate(deg, uVector)).normalize();
		       myGame.camera.setUp((Vector3f)vTransform);
		       myGame.camera.setFd((Vector3f)nTransform);
		      
		       //Console Output
		       System.out.println("Rotate Camera Up Action off the dolphin");  
		       
			}
			
	  }
	
}