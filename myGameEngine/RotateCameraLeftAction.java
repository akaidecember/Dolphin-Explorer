package myGameEngine;

import a1.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rml.Angle;
import ray.rml.Degreef;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class RotateCameraLeftAction extends AbstractInputAction{
	
	private MyGame myGame;
	
	public RotateCameraLeftAction(MyGame g) {
		
		myGame = g;
		
	}
	
	public void performAction(float time, Event e) {
		
			if(myGame.camera.getMode() =='n') {
				
		       Angle deg = Degreef.createFrom(5.0f);
		       myGame.dolphinNode.yaw(deg);
		       
		       //Console output
		       System.out.println("Rotate Camera Left Action riding on dolphin");       
		       
			}
			else{
				
		       Angle deg = Degreef.createFrom(5.0f);
		       Vector3 uVector = myGame.camera.getRt();
		       Vector3 vVector = myGame.camera.getUp();
		       Vector3 nVector = myGame.camera.getFd();
		       Vector3 uTransform = (uVector.rotate(deg, vVector)).normalize();
		       Vector3 nTransform = (nVector.rotate(deg, vVector)).normalize();
		       myGame.camera.setRt((Vector3f)uTransform);
		       myGame.camera.setFd((Vector3f)nTransform);
		      
		       //Console Output
		       System.out.println("Rotate Camera Left Action off the dolphin");  
		       
			}
			
	  }
	
}