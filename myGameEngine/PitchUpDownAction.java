package myGameEngine;

import a1.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rml.Angle;
import ray.rml.Degreef;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class PitchUpDownAction extends AbstractInputAction{
	
	private MyGame myGame;
	
	public PitchUpDownAction(MyGame g){
		
		myGame = g;
		
	}
	
	public void performAction(float time, Event e){
		
		if(e.getValue() >= 0.2f){
			
			if(myGame.camera.getMode()=='n'){
				
		       Angle deg = Degreef.createFrom(1.0f);
		       myGame.dolphinNode.pitch(deg);
		       
		       System.out.println("Pitch down off dolphin");

			}
			else{
				
		       Angle deg = Degreef.createFrom(1.0f); 
		       Vector3 uVector = myGame.camera.getRt();
		       Vector3 vVector = myGame.camera.getUp();
		       Vector3 nVector = myGame.camera.getFd();
		       Vector3 vTransform = (vVector.rotate(deg, uVector)).normalize();
		       Vector3 nTransform = (nVector.rotate(deg, uVector)).normalize();
		       myGame.camera.setUp((Vector3f)vTransform);
		       myGame.camera.setFd((Vector3f)nTransform);
		       
		       //Console output
		       System.out.println("Pitch down on the dolphin");
		       
			}
			
		}		
		
		if(e.getValue() <= -0.2f){
			
			if(myGame.camera.getMode()=='n'){
				
			       Angle deg = Degreef.createFrom(-1.0f);
			       myGame.dolphinNode.pitch(deg);

			       //Console output
			       System.out.println("Pitch up off the dolphin");
			       
			}
			else{
				
			       Angle   deg  = Degreef.createFrom(-1.0f);
			       Vector3 uVector = myGame.camera.getRt();
			       Vector3 vVector = myGame.camera.getUp();
			       Vector3 nVector = myGame.camera.getFd();
			       Vector3 vTransform = (vVector.rotate(deg, uVector)).normalize();
			       Vector3 nTransform = (nVector.rotate(deg, uVector)).normalize();
			       myGame.camera.setUp((Vector3f)vTransform);
			       myGame.camera.setFd((Vector3f)nTransform);
			       
			       //Console output
			       System.out.println("Pitch up on the dolphin");
			       
			}
			
		}

	}
}