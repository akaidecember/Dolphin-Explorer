package myGameEngine;

import a1.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rml.Angle;
import ray.rml.Degreef;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class RotateCameraDownAction extends AbstractInputAction{
	
	private MyGame myGame;
	
	public RotateCameraDownAction(MyGame g) {
		
		myGame = g;
		
	}
	
	@Override
	public void performAction(float time, Event e) {
		
			if(myGame.camera.getMode()=='n') {
				
		       Angle rotAmt = Degreef.createFrom(-5.0f);
		       myGame.dolphinNode.pitch(rotAmt);
		       
		       //Console output
		       System.out.println("Rotate Camera Down Action on dolphin");

			}
			else{
				
		       Angle deg = Degreef.createFrom(5.0f);
		       Vector3 uVector = myGame.camera.getRt();
		       Vector3 vVector = myGame.camera.getUp();
		       Vector3 nVector = myGame.camera.getFd();
		       Vector3 vTransform = (vVector.rotate(deg, uVector)).normalize();
		       Vector3 nTransform = (nVector.rotate(deg, uVector)).normalize();
		       myGame.camera.setUp((Vector3f)vTransform);
		       myGame.camera.setFd((Vector3f)nTransform);
		       
		       //Console output
		       System.out.println("Rotate camera down off the dolphin");
		       
			}
			
	  }
	
}